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

    /** A listener for changes to the connection */
    final MmsConnection.Listener connectionListener;

    /** The listener of transport events. */
    final ClientTransportListener transportListener;

    /**
     * Creates a new ClientTransport
     *
     * @param transportListener
     *            the transport listener
     * @param connectionListener
     *            the connection listener
     */
    protected ClientTransport(ClientTransportListener transportListener, MmsConnection.Listener connectionListener) {
        this.transportListener = requireNonNull(transportListener);
        this.connectionListener = requireNonNull(connectionListener);
    }

    /**
     * Closes the transport with the specified closing code
     *
     * @param closingCode
     *            the closing code
     */
    public abstract void closeTransport(MmsConnectionClosingCode closingCode);

    /**
     * Tries to connect (blockingly) to the specified URI with a timeout of 20 seconds.
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

    /**
     * Called when a binary message is received over the wire
     *
     * @param binaryMessage
     *            the text message
     */
    void onBinaryMessage(byte[] binaryMessage) {
        connectionListener.binaryMessageReceived(binaryMessage);

        MmsMessage msg;
        try {
            msg = MmsMessage.parseBinaryMessage(binaryMessage);
            msg.setInbound(true);
        } catch (Exception e) {
            LOGGER.error("Failed to parse incoming binary message", e);
            closeTransport(MmsConnectionClosingCode.WRONG_MESSAGE.withMessage(e.getMessage()));
            return;
        }
        transportListener.onMessageReceived(msg);
    }

    /**
     * Called when a text message is received over the wire
     *
     * @param textMessage
     *            the text message
     */
    void onTextMessage(String textMessage) {
        connectionListener.textMessageReceived(textMessage);

        MmsMessage msg;
        try {
            msg = MmsMessage.parseTextMessage(textMessage);
            msg.setInbound(true);
        } catch (Exception e) {
            LOGGER.error("Failed to parse incoming text message", e);
            closeTransport(MmsConnectionClosingCode.WRONG_MESSAGE.withMessage(e.getMessage()));
            return;
        }
        transportListener.onMessageReceived(msg);
    }

    /**
     * Sends the specified message.
     *
     * @param message
     *            the message to send
     */
    public abstract void sendMessage(MmsMessage message);
}
