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
package net.maritimecloud.internal.net.client.connection;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.internal.net.client.ClientContainer;
import net.maritimecloud.internal.net.client.util.ThreadManager;
import net.maritimecloud.net.MaritimeCloudClientConfiguration;
import net.maritimecloud.net.MaritimeCloudConnection;

import org.picocontainer.Startable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Kasper Nielsen
 */
public class ConnectionManager implements MaritimeCloudConnection, Startable {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);

    final ClientContainer client;

    volatile ClientConnection connection;

    ConnectionMessageBus hub;

    /** Listeners for updates to the connection status. */
    final CopyOnWriteArraySet<MaritimeCloudConnection.Listener> listeners = new CopyOnWriteArraySet<>();

    /** The main lock of the connection manager. */
    final ReentrantLock lock = new ReentrantLock();

    /** The state of the connection manager. */
    volatile State state = State.SHOULD_STAY_DISCONNECTED;

    /** Signaled when the state of the connection manager changes. */
    final Condition stateChange = lock.newCondition();

    final ThreadManager threadManager;

    /** The URI to connect to. Is constant. */
    final URI uri;

    public ConnectionManager(ClientContainer client, ThreadManager threadManager, MaritimeCloudClientConfiguration b) {
        this.client = client;
        this.threadManager = threadManager;
        for (MaritimeCloudConnection.Listener listener : b.getListeners()) {
            addListener(listener);
        }
        try {
            this.uri = new URI("ws://" + b.getHost());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void addListener(MaritimeCloudConnection.Listener listener) {
        listeners.add(requireNonNull(listener));
    }

    /** {@inheritDoc} */
    @Override
    public final boolean awaitConnected(long timeout, TimeUnit unit) throws InterruptedException {
        return awaitState(timeout, unit, true);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean awaitDisconnected(long timeout, TimeUnit unit) throws InterruptedException {
        return awaitState(timeout, unit, false);
    }

    private boolean awaitState(long timeout, TimeUnit unit, boolean state) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            for (;;) {
                if (isConnected() == state) {
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

    /** {@inheritDoc} */
    @Override
    public final void connect() {
        lock.lock();
        try {
            State state = this.state;
            if (state.isShutdown()) {
                throw new IllegalStateException("The client has been shutdown");
            }
            if (connection == null) {
                connection = ClientConnection.create(this);
            }
            if (state == State.SHOULD_STAY_DISCONNECTED) {
                this.state = State.SHOULD_STAY_CONNECTED;
                stateChange.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void disconnect() {
        lock.lock();
        try {
            State state = this.state;
            if (state.isShutdown()) {
                throw new IllegalStateException("The client has been shutdown");
            } else if (state == State.SHOULD_STAY_CONNECTED) {
                this.state = State.SHOULD_STAY_DISCONNECTED;
                stateChange.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isConnected() {
        ClientConnection cc = connection;
        return cc != null && cc.isConnected();
    }

    void mainThread() {
        lock.lock();
        try {
            for (;;) {
                switch (this.state) {
                case SHOULD_STAY_CONNECTED:
                    if (connection == null) {
                        connection = ClientConnection.create(this);
                    }
                    if (!isConnected()) {
                        connection.connect();
                    }
                    break;
                case SHOULD_STAY_DISCONNECTED:
                    if (connection != null) {
                        connection.disconnect();
                    }
                    break;
                case SHOULD_SHUTDOWN:
                case SHUTDOWN: // should never be invoked, but no reason to check
                    if (connection != null) {
                        connection.disconnect();
                    } else {
                        this.state = State.SHUTDOWN;
                        stateChange.signalAll();
                        return;
                    }
                }
                try {
                    stateChange.await();
                } catch (InterruptedException ignore) {}
            }
        } finally {
            lock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void start() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    mainThread();
                } catch (Throwable t) {
                    LOG.error("Something went wrong", t);
                    throw t;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /** {@inheritDoc} */
    @Override
    public final void stop() {
        // First shutdown the websocket
        // WebSocketContainer container = this.container;
        // if (container != null) {
        // try {
        // ((ContainerLifeCycle) container).stop();
        // } catch (Exception e) {
        // LOG.error("Failed to close websocket container", e);
        // }
        // }

        lock.lock();
        try {
            for (;;) {
                State state = this.state;
                if (state == State.SHUTDOWN) {
                    return;
                } else if (state == State.SHOULD_STAY_CONNECTED || state == State.SHOULD_STAY_DISCONNECTED) {
                    this.state = State.SHOULD_SHUTDOWN;
                    stateChange.signalAll();
                }
                try {
                    stateChange.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    LOG.error("Thread interrupted", e);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    enum State {
        SHOULD_SHUTDOWN, SHOULD_STAY_CONNECTED, SHOULD_STAY_DISCONNECTED, SHUTDOWN;

        boolean isShutdown() {
            return this == SHOULD_SHUTDOWN || this == SHUTDOWN;
        }
    }
}
