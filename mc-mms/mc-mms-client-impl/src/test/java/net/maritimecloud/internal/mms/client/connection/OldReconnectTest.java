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
package net.maritimecloud.internal.mms.client.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

import org.junit.Ignore;
import org.junit.Test;


/**
 *
 * @author Kasper Nielsen
 */
public class OldReconnectTest extends AbstractClientConnectionTest {

    @Test
    public void messageId() throws Exception {
        MmsClient c = createAndConnect();

        c.broadcast(new BroadcastTestMessage().setMsg("hello"));
        MmsMessage mm = t.t();
        assertEquals(1L, mm.getMessageId());
        assertEquals(0L, mm.getLatestReceivedId());

        c.broadcast(new BroadcastTestMessage().setMsg("hello"));
        mm = t.t();
        assertEquals(2L, mm.getMessageId());
        assertEquals(0L, mm.getLatestReceivedId());
    }

    @Test
    public void messageIdMany() throws Exception {
        MmsClient c = createAndConnect();
        for (int i = 0; i < 200; i++) {
            c.broadcast(new BroadcastTestMessage().setMsg("hello"));
            MmsMessage mm = t.t();
            assertEquals(i + 1, mm.getMessageId());
            assertEquals(0L, mm.getLatestReceivedId());
        }
    }

    @Test
    public void messageAck() throws Exception {
        MmsClient c = createAndConnect();
        final CountDownLatch cdl1 = new CountDownLatch(1);
        final CountDownLatch cdl3 = new CountDownLatch(3);
        c.broadcastSubscribe(BroadcastTestMessage.class, (header, m) -> {
            cdl1.countDown();
            cdl3.countDown();
        });

        // BroadcastDeliver bm = new BroadcastDeliver(ID3, PositionTime.create(2, 1, 4),
        // BroadcastTestMessage.class.getCanonicalName(), persistAndEscape(new BroadcastTestMessage("A")));
        Broadcast bm = new Broadcast();
        bm.setBroadcastType(BroadcastTestMessage.class.getCanonicalName());
        bm.setPayload(Binary.copyFromUtf8(new BroadcastTestMessage().setMsg("A").toJSON()));
        bm.setMessageId(Binary.random(32));

        bm.setSenderPosition(Position.create(2, 1));
        bm.setSenderTimestamp(Timestamp.create(4));


        bm.setSenderId(ID3.toString());


        t.send(bm, 1, 0);
        assertTrue(cdl1.await(1, TimeUnit.SECONDS));

        Thread.sleep(1000);
        c.broadcast(new BroadcastTestMessage().setMsg("hello"));
        MmsMessage mm = t.t();
        assertEquals(1L, mm.getMessageId());
        assertEquals(1L, mm.getLatestReceivedId());

        t.send(bm, 2, 1);

        t.send(bm, 3, 1);

        assertTrue(cdl3.await(1, TimeUnit.SECONDS));
        c.broadcast(new BroadcastTestMessage().setMsg("hello"));
        mm = t.t();
        assertEquals(2L, mm.getMessageId());
        assertEquals(3L, mm.getLatestReceivedId());
    }

    /** Tests a mix of messages. */
    @Test
    @Ignore
    public void messageAckMany() throws Exception {
        MmsClient c = createAndConnect();
        int count = 200;
        LinkedBlockingDeque<MmsMessage> bq = t.setQueue(new LinkedBlockingDeque<MmsMessage>());
        int lastestOut = 0;
        for (int i = 0; i < count; i++) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                Broadcast bm = new Broadcast();
                bm.setBroadcastType(BroadcastTestMessage.class.getCanonicalName());
                bm.setPayload(Binary.copyFromUtf8(new BroadcastTestMessage().setMsg("A").toJSON()));
                bm.setMessageId(Binary.random(32));

                bm.setSenderPosition(Position.create(2, 1));
                bm.setSenderTimestamp(Timestamp.create(4));


                bm.setSenderId(ID3.toString());


                t.send(bm, ++lastestOut, 0);
            } else {
                c.broadcast(new BroadcastTestMessage().setMsg("hello"));
            }
        }
        for (int i = 0; i < count - lastestOut; i++) {
            assertNotNull(bq.poll(1, TimeUnit.SECONDS));
        }
        bq.clear();
        c.broadcast(new BroadcastTestMessage().setMsg("hello"));
        MmsMessage mm = t.t();
        assertEquals(count - lastestOut + 1, mm.getMessageId());
        assertEquals(lastestOut, mm.getLatestReceivedId());
    }
}
