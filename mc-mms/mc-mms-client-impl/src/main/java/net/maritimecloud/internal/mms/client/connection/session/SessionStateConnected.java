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

import net.maritimecloud.internal.mms.client.connection.transport.ConnectionTransport;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.util.Binary;

/**
 * A session is in the connected state whenever there exist an open websocket connection between the client and a MMS
 * server.
 *
 * @author Kasper Nielsen
 */
public final class SessionStateConnected extends SessionState {

    /** The logger. */
    static final Logger LOGGER = Logger.get(SessionStateConnected.class);

    /** The actual transport used for sending and receiving messages. */
    final ConnectionTransport transport;

    private SessionStateConnected(Session session, ConnectionTransport transport) {
        super(session);
        this.transport = transport;
    }

    /** {@inheritDoc} */
    @Override
    public void onMessage(MmsMessage message) {
        session.receiveLock.lock();
        try {
            session.latestReceivedId = message.getMessageId();
            session.sender.onAck(message.getLatestReceivedId());
            session.listener.onMessage(message);
        } finally {
            session.receiveLock.unlock();
        }
    }

    /**
     * Invoked whenever a remote connection has been successfully created.
     *
     * @param connectingState
     *            the connecting state we are transitioning from
     * @param existingSessionId
     *            an existing session id if we are reconnecting
     * @param newSessionId
     *            a new session id if it is the initial connection
     * @param lastReceivedMessage
     *            the id of the last received message id
     */
    static void connected(SessionStateConnecting connectingState, Binary existingSessionId, Binary newSessionId,
            long lastReceivedMessage) {
        Session session = connectingState.session;
        session.fullyLock();
        try {
            // only update state if the current state == expected state after fully locking
            if (session.state == connectingState) {
                LOGGER.info("Connected to  " + connectingState.uri);

                if (existingSessionId == null) { // New session
                    LOGGER.debug("Created new session with id " + newSessionId);
                    session.sessionId = newSessionId;
                }

                session.state = new SessionStateConnected(session, connectingState.transport);

                // If we are reconnecting make sure we resend messages that have not been acknowledged
                if (existingSessionId != null) {
                    LOGGER.debug("Reconnected with session id " + existingSessionId);
                    session.sender.reconnectUnderLock(lastReceivedMessage);
                }

                // invoke user specified connection listeners.
                session.connectionListener.connected(connectingState.uri);
            }
        } finally {
            session.fullyUnlock();
        }
    }
}
