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
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class OldReconnect2Test extends AbstractClientConnectionTest {

    @Test
    public void simpleMessage() throws Exception {
        MmsClient c = create();
        t.m.take();
        t.send(new Connected().setSessionId(BIN4).setLastReceivedMessageId(0L));

        c.broadcast(new BroadcastTestMessage().setMsg("hello"));
        MmsMessage mm = t.t();
        assertEquals(1L, mm.getMessageId());
        assertEquals(0L, mm.getLatestReceivedId());

        t.close();

        Thread.sleep(50);
        c.broadcast(new BroadcastTestMessage().setMsg("hello"));
        Thread.sleep(50);
        c.broadcast(new BroadcastTestMessage().setMsg("hello"));

        Hello hm = t.take(Hello.class);
        assertEquals(BIN4, hm.getSessionId());

        t.send(new Connected().setSessionId(BIN4).setLastReceivedMessageId(1L));

        mm = t.t();
        assertEquals(2L, mm.getMessageId());
        assertEquals(0L, mm.getLatestReceivedId());

        mm = t.t();
        assertEquals(3L, mm.getMessageId());
        assertEquals(0L, mm.getLatestReceivedId());
    }

    /** Like the first one. But we have sent a message from the server before disconnecting. */
    @Test
    public void simpleMessage2() throws Exception {
        MmsClient c = create();
        t.m.take();
        t.send(new Connected().setSessionId(BIN4).setLastReceivedMessageId(0L));

        c.broadcast(new BroadcastTestMessage().setMsg("hello"));
        MmsMessage mm = t.t();
        assertEquals(1L, mm.getMessageId());
        assertEquals(0L, mm.getLatestReceivedId());

        final CountDownLatch cdl = new CountDownLatch(1);
        c.broadcastSubscribe(BroadcastTestMessage.class, (header, msg) -> cdl.countDown());

        // BroadcastDeliver bm = new BroadcastDeliver(ID2, PositionTime.create(1, 1, 1),
        // BroadcastTestMessage.class.getCanonicalName(), persistAndEscape(new BroadcastTestMessage("foo")));
        Broadcast bm = new Broadcast();
        bm.setBroadcastType(BroadcastTestMessage.class.getCanonicalName());
        bm.setPayload(Binary.copyFromUtf8(new BroadcastTestMessage().setMsg("foo").toJSON()));
        bm.setMessageId(Binary.random(32));
        bm.setSenderPosition(Position.create(1, 1));
        bm.setSenderTimestamp(Timestamp.create(1));

        bm.setSenderId(ID2.toString());

        t.send(bm, 1, 1);


        assertTrue(cdl.await(1, TimeUnit.SECONDS));
        t.close();

        Thread.sleep(50);
        c.broadcast(new BroadcastTestMessage().setMsg("hello"));
        Thread.sleep(50);
        c.broadcast(new BroadcastTestMessage().setMsg("hello"));

        Hello hm = t.take(Hello.class);
        assertEquals(BIN4, hm.getSessionId());

        t.send(new Connected().setSessionId(BIN4).setLastReceivedMessageId(1L));

        mm = t.t();
        assertEquals(2L, mm.getMessageId());
        assertEquals(1L, mm.getLatestReceivedId());

        mm = t.t();
        assertEquals(3L, mm.getMessageId());
        assertEquals(1L, mm.getLatestReceivedId());
    }

    /** Like the first one. But when we connect the server indicates that it have not received any message. */
    @Test
    public void serverReceivedNoMessage() throws Exception {
        MmsClient c = create();
        t.m.take();
        t.send(new Connected().setSessionId(BIN4).setLastReceivedMessageId(0L));

        c.broadcast(new BroadcastTestMessage().setMsg("hello1"));
        MmsMessage mm = t.t();
        Broadcast m = mm.cast(Broadcast.class);
        assertEquals(1L, mm.getMessageId());
        assertEquals(0L, mm.getLatestReceivedId());

        final CountDownLatch cdl = new CountDownLatch(1);
        c.broadcastSubscribe(BroadcastTestMessage.class, (header, msg) -> cdl.countDown());

        // BroadcastDeliver bm = new BroadcastDeliver(ID2, PositionTime.create(1, 1, 1),
        // BroadcastTestMessage.class.getCanonicalName(), persistAndEscape(new BroadcastTestMessage("foo")));
        Broadcast bm = new Broadcast();
        bm.setBroadcastType(BroadcastTestMessage.class.getCanonicalName());
        bm.setPayload(Binary.copyFromUtf8(new BroadcastTestMessage().setMsg("foo").toJSON()));
        bm.setMessageId(Binary.random(32));
        bm.setSenderPosition(Position.create(1, 1));
        bm.setSenderTimestamp(Timestamp.create(1));

        bm.setSenderId(ID2.toString());


        t.send(bm, 1, 0);


        assertTrue(cdl.await(1, TimeUnit.SECONDS));
        t.close();

        Thread.sleep(50);
        c.broadcast(new BroadcastTestMessage().setMsg("hello2"));
        Thread.sleep(50);
        c.broadcast(new BroadcastTestMessage().setMsg("hello3"));

        Hello hm = t.take(Hello.class);
        assertEquals(BIN4, hm.getSessionId());
        t.send(new Connected().setSessionId(BIN4).setLastReceivedMessageId(0L));

        mm = t.t();
        m = mm.cast(Broadcast.class);
        assertEquals(1L, mm.getMessageId());
        assertEquals(1L, mm.getLatestReceivedId());
        assertEquals("hello1", ((BroadcastTestMessage) MmsMessage.tryRead(m)).getMsg());

        mm = t.t();
        m = mm.cast(Broadcast.class);
        assertEquals(2L, mm.getMessageId());
        assertEquals(1L, mm.getLatestReceivedId());
        assertEquals("hello2", ((BroadcastTestMessage) MmsMessage.tryRead(m)).getMsg());

        mm = t.t();
        m = mm.cast(Broadcast.class);
        assertEquals(3L, mm.getMessageId());
        assertEquals(1L, mm.getLatestReceivedId());
        assertEquals("hello3", ((BroadcastTestMessage) MmsMessage.tryRead(m)).getMsg());
    }
}
