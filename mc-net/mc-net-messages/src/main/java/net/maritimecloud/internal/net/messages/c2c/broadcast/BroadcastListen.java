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

import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.internal.net.messages.s2c.ServerRequestMessage;
import net.maritimecloud.util.geometry.Area;

/**
 * 
 * @author Kasper Nielsen
 */
public class BroadcastListen extends ServerRequestMessage<BroadcastListenAck> {

    final Area area;

    final String channel;

    /**
     * @param messageType
     */
    public BroadcastListen(String channel, Area area) {
        super(MessageType.BROADCAST_LISTEN);
        this.channel = requireNonNull(channel);
        this.area = requireNonNull(area);
    }

    // Area
    public BroadcastListen(TextMessageReader pr) throws IOException {
        super(MessageType.BROADCAST_LISTEN, pr);
        this.channel = requireNonNull(pr.takeString());
        this.area = requireNonNull(pr.takeArea());
    }

    public BroadcastListenAck createReply() {
        return new BroadcastListenAck(getReplyTo());
    }

    /**
     * @return the area
     */
    public Area getArea() {
        return area;
    }

    public String getChannel() {
        return channel;
    }

    /** {@inheritDoc} */
    @Override
    protected void write1(TextMessageWriter w) {
        w.writeString(channel);
        w.writeArea(area);
    }
}
