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
package net.maritimecloud.internal.mms.client;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.client.broadcast.BroadcastDeserializer;
import net.maritimecloud.internal.mms.client.broadcast.ClientBroadcastManager;
import net.maritimecloud.internal.mms.client.connection.ClientConnection;
import net.maritimecloud.internal.mms.client.connection.DefaultMmsConnection;
import net.maritimecloud.internal.mms.client.connection.transport.ClientTransportFactory;
import net.maritimecloud.internal.mms.client.endpoint.ClientEndpointManager;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.net.BroadcastConsumer;
import net.maritimecloud.net.BroadcastMessage;
import net.maritimecloud.net.BroadcastSubscription;
import net.maritimecloud.net.DispatchedMessage;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.EndpointRegistration;
import net.maritimecloud.net.LocalEndpoint;
import net.maritimecloud.net.mms.MmsBroadcastOptions;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsEndpointLocator;
import net.maritimecloud.util.geometry.Area;

import org.cakeframework.container.Container;
import org.cakeframework.container.Container.State;
import org.cakeframework.container.ContainerConfiguration;

/**
 * An implementation of {@link MmsClient} using WebSockets and JSON. This class delegates all work to other services.
 *
 * @author Kasper Nielsen
 */
public class DefaultMmsClient implements MmsClient {

    /** The logger. */
    private static final Logger LOGGER = Logger.get(DefaultMmsClient.class);

    /** Responsible for listening and sending broadcasts. */
    private final ClientBroadcastManager broadcaster;

    private final ClientInfo clientInfo;

    /** Manages registration of services. */
    private final MmsConnection connection;

    /** The internal client. */
    private final Container container;

    /** Manages registration of services. */
    private final ClientEndpointManager endpoints;

    /**
     * Creates a new instance of this class.
     *
     * @param configuration
     *            the configuration of the connection
     */
    public DefaultMmsClient(MmsClientConfiguration configuration) {
        container = createClient(configuration);
        broadcaster = container.getService(ClientBroadcastManager.class);
        connection = container.getService(MmsConnection.class);
        endpoints = container.getService(ClientEndpointManager.class);
        clientInfo = container.getService(ClientInfo.class);
    }

    /** {@inheritDoc} */
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return container.awaitState(State.TERMINATED, timeout, unit);
    }

    /** {@inheritDoc} */
    @Override
    public DispatchedMessage broadcast(BroadcastMessage message, MmsBroadcastOptions options) {
        return broadcaster.broadcast(message, options);
    }

    public <T extends BroadcastMessage> BroadcastSubscription broadcastSubscribe(BroadcastDeserializer bd, String type,
            BroadcastConsumer<T> listener, Area area) {
        return broadcaster.broadcastSubscribe(bd, type, listener, area);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends BroadcastMessage> BroadcastSubscription broadcastSubscribe(Class<T> messageType,
            BroadcastConsumer<T> consumer) {
        return broadcaster.broadcastSubscribe(messageType, consumer, null);
    }


    /** {@inheritDoc} */
    @Override
    public <T extends BroadcastMessage> BroadcastSubscription broadcastSubscribe(Class<T> messageType,
            BroadcastConsumer<T> consumer, Area area) {
        return broadcaster.broadcastSubscribe(messageType, consumer, requireNonNull(area, "area is null"));
    }

    /** {@inheritDoc} */
    @Override
    public void shutdown() {
        container.shutdown();
    }

    /** {@inheritDoc} */
    @Override
    public MmsConnection connection() {
        return connection;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends LocalEndpoint> T endpointCreate(MaritimeId id, Class<T> endpointType) {
        return endpoints.endpointFrom(id, endpointType);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends LocalEndpoint> MmsEndpointLocator<T> endpointLocate(Class<T> endpointType) {
        return endpoints.endpointFind(endpointType);
    }

    /** {@inheritDoc} */
    @Override
    public EndpointRegistration endpointRegister(EndpointImplementation implementation) {
        return endpoints.endpointRegister(implementation);
    }

    protected void finalize() {
        shutdown();
    }

    /** {@inheritDoc} */
    @Override
    public final MaritimeId getClientId() {
        return clientInfo.getClientId();
    }

    public <T> EndpointInvocationFuture<T> invokeRemote(MaritimeId receiver, String endpoint, Message parameters,
            MessageSerializer<? extends Message> serializer, ValueSerializer<T> resultParser) {
        return endpoints.invokeRemote(receiver, endpoint, parameters, serializer, resultParser);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isShutdown() {
        return container.getState().isShutdown();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTerminated() {
        return container.getState() == State.TERMINATED;
    }

    public static Container createClient(MmsClientConfiguration configuration) {
        MaritimeId clientId = requireNonNull(configuration.getId());

        ContainerConfiguration cc = new ContainerConfiguration();
        cc.addService(configuration.getPositionReader());
        cc.addService(clientId);
        cc.addService(configuration);
        cc.addService(ConnectionKeepAlive.class);
        cc.addService(ClientInfo.class);
        cc.addService(ClientBroadcastManager.class);
        cc.addService(ClientEndpointManager.class);
        cc.addService(DefaultMmsConnection.class);
        cc.addService(MmsThreadManager.class);
        cc.addService(ClientConnection.class);
        cc.addService(ClientTransportFactory.create());
        LOGGER.debug("Creating client for " + clientId);
        return cc.create();
    }
}
