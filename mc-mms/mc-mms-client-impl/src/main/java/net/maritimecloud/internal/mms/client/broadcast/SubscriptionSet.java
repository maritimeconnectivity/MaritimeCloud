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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import net.maritimecloud.internal.util.Coverage;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.net.BroadcastConsumer;
import net.maritimecloud.net.BroadcastMessage;
import net.maritimecloud.net.BroadcastSubscription;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.util.Binary;

/**
 * A set of 1 or more subscribers of a particular type.
 *
 * @author Kasper Nielsen
 */
class SubscriptionSet {

    /** The logger. */
    static final Logger LOG = Logger.get(SubscriptionSet.class);

    /** The broadcast manager. */
    final ClientBroadcastManager broadcastManager;

    /** The type of broadcast messages. */
    final String broadcastType;

    /** A list of all listeners for the particular type. */
    final CopyOnWriteArrayList<DefaultSubscription> listeners = new CopyOnWriteArrayList<>();

    volatile Set<BroadcastDeserializer> deserializers = new HashSet<>();

    SubscriptionSet(ClientBroadcastManager broadcastManager, String broadcastType) {
        this.broadcastManager = requireNonNull(broadcastManager);
        this.broadcastType = requireNonNull(broadcastType);
    }

    BroadcastSubscription newSubscription(BroadcastDeserializer bd,
            BroadcastConsumer<? extends BroadcastMessage> listener, Coverage coverage) {
        DefaultSubscription bs = new DefaultSubscription(bd, listener, coverage);
        listeners.add(bs);
        return bs;
    }

    void remove(DefaultSubscription s) {
        broadcastManager.subscribeLock.readLock().lock();
        try {
            listeners.remove(s);
        } finally {
            broadcastManager.subscribeLock.readLock().unlock();
        }
    }

    class DefaultSubscription implements BroadcastSubscription {

        /** The number of broadcast messages received. */
        private final AtomicLong count = new AtomicLong();

        /** A unique id of this subscription. */
        private final Binary id = Binary.random(32);

        /** The listener. */
        private final BroadcastConsumer<? extends BroadcastMessage> listener;

        final Coverage coverage;

        final BroadcastDeserializer bd;

        DefaultSubscription(BroadcastDeserializer bd, BroadcastConsumer<? extends BroadcastMessage> listener,
                Coverage coverage) {
            this.bd = requireNonNull(bd);
            this.listener = requireNonNull(listener);
            this.coverage = coverage;
        }

        /** {@inheritDoc} */
        @Override
        public void cancel() {
            remove(this);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        void deliver(MessageHeader broadcastHeader, BroadcastMessage message) {
            // Vi har et problem, hvis vi ikke sender et evt, broadcast area med...
            // Naar man broadcaster til et area har man ingen position med, Vi bliver sgu noedt til at sende et area
            // med.

            if (broadcastHeader.getSenderPosition() == null || coverage.isCovered(broadcastHeader.getSenderPosition())) {
                try {
                    ((BroadcastConsumer) listener).onMessage(broadcastHeader, message);
                    count.incrementAndGet();
                } catch (Exception e) {
                    // Never throw anything, we are invoked from a dispatcher thread
                    LOG.error("Exception while handling an incoming broadcast message of type " + message.getClass(), e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public Binary getId() {
            return id;
        }

        /** {@inheritDoc} */
        @Override
        public long getNumberOfReceivedMessages() {
            return count.get();
        }

        /** {@inheritDoc} */
        @Override
        public String getBroadcastType() {
            return broadcastType;
        }
    }
}
