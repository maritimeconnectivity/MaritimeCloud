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
package net.maritimecloud.internal.mms.client.connection;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.maritimecloud.internal.mms.client.ClientInfo;
import net.maritimecloud.internal.mms.client.connection.session.Session;
import net.maritimecloud.internal.mms.client.connection.session.SessionListener;
import net.maritimecloud.internal.mms.client.connection.transport.ClientTransportFactory;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.message.Message;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import org.cakeframework.container.lifecycle.RunOnStop;

/**
 *
 * @author Kasper Nielsen
 */
public class ClientConnection {

    /** The logger. */
    private static final Logger LOGGER = Logger.get(ClientConnection.class);

    private final MmsConnection.Listener mmsListener;

    final ClientInfo clientInfo;

    /** The manager responsible for creating new WebSockets. */
    private final ClientTransportFactory ctm;

    /** Whether or not the connection is enabled. */
    private volatile boolean isEnabled;

    final ReentrantLock lock = new ReentrantLock();

    volatile Session session;

    final SessionListener sessionListener = new SessionListener() {

        @Override
        public void onMessage(MmsMessage message) {
            listenerOnMessage(message);
        }

        @Override
        public void onSessionClose(MmsConnectionClosingCode closingCode) {
            listenerConnectionClosed(closingCode);
        }
    };

    /** Signaled when the state of the connection manager changes. */
    final Condition stateChange = lock.newCondition();

    /** Consumers of messages. */
    private final CopyOnWriteArraySet<Consumer<MmsMessage>> subscribers = new CopyOnWriteArraySet<>();

    public ClientConnection(ClientTransportFactory ctm, ClientInfo info, MmsClientConfiguration b) {
        this.ctm = requireNonNull(ctm);
        this.clientInfo = requireNonNull(info);
        this.mmsListener = new MmsConnectionListenerInvoker(this, b);
    }

    public boolean await(boolean connected, long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            for (;;) {
                if (isConnected() == connected) {
                    return true;
                } else if (nanos <= 0) {
                    return false;
                }
                nanos = stateChange.awaitNanos(nanos);
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean isConnected() {
        Session session = this.session;
        return session != null && session.isConnected();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    void listenerConnectionClosed(MmsConnectionClosingCode closingCode) {
        lock.lock();
        try {
            if (isEnabled) { // Reconnect
                session = Session.createNewSessionAndConnect(ctm, clientInfo, sessionListener, mmsListener);
            } else {
                session = null;
            }
            stateChange.signalAll();
        } finally {
            lock.unlock();
        }
    }

    void listenerOnMessage(MmsMessage message) {
        // save somewhere as delivered
        for (Consumer<MmsMessage> c : subscribers) {
            try {
                c.accept(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CompletableFuture<Void> sendMessage(Message b) {
        lock.lock();
        try {
            if (!isEnabled) {
                throw new IllegalStateException("The mms connection has not been enabled.");
            }
            Session session = this.session;
            CompletableFuture<Void> result = new CompletableFuture<>();
            // Save message for later sending
            session.sendMessage(b, result);
            return result;
        } finally {
            lock.unlock();
        }
    }

    public void setEnabled(boolean isEnabled) {
        LOGGER.debug(isEnabled ? "Enabling" : "Disabling" + " connection to the MMS Server");
        lock.lock();
        try {
            if (isEnabled != this.isEnabled) {
                this.isEnabled = isEnabled;
                if (isEnabled) {
                    if (session == null) {
                        session = Session.createNewSessionAndConnect(ctm, clientInfo, sessionListener, mmsListener);
                    }
                } else { // Disable
                    if (session != null) { // only disconnect if we have an active session
                        session.closeSession(MmsConnectionClosingCode.NORMAL);
                    }
                }
                stateChange.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    @RunOnStop
    public void shutdown() {
        setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    public <T> void subscribe(Class<? extends T> messageType, BiConsumer<MmsMessage, T> consumer) {
        LOGGER.debug("Subscribing " + consumer + " to instances of " + messageType);
        subscribers.add((m) -> {
            Message e = m.getMessage();
            if (messageType.isAssignableFrom(e.getClass())) {
                consumer.accept(m, (T) e);
            }
        });
    }
}
