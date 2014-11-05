/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.maritimecloud.internal.mms.client.broadcast;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.internal.mms.client.ClientInfo;
import net.maritimecloud.internal.mms.client.MmsThreadManager;
import net.maritimecloud.internal.mms.client.connection.ClientConnection;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.net.messages.BroadcastAck;
import net.maritimecloud.internal.net.messages.MessageHasher;
import net.maritimecloud.internal.net.util.DefaultAcknowledgement;
import net.maritimecloud.internal.net.util.DefaultMessageHeader;
import net.maritimecloud.internal.util.Coverage;
import net.maritimecloud.internal.util.MessageStore;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.net.BroadcastConsumer;
import net.maritimecloud.net.BroadcastMessage;
import net.maritimecloud.net.BroadcastSubscription;
import net.maritimecloud.net.DispatchedMessage;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientClosedException;
import net.maritimecloud.net.mms.WithBroadcast;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.PositionTime;

import org.cakeframework.container.RunOnStop;
import org.cakeframework.util.concurrent.ScheduleWithFixedDelay;
import org.cakeframework.util.concurrent.ThreadManager;

/**
 * Manages sending and receiving of broadcasts.
 *
 * @author Kasper Nielsen
 */
public class ClientBroadcastManager {

    /** The logger. */
    private static final Logger LOG = Logger.get(ClientBroadcastManager.class);

    /** The network */
    private final ClientConnection connection;

    /** Broadcast that have been sent. Will be cleared regularly */
    private final MessageStore<DispatchedBroadcast> dispatchedBroadcasts = new MessageStore<>();

    volatile boolean isShutdown;

    final ReentrantReadWriteLock sendLock = new ReentrantReadWriteLock();

    final ReentrantReadWriteLock subscribeLock = new ReentrantReadWriteLock();

    /** A map of local broadcast subscribers. */
    final ConcurrentHashMap<String, SubscriptionSet> subscribers = new ConcurrentHashMap<>();

    /** Thread manager takes care of asynchronous processing. */
    private final MmsThreadManager threadManager;

    private final ClientInfo info;

    final ScheduledExecutorService ses;

    public ClientBroadcastManager(ClientInfo info, MmsThreadManager threadManager, ClientConnection connection,
            ThreadManager tmm) {
        this.connection = requireNonNull(connection);
        this.threadManager = requireNonNull(threadManager);
        this.info = requireNonNull(info);
        ses = tmm.getScheduledExecutor("");

        connection.subscribe(BroadcastAck.class, (a, e) -> onBroadcastAck(e));
        connection.subscribe(Broadcast.class, (a, e) -> onBroadcastMessage(e));
    }

    public WithBroadcast broadcast(BroadcastMessage message) {
        return new DefaultWithBroadcast(this, message);
    }

    /**
     * Sets up listeners for incoming broadcast messages.
     *
     * @param messageType
     *            the type of message to receive
     * @param listener
     *            the callback listener
     * @return a subscription
     * @see MmsClient#broadcastSubscribe(Class, BroadcastConsumer)
     */
    public <T extends BroadcastMessage> BroadcastSubscription broadcastSubscribe(Class<T> messageType,
            BroadcastConsumer<T> listener, Area area) {
        subscribeLock.readLock().lock();
        try {
            if (isShutdown) {
                throw new MmsClientClosedException("The mms client has been shutdown");
            }
            String type = MessageHelper.getName(messageType);
            SubscriptionSet set = subscribers.computeIfAbsent(type, e -> new SubscriptionSet(this, type, messageType));
            return set.newSubscription(listener, area == null ? Coverage.ALL : new Coverage.StaticAreaCoverage(area));
        } finally {
            subscribeLock.readLock().unlock();
        }
    }

