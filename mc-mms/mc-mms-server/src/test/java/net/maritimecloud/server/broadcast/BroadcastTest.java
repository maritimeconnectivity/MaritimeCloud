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
package net.maritimecloud.server.broadcast;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.internal.mms.messages.PositionReport;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.net.BroadcastMessage;
import net.maritimecloud.server.AbstractServerConnectionTest;
import net.maritimecloud.server.TesstEndpoint;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class BroadcastTest extends AbstractServerConnectionTest {

    @Test
    public void foo() {
        BroadcastTestMessage hw = new BroadcastTestMessage().setMsg("foo1");
        Broadcast bp = createBroadcast(ID1, PositionTime.create(1, 1, 1), hw, null, 10, null);
        System.out.println(bp.toJSON());

        Broadcast bp2 = MessageSerializer.readFromJSON(Broadcast.SERIALIZER, bp.toJSON());
        System.out.println(bp2.toJSON());

        System.out.println(bp.equals(bp2));
        System.out.println(bp.getArea().equals(bp2.getArea()));

    }

    @Test
    public void oneClient() throws Exception {
        TesstEndpoint c1 = newClient(ID1);
        TesstEndpoint c2 = newClient(ID6);

        BroadcastTestMessage hw = new BroadcastTestMessage().setMsg("foo1");
        c1.send(createBroadcast(ID1, PositionTime.create(1, 1, 1), hw, null, 10, null));

        Broadcast bd = c2.take(Broadcast.class);
        BroadcastTestMessage tryRead = (BroadcastTestMessage) MmsMessage.tryRead(bd);
        assertEquals("foo1", tryRead.getMsg());

        c1.take(PositionReport.class);
    }

    @Test
    public void twoClients() throws Exception {
        TesstEndpoint c1 = newClient(ID1);
        TesstEndpoint c3 = newClient(ID3);
        TesstEndpoint c4 = newClient(ID4);

        BroadcastTestMessage hw = new BroadcastTestMessage().setMsg("foo1");
        c1.send(createBroadcast(ID1, PositionTime.create(1, 1, 1), hw, null, 100, null));

        assertEquals("foo1", ((BroadcastTestMessage) MmsMessage.tryRead(c3.take(Broadcast.class))).getMsg());
        assertEquals("foo1", ((BroadcastTestMessage) MmsMessage.tryRead(c4.take(Broadcast.class))).getMsg());

        c1.take(PositionReport.class);
    }

    @Test
    public void areaBroadcast() throws Exception {
        TesstEndpoint c1 = newClient(ID1, 1, 1);
        TesstEndpoint c3 = newClient(ID3, 1.63, 1.63);
        TesstEndpoint c4 = newClient(ID4, 1.64, 1.64);

        c1.send(createBroadcast(ID1, PositionTime.create(1, 1, 1), new BroadcastTestMessage().setMsg("foo1"), null,
                100000, null));

        assertEquals("foo1", ((BroadcastTestMessage) MmsMessage.tryRead(c3.take(Broadcast.class))).getMsg());

        // send message with c4 with 110 kilometers
        c1.send(createBroadcast(ID1, PositionTime.create(1, 1, 1), new BroadcastTestMessage().setMsg("foo2"), null,
                110000, null));

        assertEquals("foo2", ((BroadcastTestMessage) MmsMessage.tryRead(c3.take(Broadcast.class))).getMsg());
        assertEquals("foo2", ((BroadcastTestMessage) MmsMessage.tryRead(c4.take(Broadcast.class))).getMsg());


        c1.take(PositionReport.class);
        c1.take(PositionReport.class);
    }


    public static Broadcast createBroadcast(MaritimeId sender, PositionTime position, BroadcastMessage message,
            Area area, int radius, Consumer<?> consumer) {
        Area broadcastArea = area;
        if (broadcastArea == null) {
            broadcastArea = Circle.create(position, radius);
        }
        Broadcast b = new Broadcast();
        b.setSenderPosition(position);
        b.setSenderTimestamp(position.timestamp());
        b.setSenderId(sender.toString());
        b.setBroadcastType(message.getClass().getName());
        b.setMessageId(Binary.random(32));
        b.setPayload(Binary.copyFromUtf8(MessageSerializer.writeToJSON(message, MessageHelper.getSerializer(message))));
        // b.setMsg(MessageHelpers.persist(message));


        b.setArea(broadcastArea);
        b.setAckBroadcast(consumer != null);
        // These are set for tests
        return b;
        // return new BroadcastSend(sender, position, message.channel(), TMHelpers.persistAndEscape(message),
        // broadcastArea, options.isReceiverAckEnabled());
    }

}
