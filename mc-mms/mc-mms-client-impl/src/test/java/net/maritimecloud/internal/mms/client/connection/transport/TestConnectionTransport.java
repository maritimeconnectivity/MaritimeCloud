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
package net.maritimecloud.internal.mms.client.connection.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.maritimecloud.internal.mms.client.TestWebSocketServer;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.MmsWireProtocol;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
@SuppressWarnings("unused")
public class TestConnectionTransport {

    TestWebSocketServer ws;

    MmsClient client;

    int clientPort;

    ServerTestEndpoint st;

    ClientTransportFactoryJetty tm;

    boolean wasSendBinary = MmsWireProtocol.USE_BINARY;

    @Before
    public void before() throws Exception {

        // Force text over the wire protocol
        MmsWireProtocol.USE_BINARY = false;

        tm = new ClientTransportFactoryJetty();
        tm.start();
        clientPort = ThreadLocalRandom.current().nextInt(40000, 50000);
        ws = new TestWebSocketServer(clientPort);
        ws.start();
        st = ws.addEndpoint(new ServerTestEndpoint());

    }

    @After
    public void after() throws Exception {
        tm.stop();
        if (st != null) {
            Session s = st.session;
            if (s != null) {
                s.close();
            }
            assertTrue(st.receivedTests.isEmpty());
        }

        // Restore the wire protocol
        MmsWireProtocol.USE_BINARY = wasSendBinary;
    }

    @Test
    public void closeLocal() throws Exception {
        CountDownLatch onOpenInvoke = new CountDownLatch(1);
        CountDownLatch close = new CountDownLatch(1);

        ClientTransport tr = tm.create(new ClientTransportListener() {
            public void onOpen() {
                onOpenInvoke.countDown();
            }

            public void onClose(MmsConnectionClosingCode closingCode) {
                assertEquals(MmsConnectionClosingCode.NORMAL.getId(), closingCode.getId());
                close.countDown();
            }
        }, new MmsConnection.Listener() {});

        tr.connectBlocking(new URI("ws://localhost:" + clientPort));
        assertTrue(st.opened.await(1, TimeUnit.SECONDS));
        assertTrue(onOpenInvoke.await(1, TimeUnit.SECONDS));

        tr.closeTransport(MmsConnectionClosingCode.NORMAL);
        assertTrue(close.await(1, TimeUnit.SECONDS));
        assertTrue(st.closed.await(1, TimeUnit.SECONDS));
        assertEquals(CloseReason.CloseCodes.NORMAL_CLOSURE, st.closingReason.getCloseCode());
    }

    @Test
    public void closeRemote() throws Exception {
        CountDownLatch onOpenInvoke = new CountDownLatch(1);
        CountDownLatch close = new CountDownLatch(1);

        ClientTransport tr = tm.create(new ClientTransportListener() {
            public void onOpen() {
                onOpenInvoke.countDown();
            }

            public void onClose(MmsConnectionClosingCode closingCode) {
                assertEquals(MmsConnectionClosingCode.NORMAL.getId(), closingCode.getId());
                close.countDown();
            }
        }, new MmsConnection.Listener() {});

        tr.connectBlocking(new URI("ws://localhost:" + clientPort));
        assertTrue(st.opened.await(1, TimeUnit.SECONDS));
        assertTrue(onOpenInvoke.await(1, TimeUnit.SECONDS));

        st.session.close();
        assertTrue(close.await(1, TimeUnit.SECONDS));
        assertTrue(st.closed.await(1, TimeUnit.SECONDS));
        assertEquals(CloseReason.CloseCodes.NORMAL_CLOSURE, st.closingReason.getCloseCode());
    }


    @Test
    public void connect() throws Exception {
        CountDownLatch onOpenInvoke = new CountDownLatch(1);

        ClientTransport tr = tm.create(new ClientTransportListener() {
            public void onOpen() {
                onOpenInvoke.countDown();
            }
        }, new MmsConnection.Listener() {});

        tr.connectBlocking(new URI("ws://localhost:" + clientPort));
        assertTrue(st.opened.await(1, TimeUnit.SECONDS));
        assertTrue(onOpenInvoke.await(1, TimeUnit.SECONDS));
    }

