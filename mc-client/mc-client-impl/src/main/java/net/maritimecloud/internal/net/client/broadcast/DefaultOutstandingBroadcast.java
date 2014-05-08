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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.util.DefaultConnectionFuture;
import net.maritimecloud.internal.net.client.util.ThreadManager;
import net.maritimecloud.internal.net.messages.BroadcastPublicRemoteAck;
import net.maritimecloud.net.NetworkFuture;
import net.maritimecloud.net.broadcast.BroadcastFuture;
import net.maritimecloud.net.broadcast.BroadcastMessageReceived;
import net.maritimecloud.net.broadcast.BroadcastSendOptions;
import net.maritimecloud.util.function.Consumer;
import net.maritimecloud.util.geometry.PositionTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of {@link BroadcastFuture}.
 *
 * @author Kasper Nielsen
 */
class DefaultOutstandingBroadcast implements BroadcastFuture {

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(BroadcastManager.class);

    /** A list of all ACKs we have received. */
    private final List<BroadcastMessageReceived> acks = new ArrayList<>();

    /** All registered consumers. */
    private final CopyOnWriteArrayList<Consumer<? super BroadcastMessageReceived>> consumers = new CopyOnWriteArrayList<>();

    /** The main lock. */
    private final ReentrantLock lock = new ReentrantLock();

    /** The options for this broadcast. */
    private final BroadcastSendOptions options;

    /** A connection future used to determined if the broadcast message has been received on the server */
    final DefaultConnectionFuture<Void> receivedOnServer;

    DefaultOutstandingBroadcast(ThreadManager tm, BroadcastSendOptions options) {
        this.receivedOnServer = tm.create();
        this.options = requireNonNull(options);
    }

    /** {@inheritDoc} */
    @Override
    public void onReceived(Consumer<? super BroadcastMessageReceived> consumer) {
        requireNonNull(consumer);
        if (!options.isReceiverAckEnabled()) {
            throw new UnsupportedOperationException("Receiver ack is not enabled, must be set in BroadcastOptions");
        }
        lock.lock();
        try {
            consumers.add(consumer);
            // We need to replay acks in case someone have already acked a messages.
            // before this method is called (big GC pause, for example)
            for (BroadcastMessageReceived ack : acks) {
                try {
                    consumer.accept(ack);
                } catch (Exception e) {
                    LOG.error("Failed to process broadcast ack", e);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    void onAckMessage(BroadcastPublicRemoteAck ack) {
        final PositionTime pt = ack.getPositionTime();
        final MaritimeId mid = MaritimeId.create(ack.getId());
        onAckMessage0(new BroadcastMessageReceived() {
            public MaritimeId getId() {
                return mid;
            }

            public PositionTime getPosition() {
                return pt;
            }
        });
    }

    private void onAckMessage0(BroadcastMessageReceived ack) {
        requireNonNull(ack);
        lock.lock();
        try {
            acks.add(ack);
            for (Consumer<? super BroadcastMessageReceived> consumer : consumers) {
                try {
                    consumer.accept(ack);
                } catch (Exception e) {
                    LOG.error("Failed to process broadcast ack", e);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public NetworkFuture<Void> receivedOnServer() {
        return receivedOnServer;
    }
}
