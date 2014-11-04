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
 *
 * @author Kasper Nielsen
 */
public class SessionStateConnected extends SessionState {

    /** The logger. */
    private static final Logger LOGGER = Logger.get(SessionStateConnected.class);

    final ConnectionTransport transport;

    /**
     * @param session
     */
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

    static void connected(SessionStateConnecting connecting, Binary existingSessionId, Binary newSessionId,
            long lastReceivedMessage) {
        Session s = connecting.session;
        s.fullyLock();
        try {
            if (s.state == connecting) {

                if (existingSessionId == null) { // New session
                    LOGGER.debug("Created new session with id " + newSessionId);
                    s.sessionId = newSessionId;
                }

                s.state = new SessionStateConnected(s, connecting.transport);

                if (existingSessionId != null) {
                    LOGGER.debug("Reconnected with session id " + existingSessionId);
                    s.sender.reconnectUnderLock(lastReceivedMessage);
                }

                s.connectionListener.connected(connecting.uri);
            }
        } finally {
            s.fullyUnlock();
        }
    }
}
