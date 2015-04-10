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

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.MmsWireProtocol;
import net.maritimecloud.internal.util.concurrent.CompletableFuture;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of a connection transport.
 *
 * @author Kasper Nielsen
 */
@ClientEndpoint
public final class ClientTransportJsr356 extends ClientTransport { // Class must be public to be detected

    /** The logger. */
    static final Logger LOG = Logger.get(ClientTransportJsr356.class);

    /** The WebSocket container. */
    private final WebSocketContainer container;

    /** The WebSocket session object set after having successfully connected. */
    private volatile Session wsSession;

    ClientTransportJsr356(ClientTransportListener listener, MmsConnection.Listener connectionListener,
            WebSocketContainer container) {
        super(listener, connectionListener);
        this.container = requireNonNull(container);
    }

    /** {@inheritDoc} */
    @Override
    public void connectBlocking(URI uri, long time, TimeUnit unit) throws IOException {
        // Someone forgot to add a timeout argument to the javax.websocket.WebSocketContainer.connectToServer, sigh...
        CompletableFuture<Void> cf = new CompletableFuture<>();
        Thread t = Thread.currentThread();
        cf.orTimeout(time, unit).handle((v, tt) -> {
            if (tt instanceof TimeoutException) {
                LOG.error("Connect timed out after " + time + " " + unit);
                t.interrupt();
            }
            return v;
        });

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
            cf.complete(null);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void closeTransport(MmsConnectionClosingCode reason) {
        Session session = this.wsSession;
        if (session != null) {
            CloseReason cr = new CloseReason(reason::getId, reason.getMessage());

            try {
                session.close(cr);
            } catch (Exception e) {
                LOG.error("Failed to close connection", e);
            }
        }
    }

    /**
     * Called when a web socket connection is closed
     * @param closeReason the close reason
     */
    @OnClose
    public void onClose(CloseReason closeReason) {
        wsSession = null;
        MmsConnectionClosingCode reason = MmsConnectionClosingCode.create(closeReason.getCloseCode().getCode(),
                closeReason.getReasonPhrase());
        listener.onClose(reason);
        connectionListener.disconnected(reason);
    }

    /** Called when a new web socket connection is opened */
    @OnOpen
    public void onOpen(Session session) {
        this.wsSession = session; // wait on the server to send a hello message
        session.setMaxTextMessageBufferSize(10 * 1024 * 1024);
        listener.onOpen();
    }

    /** {@inheritDoc} */
    @OnMessage
    @Override
    public void onTextMessage(String textMessage) {
        super.onTextMessage(textMessage); // overridden for the @OnMessage annotation
    }

    /** {@inheritDoc} */
    @OnMessage
    @Override
    public void onBinaryMessage(byte[] binaryMessage) {
        super.onBinaryMessage(binaryMessage); // overridden for the @OnMessage annotation
    }

    /** {@inheritDoc} */
    @Override
    public void sendMessage(MmsMessage message) {
        Session session = this.wsSession;
        if (session != null) {
            if (MmsWireProtocol.USE_BINARY) {
                try {
                    byte[] data = message.toBinary();
                    connectionListener.binaryMessageSend(data);
                    session.getAsyncRemote().sendBinary(ByteBuffer.wrap(data));
                } catch (IOException e) {
                    // TODO: Proper error handling
                    throw new RuntimeException("Error sending binary message", e);
                }
            } else {
                String textToSend = message.toText();
                connectionListener.textMessageSend(textToSend);
                session.getAsyncRemote().sendText(textToSend);
            }
        }
    }
}
