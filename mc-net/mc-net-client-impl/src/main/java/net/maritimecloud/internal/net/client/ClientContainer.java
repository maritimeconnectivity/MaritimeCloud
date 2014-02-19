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
package net.maritimecloud.internal.net.client;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.broadcast.BroadcastManager;
import net.maritimecloud.internal.net.client.connection.ConnectionManager;
import net.maritimecloud.internal.net.client.connection.ConnectionMessageBus;
import net.maritimecloud.internal.net.client.connection.ConnectionTransportManager;
import net.maritimecloud.internal.net.client.service.ClientServiceManager;
import net.maritimecloud.internal.net.client.service.PositionManager;
import net.maritimecloud.internal.net.client.util.ThreadManager;
import net.maritimecloud.net.MaritimeCloudClientConfiguration;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionTime;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.behaviors.Caching;
import org.picocontainer.containers.ImmutablePicoContainer;

/**
 * The internal client.
 * 
 * @author Kasper Nielsen
 */
@SuppressWarnings("serial")
public class ClientContainer extends ReentrantLock {

    /** The container is is normal running mode. (certain pre-start hooks may still be running. */
    static final int S_RUNNING = 0;

    /** The container has been started either by a preStart() or by invoking a lazy-starting method. */
    static final int S_STARTING = 1;

    /** The container has been shutdown, for example, by calling shutdown(). */
    static final int S_SHUTDOWN = 2;

    /** The container has been fully terminated. */
    static final int S_TERMINATED = 3;

    private final String clientConnectString;

    /** The id of this client */
    private final MaritimeId clientId;

    /** PicoContainer instance. Got really tired of Guice, so replaced it with PicoContainer. */
    private final DefaultPicoContainer picoContainer = new DefaultPicoContainer(new Caching());

    /** Supplies the current position. */
    private final PositionReader positionSupplier;

    /** The current state of the client. Only set while holding lock, can be read at any time. */
    private volatile int state /* = 0 */;

    /** A latch that is released when the client has been terminated. */
    private final CountDownLatch terminated = new CountDownLatch(1);

    private final ThreadManager threadManager;

    /**
     * Creates a new instance of this class.
     * 
     * @param configuration
     *            the configuration
     */
    ClientContainer(MaritimeCloudClientConfiguration configuration) {
        clientId = requireNonNull(configuration.getId());
        positionSupplier = requireNonNull(configuration.getPositionReader());

        picoContainer.addComponent(configuration);
        picoContainer.addComponent(this);
        picoContainer.addComponent(PositionManager.class);
        picoContainer.addComponent(BroadcastManager.class);
        picoContainer.addComponent(ClientServiceManager.class);
        picoContainer.addComponent(ConnectionMessageBus.class);
        picoContainer.addComponent(ThreadManager.class);
        picoContainer.addComponent(ConnectionManager.class);
        picoContainer.addComponent(ConnectionTransportManager.create());

        picoContainer.addComponent(new ImmutablePicoContainer(picoContainer));
        threadManager = picoContainer.getComponent(ThreadManager.class);

        String s = "version=0.1";
        if (configuration.properties().getName() != null) {
            s += ",name=" + configuration.properties().getName();
        }
        if (configuration.properties().getDescription() != null) {
            s += ",description=" + configuration.properties().getDescription();
        }
        if (configuration.properties().getOrganization() != null) {
            s += ",organization=" + configuration.properties().getOrganization();
        }
        clientConnectString = s;
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return terminated.await(timeout, unit);
    }

    public void close() {
        lock();
        try {
            if (state < S_SHUTDOWN) {
                state = S_SHUTDOWN;
                threadManager.startCloseThread(new Runnable() {
                    public void run() {
                        close0();
                    }
                });
            }
        } finally {
            unlock();
        }
    }

    void close0() {
        try {
            picoContainer.stop();
        } finally {
            lock();
            try {
                state = S_TERMINATED;
                terminated.countDown();
            } finally {
                unlock();
            }
        }
    }

    protected void finalize() {
        close();
    }

    /**
     * @return the clientConnectString
     */
    public String getClientConnectString() {
        return clientConnectString;
    }

    /**
     * Returns the maritime id of the client.
     * 
     * @return the maritime id of the client
     */
    public MaritimeId getLocalId() {
        return clientId;
    }

    /**
     * @return the picoContainer
     */
    public PicoContainer getPicoContainer() {
        return picoContainer;
    }

    public boolean isClosed() {
        return state >= S_SHUTDOWN;
    }

    public boolean isTerminated() {
        return state == S_TERMINATED;
    }

    /**
     * Reads and returns the current position.
     * 
     * @return the current position
     */
    public PositionTime readCurrentPosition() {
        return positionSupplier.getCurrentPosition();
    }

    static PicoContainer create(MaritimeCloudClientConfiguration builder) {
        ClientContainer client = new ClientContainer(builder);
        client.picoContainer.start();
        return client.picoContainer;
    }
}
