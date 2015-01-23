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
import java.net.URI;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

/**
 * The client implementation of a transport. Every time the client connects to a server a new transport is created.
 * Unlike the session mechanism which will persist over multiple connects, and provide smooth reconnect.
 *
 * @author Kasper Nielsen
 */
public abstract class ClientTransport {

    /** The logger. */
    private static final Logger LOGGER = Logger.get(ClientTransport.class);

    /** The listener of transport events. */
    final ClientTransportListener listener;

    /** A listener for changes to the connection */
    final MmsConnection.Listener connectionListener;

    protected ClientTransport(ClientTransportListener listener, MmsConnection.Listener connectionListener) {
        this.listener = requireNonNull(listener);
        this.connectionListener = requireNonNull(connectionListener);
    }

    /**
     * Tries to connect to the specified URI with a timeout of 20 seconds.
     *
     * @param uri
     *            the URI to connect to
     * @throws IOException
     *             if we failed to connect
     */
    public final void connectBlocking(URI uri) throws IOException {
        connectBlocking(uri, 20, TimeUnit.SECONDS);
    }

    public abstract void connectBlocking(URI uri, long time, TimeUnit unit) throws IOException;

    public abstract void closeTransport(MmsConnectionClosingCode reason);

    void onTextMessage(String textMessage) {
        MmsMessage msg;
        connectionListener.textMessageReceived(textMessage);
        try {
            msg = MmsMessage.parseTextMessage(textMessage);
        } catch (Exception e) {
            LOGGER.error("Failed to parse incoming message", e);
            closeTransport(MmsConnectionClosingCode.WRONG_MESSAGE.withMessage(e.getMessage()));
            return;
        }
        listener.onMessage(msg);
    }

    /**
     * Send the specified message with the transport.
     *
     * @param message
     *            the message to send
     */
    public abstract void sendMessage(MmsMessage message);
}
