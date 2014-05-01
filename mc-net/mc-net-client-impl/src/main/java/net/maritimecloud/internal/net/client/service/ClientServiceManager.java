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
package net.maritimecloud.internal.net.client.service;

import static java.util.Objects.requireNonNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.ClientContainer;
import net.maritimecloud.internal.net.client.connection.ConnectionMessageBus;
import net.maritimecloud.internal.net.client.connection.OnMessage;
import net.maritimecloud.internal.net.client.connection.OutstandingMessage;
import net.maritimecloud.internal.net.client.util.DefaultConnectionFuture;
import net.maritimecloud.internal.net.client.util.ThreadManager;
import net.maritimecloud.internal.net.messages.FindService;
import net.maritimecloud.internal.net.messages.FindServiceAck;
import net.maritimecloud.internal.net.messages.RegisterService;
import net.maritimecloud.internal.net.messages.RegisterServiceAck;
import net.maritimecloud.internal.net.messages.ServiceInvoke;
import net.maritimecloud.internal.net.messages.ServiceInvokeAck;
import net.maritimecloud.internal.net.messages.spi.TMHelpers;
import net.maritimecloud.net.service.ServiceLocator;
import net.maritimecloud.net.service.invocation.InvocationCallback;
import net.maritimecloud.net.service.registration.ServiceRegistration;
import net.maritimecloud.net.service.spi.ServiceInitiationPoint;
import net.maritimecloud.net.service.spi.ServiceMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Manages local and remote services.
 *
 * @author Kasper Nielsen
 */
public class ClientServiceManager {

    final ConnectionMessageBus connection;

    /** The client container. */
    private final ClientContainer container;

    private final ConcurrentHashMap<String, DefaultConnectionFuture<?>> invokers = new ConcurrentHashMap<>();

    /** A map of subscribers. ChannelName -> List of listeners. */
    final ConcurrentHashMap<String, DefaultLocalServiceRegistration> localServices = new ConcurrentHashMap<>();

    private final ThreadManager threadManager;

    /**
     * Creates a new instance of this class.
     *
     * @param network
     *            the network
     */
    public ClientServiceManager(ClientContainer container, ConnectionMessageBus connection, ThreadManager threadManager) {
        this.container = requireNonNull(container);
        this.connection = requireNonNull(connection);
        this.threadManager = requireNonNull(threadManager);
    }

    /** {@inheritDoc} */
    public <T, S extends ServiceMessage<T>> DefaultServiceInvocationFuture<T> invokeService(MaritimeId id, S msg) {

        ServiceInvoke is = new ServiceInvoke();
        is.setStatus(1);
        is.setConversationId(UUID.randomUUID().toString());
        is.setServiceType(msg.getClass().getName());
        is.setMessageType(msg.messageName());
        is.setMsg(TMHelpers.persist(msg));
        // public InvokeService(int status, String conversationId, String serviceType, String messageType, Object o) {
        // this(status, conversationId, serviceType, messageType, TMHelpers.persistAndEscape(o));
        // }
        // InvokeService is = new InvokeService(1, UUID.randomUUID().toString(), msg.getClass().getName(),
        // msg.messageName(), msg);


        is.setDestination(id.toString());
        is.setSource(container.getLocalId().toString());
        final DefaultConnectionFuture<T> f = threadManager.create();
        DefaultConnectionFuture<ServiceInvokeAck> fr = threadManager.create();
        invokers.put(is.getConversationId(), fr);
        fr.thenAcceptAsync(new DefaultConnectionFuture.Action<Object>() {
            @SuppressWarnings("unchecked")
            public void accept(Object ack) {
                f.complete((T) ack);
            }
        });
        OutstandingMessage om = connection.sendConnectionMessage(is);
        DefaultConnectionFuture<Object> fffClient = new DefaultConnectionFuture<>(threadManager, om.acked());
        DefaultServiceInvocationFuture<T> sif = new DefaultServiceInvocationFuture<>(f, fffClient, fffClient);
        return sif;
    }

    @OnMessage
    public void onInvokeService(ServiceInvoke message) {
        String type = message.getServiceType();
        DefaultLocalServiceRegistration s = localServices.get(type);
        if (s != null) {
            s.invoke(message);
        } else {
            System.err.println("Could not find service " + type + " from " + localServices.keySet());
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @OnMessage
    public void receiveInvokeServiceAck(ServiceInvokeAck m) {
        DefaultConnectionFuture f = invokers.get(m.getUuid());
        if (f != null) {
            Object o = null;
            try {
                Class<?> mt = Class.forName(m.getReplyType());
                ObjectMapper om = new ObjectMapper();
                o = om.readValue(m.getMsg(), mt);
                f.complete(o);
            } catch (Exception e) {
                e.printStackTrace();
                f.completeExceptionally(e);
            }
        } else {
            System.err.println("Could not find invoked service " + m.getUuid() + " from " + invokers.keySet());
        }
    }

    public <T, E extends ServiceMessage<T>> ServiceLocator<T, E> serviceFind(ServiceInitiationPoint<E> sip) {
        return new DefaultServiceLocator<>(threadManager, sip, this, 0);
    }

    <T, E extends ServiceMessage<T>> DefaultConnectionFuture<FindServiceAck> serviceFindOne(FindService fs) {
        return connection.sendMessage(FindServiceAck.class, fs);
    }

    /** {@inheritDoc} */
    public <T, E extends ServiceMessage<T>> ServiceRegistration serviceRegister(ServiceInitiationPoint<E> sip,
            InvocationCallback<E, T> callback) {
        final DefaultLocalServiceRegistration reg = new DefaultLocalServiceRegistration(connection, sip, callback);
        if (localServices.putIfAbsent(sip.getName(), reg) != null) {
            throw new IllegalArgumentException(
                    "A service of the specified type has already been registered. Can only register one at a time");
        }
        final DefaultConnectionFuture<RegisterServiceAck> f = connection.sendMessage(RegisterServiceAck.class,
                new RegisterService().setServiceName(sip.getName()));
        f.thenAcceptAsync(new DefaultConnectionFuture.Action<RegisterServiceAck>() {
            public void accept(RegisterServiceAck ack) {
                reg.replied.countDown();
            }
        });
        return reg;
    }
}