    @Test
    public void connectTimedOut() throws Exception {
        try (ServerSocket ss = new ServerSocket(55555)) {
            ClientTransport tr = tm.create(new ClientTransportListener() {}, new MmsConnection.Listener() {});
            try {
                tr.connectBlocking(new URI("ws://localhost:55555"), 100, TimeUnit.MILLISECONDS);
                fail("Should fail");
            } catch (InterruptedIOException ok) {}
        }
    }

    @Test
    public void receive() throws Exception {
        CountDownLatch textReceived = new CountDownLatch(1);
        CountDownLatch onMessage = new CountDownLatch(1);
        ClientTransport tr = tm.create(new ClientTransportListener() {
            /** {@inheritDoc} */
            @Override
            public void onMessage(MmsMessage message) {
                assertTrue(textReceived.getCount() == 0);
                assertTrue(message.getM() instanceof Hello);
                assertEquals("aBcDe", ((Hello) message.getM()).getClientId());
                onMessage.countDown();
            }
        }, new MmsConnection.Listener() {
            @Override
            public void textMessageReceived(String text) {
                assertTrue(text.contains("aBcDe"));
                textReceived.countDown();
            }
        });

        tr.connectBlocking(new URI("ws://localhost:" + clientPort));
        assertTrue(st.opened.await(1, TimeUnit.SECONDS));


        st.session.getAsyncRemote().sendText(new MmsMessage(new Hello().setClientId("aBcDe")).toText());

        assertTrue(textReceived.await(1, TimeUnit.SECONDS));
        assertTrue(onMessage.await(1, TimeUnit.SECONDS));
    }

    @Test
    public void send() throws Exception {
        CountDownLatch onOpenInvoke = new CountDownLatch(1);
        CountDownLatch textSend = new CountDownLatch(1);
        AtomicReference<String> ar = new AtomicReference<>();
        ClientTransport tr = tm.create(new ClientTransportListener() {
            public void onOpen() {
                onOpenInvoke.countDown();
            }

        }, new MmsConnection.Listener() {

            /** {@inheritDoc} */
            @Override
            public void textMessageSend(String text) {
                ar.set(text);
                textSend.countDown();
            }
        });

        tr.connectBlocking(new URI("ws://localhost:" + clientPort));
        assertTrue(onOpenInvoke.await(1, TimeUnit.SECONDS));

        tr.sendMessage(new MmsMessage(new Hello().setClientId("AbCdE")));
        assertTrue(textSend.await(1, TimeUnit.SECONDS));
        String str = st.receivedTests.poll(1, TimeUnit.SECONDS);
        assertEquals(ar.get(), str);
        MmsMessage mmsMessage = MmsMessage.parseTextMessage(str);
        assertTrue(mmsMessage.getM() instanceof Hello);
        assertEquals("AbCdE", ((Hello) mmsMessage.getM()).getClientId());
    }

    @Test
    @Ignore
    public void receiveInvalid() throws Exception {
        CountDownLatch cdl = new CountDownLatch(1);
        ClientTransport tr = tm.create(new ClientTransportListener() {

        }, new MmsConnection.Listener() {
            /** {@inheritDoc} */
            @Override
            public void textMessageReceived(String text) {
                assertEquals("DFDF", text);
                cdl.countDown();
            }
        });

        tr.connectBlocking(new URI("ws://localhost:" + clientPort));
        assertTrue(st.opened.await(1, TimeUnit.SECONDS));
        st.session.getAsyncRemote().sendText("DFDF");

        assertTrue(cdl.await(1, TimeUnit.SECONDS));
    }


    @ServerEndpoint(value = "/")
    public class ServerTestEndpoint {
        final BlockingQueue<String> receivedTests = new LinkedBlockingQueue<>(5);

        Session session;

        final CountDownLatch opened = new CountDownLatch(1);

        final CountDownLatch closed = new CountDownLatch(1);

        volatile CloseReason closingReason;

        public void close() throws IOException {
            session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "TestClientEndpoint.close()"));
        }

        @OnClose
        public void onClose(CloseReason reason) {
            closed.countDown();
            this.closingReason = reason;
        }

        @OnMessage
        public final void messageReceived(String msg, Session userSession) {
            receivedTests.add(msg);
        }

        @OnOpen
        public void onOpen(Session session) {
            this.session = session;
            opened.countDown();
        }
    }
}
