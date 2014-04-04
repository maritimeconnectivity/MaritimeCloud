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

import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.broadcast.BroadcastManager;
import net.maritimecloud.internal.net.client.service.ClientServiceManager;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.MaritimeCloudClientConfiguration;
import net.maritimecloud.net.MaritimeCloudConnection;
import net.maritimecloud.net.broadcast.BroadcastFuture;
import net.maritimecloud.net.broadcast.BroadcastListener;
import net.maritimecloud.net.broadcast.BroadcastMessage;
import net.maritimecloud.net.broadcast.BroadcastSendOptions;
import net.maritimecloud.net.broadcast.BroadcastSubscription;
import net.maritimecloud.net.service.ServiceInvocationFuture;
import net.maritimecloud.net.service.ServiceLocator;
import net.maritimecloud.net.service.invocation.InvocationCallback;
import net.maritimecloud.net.service.registration.ServiceRegistration;
import net.maritimecloud.net.service.spi.ServiceInitiationPoint;
import net.maritimecloud.net.service.spi.ServiceMessage;
import net.maritimecloud.util.geometry.Area;

import org.picocontainer.PicoContainer;

/**
 * An implementation of {@link MaritimeCloudClient} using WebSockets and JSON. This class delegates all work to other
 * services.
 *
 * @author Kasper Nielsen
 */
public class DefaultMaritimeCloudClient implements MaritimeCloudClient {

    /** Responsible for listening and sending broadcasts. */
    private final BroadcastManager broadcaster;

    /** Manages registration of services. */
    private final MaritimeCloudConnection connection;

    /** The internal client. */
    private final ClientContainer internalClient;

    /** Manages registration of services. */
    private final ClientServiceManager services;

    private final BroadcastSendOptions broadcastDefaultOptions;

    /**
     * Creates a new instance of this class.
     *
     * @param builder
     *            the configuration of the connection
     */
    public DefaultMaritimeCloudClient(MaritimeCloudClientConfiguration configuration) {
        PicoContainer pc = ClientContainer.create(configuration);
        broadcaster = pc.getComponent(BroadcastManager.class);
        connection = pc.getComponent(MaritimeCloudConnection.class);
        internalClient = pc.getComponent(ClientContainer.class);
        services = pc.getComponent(ClientServiceManager.class);
        broadcastDefaultOptions = configuration.getDefaultBroadcastOptions().immutable();
    }

    /** {@inheritDoc} */
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return internalClient.awaitTermination(timeout, unit);
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastFuture broadcast(BroadcastMessage message) {
        return broadcast(message, broadcastDefaultOptions);
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastFuture broadcast(BroadcastMessage message, BroadcastSendOptions options) {
        return broadcaster.sendBroadcastMessage(message, options);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends BroadcastMessage> BroadcastSubscription broadcastListen(Class<T> messageType,
            BroadcastListener<T> consumer) {
        return broadcaster.listenFor(messageType, consumer);
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        internalClient.close();
    }

    /** {@inheritDoc} */
    @Override
    public MaritimeCloudConnection connection() {
        return connection;
    }

    /** {@inheritDoc} */
    @Override
    public final MaritimeId getClientId() {
        return internalClient.getLocalId();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isClosed() {
        return internalClient.isClosed();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTerminated() {
        return internalClient.isTerminated();
    }

    /** {@inheritDoc} */
    @Override
    public <T, E extends ServiceMessage<T>> ServiceLocator<T, E> serviceLocate(ServiceInitiationPoint<E> sip) {
        return services.serviceFind(sip);
    }

    /** {@inheritDoc} */
    @Override
    public <T, S extends ServiceMessage<T>> ServiceInvocationFuture<T> serviceInvoke(MaritimeId id,
            S initiatingServiceMessage) {
        return services.invokeService(id, initiatingServiceMessage);
    }

    /** {@inheritDoc} */
    @Override
    public <T, E extends ServiceMessage<T>> ServiceRegistration serviceRegister(ServiceInitiationPoint<E> sip,
            InvocationCallback<E, T> callback) {
        return services.serviceRegister(sip, callback);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends BroadcastMessage> BroadcastSubscription broadcastListen(Class<T> messageType,
            BroadcastListener<T> consumer, Area area) {
        return null;
    }
}
