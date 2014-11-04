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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.Binary;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class ReconnectTest extends AbstractSessionTest {


    /** Tests that we will reconnect if the connection is dropped. */
    @Test
    public void reconnect() throws Exception {
        CountDownLatch connectCount = new CountDownLatch(2);
        Session s = connect(new SessionListener() {}, new MmsConnection.Listener() {
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

        Connected co = new Connected();
        co.setSessionId(sessionId);
        t.send(co);

        assertTrue(connectCount.await(2, TimeUnit.SECONDS));
        // assertEquals(s, createdSession.get());
        assertTrue(s.isConnected());
        assertEquals(s.sessionId, co.getSessionId());

        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }

    /** Tests that we will reconnect if the connection is dropped. */
    @Test
    public void reconnectMultipleTimes() throws Exception {
        CountDownLatch connectCount = new CountDownLatch(10);
        Session s = connect(new SessionListener() {}, new MmsConnection.Listener() {
            @Override
            public void connected(URI host) {
                connectCount.countDown();
            }
        });

        Binary sessionId = s.sessionId;

        for (int i = 0; i < 9; i++) {
            t.disconnect();

            Hello h = t.take(Hello.class);
            assertEquals(conf.getId().toString(), h.getClientId());
            assertEquals(sessionId, h.getSessionId());
            assertEquals(0L, h.getLastReceivedMessageId().longValue());

            Connected co = new Connected();
            co.setSessionId(sessionId);
            t.send(co);
        }

        assertTrue(connectCount.await(2, TimeUnit.SECONDS));
        assertTrue(s.isConnected());

        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }


    @Test
    public void reconnectWithMessage() throws Exception {
        CountDownLatch connectCount = new CountDownLatch(2);
        Session s = connectNormally(e -> {}, new MmsConnection.Listener() {

            /** {@inheritDoc} */
            @Override
            public void connected(URI host) {
                connectCount.countDown();
            }
        });
        // send a single message
        CompletableFuture<Void> cf = new CompletableFuture<>();
        s.sendMessage(new Broadcast().setSenderId("abc"), cf);
        MmsMessage mm = t.t();
        t.send(new Broadcast().setSenderId("abc"), 1, 1);
        cf.join();

        Binary sessionId = s.sessionId;

        t.disconnect();

        // Should reconnect
        Hello h = t.take(Hello.class);
        assertEquals(sessionId, h.getSessionId());
        assertEquals(1L, h.getLastReceivedMessageId().longValue());
        t.send(new Connected().setSessionId(sessionId).setLastReceivedMessageId(1L));

        //
        assertTrue(connectCount.await(2, TimeUnit.SECONDS));
        cf = new CompletableFuture<>();
        s.sendMessage(new Broadcast().setSenderId("abcd"), cf);
        mm = t.t();
        assertEquals(2, mm.getMessageId());
        assertEquals("abcd", mm.cast(Broadcast.class).getSenderId());

        // kill the session
        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }

    @Test
    public void reconnectResend() throws Exception {
        CountDownLatch connectCount = new CountDownLatch(2);
        Session s = connectNormally(e -> {}, new MmsConnection.Listener() {

            /** {@inheritDoc} */
            @Override
            public void connected(URI host) {
                connectCount.countDown();
            }
        });
        // send a single message
        CompletableFuture<Void> cf = new CompletableFuture<>();
        s.sendMessage(new Broadcast().setSenderId("abc"), cf);
        MmsMessage mm = t.t();
        assertEquals(1, mm.getMessageId());

        Binary sessionId = s.sessionId;

        t.disconnect();

        // Should reconnect
        Hello h = t.take(Hello.class);
        assertEquals(sessionId, h.getSessionId());
        assertEquals(0L, h.getLastReceivedMessageId().longValue());
        t.send(new Connected().setSessionId(sessionId).setLastReceivedMessageId(0L));

        assertTrue(connectCount.await(2, TimeUnit.SECONDS));

        // Session should resend msg
        mm = t.t();
        assertEquals(1, mm.getMessageId());
        assertEquals("abc", mm.cast(Broadcast.class).getSenderId());

        // Ack it
        t.send(new Broadcast().setSenderId("abc"), 1, 1);
        cf.join();
        // kill the session
        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }

    @Test
    public void reconnectResendMany() throws Exception {
        CountDownLatch connectCount = new CountDownLatch(2);
        Session s = connectNormally(e -> {}, new MmsConnection.Listener() {

            /** {@inheritDoc} */
            @Override
            public void connected(URI host) {
                connectCount.countDown();
            }
        });
        // send a single message
        Map<Integer, CompletableFuture<Void>> futures = new HashMap<>();
        for (int i = 1; i < 20; i++) {
            futures.put(i, new CompletableFuture<>());
            s.sendMessage(new Broadcast().setSenderId("abc" + i), futures.get(i));
        }
        Binary sessionId = s.sessionId;

        for (int i = 1; i < 20; i++) {
            for (int j = i; j < 20; j++) {
                MmsMessage mm = t.t();
                assertEquals(j, mm.getMessageId());
                assertEquals("abc" + j, mm.cast(Broadcast.class).getSenderId());
            }

            t.disconnect();
            Hello h = t.take(Hello.class);
            assertEquals(sessionId, h.getSessionId());
            assertEquals(0L, h.getLastReceivedMessageId().longValue());
            t.send(new Connected().setSessionId(sessionId).setLastReceivedMessageId((long) i));
            futures.get(i).join();
        }

        // kill the session
        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }

}
