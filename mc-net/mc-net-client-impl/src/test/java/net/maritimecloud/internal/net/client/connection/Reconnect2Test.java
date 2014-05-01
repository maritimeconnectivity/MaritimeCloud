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
package net.maritimecloud.internal.net.client.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.net.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.net.client.broadcast.stubs.HelloWorld;
import net.maritimecloud.internal.net.messages.BroadcastPublish;
import net.maritimecloud.internal.net.messages.BroadcastRelay;
import net.maritimecloud.internal.net.messages.Connected;
import net.maritimecloud.internal.net.messages.Hello;
import net.maritimecloud.internal.net.messages.spi.BroadcastHelper;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.broadcast.BroadcastListener;
import net.maritimecloud.net.broadcast.BroadcastMessageHeader;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class Reconnect2Test extends AbstractClientConnectionTest {

    @Test
    public void simpleMessage() throws Exception {
        MaritimeCloudClient c = create();
        t.m.take();
        t.send(new Connected().setConnectionId("ABCDEFG").setLastReceivedMessageId(0L));

        c.broadcast(new HelloWorld("hello"));
        BroadcastPublish m = t.take(BroadcastPublish.class);
        assertEquals(1L, m.getMessageId().longValue());
        assertEquals(0L, m.getLatestReceivedId().longValue());

        t.close();

        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello"));
        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello"));

        Hello hm = t.take(Hello.class);
        assertEquals("ABCDEFG", hm.getReconnectId());
        System.out.println(hm.toText());

        t.send(new Connected().setConnectionId("ABCDEFG").setLastReceivedMessageId(1L));

        m = t.take(BroadcastPublish.class);
        assertEquals(2L, m.getMessageId().longValue());
        assertEquals(0L, m.getLatestReceivedId().longValue());

        m = t.take(BroadcastPublish.class);
        assertEquals(3L, m.getMessageId().longValue());
        assertEquals(0L, m.getLatestReceivedId().longValue());
    }

    /** Like the first one. But we have sent a message from the server before disconnecting. */
    @Test
    public void simpleMessage2() throws Exception {
        MaritimeCloudClient c = create();
        t.m.take();
        t.send(new Connected().setConnectionId("ABCDEFG").setLastReceivedMessageId(0L));

        c.broadcast(new HelloWorld("hello"));
        BroadcastPublish m = t.take(BroadcastPublish.class);
        assertEquals(1L, m.getMessageId().longValue());
        assertEquals(0L, m.getLatestReceivedId().longValue());

        final CountDownLatch cdl = new CountDownLatch(1);
        c.broadcastListen(HelloWorld.class, new BroadcastListener<HelloWorld>() {
            public void onMessage(HelloWorld broadcast, BroadcastMessageHeader header) {
                cdl.countDown();
            }
        });

        // BroadcastDeliver bm = new BroadcastDeliver(ID2, PositionTime.create(1, 1, 1),
        // HelloWorld.class.getCanonicalName(), persistAndEscape(new HelloWorld("foo")));
        BroadcastRelay bm = new BroadcastRelay();
        bm.setChannel(HelloWorld.class.getCanonicalName());
        bm.setMsg(persist(new HelloWorld("foo")));
        bm.setPositionTime(PositionTime.create(1, 1, 1));
        bm.setId(ID2.toString());

        bm.setMessageId(1L);
        bm.setLatestReceivedId(1L);
        t.send(bm);


        assertTrue(cdl.await(1, TimeUnit.SECONDS));
        t.close();

        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello"));
        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello"));

        Hello hm = t.take(Hello.class);
        assertEquals("ABCDEFG", hm.getReconnectId());
        System.out.println(hm.toText());

        t.send(new Connected().setConnectionId("ABCDEFG").setLastReceivedMessageId(1L));

        m = t.take(BroadcastPublish.class);
        assertEquals(2L, m.getMessageId().longValue());
        assertEquals(1L, m.getLatestReceivedId().longValue());

        m = t.take(BroadcastPublish.class);
        assertEquals(3L, m.getMessageId().longValue());
        assertEquals(1L, m.getLatestReceivedId().longValue());
    }

    /** Like the first one. But when we connect the server indicates that it have not received any message. */
    @Test
    public void serverReceivedNoMessage() throws Exception {
        MaritimeCloudClient c = create();
        t.m.take();
        t.send(new Connected().setConnectionId("ABCDEFG").setLastReceivedMessageId(0L));

        c.broadcast(new HelloWorld("hello1"));
        BroadcastPublish m = t.take(BroadcastPublish.class);
        assertEquals(1L, m.getMessageId().longValue());
        assertEquals(0L, m.getLatestReceivedId().longValue());

        final CountDownLatch cdl = new CountDownLatch(1);
        c.broadcastListen(HelloWorld.class, new BroadcastListener<HelloWorld>() {
            public void onMessage(HelloWorld broadcast, BroadcastMessageHeader header) {
                cdl.countDown();
            }
        });

        // BroadcastDeliver bm = new BroadcastDeliver(ID2, PositionTime.create(1, 1, 1),
        // HelloWorld.class.getCanonicalName(), persistAndEscape(new HelloWorld("foo")));
        BroadcastRelay bm = new BroadcastRelay();
        bm.setChannel(HelloWorld.class.getCanonicalName());
        bm.setMsg(persist(new HelloWorld("foo")));
        bm.setPositionTime(PositionTime.create(1, 1, 1));
        bm.setId(ID2.toString());


        bm.setMessageId(1L);
        bm.setLatestReceivedId(0L);
        t.send(bm);


        assertTrue(cdl.await(1, TimeUnit.SECONDS));
        t.close();

        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello2"));
        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello3"));

        Hello hm = t.take(Hello.class);
        assertEquals("ABCDEFG", hm.getReconnectId());
        System.out.println(hm.toText());

        t.send(new Connected().setConnectionId("ABCDEFG").setLastReceivedMessageId(0L));

        m = t.take(BroadcastPublish.class);
        assertEquals(1L, m.getMessageId().longValue());
        assertEquals(1L, m.getLatestReceivedId().longValue());
        assertEquals("hello1", ((HelloWorld) BroadcastHelper.tryRead(m)).getMessage());

        m = t.take(BroadcastPublish.class);
        assertEquals(2L, m.getMessageId().longValue());
        assertEquals(1L, m.getLatestReceivedId().longValue());
        assertEquals("hello2", ((HelloWorld) BroadcastHelper.tryRead(m)).getMessage());

        m = t.take(BroadcastPublish.class);
        assertEquals(3L, m.getMessageId().longValue());
        assertEquals(1L, m.getLatestReceivedId().longValue());
        assertEquals("hello3", ((HelloWorld) BroadcastHelper.tryRead(m)).getMessage());
    }
}
