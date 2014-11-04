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

import java.util.function.Consumer;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.net.messages.BroadcastAck;
import net.maritimecloud.internal.net.util.DefaultAcknowledgement;
import net.maritimecloud.internal.net.util.DefaultDispatchedMessage;
import net.maritimecloud.internal.net.util.DefaultMessageHeader;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.net.mms.MmsClientClosedException;

/**
 * A dispatched broadcast.
 *
 * @author Kasper Nielsen
 */
class DispatchedBroadcast extends DefaultDispatchedMessage {

    /** The logger. */
    private static final Logger LOG = Logger.get(DispatchedBroadcast.class);

    /** An optional consumer of broadcast acks. */
    final Consumer<? super MessageHeader> ackConsumer;

    /** The type of the broadcast. */
    final String broadcastType;

    DispatchedBroadcast(Broadcast broadcast, DefaultAcknowledgement acknowledgement,
            Consumer<? super MessageHeader> consumer) {
        super(broadcast.getMessageId(), broadcast.getSenderPosition(), broadcast.getSenderTimestamp(), acknowledgement);
        this.ackConsumer = consumer;
        this.broadcastType = broadcast.getBroadcastType();
    }

    /**
     * Invoked whenever the MMS client is shutdown.
     *
     * @param e
     *            the close exception that we should complete with
     */
    void shutdownClient(MmsClientClosedException e) {
        super.acknowledgement.completeExceptionally(e); // only completes if not allready done so
    }

    /**
     * This method is invoked whenever we receive an ack from a remote client.
     *
     * @param ack
     *            the ack that we received
     */
    void acked(BroadcastAck ack) {
        if (ackConsumer != null) { // only makes sense if we have ack consumer
            try {
                MaritimeId id = MaritimeId.create(ack.getReceiverId());
                MessageHeader header = new DefaultMessageHeader(id, ack.getAckForMessageId(),
                        ack.getReceiverTimestamp(), ack.getReceiverPosition());
                if (!acknowledgement.isDone()) {
                    // highly unlikely we will get an ack back before the actual acknowledgement.
                    // But not impossible. So complete it just in case
                    acknowledgement.complete(null);
                }
                ackConsumer.accept(header);
            } catch (Exception e) {
                LOG.error("Failed to process broadcast ack for id = " + ack.getAckForMessageId(), e);
            }
        }
    }
}
