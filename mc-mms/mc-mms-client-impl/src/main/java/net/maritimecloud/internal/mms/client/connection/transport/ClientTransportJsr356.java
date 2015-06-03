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
import net.maritimecloud.internal.util.concurrent.CompletableFuture;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.message.MessageFormatType;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of a connection transport.
 *
 * @author Kasper Nielsen
 */
public final class ClientTransportJsr356 extends ClientTransport { // Class must be public to be detected

    /** The logger. */
    static final Logger LOGGER = Logger.get(ClientTransportJsr356.class);

    /** The WebSocket container. */
    private final WebSocketContainer container;

    private final MmsClientConfiguration conf;

    private final MessageFormatType mft;

    /**
     * See https://github.com/MaritimeCloud/MaritimeCloud/issues/29, basically we need a lock for async writes in
     * Tomcat.
     */
    private final Object writeLock = new Object();

    /** The WebSocket session object set after having successfully connected. */
    private volatile Session wsSession;

    /**
     * Creates a new ClientTransportJsr356.
     *
     * @param conf the MMS client configuration
     * @param transportListener
     *            the transport listener
     * @param connectionListener
     *            the connection listener
     * @param container
     *            the web-socket container
     */
    ClientTransportJsr356(MmsClientConfiguration conf, ClientTransportListener transportListener,
            MmsConnection.Listener connectionListener, WebSocketContainer container) {
        super(transportListener, connectionListener);

        this.conf = conf;
        this.mft = conf != null && conf.useBinary() ? MessageFormatType.MACHINE_READABLE : MessageFormatType.HUMAN_READABLE;
        this.container = requireNonNull(container);
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
                LOGGER.error("Failed to close connection", e);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void connectBlocking(URI uri, long time, TimeUnit unit) throws IOException {
        // Someone forgot to add a timeout argument to the javax.websocket.WebSocketContainer.connectToServer, sigh...
        CompletableFuture<Void> cf = new CompletableFuture<>();
        Thread t = Thread.currentThread();
        cf.orTimeout(time, unit).handle((v, tt) -> {
            if (tt instanceof TimeoutException) {
                LOGGER.error("Connect timed out after " + time + " " + unit);
                t.interrupt();
            }
            return v;
        });

        try {
            // Associate a client endpoint configurator
            ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                    .configurator(new ClientTransportConfigurator())
                    .build();

            container.connectToServer(new ClientTransportEndpoint(), config, uri);
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
    public void sendMessage(MmsMessage message) {
        Session session = this.wsSession;
        if (session != null) {
            message.setInbound(false);
            if (mft == MessageFormatType.MACHINE_READABLE) {
                try {
                    byte[] data = message.toBinary();
                    connectionListener.binaryMessageSend(data);
                    synchronized (writeLock) {
                        session.getBasicRemote().sendBinary(ByteBuffer.wrap(data));
                    }
                } catch (IOException e) {
                    if (wsSession.isOpen()) {
                        // TODO: Proper error handling
                        throw new RuntimeException("Error sending binary message", e);
                    }
                }
            } else {
                try {
                    String textToSend = message.toText();
                    connectionListener.textMessageSend(textToSend);
                    synchronized (writeLock) {
                        session.getBasicRemote().sendText(textToSend);
                    }
                } catch (IOException e) {
                    if (wsSession.isOpen()) {
                        // TODO: Proper error handling
                        throw new RuntimeException("Error sending text message", e);
                    }
                }
            }

            transportListener.onMessageSent(message);
        }
    }

    /**
     * The websocket endpoint implementation
     */
    class ClientTransportEndpoint extends Endpoint {

        /**
         * Called when a new web socket connection is opened
         *
         * @param session the websocket session
         * @param config the websocket endpoint configuration
         */
        @Override
        @SuppressWarnings("all")
        public void onOpen(Session session, EndpointConfig config) {
            wsSession = session; // wait on the server to send a hello message
            session.setMaxTextMessageBufferSize(10 * 1024 * 1024);
            transportListener.onOpen();

            // NB: Do not replace with Lambda - it does not work, and I plainly do not understand why that is...
            //session.addMessageHandler((MessageHandler.Whole<String>) this::onTextMessage);
            //session.addMessageHandler((MessageHandler.Whole<ByteBuffer>) message -> onBinaryMessage(message.array()));

            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    onTextMessage(message);
                }
            });
            session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
                @Override
                public void onMessage(ByteBuffer message) {
                    onBinaryMessage(message.array());
                }
            });
        }

        /**
         * Called when a web socket connection is closed
         *
         * @param session the websocket session
         * @param closeReason the close reason
         */
        @Override
        public void onClose(Session session, CloseReason closeReason) {
            LOGGER.info("Socket closed");
            wsSession = null;
            MmsConnectionClosingCode reason = MmsConnectionClosingCode.create(closeReason.getCloseCode().getCode(),
                    closeReason.getReasonPhrase());
            //Start a new thread to close it. Websocket async is a total mess
            //Basically there is a deadlock if writing at the same time.
            Thread t = new Thread(() -> {
                transportListener.onClose(reason);
                connectionListener.disconnected(reason);
            });
            t.start();
        }
    }

    /**
     * Custom websocket endpoint configurator class.
     * <p>
     * The {@code beforeRequest()} method will be called before the upgrade request
     * is issued, and may be used for updating the list of request headers,
     * to e.g. facilitate authentication.
     */
    class ClientTransportConfigurator extends ClientEndpointConfig.Configurator {

        /** {@inheritDoc} **/
        @Override
        public void beforeRequest(Map<String, List<String>> headers) {
            if (conf != null) {
                conf.getHeaders().entrySet()
                        .forEach(e -> headers.put(e.getKey(), Collections.singletonList(e.getValue())));
            }

            super.beforeRequest(headers);
        }
    }
}
