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
package net.maritimecloud.internal.net.client.endpoint;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ConcurrentHashMap;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.ClientContainer;
import net.maritimecloud.internal.net.client.connection.ConnectionMessageBus;
import net.maritimecloud.internal.net.client.util.DefaultConnectionFuture;
import net.maritimecloud.internal.net.client.util.ThreadManager;
import net.maritimecloud.internal.net.messages.RegisterEndpoint;
import net.maritimecloud.internal.net.messages.RegisterEndpointAck;
import net.maritimecloud.net.ConnectionClosedException;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.endpoint.EndpointImplementation;
import net.maritimecloud.net.endpoint.EndpointLocal;
import net.maritimecloud.net.endpoint.EndpointLocator;
import net.maritimecloud.net.endpoint.EndpointRegistration;

/**
 *
 * @author Kasper Nielsen
 */
public class ClientEndpointManager {

    final ConnectionMessageBus connection;

    /** The client container. */
    private final ClientContainer container;

    private final ConcurrentHashMap<String, DefaultConnectionFuture<?>> invokers = new ConcurrentHashMap<>();

    /** A map of subscribers. ChannelName -> List of listeners. */
    final ConcurrentHashMap<String, DefaultLocalEndpointRegistration> localServices = new ConcurrentHashMap<>();

    private final ThreadManager threadManager;

    /**
     * Creates a new instance of this class.
     *
     * @param network
     *            the network
     */
    public ClientEndpointManager(ClientContainer container, ConnectionMessageBus connection, ThreadManager threadManager) {
        this.container = requireNonNull(container);
        this.connection = requireNonNull(connection);
        this.threadManager = requireNonNull(threadManager);
    }


    /**
     * Creates a ServiceLocator for a service of the specified type.
     *
     * @param sip
     *            the service initiation point
     * @return a service locator object
     *
     * @throws ConnectionClosedException
     *             if the connection has been permanently closed
     */
    public <T extends EndpointLocal> T endpointFind(MaritimeId id, Class<? extends T> endpointType) {
        String name = Util.getName(endpointType);

        throw new UnsupportedOperationException();
    }

    public <T extends EndpointLocal> EndpointLocator<T> endpointFind(Class<? extends T> endpointType) {
        throw new UnsupportedOperationException();
    }

    /**
     * Registers the specified endpoint with the maritime cloud. If a client is closed via
     * {@link MaritimeCloudClient#close()} the server will automatically deregister all endpoints.
     *
     * @param implementation
     *            the endpoint implementation
     * @return an endpoint registration object
     *
     * @throws ConnectionClosedException
     *             if the connection has been permanently closed
     */
    public EndpointRegistration endpointRegister(EndpointImplementation implementation) {
        final DefaultLocalEndpointRegistration reg = new DefaultLocalEndpointRegistration(connection, implementation);
        if (localServices.putIfAbsent(reg.getName(), reg) != null) {
            throw new IllegalArgumentException(
                    "A service of the specified type has already been registered. Can only register one at a time");
        }
        final DefaultConnectionFuture<RegisterEndpointAck> f = connection.sendMessage(RegisterEndpointAck.class,
                new RegisterEndpoint().setEndpointName(reg.getName()));
        f.thenAcceptAsync(new DefaultConnectionFuture.Action<RegisterEndpointAck>() {
            public void accept(RegisterEndpointAck ack) {
                reg.replied.countDown();
            }
        });
        return reg;
    }
}
