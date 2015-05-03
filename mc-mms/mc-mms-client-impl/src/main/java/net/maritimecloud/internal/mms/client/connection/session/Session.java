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

import static java.util.Objects.requireNonNull;

import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.internal.mms.client.ClientInfo;
import net.maritimecloud.internal.mms.client.connection.transport.ClientTransportFactory;
import net.maritimecloud.internal.mms.client.connection.transport.ClientTransportListener;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.util.concurrent.CompletableFuture;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.message.Message;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class Session {

    /** The logger. */
    private static final Logger LOGGER = Logger.get(Session.class);

    final MmsConnection.Listener connectionListener;

    final ClientTransportFactory ctm;

    final ClientInfo info;

    volatile boolean isClosed;

    volatile long latestReceivedId;

    final SessionListener listener;

    final ReentrantLock lock = new ReentrantLock();

    final ReentrantLock receiveLock = new ReentrantLock();

    final SessionSender sender;

    volatile Binary sessionId;

    /** The current state of this session must only changed when fully locked. Is null when the session is dead. */
    volatile SessionState state;

    final ClientTransportListener tl;

    Session(ClientTransportFactory ctm, ClientInfo info, SessionListener listener,
            MmsConnection.Listener connectionListener) {
        this.ctm = requireNonNull(ctm);
        this.info = requireNonNull(info);
        this.sender = new SessionSender(this);
        this.listener = requireNonNull(listener);
        this.connectionListener = requireNonNull(connectionListener);
        this.tl = new ClientTransportListener() {
            @Override
            public void onClose(MmsConnectionClosingCode closingCode) {
                listenerOnClose(closingCode);
            }

            @Override
            public void onMessageReceived(MmsMessage message) {
                listenerOnMessage(message);
            }
        };
    }

    /**
     * Closes this session.
     *
     * @param reason
     *            the reason for the close
     */
    public void closeSession(MmsConnectionClosingCode reason) {
        fullyLock();
        try {
            isClosed = true;
            if (state instanceof SessionStateConnecting) {
                ((SessionStateConnecting) state).cancelWhileFullyLocked(reason);
            } else if (state instanceof SessionStateConnected) {
                SessionStateDisconnecting.disconnectWhileFullyLocked((SessionStateConnected) state, reason);
            }
            sender.sup();// notify sender thread of disconnect
        } finally {
            fullyUnlock();
        }
    }

    void fullyLock() {
        // must be reverse order of fullyUnlock
        lock.lock();
        receiveLock.lock();
        sender.lock.lock();
    }

    void fullyUnlock() {
        // must be reverse order of fullyLock
        sender.lock.unlock();
        receiveLock.unlock();
        lock.unlock();
    }

    /** @return whether or not this session is currently connected */
    public boolean isConnected() {
        return state instanceof SessionStateConnected;
    }

    void listenerOnClose(MmsConnectionClosingCode closingCode) {
        fullyLock();
        try {
            if (state instanceof SessionStateConnecting && closingCode.equals(MmsConnectionClosingCode.INVALID_SESSION)) {
                state = new SessionStateDisconnected(this);
                listener.onSessionClose(closingCode);
                connectionListener.disconnected(closingCode);
            } else if (state instanceof SessionStateDisconnecting) {
                if (closingCode.getId() == MmsConnectionClosingCode.NORMAL.getId()) {
                    // Complete all outstand
                    sender.completeAll();

                    state = new SessionStateDisconnected(this);
                    listener.onSessionClose(closingCode);
                    connectionListener.disconnected(closingCode);
                }
            } else if (!isClosed) { // Was not closed properly, lets reconnect
                SessionStateConnecting ssc = new SessionStateConnecting(this, info);
                this.state = ssc;
                ssc.connectAsynchronously();
            }
        } finally {
            fullyUnlock();
        }
    }

    void listenerOnMessage(MmsMessage message) {
        receiveLock.lock();
        try {
            SessionState state = this.state;
            if (state != null) {
                state.onMessage(message);
            }
        } finally {
            receiveLock.unlock();
        }
    }

    public void sendMessage(Message message, CompletableFuture<Void> onAck) {
        sender.send(message, onAck);
    }

    public static Session createNewSessionAndConnect(ClientTransportFactory ctm, ClientInfo info,
            SessionListener listener, MmsConnection.Listener connectionListener) {
        LOGGER.debug("Creating new session");
        Session session = new Session(ctm, info, listener, connectionListener);
        session.fullyLock();
        try {
            SessionStateConnecting ssc = new SessionStateConnecting(session, info);
            session.state = ssc;
            ssc.connectAsynchronously();

            session.sender.start();
        } finally {
            session.fullyUnlock();
        }
        return session;
    }
}
