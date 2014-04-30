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
package net.maritimecloud.internal.net.messages.c2c.broadcast;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.messages.ConnectionOldMessage;
import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.util.geometry.PositionTime;

/**
 * 
 * @author Kasper Nielsen
 */
public class BroadcastAck extends ConnectionOldMessage {

    final long broadcastId;

    final MaritimeId id;

    final PositionTime positionTime;

    public BroadcastAck(long broadcastId, MaritimeId id, PositionTime position) {
        super(MessageType.BROADCAST_DELIVER_ACK);
        this.broadcastId = broadcastId;
        this.id = requireNonNull(id);
        this.positionTime = requireNonNull(position);
    }

    /**
     * @param messageType
     * @throws IOException
     */
    public BroadcastAck(TextMessageReader pr) throws IOException {
        super(MessageType.BROADCAST_DELIVER_ACK, pr);
        this.broadcastId = pr.takeLong();
        this.id = requireNonNull(MaritimeId.create(pr.takeString()));
        this.positionTime = requireNonNull(PositionTime.create(pr.takeDouble(), pr.takeDouble(), pr.takeLong()));

    }

    /**
     * @return the broadcastId
     */
    public long getBroadcastId() {
        return broadcastId;
    }

    /**
     * @return the id
     */
    public MaritimeId getId() {
        return id;
    }

    /**
     * @return the positionTime
     */
    public PositionTime getPositionTime() {
        return positionTime;
    }

    /** {@inheritDoc} */
    @Override
    protected void write0(TextMessageWriter w) {
        w.writeLong(broadcastId);
        w.writeString(id.toString());
        w.writeDouble(positionTime.getLatitude());
        w.writeDouble(positionTime.getLongitude());
        w.writeLong(positionTime.getTime());
    }

}
