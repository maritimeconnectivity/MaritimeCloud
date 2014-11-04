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
package net.maritimecloud.server.connection;

import static org.junit.Assert.assertEquals;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.Welcome;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.server.AbstractServerConnectionTest;
import net.maritimecloud.server.TesstEndpoint;
import net.maritimecloud.server.broadcast.BroadcastTest;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class ReconnectTest extends AbstractServerConnectionTest {

    @Test
    public void reconnect1() throws Exception {
        TesstEndpoint t = newClient();
        t.take(Welcome.class);

        Hello h = new Hello().setClientId(ID2.toString()).setLastReceivedMessageId(0L)
                .setPositionTime(PositionTime.create(1, 1, System.currentTimeMillis()));
        // new HelloMessage(ID2, "foo", "", 0, 1, 1)
        t.send(h);

        Connected cm = t.take(Connected.class);
        Binary reconnectId = cm.getSessionId();
        assertEquals(0, cm.getLastReceivedMessageId().longValue());

        Broadcast bs = BroadcastTest.createBroadcast(ID1, PositionTime.create(1, 1, 1),
                new BroadcastTestMessage().setMsg("foo1"), null, 10, null);
        t.send(bs, 1, 0);

        MmsMessage mm = t.t();
        assertEquals(1, mm.getMessageId());
        assertEquals(1, mm.getLatestReceivedId());

        t.close();

        t = newClient();
        t.take(Welcome.class);

        h = new Hello().setClientId(ID2.toString()).setSessionId(reconnectId).setLastReceivedMessageId(1L)
                .setPositionTime(PositionTime.create(1, 1, System.currentTimeMillis()));
        // t.send(new HelloMessage(ID2, "foo", reconnectId, 1, 1, 1));
        t.send(h);


        cm = t.take(Connected.class);
        assertEquals(reconnectId, cm.getSessionId());
        assertEquals(1, cm.getLastReceivedMessageId().longValue());

        bs = BroadcastTest.createBroadcast(ID1, PositionTime.create(1, 1, 1),
                new BroadcastTestMessage().setMsg("foo1"), null, 10, null);
        t.send(bs, 2, 1);

        mm = t.t();
        assertEquals(2, mm.getMessageId());
        assertEquals(2, mm.getLatestReceivedId());
    }

    @Test
    public void connectTest() throws Exception {
        TesstEndpoint t = newClient();
        t.take(Welcome.class);

        Hello h = new Hello().setClientId(ID2.toString()).setLastReceivedMessageId(0L)
                .setPositionTime(PositionTime.create(1, 1, System.currentTimeMillis()));
        // new HelloMessage(ID2, "foo", "", 0, 1, 1)
        t.send(h);
        Connected cm = t.take(Connected.class);
        Binary reconnectId = cm.getSessionId();
        assertEquals(0, cm.getLastReceivedMessageId().longValue());

        Broadcast bs = BroadcastTest.createBroadcast(ID1, PositionTime.create(1, 1, 1),
                new BroadcastTestMessage().setMsg("foo1"), null, 10, null);
        t.send(bs, 1, 0);

        MmsMessage mm = t.t();
        assertEquals(1, mm.getMessageId());
        assertEquals(1, mm.getLatestReceivedId());

        t.close();

        t = newClient();
        t.take(Welcome.class);

        h = new Hello().setClientId(ID2.toString()).setSessionId(reconnectId).setLastReceivedMessageId(0L)
                .setPositionTime(PositionTime.create(1, 1, System.currentTimeMillis()));
        // t.send(new HelloMessage(ID2, "foo", reconnectId, 0, 1, 1));
        t.send(h);

        cm = t.take(Connected.class);
        assertEquals(reconnectId, cm.getSessionId());
        assertEquals(1, cm.getLastReceivedMessageId().longValue());

        mm = t.t();
        assertEquals(1, mm.getMessageId());
        assertEquals(1, mm.getLatestReceivedId());

        bs = BroadcastTest.createBroadcast(ID1, PositionTime.create(1, 1, 1),
                new BroadcastTestMessage().setMsg("foo2"), null, 10, null);
        t.send(bs, 2, 1);

        mm = t.t();
        assertEquals(2, mm.getMessageId());
        assertEquals(2, mm.getLatestReceivedId());
    }
}
