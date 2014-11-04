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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.net.BroadcastMessage;
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
public class BroadcastTest extends AbstractClientConnectionTest {


    @Test
    public void broadcast() throws Exception {
        MmsClient c = createAndConnect();

        c.broadcast(new BroadcastTestMessage().setMsg("hello"));

        Broadcast mb = t.take(Broadcast.class);
        BroadcastTestMessage hw = (BroadcastTestMessage) MmsMessage.tryRead(mb);
        assertEquals("hello", hw.getMsg());
    }

    @Test
    public void broadcastListen() throws Exception {
        MmsClient c = createAndConnect();

        final CountDownLatch cdl = new CountDownLatch(1);
        c.broadcastSubscribe(BroadcastTestMessage.class, (header, m) -> {
            assertEquals(ID2, header.getSender());
            assertEquals(Position.create(1, 1), header.getSenderPosition());
            assertEquals(Timestamp.create(1), header.getSenderTime());
            assertEquals("foo$\\\n", m.getMsg());
            cdl.countDown();
        });

        // the first message should not be send to the handler
        // BroadcastTestMessage2 hw2 = new BroadcastTestMessage2("NOTNT");

        // BroadcastDeliver bm = new BroadcastDeliver(ID3, PositionTime.create(1, 1, 1),
        // BroadcastTestMessage2.class.getCanonicalName(), persistAndEscape(hw2));

        BroadcastTestMessage hw = new BroadcastTestMessage().setMsg("foo$\\\n");
        // BroadcastDeliver bm = new BroadcastDeliver(ID2, PositionTime.create(1, 1, 1),
        // BroadcastTestMessage.class.getCanonicalName(), persistAndEscape(hw));
        //
        Broadcast bm = new Broadcast();
        bm.setBroadcastType(BroadcastTestMessage.class.getCanonicalName());
        bm.setMessageId(Binary.random(32));
        bm.setPayload(Binary.copyFromUtf8(hw.toJSON()));
        bm.setSenderPosition(Position.create(1, 1));
        bm.setSenderTimestamp(Timestamp.create(1));
        bm.setSenderId(ID2.toString());


        t.send(bm, 0, 0);

        assertTrue(cdl.await(2, TimeUnit.SECONDS));
    }

    @Test
    @Ignore
    // Subtype does not work, probably never will
    // Its OOD and
    public void broadcastListenSubType() throws Exception {
        MmsClient c = createAndConnect();

        final CountDownLatch cdl = new CountDownLatch(2);
        c.broadcastSubscribe(BroadcastMessage.class, (header, m) -> {
            if (cdl.getCount() == 2) {
                // BroadcastTestMessage2 hw = (BroadcastTestMessage2) broadcast;
                // assertEquals(ID3, header.getId());
                // assertEquals(PositionTime.create(2, 1, 4), header.getPosition());
                // assertEquals("NOTNT", hw.getMessage());
            } else if (cdl.getCount() == 1) {
                BroadcastTestMessage hw = (BroadcastTestMessage) m;
                assertEquals(ID2, header.getSender());
                assertEquals(Position.create(1, 1), header.getSenderPosition());
                assertEquals(Timestamp.create(1), header.getSenderTime());
                assertEquals("foo$\\\n", hw.getMsg());
            } else {
                throw new AssertionError();
            }
            cdl.countDown();
        });

        // the first message should not be send to the handler
        // BroadcastTestMessage2 hw2 = new BroadcastTestMessage2("NOTNT");
        // BroadcastDeliver bm = new BroadcastDeliver(ID3, PositionTime.create(2, 1, 4),
        // BroadcastTestMessage2.class.getCanonicalName(), persistAndEscape(hw2));
        //
        // BroadcastTestMessage hw = new BroadcastTestMessage("foo$\\\n");
        // bm = new BroadcastDeliver(ID2, PositionTime.create(1, 1, 1), BroadcastTestMessage.class.getCanonicalName(),
        // persistAndEscape(hw));
        // t.send(bm);

        assertTrue(cdl.await(2, TimeUnit.SECONDS));
    }
}
