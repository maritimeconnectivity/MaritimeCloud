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
import net.maritimecloud.internal.net.messages.BroadcastPublish;
import net.maritimecloud.internal.net.messages.BroadcastRelay;
import net.maritimecloud.net.broadcast.BroadcastMessage;
import net.maritimecloud.net.broadcast.BroadcastSendOptions;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.PositionTime;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Kasper Nielsen
 */
public class BroadcastHelper {

    public static BroadcastMessage tryRead(BroadcastRelay bd) throws Exception {
        Class<BroadcastMessage> cl = (Class<BroadcastMessage>) Class.forName(bd.getChannel());
        ObjectMapper om = new ObjectMapper();
        return om.readValue(bd.getMsg(), cl);
    }

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
        b.setId(sender.toString());
        b.setChannel(message.channel());
        b.setMsg(TMHelpers.persist(message));
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
