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
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.Binary;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class ReconnectNewSessionTest extends AbstractSessionTest {

    /** Tests that we will reconnect if the connection is dropped. */
    @Test
    public void reconnectNewSession() throws Exception {
        CountDownLatch connectCount = new CountDownLatch(1);

        CountDownLatch sessionClosed = new CountDownLatch(1);

        Session s = connect(new SessionListener() {
            @Override
            public void onSessionClose(MmsConnectionClosingCode closingCode) {
                assertEquals(MmsConnectionClosingCode.INVALID_SESSION, closingCode);
                sessionClosed.countDown();
            }
        }, new MmsConnection.Listener() {
            @Override
            public void connected(URI host) {
                connectCount.countDown();
            }
        });

        Binary sessionId = s.sessionId;
        t.disconnect();

        // Second connect
        Hello h = t.take(Hello.class);
        assertEquals(conf.getId().toString(), h.getClientId());
        assertEquals(sessionId, h.getSessionId());
        assertEquals(0L, h.getLastReceivedMessageId().longValue());

        t.close(MmsConnectionClosingCode.INVALID_SESSION.getId(), "invalid session");

        assertTrue(connectCount.await(2, TimeUnit.SECONDS));
        assertTrue(sessionClosed.await(2, TimeUnit.SECONDS));
    }

    // /** Tests that we will reconnect if the connection is dropped. */
    // @Test
    // @Ignore
    // public void reconnectNewSessionMultipleTimes() throws Exception {
    // CountDownLatch connectCount = new CountDownLatch(10);
    //
    // BlockingQueue<Session> latestSession = new ArrayBlockingQueue<>(1);
    //
    // Session s = connect(new SessionListener() {
    // @Override
    // public void onSessionReset(Session session) {
    // latestSession.add(session);
    // }
    // }, new MmsConnection.Listener() {
    // @Override
    // public void connected() {
    // connectCount.countDown();
    // }
    // });
    //
    // Binary sessionId = s.sessionId;
    // boolean newSession = false;
    // for (int i = 0; i < 9; i++) {
    // t.disconnect();
    //
    // Hello h = t.take(Hello.class);
    // assertEquals(conf.getId().toString(), h.getClientId());
    // assertEquals(sessionId, h.getSessionId());
    // assertEquals(0L, h.getLastReceivedMessageId().longValue());
    //
    // newSession = ThreadLocalRandom.current().nextBoolean();
    // Connected co = new Connected();
    // if (newSession) {
    // co.setSessionId(sessionId = Binary.random(32));
    // } else {
    // co.setSessionId(sessionId);
    // }
    // t.send(co);
    //
    // if (newSession) {
    // Session sNew = latestSession.poll(2, TimeUnit.SECONDS);
    // assertNotSame(s, sNew);
    // s = sNew;
    // }
    // assertEquals(sessionId, s.sessionId);
    // }
    //
    // assertTrue(connectCount.await(2, TimeUnit.SECONDS));
    // // assertTrue(s.isConnected());
    //
    // s.closeSession(MmsConnectionClosingCode.NORMAL);
    // }
}
