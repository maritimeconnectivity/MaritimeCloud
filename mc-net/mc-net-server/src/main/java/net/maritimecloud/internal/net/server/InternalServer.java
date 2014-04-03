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
package net.maritimecloud.internal.net.server;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.core.id.ServerId;
import net.maritimecloud.internal.net.server.broadcast.BroadcastManager;
import net.maritimecloud.internal.net.server.connection.ConnectionManager;
import net.maritimecloud.internal.net.server.connection.WebSocketServer;
import net.maritimecloud.internal.net.server.requests.ServerMessageBus;
import net.maritimecloud.internal.net.server.rest.WebServer;
import net.maritimecloud.internal.net.server.services.ServiceManager;
import net.maritimecloud.internal.net.server.targets.TargetManager;
import net.maritimecloud.internal.net.server.util.ThreadManager;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.behaviors.Caching;
import org.picocontainer.containers.ImmutablePicoContainer;

/**
 *
 * @author Kasper Nielsen
 */
public class InternalServer {

    /** The container is is normal running mode. (certain pre-start hooks may still be running. */
    static final int S_INITIALIZED = 0;

    /** The container has been started either by a preStart() or by invoking a lazy-starting method. */
    static final int S_RUNNING = 1;

    /** The container has been shutdown, for example, by calling shutdown(). */
    static final int S_SHUTDOWN = 2;

    /** The container has been fully terminated. */
    static final int S_TERMINATED = 3;

    private final ReentrantLock lock = new ReentrantLock();

    /** PicoContainer instance. Got really tired of Guice, so replaced it with PicoContainer. */
    private final DefaultPicoContainer picoContainer = new DefaultPicoContainer(new Caching());

    private final ServerId serverId;

    /** The current state of the client. Only set while holding lock, can be read at any time. */
    private volatile int state /* = 0 */;


    private final ServerInfo info;

    /** A latch that is released when the client has been terminated. */
    private final CountDownLatch terminated = new CountDownLatch(1);

    public InternalServer(int port) {
        this(new ServerConfiguration().setServerPort(port));
    }

    public InternalServer(ServerConfiguration configuration) {
        serverId = requireNonNull(configuration.getId());

        picoContainer.addComponent(configuration);
        picoContainer.addComponent(this);

        picoContainer.addComponent(ServerInfo.class);
        picoContainer.addComponent(ThreadManager.class);
        picoContainer.addComponent(TargetManager.class);
        picoContainer.addComponent(ConnectionManager.class);
        picoContainer.addComponent(WebSocketServer.class);
        picoContainer.addComponent(ServerMessageBus.class);
        picoContainer.addComponent(BroadcastManager.class);
        picoContainer.addComponent(ServiceManager.class);
        if (configuration.getWebserverPort() > 0) {
            picoContainer.addComponent(WebServer.class);
        }
        picoContainer.addComponent(new ImmutablePicoContainer(picoContainer));
        info = picoContainer.getComponent(ServerInfo.class);
    }

    public ServerInfo info() {
        return info;
    }

    public <T> T getService(Class<T> service) {
        return picoContainer.getComponent(service);
    }

    public boolean awaitTerminated(long timeout, TimeUnit unit) throws InterruptedException {
        return terminated.await(timeout, unit);
    }

    /**
     * @return the serverId
     */
    public ServerId getServerId() {
        return serverId;
    }

    public void shutdown() {
        lock.lock();
        try {
            if (state == S_RUNNING) {
                state = S_SHUTDOWN;
                picoContainer.stop();
            }
            state = S_TERMINATED;
            terminated.countDown();
        } finally {
            lock.unlock();
        }
    }

    public void start() {
        lock.lock();
        try {
            if (state == S_INITIALIZED) {
                picoContainer.start();
                picoContainer.getComponent(ConnectionManager.class);
                state = S_RUNNING;
            }
        } finally {
            lock.unlock();
        }
    }
}
