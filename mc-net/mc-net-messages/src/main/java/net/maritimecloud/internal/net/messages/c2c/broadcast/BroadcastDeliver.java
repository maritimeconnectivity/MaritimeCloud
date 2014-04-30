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
import net.maritimecloud.internal.net.messages.TMHelpers;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.net.broadcast.BroadcastMessage;
import net.maritimecloud.util.geometry.PositionTime;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This message is send from the server to the client because the client was in proximity of broadcast that was sent to
 * the server.
 *
 * @author Kasper Nielsen
 * @see BroadcastSend
 */
public class BroadcastDeliver extends ConnectionOldMessage {

    final String channel;

    final MaritimeId id;

    final String message;

    final PositionTime positionTime;

    /**
     * @param messageType
     */
    public BroadcastDeliver(MaritimeId id, PositionTime position, String channel, String message) {
        super(MessageType.BROADCAST_DELIVER);
        this.id = requireNonNull(id);
        this.positionTime = requireNonNull(position);
        this.channel = requireNonNull(channel);
        this.message = requireNonNull(message);
    }

    /**
     * @param messageType
     * @throws IOException
     */
    public BroadcastDeliver(TextMessageReader pr) throws IOException {
        super(MessageType.BROADCAST_DELIVER, pr);
        this.id = requireNonNull(MaritimeId.create(pr.takeString()));
        this.positionTime = requireNonNull(PositionTime.create(pr.takeDouble(), pr.takeDouble(), pr.takeLong()));
        this.channel = requireNonNull(pr.takeString());
        this.message = requireNonNull(pr.takeString());
    }

    public BroadcastDeliver cloneIt() {
        return new BroadcastDeliver(id, positionTime, channel, TMHelpers.escape(message));
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    @SuppressWarnings("unchecked")
    public Class<BroadcastMessage> getClassFromChannel() throws ClassNotFoundException {
        return (Class<BroadcastMessage>) Class.forName(channel);
    }

    /**
     * @return the id
     */
    public MaritimeId getId() {
        return id;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the positionTime
     */
    public PositionTime getPositionTime() {
        return positionTime;
    }

    public BroadcastMessage tryRead() throws Exception {
        Class<BroadcastMessage> cl = getClassFromChannel();
        ObjectMapper om = new ObjectMapper();
        return om.readValue(getMessage(), cl);
    }

    /** {@inheritDoc} */
    @Override
    protected void write0(TextMessageWriter w) {
        w.writeString(id.toString());
        w.writeDouble(positionTime.getLatitude());
        w.writeDouble(positionTime.getLongitude());
        w.writeLong(positionTime.getTime());
        w.writeString(channel);
        w.writeString(message);
    }

    public static BroadcastDeliver create(MaritimeId sender, PositionTime position, String channel, String message) {
        return new BroadcastDeliver(sender, position, channel, TMHelpers.escape(message));
    }
}
