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
package net.maritimecloud.internal.net.messages.spi;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.internal.net.messages.BroadcastPublish;
import net.maritimecloud.internal.net.messages.BroadcastRelay;
import net.maritimecloud.net.broadcast.BroadcastMessage;
import net.maritimecloud.net.broadcast.BroadcastSendOptions;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.PositionTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 *
 * @author Kasper Nielsen
 */
public class MessageHelpers {

    public static TransportMessage parseMessage(String msg) {
        int io = msg.indexOf(':');
        String t = msg.substring(0, io);
        msg = msg.substring(io + 1);
        int type = Integer.parseInt(t);// pr.takeInt();
        MessageParser<? extends TransportMessage> p = MessageType.getParser(type);
        // System.out.println("Got " + msg);
        TransportMessage tm = MessageSerializers.readFromJSON(p, msg);
        return tm;
    }

    public static String persist(Object o) {
        ObjectMapper om = new ObjectMapper();
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            return om.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not be persisted", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static BroadcastMessage tryRead(BroadcastRelay bd) throws Exception {
        Class<BroadcastMessage> cl = (Class<BroadcastMessage>) Class.forName(bd.getChannel());
        ObjectMapper om = new ObjectMapper();
        return om.readValue(bd.getMsg(), cl);
    }


    @SuppressWarnings("unchecked")
    public static BroadcastMessage tryRead(BroadcastPublish bd) throws Exception {
        Class<BroadcastMessage> cl = (Class<BroadcastMessage>) Class.forName(bd.getChannel());
        ObjectMapper om = new ObjectMapper();
        return om.readValue(bd.getMsg(), cl);
    }


    public static BroadcastPublish create(MaritimeId sender, PositionTime position, BroadcastMessage message,
            BroadcastSendOptions options) {
        Area broadcastArea = options.getBroadcastArea();
        if (broadcastArea == null) {
            broadcastArea = Circle.create(position, options.getBroadcastRadius());
        }
        BroadcastPublish b = new BroadcastPublish();
        b.setPositionTime(position);
        b.setId(sender.toString());
        b.setChannel(message.channel());

        b.setMsg(MessageSerializers.writeToJSON(message));
        // b.setMsg(MessageHelpers.persist(message));


        b.setArea(broadcastArea);
        b.setReceiverAck(options.isReceiverAckEnabled());
        // These are set for tests
        b.setMessageId(0L);
        b.setLatestReceivedId(0L);
        return b;
        // return new BroadcastSend(sender, position, message.channel(), TMHelpers.persistAndEscape(message),
        // broadcastArea, options.isReceiverAckEnabled());
    }
}
