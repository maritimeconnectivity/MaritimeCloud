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

import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.net.mms.MmsClient;

import org.junit.Ignore;
import org.junit.Test;


/**
 *
 * @author Kasper Nielsen
 */
public class OldConnectTest extends AbstractClientConnectionTest {

    @Test
    public void connectTest() throws Exception {
        MmsClient c = create();
        t.m.take();
        t.send(new Connected().setSessionId(BIN3).setLastReceivedMessageId(0L));
        assertTrue(c.connection().awaitConnected(1, TimeUnit.SECONDS));
        assertTrue(c.connection().isConnected());
    }

    /**
     * Tests that messages send before the connect finished is still delivered.
     */
    @Test
    @Ignore
    public void connectedSlow() throws Exception {
        MmsClient c = create();
        t.m.take();

        c.broadcast(new BroadcastTestMessage().setMsg("foo1"));// enqueue before we have actually connected.
        Thread.sleep(50);
        t.send(new Connected().setSessionId(BIN3).setLastReceivedMessageId(0L));
        c.broadcast(new BroadcastTestMessage().setMsg("foo2"));
        assertTrue(c.connection().awaitConnected(10, TimeUnit.SECONDS));
        assertTrue(c.connection().isConnected());
        assertEquals("foo1", ((BroadcastTestMessage) MmsMessage.tryRead(t.take(Broadcast.class))).getMsg());
        assertEquals("foo2", ((BroadcastTestMessage) MmsMessage.tryRead(t.take(Broadcast.class))).getMsg());
    }
}
