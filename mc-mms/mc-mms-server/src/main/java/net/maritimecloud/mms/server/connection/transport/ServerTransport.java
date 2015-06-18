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

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.message.MessageFormatType;
import net.maritimecloud.mms.server.ServerEventListener;
import net.maritimecloud.mms.server.connection.client.Client;
import net.maritimecloud.mms.server.security.*;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 *
 * @author Kasper Nielsen
 */
public final class ServerTransport {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTransport.class);

    /** An attachment that can be attached to the transport. */
    private final ConcurrentHashMap<String, Object> attachments = new ConcurrentHashMap<>();

    /** The security manager */
    private final MmsSecurityManager securityManager;

    /** A listener of important server side events. */
    private final ServerEventListener eventListener;

    /** The listener to invoke on incoming messages. */
    private final ServerTransportListener listener;

    /** The system time of the last received message. */
    volatile long timeOfLatestIncomingMessage;

    /** The creation time of this transport. */
    private final long timeOfreationTime = System.nanoTime();

    /** The current session. */
    volatile Session wsSession;

    /** Sets the output format of messages. */
    volatile MessageFormatType channelFormatType;

    /** The client subject */
    Subject subject;

    ServerTransport(MmsSecurityManager securityManager, Session wsSession, ServerTransportListener listener, ServerEventListener eventListener) {
        this.securityManager = requireNonNull(securityManager);
        this.listener = requireNonNull(listener);
        this.wsSession = requireNonNull(wsSession);
        this.eventListener = requireNonNull(eventListener);

        // Initialize the client Subject
        initializeSubject();
    }

    /**
     * Initializes the client subject
     */
    private void initializeSubject() {
        // Instantiate the client subject
        subject = new Subject.Builder(securityManager)
                .setSession(wsSession)
                .build();

        AuthenticationToken token = null;
        try {
            // Attempt to resolve an authentication token
            token = securityManager.resolveAuthenticationToken(wsSession);
            if (token != null) {
                // Log in the subject using the authentication token
                subject.login(token);
                LOGGER.info("Successfully authenticated " + token.getPrincipal());
            }
        } catch (AuthenticationException e) {
            LOGGER.warn("Client authentication failed for " + token, e);
            close(MmsConnectionClosingCode.AUTHENTICATION_ERROR.withMessage(e.getMessage()));
        }
        // TEST
        //System.out.println("****** has role mms users: " + subject.hasRole("mms users"));
    }

    public void close(MmsConnectionClosingCode reason) {
        Session wsSession = this.wsSession;
        if (wsSession != null) {
            CloseReason cr = new CloseReason(reason::getId, reason.getMessage());

            try {
                wsSession.close(cr);
            } catch (Exception e) {
                LOGGER.error("Failed to close connection", e);
            }
        }
    }

    void endpointOnBinaryMessage(byte[] binary) {
        timeOfLatestIncomingMessage = System.nanoTime();
        if (channelFormatType == null) {
            channelFormatType = MessageFormatType.MACHINE_READABLE;
        }
        eventListener.transportBinaryMessageReceived(this, binary);
        endpointOnMessage(() -> MmsMessage.parseBinaryMessage(binary));
    }

    void endpointOnClose(CloseReason closeReason) {
        wsSession = null;
        try {
            listener.onClose(this, MmsConnectionClosingCode.create(closeReason.getCloseCode().getCode(),
                    closeReason.getReasonPhrase()));
        } catch (RuntimeException e) {
            LOGGER.error("Failed to process close request", e);
            close(MmsConnectionClosingCode.INTERNAL_ERROR.withMessage(e.getMessage()));
        }
    }

    private void endpointOnMessage(Callable<MmsMessage> c) {
        // Start by parsing the received message
        MmsMessage msg;
        try {
            msg = c.call();
        } catch (Exception e) {
            LOGGER.error("Failed to parse incoming message", e); // TODO not technically an error, we don't care
            close(MmsConnectionClosingCode.BAD_DATA.withMessage(e.getMessage()));
            return;
        }

        eventListener.transportMessageReceived(this, msg);

        // process message
        try {
            listener.onMessageReceived(this, msg);
        } catch (RuntimeException e) {
            LOGGER.error("Failed to process message", e);
            close(MmsConnectionClosingCode.INTERNAL_ERROR.withMessage(e.getMessage()));
        }
    }

    void endpointOnOpen() {
        timeOfLatestIncomingMessage = System.nanoTime();
        try {
            listener.onOpen(this);
        } catch (RuntimeException e) {
            LOGGER.error("Failed to process open request", e);
            close(MmsConnectionClosingCode.INTERNAL_ERROR.withMessage(e.getMessage()));
        }
    }

    void endpointOnTextMessage(String textMessage) {
        timeOfLatestIncomingMessage = System.nanoTime();
        if (channelFormatType == null) {
            channelFormatType = MessageFormatType.HUMAN_READABLE;
        }
        eventListener.transportTextMessageReceived(this, textMessage);
        endpointOnMessage(() -> MmsMessage.parseTextMessage(textMessage));
    }

    /**
     * @return the attachment
     */
    public <T> T getAttachment(String key, Class<T> type) {
        return type.cast(attachments.get(key));
    }

    /**
     * @return the creationTime
     */
    public long getTimeOfCreation() {
        return timeOfreationTime;
    }

    /**
     * @return the latestReceivedMessage
     */
    public long getTimeOfLatestIncomingMessage() {
        return timeOfLatestIncomingMessage;
    }

    /**
     * Send the specified message with the transport.
     *
     * @param message
     *            the message to send
     */
    public void sendMessage(MmsMessage message) {
        try {
            eventListener.transportMessageSend(this, message);
        } catch (RuntimeException e) {
            LOGGER.error("Event listener failed", e);
        }
        Session wsSession = this.wsSession;
        if (wsSession != null) {
            try {
                if (channelFormatType == MessageFormatType.MACHINE_READABLE) {
                    byte[] data = message.toBinary();
                    eventListener.transportBinaryMessageSend(this, data);
                    wsSession.getAsyncRemote().sendBinary(ByteBuffer.wrap(data));
                } else {
                    String textToSend = message.toText();
                    eventListener.transportTextMessageSend(this, textToSend);
                    wsSession.getAsyncRemote().sendText(textToSend);
                }
                listener.onMessageSent(this, message);
            } catch (Exception e) {
                LOGGER.error("Failed to serialize data", e);
                close(MmsConnectionClosingCode.INTERNAL_ERROR.withMessage(e.getMessage()));
            }
        }
    }

    /**
     * Sets a named attachment for this transport. If the attachment is null, the attachment is cleared.
     *
     * @param key
     *            the string of the attachment
     * @param attachment
     *            the attachment
     * @throws NullPointerException
     *             if the specified key is null
     */
    public void setAttachment(String key, Object attachment) {
        if (attachment == null) {
            attachments.remove(key);
        } else {
            attachments.put(key, attachment);
        }
    }

    public MessageFormatType getChannelFormatType() {
        return channelFormatType;
    }

    /**
     * Called when the client has been resolved from a Hello message
     * @param client the client
     */
    public void clientResolved(Client client) {
        try {
            subject.checkClient(client.getId());
            LOGGER.info("Verified client " + client.getId() + " for principal " + subject.getPrincipal());
        } catch (ClientVerificationException e) {
            LOGGER.warn("Client verification failed for client " + client.getId() + " and principal " + subject.getPrincipal(), e);
            close(MmsConnectionClosingCode.INVALID_CLIENT.withMessage(e.getMessage()));
        }
    }
}
