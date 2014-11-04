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
package net.maritimecloud.internal.mms.client.connection.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import net.maritimecloud.internal.mms.client.ClientInfo;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.Binary;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class ConnectTest extends AbstractSessionTest {

    @Test
    public void connect() throws Exception {
        ClientInfo ci = new ClientInfo(conf);
        CountDownLatch connected = new CountDownLatch(1);

        Session s = Session.createNewSessionAndConnect(ctm, ci, new SessionListener() {}, new MmsConnection.Listener() {
            @Override
            public void connected(URI host) {
                connected.countDown();
            }
        });


        assertFalse(s.isConnected());
        Hello h = t.take(Hello.class);
        assertEquals(conf.getId().toString(), h.getClientId());
        // todo check h.getProperties();

        assertNull(h.getSessionId());
        assertNull(h.getLastReceivedMessageId());

        assertNotNull(h.getPositionTime());

        Connected co = new Connected();
        co.setSessionId(Binary.random(32));
        t.send(co);

        assertTrue(connected.await(2, TimeUnit.SECONDS));
        assertTrue(s.isConnected());
        assertEquals(s.sessionId, co.getSessionId());

        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }

    @Test
    public void cancelConnect() throws Exception {
        MmsConnectionClosingCode cancel = MmsConnectionClosingCode.create(12345, "foo");

        ClientInfo ci = new ClientInfo(conf);
        CountDownLatch closed = new CountDownLatch(1);
        AtomicReference<MmsConnectionClosingCode> createdSession = new AtomicReference<>();

        Session s = Session.createNewSessionAndConnect(ctm, ci, new SessionListener() {
            /** {@inheritDoc} */
            @Override
            public void onSessionClose(MmsConnectionClosingCode closingCode) {
                createdSession.set(closingCode);
                closed.countDown();
            }
        }, new MmsConnection.Listener() {});
        assertFalse(s.isConnected());

        SessionStateConnecting ssc = (SessionStateConnecting) s.state;
        assertFalse(ssc.cancel.getCount() == 0);


        t.take(Hello.class);
        assertFalse(s.isClosed);

        s.closeSession(cancel);
        assertTrue(s.isClosed);

        assertEquals(0, ssc.cancel.getCount());

        assertTrue(closed.await(2, TimeUnit.SECONDS));

        // Maybe we need to await a bit here
        assertTrue(t.closed.await(2, TimeUnit.SECONDS));
        assertNotNull(t.reason);
        // assertEquals("foo", t.reason.getReasonPhrase()); // Problem with jetty

        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }


}
