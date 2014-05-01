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
package net.maritimecloud.internal.net.client.broadcast;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.ClientContainer;
import net.maritimecloud.internal.net.client.connection.ConnectionMessageBus;
import net.maritimecloud.internal.net.client.connection.OnMessage;
import net.maritimecloud.internal.net.client.service.PositionManager;
import net.maritimecloud.internal.net.client.util.CustomConcurrentHashMap;
import net.maritimecloud.internal.net.client.util.CustomConcurrentHashMap.Strength;
import net.maritimecloud.internal.net.client.util.DefaultConnectionFuture;
import net.maritimecloud.internal.net.client.util.ThreadManager;
import net.maritimecloud.internal.net.messages.BroadcastPublicRemoteAck;
import net.maritimecloud.internal.net.messages.BroadcastPublish;
import net.maritimecloud.internal.net.messages.BroadcastPublishAck;
import net.maritimecloud.internal.net.messages.BroadcastRelay;
import net.maritimecloud.internal.net.messages.spi.BroadcastHelper;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.broadcast.BroadcastFuture;
import net.maritimecloud.net.broadcast.BroadcastListener;
import net.maritimecloud.net.broadcast.BroadcastMessage;
import net.maritimecloud.net.broadcast.BroadcastMessageHeader;
import net.maritimecloud.net.broadcast.BroadcastSendOptions;
import net.maritimecloud.net.broadcast.BroadcastSubscription;
import net.maritimecloud.util.function.BiConsumer;
import net.maritimecloud.util.geometry.Area;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages sending and receiving of broadcasts.
 *
 * @author Kasper Nielsen
 */
public class BroadcastManager {

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(BroadcastManager.class);

    /** Broadcast that have been sent. Will be cleared out as the user drops a reference to them */
    private final ConcurrentMap<Long, DefaultOutstandingBroadcast> outstandingBroadcasts = new CustomConcurrentHashMap<>(
            Strength.strong, Strength.weak);


    /** The client */
    private final ClientContainer client;

    /** The network */
    private final ConnectionMessageBus connection;

    /** A map of local broadcast listeners. ChannelName -> List of listeners. */
    final ConcurrentHashMap<String, CopyOnWriteArraySet<BroadcastMessageSubscription>> listeners = new ConcurrentHashMap<>();

    /** Maintains latest position for the client. */
    private final PositionManager positionManager;

    /** Thread manager takes care of asynchronous processing. */
    private final ThreadManager threadManager;

    /**
     * Creates a new instance of this class.
     *
     * @param network
     *            the network
     */
    public BroadcastManager(PositionManager positionManager, ThreadManager threadManager, ClientContainer client,
            ConnectionMessageBus connection) {
        this.connection = requireNonNull(connection);
        this.positionManager = requireNonNull(positionManager);
        this.threadManager = requireNonNull(threadManager);
        this.client = requireNonNull(client);
    }

    /**
     * Sets up listeners for incoming broadcast messages.
     *
     * @param messageType
     *            the type of message to receive
     * @param listener
     *            the callback listener
     * @return a subscription
     * @see MaritimeCloudClient#broadcastListen(Class, BroadcastListener)
     */
    public <T extends BroadcastMessage> BroadcastSubscription listenFor(Class<T> messageType,
            BroadcastListener<T> listener) {
        requireNonNull(messageType, "messageType is null");
        requireNonNull(listener, "listener is null");

        String channelName = messageType.getCanonicalName();

        BroadcastMessageSubscription sub = new BroadcastMessageSubscription(this, channelName, listener);

        listeners.putIfAbsent(messageType.getCanonicalName(), new CopyOnWriteArraySet<BroadcastMessageSubscription>());
        CopyOnWriteArraySet<BroadcastMessageSubscription> set = listeners.get(messageType.getCanonicalName());
        set.add(sub);
        return sub;
    }


    @OnMessage
    public void onBroadcastAck(BroadcastPublicRemoteAck ack) {
        DefaultOutstandingBroadcast f = outstandingBroadcasts.get(ack.getBroadcastId());
        // if we do not have a valid outstanding broadcast just ignore the ack
        if (f != null) {
            f.onAckMessage(ack);
        }
    }

    /**
     * Invoked whenever a broadcast message is received from a remote actor.
     *
     * @param broadcast
     *            the broadcast that was received
     */
    @OnMessage
    public void onBroadcastMessage(BroadcastRelay broadcast) {
        // Find out if we actually listens for it
        CopyOnWriteArraySet<BroadcastMessageSubscription> set = listeners.get(broadcast.getChannel());
        if (set != null && !set.isEmpty()) {
            BroadcastMessage bm = null;
            try {
                bm = BroadcastHelper.tryRead(broadcast);
            } catch (Exception e) {
                LOG.error("Exception while trying to deserialize an incoming broadcast message ", e);
                LOG.error(broadcast.toText());
                return;
            }

            final BroadcastMessage bmm = bm;
            final BroadcastMessageHeader bp = new BroadcastMessageHeader(MaritimeId.create(broadcast.getId()),
                    broadcast.getPositionTime());

            // Deliver to each listener
            for (final BroadcastMessageSubscription s : set) {
                threadManager.execute(new Runnable() {
                    public void run() {
                        s.deliver(bp, bmm); // deliver() handles any exception
                    }
                });
            }
        }
    }

    public BroadcastFuture sendBroadcastMessage(BroadcastMessage broadcast, BroadcastSendOptions options) {
        requireNonNull(broadcast, "broadcast is null");
        requireNonNull(options, "options is null");
        options = options.immutable(); // we make the options immutable just in case

        // create the message we will send to the server
        // BroadcastPublish b = new BroadcastPublish();
        // b.setId(client.getLocalId().toString());
        // b.setPositionTime(positionManager.getPositionTime());
        // b.setMsg(null)
        BroadcastPublish b = BroadcastHelper.create(client.getLocalId(), positionManager.getPositionTime(), broadcast,
                options);

        DefaultConnectionFuture<BroadcastPublishAck> response = connection.sendMessage(BroadcastPublishAck.class, b);

        final DefaultOutstandingBroadcast dbf = new DefaultOutstandingBroadcast(threadManager, options);
        outstandingBroadcasts.put(b.getReplyTo(), dbf);

        response.handle(new BiConsumer<BroadcastPublishAck, Throwable>() {
            public void accept(BroadcastPublishAck ack, Throwable cause) {
                if (ack != null) {
                    dbf.receivedOnServer.complete(null);
                } else {
                    dbf.receivedOnServer.completeExceptionally(cause);
                    // remove from broadcasts??
                }
            }
        });
        return dbf;
    }

    public <T extends BroadcastMessage> BroadcastSubscription broadcastListen(Class<T> messageType,
            BroadcastListener<T> consumer, Area area) {
        throw new UnsupportedOperationException();
        // String channelName = messageType.getCanonicalName();
        // final DefaultConnectionFuture<BroadcastListenAck> f = connection.sendMessage(new BroadcastListen(channelName,
        // area));

        // f.thenAcceptAsync(new DefaultConnectionFuture.Action<RegisterServiceResult>() {
        // public void accept(RegisterServiceResult ack) {
        // reg.replied.countDown();
        // }
        // });
        // return reg;
    }
}
