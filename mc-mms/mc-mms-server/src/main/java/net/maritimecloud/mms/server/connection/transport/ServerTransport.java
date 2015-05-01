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
package net.maritimecloud.mms.server.connection.transport;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCode;
import javax.websocket.Session;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.mms.transport.MmsWireProtocol;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kasper Nielsen
 */
public final class ServerTransport {

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ServerTransport.class);

    Object attachment;

    private final long creationTime = System.nanoTime();

    volatile long latestReceivedMessage;

    /** The listener to invoke. */
    final ServerTransportListener listener;

    /** The current session. */
    volatile Session wsSession;

    ServerTransport(Session session, ServerTransportListener listener) {
        this.listener = requireNonNull(listener);
        this.wsSession = requireNonNull(session);
    }

    public void close(MmsConnectionClosingCode reason) {
        Session wsSession = this.wsSession;
        if (wsSession != null) {
            CloseReason cr = new CloseReason(new CloseCode() {
                public int getCode() {
                    return reason.getId();
                }
            }, reason.getMessage());

            try {
                wsSession.close(cr);
            } catch (Exception e) {
                LOG.error("Failed to close connection", e);
            }
        }
    }

    void endpointOnBinaryMessage(byte[] binary) {
        latestReceivedMessage = System.nanoTime();
        MmsMessage msg;
        try {
            msg = MmsMessage.parseBinaryMessage(binary);
            msg.setInbound(true);
        } catch (Exception e) {
            LOG.error("Failed to parse incoming message", e);
            close(MmsConnectionClosingCode.WRONG_MESSAGE.withMessage(e.getMessage()));
            return;
        }
        listener.onMessage(this, msg);
    }

    void endpointOnClose(CloseReason closeReason) {
        wsSession = null;
        listener.onClose(this,
                MmsConnectionClosingCode.create(closeReason.getCloseCode().getCode(), closeReason.getReasonPhrase()));
    }

    void endpointOnOpen() {
        latestReceivedMessage = System.nanoTime();
        listener.onOpen(this);
    }

    void endpointOnTextMessage(String textMessage) {
        latestReceivedMessage = System.nanoTime();
        MmsMessage msg;
        try {
            msg = MmsMessage.parseTextMessage(textMessage);
            msg.setInbound(true);
        } catch (Exception e) {
            LOG.error("Failed to parse incoming message", e);
            close(MmsConnectionClosingCode.WRONG_MESSAGE.withMessage(e.getMessage()));
            return;
        }
        listener.onMessage(this, msg);
    }

    /**
     * @return the attachment
     */
    public Object getAttachment() {
        return attachment;
    }

    /**
     * @return the creationTime
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * @return the latestReceivedMessage
     */
    public long getLatestReceivedMessage() {
        return latestReceivedMessage;
    }

    /**
     * Send the specified message with the transport.
     *
     * @param message
     *            the message to send
     */
    public void sendMessage(MmsMessage message) {
        Session wsSession = this.wsSession;
        if (wsSession != null) {
            message.setInbound(false);
            message.setBinary(MmsWireProtocol.USE_BINARY);

            if (message.isBinary()) {
                try {
                    wsSession.getAsyncRemote().sendBinary(ByteBuffer.wrap(message.toBinary()));
                } catch (IOException e) {
                    LOG.error("Failed to send message", e);
                }
            } else {
                String textToSend = message.toText();
                wsSession.getAsyncRemote().sendText(textToSend);
            }
        }
    }

    /**
     * @param attachment
     *            the attachment to set
     */
    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }
}
