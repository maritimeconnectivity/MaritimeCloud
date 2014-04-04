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
import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.PositionTimeMessage;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.internal.net.messages.s2c.ServerRequestMessage;
import net.maritimecloud.internal.net.util.RelativeCircularArea;
import net.maritimecloud.net.broadcast.BroadcastMessage;
import net.maritimecloud.net.broadcast.BroadcastSendOptions;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.PositionTime;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This message is send from the client to server. Unlike {@link BroadcastDeliver} which is the relay message from the
 * server to the clients that need to receive the broadcast.
 * 
 * @author Kasper Nielsen
 */
public class BroadcastSend extends ServerRequestMessage<BroadcastSendAck> implements PositionTimeMessage {

    final String channel;

    final Area area;

    final MaritimeId id;

    final String message;

    final PositionTime positionTime;

    final boolean receiverAck;

    /**
     * @return the receiverAck
     */
    public boolean isReceiverAck() {
        return receiverAck;
    }

    /**
     * @param messageType
     */
    public BroadcastSend(MaritimeId id, PositionTime position, String channel, String message, Area area,
            boolean receiverAck) {
        super(MessageType.BROADCAST_SEND);
        this.id = requireNonNull(id);
        this.positionTime = requireNonNull(position);
        this.channel = requireNonNull(channel);
        this.message = requireNonNull(message);
        this.area = area;
        this.receiverAck = receiverAck;
    }

    /**
     * @param messageType
     * @throws IOException
     */
    public BroadcastSend(TextMessageReader pr) throws IOException {
        super(MessageType.BROADCAST_SEND, pr);
        this.id = requireNonNull(MaritimeId.create(pr.takeString()));
        this.positionTime = requireNonNull(PositionTime.create(pr.takeDouble(), pr.takeDouble(), pr.takeLong()));
        this.channel = requireNonNull(pr.takeString());
        this.message = requireNonNull(pr.takeString());
        this.area = pr.takeArea();
        this.receiverAck = pr.takeBoolean();
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    //
    // public BroadcastSend cloneIt() {
    // return new BroadcastSend(id, positionTime, channel, escape(message));
    // }

    @SuppressWarnings("unchecked")
    public Class<BroadcastMessage> getClassFromChannel() throws ClassNotFoundException {
        return (Class<BroadcastMessage>) Class.forName(channel);
    }

    /**
     * @return the distance
     */
    public Area getArea() {
        return area;
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

    public <T extends BroadcastMessage> T tryRead(Class<T> type) throws Exception {
        return type.cast(tryRead());
    }

    /** {@inheritDoc} */
    @Override
    protected void write1(TextMessageWriter w) {
        w.writeString(id.toString());
        w.writeDouble(positionTime.getLatitude());
        w.writeDouble(positionTime.getLongitude());
        w.writeLong(positionTime.getTime());
        w.writeString(channel);
        w.writeString(message);
        w.writeArea(area);
        w.writeBoolean(receiverAck);
    }

    public static BroadcastSend create(MaritimeId sender, PositionTime position, BroadcastMessage message,
            BroadcastSendOptions options) {
        Area broadcastArea = options.getBroadcastArea();
        if (broadcastArea == null) {
            broadcastArea = new RelativeCircularArea(options.getBroadcastRadius());
        }
        return new BroadcastSend(sender, position, message.channel(), persistAndEscape(message), broadcastArea,
                options.isReceiverAckEnabled());
    }

    public BroadcastSendAck createReply() {
        return new BroadcastSendAck(getReplyTo());
    }
}
