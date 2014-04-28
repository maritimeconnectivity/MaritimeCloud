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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCode;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import net.maritimecloud.net.ClosingCode;
import net.maritimecloud.net.MaritimeCloudConnection;
import net.maritimecloud.net.MaritimeCloudConnection.Listener;
import net.maritimecloud.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kasper Nielsen
 */
class ConnectionTransportManagerJsr356 extends ConnectionTransportManager {

    /** The single instance of a WebSocketContainer. */
    static volatile WebSocketContainer CACHED_CONTAINER;

    /** A lock protecting the creation of the cached websocket container. */
    private final static Object LOCK = new Object();

    /** {@inheritDoc} */
    @Override
    public ConnectionTransport create(ClientConnection cc, ClientConnectFuture future) {
        return new ConnectionTransportJsr356(future, cc, getContainer());
    }

    private static WebSocketContainer getContainer() {
        WebSocketContainer container = CACHED_CONTAINER;
        if (container == null) {
            synchronized (LOCK) {
                CACHED_CONTAINER = container = ContainerProvider.getWebSocketContainer();
            }
        }
        return container;
    }


    // Class must be public to be detected
    @ClientEndpoint
    public static final class ConnectionTransportJsr356 extends ConnectionTransport {

        /** The logger. */
        static final Logger LOG = LoggerFactory.getLogger(ConnectionTransportJsr356.class);

        final WebSocketContainer container;

        /** The websocket session. */
        volatile Session session;

        ConnectionTransportJsr356(ClientConnectFuture connectFuture, ClientConnection connection,
                WebSocketContainer container) {
            super(connectFuture, connection);
            this.container = requireNonNull(container);
        }

        void connect(URI uri) throws IOException {
            CountDownLatch done = new CountDownLatch(1);
            Thread t = Thread.currentThread();
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        if (!done.await(20, TimeUnit.SECONDS)) {
                            LOG.error("Connect timed out after 20 seconds");
                            t.interrupt();
                        }
                    } catch (InterruptedException ignore) {}
                }
            };
            new Thread(r, "Connect timeout thread").start();
            try {
                container.connectToServer(this, uri);
            } catch (DeploymentException e) {
                throw new IllegalStateException("Internal Error", e);
            } finally {
                done.countDown();
            }
        }

        /** {@inheritDoc} */
        void doClose(final ClosingCode reason) {
            Session session = this.session;
            if (session != null) {
                CloseReason cr = new CloseReason(new CloseCode() {
                    public int getCode() {
                        return reason.getId();
                    }
                }, reason.getMessage());

                try {
                    session.close(cr);
                } catch (Exception e) {
                    LOG.error("Failed to close connection", e);
                }
            }
        }

        /** {@inheritDoc} */
        @OnClose
        public void onClose(CloseReason closeReason) {
            session = null;
            ClosingCode reason = ClosingCode
                    .create(closeReason.getCloseCode().getCode(), closeReason.getReasonPhrase());
            connection.transportDisconnected(this, reason);
        }

        @OnOpen
        public void onOpen(Session session) {
            this.session = session; // wait on the server to send a hello message
        }

        @OnMessage
        public void onTextMessage(String textMessage) {
            super.onTextMessage(textMessage);
        }

        Executor e = Executors.newSingleThreadExecutor();

        public void sendText(final String text) {
            Session session = this.session;
            if (session != null) {
                connection.connectionManager.forEachListener(new Consumer<MaritimeCloudConnection.Listener>() {
                    public void accept(Listener t) {
                        t.messageSend(text);
                    }
                });
                session.getAsyncRemote().sendText(text);
                // final Future<?> f = session.getAsyncRemote().sendText(text);
                // e.execute(new Runnable() {
                // public void run() {
                // try {
                // f.get();
                // System.out.println("Ok");
                // } catch (InterruptedException | ExecutionException e) {
                // e.printStackTrace();
                // }
                // }
                // });
            }
        }
    }
}
