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
package net.maritimecloud.internal.mms.client.endpoint;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.client.ClientInfo;
import net.maritimecloud.internal.mms.client.MmsThreadManager;
import net.maritimecloud.internal.mms.client.connection.ClientConnection;
import net.maritimecloud.internal.mms.messages.services.Services;
import net.maritimecloud.internal.net.endpoint.EndpointManager;
import net.maritimecloud.internal.net.endpoint.EndpointMirror;
import net.maritimecloud.internal.net.messages.MethodInvoke;
import net.maritimecloud.internal.net.messages.MethodInvokeResult;
import net.maritimecloud.internal.net.util.DefaultEndpointInvocationFuture;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.EndpointRegistration;
import net.maritimecloud.net.LocalEndpoint;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientClosedException;
import net.maritimecloud.net.mms.MmsEndpointLocator;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public class ClientEndpointManager {

    final ClientInfo clientInfo;

    final ClientConnection connection;

    /** A map of subscribers. ChannelName -> List of listeners. */
    final EndpointManager em = new EndpointManager();

    /** The client container. */
    final ConcurrentHashMap<Binary, RemoteInvocation> invokers = new ConcurrentHashMap<>();

    final MmsThreadManager threadManager;

    /**
     * Creates a new instance of this class.
     *
     * @param connection
     *            the client connection
     * @param threadManager
     *            the thread manager
     * @param clientInfo
     *            clientInfo
     */
    public ClientEndpointManager(ClientConnection connection, MmsThreadManager threadManager, ClientInfo clientInfo) {
        this.connection = requireNonNull(connection);
        this.threadManager = requireNonNull(threadManager);
        this.clientInfo = requireNonNull(clientInfo);

        connection.subscribe(MethodInvoke.class, (a, e) -> onMethodInvoke(e));
        connection.subscribe(MethodInvokeResult.class, (a, e) -> onMethodInvokeResult(e));
    }

    public <T extends LocalEndpoint> MmsEndpointLocator<T> endpointFind(Class<T> endpointType) {
        return new DefaultEndpointLocator<>(this, EndpointMirror.from(endpointType), Integer.MAX_VALUE);
    }

    /**
     * Creates a ServiceLocator for a service of the specified type.
     *
     * @param id
     *            the id to create an endpoint from
     * @param endpointType
     *            the type of endpoint to create
     * @param <T>
     *            the type of endpoint
     * @return a service locator object
     *
     * @throws MmsClientClosedException
     *             if the connection has been permanently closed
     */
    @SuppressWarnings("unchecked")
    public <T extends LocalEndpoint> T endpointFrom(MaritimeId id, Class<? extends T> endpointType) {
        EndpointMirror m = EndpointMirror.from(endpointType);
        return (T) m.instantiate(new DefaultEndpointInvocator(this, id));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> EndpointInvocationFuture<T> invokeRemote(MaritimeId receiver, String endpoint, Message parameters,
            MessageSerializer<? extends Message> serializer, ValueSerializer<T> resultParser) {
        requireNonNull(endpoint, "endpoint is null");
        requireNonNull(parameters, "parameters is null");

        MethodInvoke ei = new MethodInvoke();
        ei.setMessageId(Binary.random(32));
        ei.setEndpointMethod(endpoint);
        // Vi skal have en serializer med i metoden
        ei.setParameters(MessageSerializer.writeToJSON(parameters, (MessageSerializer) serializer));
        if (receiver != null) {
            ei.setReceiverId(receiver.toString());
        }
        ei.setSenderTimestamp(Timestamp.now());
        Optional<PositionTime> r = clientInfo.getCurrentPosition();
        if (r.isPresent()) {
            ei.setSenderPosition(r.get());
        }
        ei.setSenderId(clientInfo.getClientId().toString());

        DefaultEndpointInvocationFuture<T> result = threadManager.create(ei.getMessageId());

        result.recivedByCloud = connection.sendMessage(ei);
        invokers.put(ei.getMessageId(), new RemoteInvocation(result, resultParser));

        return result;
    }

    /**
     * Registers the specified endpoint with the maritime cloud. If a client is closed via {@link MmsClient#shutdown()} the
     * server will automatically deregister all endpoints.
     *
     * @param implementation
     *            the endpoint implementation
     * @return an endpoint registration object
     *
     * @throws MmsClientClosedException
     *             if the connection has been permanently closed
     */
    public EndpointRegistration endpointRegister(EndpointImplementation implementation) {
        em.endpointRegister(implementation);

        Services s = endpointFrom(null, Services.class);
        DefaultEndpointRegistration reg = new DefaultEndpointRegistration();
        s.registerEndpoint(implementation.getEndpointName()).thenRun(() -> reg.replied.countDown());
        return reg;
    }

    void onMethodInvoke(MethodInvoke message) {
        em.execute(message, e -> connection.sendMessage(e));
    }

    void onMethodInvokeResult(MethodInvokeResult m) {
        RemoteInvocation f = invokers.get(m.getResultForMessageId());
        if (f != null) {
            f.complete(m);
        }
    }
}
