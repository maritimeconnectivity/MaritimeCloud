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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.Welcome;
import net.maritimecloud.mms.server.connection.client.OldClientManager;
import net.maritimecloud.server.AbstractServerConnectionTest;
import net.maritimecloud.server.TesstEndpoint;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.Ignore;
import org.junit.Test;


/**
 *
 * @author Kasper Nielsen
 */
public class Connect2Test extends AbstractServerConnectionTest {

    @Test
    public void connect() throws Exception {
        TesstEndpoint t = newClient();
        Welcome wm = t.take(Welcome.class);
        assertNotNull(wm.getServerId());

        Hello h = new Hello().setClientId(ID2.toString()).setLastReceivedMessageId(0L)
                .setPositionTime(PositionTime.create(1, 1, System.currentTimeMillis()));

        t.send(h);

        Connected cm = t.take(Connected.class);
        assertNotNull(cm.getSessionId());
        assertEquals(0, cm.getLastReceivedMessageId().longValue());

        assertNotNull(server.getService(OldClientManager.class).get(ID2));
    }

    /**
     * Instead of the client sending a {@link Hello} message as per protocol. We send a {@link Welcome} which should
     * result in an immediate disconnect.
     */
    @Test
    @Ignore
    public void connectWrongMessage() throws Exception {
        TesstEndpoint t = newClient();
        Welcome wm = t.take(Welcome.class);
        assertNotNull(wm.getServerId());

        t.send(wm);
        // exit with status code 4100 for now.
        assertEquals(4100, t.awaitClosed().getCloseCode().getCode());
    }

    /** Tests that we get a new session when reconnecting clean */
    @Test
    @Ignore
    public void reconnectClean() throws Exception {
        TesstEndpoint t = newClient();
        assertNotNull(t.take(Welcome.class));
        t.send(new Hello().setClientId(ID2.toString()).setLastReceivedMessageId(0L)
                .setPositionTime(PositionTime.create(1, 1, System.currentTimeMillis())));

        Connected cm = t.take(Connected.class);
        assertNotNull(cm.getSessionId());
        assertEquals(0, cm.getLastReceivedMessageId().longValue());


        TesstEndpoint t2 = newClient();
        assertNotNull(t2.take(Welcome.class));
        t2.send(new Hello().setClientId(ID2.toString()).setLastReceivedMessageId(0L)
                .setPositionTime(PositionTime.create(1, 1, System.currentTimeMillis())));

        Connected cm2 = t2.take(Connected.class);
        assertNotNull(cm2.getSessionId());
        assertNotEquals(cm.getSessionId(), cm2.getSessionId());
        assertEquals(0, cm2.getLastReceivedMessageId().longValue());
    }
}
