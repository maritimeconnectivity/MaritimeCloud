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
package net.maritimecloud.mms.server.connection.client;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.PositionReport;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;
import net.maritimecloud.mms.server.endpoints.ServerClientEndpointManager;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public class Client {

    /** The client manager that manages this client. */
    final ClientManager clientManager;

    /** A holder of various client properties. */
    private final ClientProperties clientProperties = new ClientProperties("Unknown", "Unknown", "Unknown");

    final ServerClientEndpointManager endpointManager = new ServerClientEndpointManager(this);

    /** The unique if of the client. */
    private final String id;

    volatile PositionTime latestPositionAndTime;

    /** A read write lock for the client. */
    final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /** The current state of this client. */
    volatile ClientInternalState state;

    volatile long timeOfLatestReceivedMessage = System.nanoTime();

    public Client(ClientManager manager, ServerTransport initialTransport, String id) {
        this.clientManager = requireNonNull(manager);
        this.id = id;
        this.state = new ClientInternalState(State.CONNECTING, initialTransport, null);
    }

    public void close(MmsConnectionClosingCode closingCode) {
        lock.writeLock().lock();
        try {
            ClientInternalState state = this.state;
            if (state.state != State.TERMINATED) {
                state.transport.close(closingCode);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    Client connectWithWriteLock(ServerTransport transport) {
        Session session = new Session(this);
        state = new ClientInternalState(State.CONNECTED, transport, session);
        MmsMessage mm = new MmsMessage(new Connected().setSessionId(session.getSessionId())
                .setLastReceivedMessageId(0L));
        transport.sendMessage(mm); // Send connected message
        session.onConnectWithWriteLock(transport, 0);
        return this;
    }


    SessionMessageFuture sendMessage(Session requireSession, Message m) {
        lock.readLock().lock();
        try {
            ClientInternalState state = this.state;
            if (state.state == State.CONNECTING || state.state == State.TERMINATED) {
                return SessionMessageFuture.notConnected(m);
            }
            final Session session = state.session;
            if (requireSession != null && requireSession != session) {
                return SessionMessageFuture.wrongSession(m);
            }
            return session.enqueueMessageWithReadLock(m);
        } finally {
            lock.readLock().unlock();
        }
    }


    /**
     * @return the clientProperties
     */
    public ClientProperties getClientProperties() {
        return clientProperties;
    }

    /**
     * @return the endpointManager
     */
    public ServerClientEndpointManager getEndpointManager() {
        return endpointManager;
    }

    /**
     * Returns the id of the client.
     *
     * @return the id of the client
     */
    public String getId() {
        return id;
    }

    /**
     * @return the latest
     */
    public PositionTime getLatestPositionAndTime() {
        return latestPositionAndTime;
    }

    /**
     * @return the latestReceivedMessage
     */
    public long getTimeOfLatestReceivedMessage() {
        return timeOfLatestReceivedMessage;
    }

    /**
     * Returns whether or not there is active websocket to the client.
     *
     * @return whether or not there is active websocket to the client
     */
    public boolean isConnected() {
        return state.state == State.CONNECTED;
    }

    // Invoked when socket closes.
    // This can happen for a number of reasons.
    // The client closed it normally
    // The closed the server exception or normally
    // No matter what this method is always invoked.
    void onClose(ServerTransport t, MmsConnectionClosingCode closingCode) {
        lock.writeLock().lock();
        try {
            ClientInternalState state = this.state;
            if (state.state == State.CONNECTED) {
                if (closingCode.getId() == 1000) {
                    this.state = ClientInternalState.TERMINATED;
                    clientManager.clients.remove(id, this);
                } else {
                    this.state = new ClientInternalState(State.DISCONNECTED, t, state.session);
                }
                state.session.disconnectedWithWriteLock(closingCode.getId() == 1000);
            } else if (state.state == State.CONNECTING) {
                this.state = ClientInternalState.TERMINATED;
                clientManager.clients.remove(id, this);
            } else {
                throw new IllegalStateException();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    void onMessage(ServerTransport t, MmsMessage message) {
        timeOfLatestReceivedMessage = System.nanoTime();
        lock.readLock().lock();
        try {
            ClientInternalState state = this.state;
            if (state.transport == t && state.state == State.CONNECTED) {
                if (message.getM() instanceof PositionReport) {
                    PositionTime pt = ((PositionReport) message.getM()).getPositionTime();
                    // Should we close the client if going back in time??? Think it can happen
                    // not for a single session, but inbetween sessions.
                    if (pt.getTime() > latestPositionAndTime.getTime()) {
                        latestPositionAndTime = pt;
                    }
                }
                state.session.onMessageWithReadLock(message);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public SessionMessageFuture send(Message message) {
        return clientManager.sendMessage(id, message);
    }

    enum State {
        CONNECTED, CONNECTING, DISCONNECTED, TERMINATED;
    }
}
