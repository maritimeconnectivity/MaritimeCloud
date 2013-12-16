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

import java.util.concurrent.atomic.AtomicLong;

import net.maritimecloud.net.broadcast.BroadcastListener;
import net.maritimecloud.net.broadcast.BroadcastMessage;
import net.maritimecloud.net.broadcast.BroadcastMessageHeader;
import net.maritimecloud.net.broadcast.BroadcastSubscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of {@link BroadcastSubscription}.
 * 
 * @author Kasper Nielsen
 */
class BroadcastMessageSubscription implements BroadcastSubscription {

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(BroadcastMessageSubscription.class);

    /** The broadcast manager. */
    private final BroadcastManager broadcastManager;

    /** The type of broadcast messages. */
    private final String channel;

    /** The number of messages received. */
    private final AtomicLong count = new AtomicLong();

    /** The listener. */
    private final BroadcastListener<? extends BroadcastMessage> listener;

    BroadcastMessageSubscription(BroadcastManager broadcastManager, String channel,
            BroadcastListener<? extends BroadcastMessage> listener) {
        this.broadcastManager = requireNonNull(broadcastManager);
        this.channel = requireNonNull(channel);
        this.listener = requireNonNull(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void cancel() {
        broadcastManager.listeners.get(channel).remove(this);
    }

    // invoked in another thread so never throw anything
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void deliver(BroadcastMessageHeader broadcastHeader, BroadcastMessage message) {
        try {
            ((BroadcastListener) listener).onMessage(broadcastHeader, message);
            count.incrementAndGet();
        } catch (Exception e) {
            LOG.error("Exception while handling an incoming broadcast message of type " + message.getClass(), e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getChannel() {
        return channel;
    }

    /** {@inheritDoc} */
    @Override
    public long getNumberOfReceivedMessages() {
        return count.get();
    }
}