    DispatchedMessage brodcast(BroadcastMessage message, Area area, int radius,
            Consumer<? super MessageHeader> ackConsumer) {
        String broadcastType = MessageHelper.getName(message.getClass());

        Broadcast broadcast = new Broadcast();
        broadcast.setBroadcastType(broadcastType);
        broadcast.setSenderId(info.getClientId().toString());
        broadcast.setSenderTimestamp(info.currentTime());
        Optional<PositionTime> r = info.getCurrentPosition();
        if (r.isPresent()) {
            broadcast.setSenderPosition(r.get());
        }

        Area broadcastArea = area;
        // Fix this, must specify an area if stationary client
        if (broadcastArea == null && r.isPresent()) {
            broadcastArea = Circle.create(r.get(), radius);
        }
        broadcast.setArea(broadcastArea);
        broadcast.setAckBroadcast(ackConsumer != null);
        broadcast.setPayload(Binary.copyFromUtf8(MessageSerializer.writeToJSON(message,
                MessageHelper.getSerializer(message.getClass()))));

        broadcast.setMessageId(MessageHasher.calculateSHA256(broadcast));

        DefaultAcknowledgement ack = new DefaultAcknowledgement(ses);
        DispatchedBroadcast db = new DispatchedBroadcast(broadcast, ack, ackConsumer);

        sendLock.readLock().lock();
        try {
            checkNotShutdown();
            dispatchedBroadcasts.addMessage(db);
            CompletableFuture<Void> om = connection.sendMessage(broadcast);
            om.handle((ac, cause) -> {
                if (cause == null) {
                    ack.complete(null);
                } else {
                    ack.completeExceptionally(cause);
                }
                return null;
            });
        } finally {
            sendLock.readLock().unlock();
        }
        return db;
    }

    /** A simple scheduled method that will clear old broadcasts. */
    @ScheduleWithFixedDelay(value = 1, unit = TimeUnit.MINUTES)
    public void clearOldMessages() {
        dispatchedBroadcasts.pruneMessagesOldThan(System.nanoTime() - TimeUnit.NANOSECONDS.convert(1, TimeUnit.HOURS));
    }

    private void onBroadcastAck(BroadcastAck ack) {
        DispatchedBroadcast f = dispatchedBroadcasts.find(ack.getAckForMessageId());
        if (f != null) { // Just ignore the ack if we do not have a matching dispatched broadcast
            f.acked(ack);
        }
    }

    /**
     * Invoked whenever a broadcast message is received from a remote actor.
     *
     * @param broadcast
     *            the broadcast that was received
     */
    private void onBroadcastMessage(Broadcast broadcast) {
        SubscriptionSet set = subscribers.get(broadcast.getBroadcastType());
        if (set != null && !set.listeners.isEmpty()) {
            final BroadcastMessage message;
            try {
                message = MmsMessage.tryRead(broadcast);
            } catch (Exception e) {
                LOG.error(
                        "Error while trying to deserialize an incoming broadcast message [text="
                                + MmsMessage.toText(broadcast) + "]", e);
                return;
            }
            MessageHeader header = new DefaultMessageHeader(MaritimeId.create(broadcast.getSenderId()),
                    broadcast.getMessageId(), broadcast.getSenderTimestamp(), broadcast.getSenderPosition());

            // Deliver to each listener
            for (SubscriptionSet.DefaultSubscription s : set.listeners) {
                threadManager.execute(() -> s.deliver(header, message));
            }
        }
    }

    private void checkNotShutdown() {
        if (isShutdown) {
            throw new MmsClientClosedException("The mms client has been shutdown");
        }
    }

    @RunOnStop
    public void stop() {
        subscribeLock.writeLock().lock();
        try {
            sendLock.writeLock().lock();
            try {
                isShutdown = true;
                // Cancel all acknowledgements
                MmsClientClosedException e = new MmsClientClosedException("Client has been shutdown");
                dispatchedBroadcasts.forEach(b -> b.shutdownClient(e));
                dispatchedBroadcasts.clear();
                subscribers.clear();
            } finally {
                sendLock.writeLock().unlock();
            }
        } finally {
            subscribeLock.writeLock().unlock();
        }
    }

    public void newSession() {
        // send subscriptions.

        // Spoergsmmalet er om vi skal serializer alle beskeder, dvs total order af beskeder?

        // Taenker den skal gemmes et sted paa connectionen istedet for
        // forEachEldestFirst(resend those that are not acked)
        // resend unacked broadcasts
    }
}
