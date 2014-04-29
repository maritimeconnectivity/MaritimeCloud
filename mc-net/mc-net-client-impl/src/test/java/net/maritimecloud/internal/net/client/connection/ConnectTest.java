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

import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.net.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.net.client.broadcast.stubs.HelloWorld;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastSend;
import net.maritimecloud.messages.Connected;
import net.maritimecloud.net.MaritimeCloudClient;

import org.junit.Test;


/**
 *
 * @author Kasper Nielsen
 */
public class ConnectTest extends AbstractClientConnectionTest {

    @Test
    public void connectTest() throws Exception {
        MaritimeCloudClient c = create();
        t.m.take();
        t.send(new Connected().setConnectionId("ABC").setLastReceivedMessageId(0L));
        assertTrue(c.connection().awaitConnected(1, TimeUnit.SECONDS));
        assertTrue(c.connection().isConnected());
    }

    /**
     * Tests that messages send before the connect finished is still delivered.
     */
    @Test
    public void connectedSlow() throws Exception {
        MaritimeCloudClient c = create();
        t.m.take();

        c.broadcast(new HelloWorld("foo1"));// enqueue before we have actually connected.
        Thread.sleep(50);
        t.send(new Connected().setConnectionId("ABC").setLastReceivedMessageId(0L));
        c.broadcast(new HelloWorld("foo2"));
        assertTrue(c.connection().awaitConnected(10, TimeUnit.SECONDS));
        assertTrue(c.connection().isConnected());
        assertEquals("foo1", t.take(BroadcastSend.class).tryRead(HelloWorld.class).getMessage());
        assertEquals("foo2", t.take(BroadcastSend.class).tryRead(HelloWorld.class).getMessage());
    }
}
