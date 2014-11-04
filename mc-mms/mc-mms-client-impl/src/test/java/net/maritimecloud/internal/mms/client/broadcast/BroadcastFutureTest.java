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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.mms.messages.PositionReport;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.net.messages.BroadcastAck;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.net.BroadcastMessage;
import net.maritimecloud.net.DispatchedMessage;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

import org.junit.Test;

/**
 * Tests the future returned by {@link MmsClient#broadcast(BroadcastMessage)} and
 * {@link MmsClient#broadcastSpecial(BroadcastMessage).
 *
 * @author Kasper Nielsen
 */
public class BroadcastFutureTest extends AbstractClientConnectionTest {

    @Test
    public void broadcastServerAck() throws Exception {
        MmsClient c = createAndConnect();

        DispatchedMessage bf = c.broadcast(new BroadcastTestMessage().setMsg("hello"));
        Broadcast mb = t.take(Broadcast.class);
        assertEquals("hello", ((BroadcastTestMessage) MmsMessage.tryRead(mb)).getMsg());

        PositionReport bsa = new PositionReport();
        t.send(bsa, 0, 1);

        // make sure it is received on the server
        bf.relayed().timeout(1, TimeUnit.SECONDS).join();
    }

    @Test
    public void broadcastClientAcks() throws Exception {
        final BlockingQueue<MessageHeader> q = new LinkedBlockingQueue<>();

        MmsClient c = createAndConnect();


        DispatchedMessage bf = c.withBroadcast(new BroadcastTestMessage().setMsg("hello"))
                .onRemoteReceive(e -> q.add(e)).send();
        Broadcast mb = t.take(Broadcast.class);
        assertEquals("hello", ((BroadcastTestMessage) MmsMessage.tryRead(mb)).getMsg());

        PositionReport bsa = new PositionReport();

        t.send(bsa, 0, 1);

        // make sure it is received on the server
        bf.relayed().timeout(1, TimeUnit.SECONDS).join();

        BroadcastAck ba = new BroadcastAck();
        ba.setReceiverId(ID3.toString());
        ba.setReceiverPosition(Position.create(3, 3));
        ba.setReceiverTimestamp(Timestamp.create(3));
        ba.setAckForMessageId(bf.getMessageId());
        t.send(ba, 0, 0);

        // ba = new BroadcastAck(mb.getReplyTo(), ID4, PositionTime.create(4, 4, 4));
        ba = new BroadcastAck();
        ba.setReceiverId(ID4.toString());
        ba.setReceiverPosition(Position.create(4, 4));
        ba.setReceiverTimestamp(Timestamp.create(4));
        ba.setAckForMessageId(bf.getMessageId());
        t.send(ba, 0, 0);

        MessageHeader a3 = q.poll(1, TimeUnit.SECONDS);
        assertEquals(ID3, a3.getSender());
        assertEquals(Position.create(3, 3), a3.getSenderPosition());
        assertEquals(Timestamp.create(3), a3.getSenderTime());

        MessageHeader a4 = q.poll(1, TimeUnit.SECONDS);
        assertEquals(ID4, a4.getSender());
        assertEquals(Position.create(4, 4), a4.getSenderPosition());
        assertEquals(Timestamp.create(4), a4.getSenderTime());

        assertTrue(q.isEmpty());
    }
}
