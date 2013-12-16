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
import net.maritimecloud.internal.net.messages.auxiliary.ConnectedMessage;
import net.maritimecloud.internal.net.messages.auxiliary.HelloMessage;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastDeliver;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastSend;
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
        t.send(new ConnectedMessage("ABCDEFG", 0));

        c.broadcast(new HelloWorld("hello"));
        BroadcastSend m = t.take(BroadcastSend.class);
        assertEquals(1L, m.getMessageId());
        assertEquals(0L, m.getLatestReceivedId());

        t.close();

        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello"));
        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello"));

        HelloMessage hm = t.take(HelloMessage.class);
        assertEquals("ABCDEFG", hm.getReconnectId());
        System.out.println(hm.toJSON());

        t.send(new ConnectedMessage("ABCDEFG", 1));

        m = t.take(BroadcastSend.class);
        assertEquals(2L, m.getMessageId());
        assertEquals(0L, m.getLatestReceivedId());

        m = t.take(BroadcastSend.class);
        assertEquals(3L, m.getMessageId());
        assertEquals(0L, m.getLatestReceivedId());
    }

    /** Like the first one. But we have sent a message from the server before disconnecting. */
    @Test
    public void simpleMessage2() throws Exception {
        MaritimeCloudClient c = create();
        t.m.take();
        t.send(new ConnectedMessage("ABCDEFG", 0));

        c.broadcast(new HelloWorld("hello"));
        BroadcastSend m = t.take(BroadcastSend.class);
        assertEquals(1L, m.getMessageId());
        assertEquals(0L, m.getLatestReceivedId());

        final CountDownLatch cdl = new CountDownLatch(1);
        c.broadcastListen(HelloWorld.class, new BroadcastListener<HelloWorld>() {
            public void onMessage(BroadcastMessageHeader header, HelloWorld broadcast) {
                cdl.countDown();
            }
        });

        BroadcastDeliver bd = new BroadcastDeliver(ID2, PositionTime.create(1, 1, 1),
                HelloWorld.class.getCanonicalName(), persistAndEscape(new HelloWorld("foo")));
        bd.setMessageId(1);
        bd.setLatestReceivedId(1);
        t.send(bd);


        assertTrue(cdl.await(1, TimeUnit.SECONDS));
        t.close();

        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello"));
        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello"));

        HelloMessage hm = t.take(HelloMessage.class);
        assertEquals("ABCDEFG", hm.getReconnectId());
        System.out.println(hm.toJSON());

        t.send(new ConnectedMessage("ABCDEFG", 1));

        m = t.take(BroadcastSend.class);
        assertEquals(2L, m.getMessageId());
        assertEquals(1L, m.getLatestReceivedId());

        m = t.take(BroadcastSend.class);
        assertEquals(3L, m.getMessageId());
        assertEquals(1L, m.getLatestReceivedId());
    }

    /** Like the first one. But when we connect the server indicates that it have not received any message. */
    @Test
    public void serverReceivedNoMessage() throws Exception {
        MaritimeCloudClient c = create();
        t.m.take();
        t.send(new ConnectedMessage("ABCDEFG", 0));

        c.broadcast(new HelloWorld("hello1"));
        BroadcastSend m = t.take(BroadcastSend.class);
        assertEquals(1L, m.getMessageId());
        assertEquals(0L, m.getLatestReceivedId());

        final CountDownLatch cdl = new CountDownLatch(1);
        c.broadcastListen(HelloWorld.class, new BroadcastListener<HelloWorld>() {
            public void onMessage(BroadcastMessageHeader header, HelloWorld broadcast) {
                cdl.countDown();
            }
        });

        BroadcastDeliver bd = new BroadcastDeliver(ID2, PositionTime.create(1, 1, 1),
                HelloWorld.class.getCanonicalName(), persistAndEscape(new HelloWorld("foo")));
        bd.setMessageId(1);
        bd.setLatestReceivedId(0);
        t.send(bd);


        assertTrue(cdl.await(1, TimeUnit.SECONDS));
        t.close();

        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello2"));
        Thread.sleep(50);
        c.broadcast(new HelloWorld("hello3"));

        HelloMessage hm = t.take(HelloMessage.class);
        assertEquals("ABCDEFG", hm.getReconnectId());
        System.out.println(hm.toJSON());

        t.send(new ConnectedMessage("ABCDEFG", 0));

        m = t.take(BroadcastSend.class);
        assertEquals(1L, m.getMessageId());
        assertEquals(1L, m.getLatestReceivedId());
        assertEquals("hello1", ((HelloWorld) m.tryRead()).getMessage());

        m = t.take(BroadcastSend.class);
        assertEquals(2L, m.getMessageId());
        assertEquals(1L, m.getLatestReceivedId());
        assertEquals("hello2", ((HelloWorld) m.tryRead()).getMessage());

        m = t.take(BroadcastSend.class);
        assertEquals(3L, m.getMessageId());
        assertEquals(1L, m.getLatestReceivedId());
        assertEquals("hello3", ((HelloWorld) m.tryRead()).getMessage());
    }
}
