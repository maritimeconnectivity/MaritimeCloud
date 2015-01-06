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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCode;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

/**
 * The default implementation of a connection transport.
 *
 * @author Kasper Nielsen
 */
@ClientEndpoint
public final class ConnectionTransportJsr356 extends ConnectionTransport { // Class must be public to be detected

    /** The logger. */
    static final Logger LOG = Logger.get(ConnectionTransportJsr356.class);

    /** The WebSocket container. */
    private final WebSocketContainer container;

    /** The WebSocket session object set after having connected. */
    volatile Session session;

    ConnectionTransportJsr356(ConnectionTransportListener listener, MmsConnection.Listener connectionListener,
            WebSocketContainer container) {
        super(listener, connectionListener);
        this.container = requireNonNull(container);
    }

    /** {@inheritDoc} */
    public void connectBlocking(URI uri, long time, TimeUnit unit) throws IOException {
        System.out.println("Trying to connect");
        CountDownLatch done = new CountDownLatch(1);
        Thread t = Thread.currentThread();
        Runnable r = () -> {
            try {
                if (!done.await(time, unit)) {
                    LOG.error("Connect timed out after " + time + " " + unit);
                    t.interrupt();
                }
            } catch (InterruptedException ignore) {}
        };
        // We spawn a separate thread to monitor whether or not we have timed out. Because someone forgot
        // to add a timeout argument to the javax.websocket.WebSocketContainer.connectToServer, sigh...
        new Thread(r, "Connect timeout thread").start();
        try {
            container.connectToServer(this, uri);
        } catch (DeploymentException e) {
            throw new IllegalStateException("Internal Error", e);
        } catch (IOException e) {
            if (e.getCause() instanceof InterruptedException) {
                throw new InterruptedIOException("Connect timed out after " + time + " " + unit);
            }
            throw e;
        } finally {
            done.countDown();
        }
    }

    /** {@inheritDoc} */
    public void closeTransport(MmsConnectionClosingCode reason) {
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
        MmsConnectionClosingCode reason = MmsConnectionClosingCode.create(closeReason.getCloseCode().getCode(),
                closeReason.getReasonPhrase());
        listener.onClose(reason);
        connectionListener.disconnected(reason);
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session; // wait on the server to send a hello message
        session.setMaxTextMessageBufferSize(10 * 1024 * 1024);
        listener.onOpen();
    }

    /** {@inheritDoc} */
    @OnMessage
    public void onTextMessage(String textMessage) {
        super.onTextMessage(textMessage);
    }

    /** {@inheritDoc} */
    public void sendMessage(MmsMessage message) {
        Session session = this.session;
        if (session != null) {
            String textToSend = message.toText();
            connectionListener.textMessageSend(textToSend);
            session.getAsyncRemote().sendText(textToSend);
        }
    }
}
