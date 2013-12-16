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
package net.maritimecloud.internal.net.client.broadcast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.net.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.net.client.broadcast.stubs.HelloWorld;
import net.maritimecloud.internal.net.client.broadcast.stubs.HelloWorld2;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastDeliver;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastSend;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.broadcast.BroadcastListener;
import net.maritimecloud.net.broadcast.BroadcastMessage;
import net.maritimecloud.net.broadcast.BroadcastMessageHeader;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Kasper Nielsen
 */
public class BroadcastTest extends AbstractClientConnectionTest {

    @Test
    public void broadcast() throws Exception {
        MaritimeCloudClient c = createAndConnect();

        c.broadcast(new HelloWorld("hello"));

        BroadcastSend mb = t.take(BroadcastSend.class);
        HelloWorld hw = (HelloWorld) mb.tryRead();
        assertEquals("hello", hw.getMessage());
    }

    @Test
    public void broadcastListen() throws Exception {
        MaritimeCloudClient c = createAndConnect();

        final CountDownLatch cdl = new CountDownLatch(1);
        c.broadcastListen(HelloWorld.class, new BroadcastListener<HelloWorld>() {
            public void onMessage(BroadcastMessageHeader header, HelloWorld broadcast) {
                assertEquals(ID2, header.getId());
                assertEquals(PositionTime.create(1, 1, 1), header.getPosition());
                assertEquals("foo$\\\n", broadcast.getMessage());
                cdl.countDown();
            }
        });

        // the first message should not be send to the handler
        HelloWorld2 hw2 = new HelloWorld2("NOTNT");
        BroadcastDeliver m = new BroadcastDeliver(ID3, PositionTime.create(1, 1, 1),
                HelloWorld2.class.getCanonicalName(), persistAndEscape(hw2));

        HelloWorld hw = new HelloWorld("foo$\\\n");
        m = new BroadcastDeliver(ID2, PositionTime.create(1, 1, 1), HelloWorld.class.getCanonicalName(),
                persistAndEscape(hw));
        t.send(m);

        assertTrue(cdl.await(2, TimeUnit.SECONDS));
    }

    @Test
    @Ignore
    // Subtype does not work, probably never will
    // Its OOD and
    public void broadcastListenSubType() throws Exception {
        MaritimeCloudClient c = createAndConnect();

        final CountDownLatch cdl = new CountDownLatch(2);
        c.broadcastListen(BroadcastMessage.class, new BroadcastListener<BroadcastMessage>() {
            public void onMessage(BroadcastMessageHeader header, BroadcastMessage broadcast) {
                if (cdl.getCount() == 2) {
                    HelloWorld2 hw = (HelloWorld2) broadcast;
                    assertEquals(ID3, header.getId());
                    assertEquals(PositionTime.create(2, 1, 4), header.getPosition());
                    assertEquals("NOTNT", hw.getMessage());
                } else if (cdl.getCount() == 1) {
                    HelloWorld hw = (HelloWorld) broadcast;
                    assertEquals(ID2, header.getId());
                    assertEquals(PositionTime.create(1, 1, 1), header.getPosition());
                    assertEquals("foo$\\\n", hw.getMessage());
                } else {
                    throw new AssertionError();
                }
                cdl.countDown();
            }
        });

        // the first message should not be send to the handler
        HelloWorld2 hw2 = new HelloWorld2("NOTNT");
        BroadcastDeliver m = new BroadcastDeliver(ID3, PositionTime.create(2, 1, 4),
                HelloWorld2.class.getCanonicalName(), persistAndEscape(hw2));

        HelloWorld hw = new HelloWorld("foo$\\\n");
        m = new BroadcastDeliver(ID2, PositionTime.create(1, 1, 1), HelloWorld.class.getCanonicalName(),
                persistAndEscape(hw));
        t.send(m);

        assertTrue(cdl.await(2, TimeUnit.SECONDS));
    }
}
