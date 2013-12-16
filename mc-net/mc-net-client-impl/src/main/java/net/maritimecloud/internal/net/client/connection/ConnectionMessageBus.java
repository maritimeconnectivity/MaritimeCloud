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

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import net.maritimecloud.internal.net.client.util.DefaultConnectionFuture;
import net.maritimecloud.internal.net.client.util.ThreadManager;
import net.maritimecloud.internal.net.messages.ConnectionMessage;
import net.maritimecloud.internal.net.messages.s2c.ServerRequestMessage;
import net.maritimecloud.internal.net.messages.s2c.ServerResponseMessage;
import net.maritimecloud.internal.net.messages.s2c.service.FindServiceResult;
import net.maritimecloud.internal.net.messages.s2c.service.RegisterServiceResult;

import org.picocontainer.PicoContainer;
import org.picocontainer.Startable;

/**
 * 
 * @author Kasper Nielsen
 */
public class ConnectionMessageBus implements Startable {

    final ConcurrentHashMap<Long, DefaultConnectionFuture<?>> acks = new ConcurrentHashMap<>();

    final AtomicInteger ai = new AtomicInteger();

    /** The connection manager. */
    final ConnectionManager cm;

    /** Consumers of messages. */
    final CopyOnWriteArraySet<MessageConsumer> consumers = new CopyOnWriteArraySet<>();

    final PicoContainer container;

    /** The thread manager. */
    final ThreadManager threadManager;

    public ConnectionMessageBus(PicoContainer container, ConnectionManager cm, ThreadManager threadManager) {
        this.cm = cm;
        this.container = container;

        cm.hub = this;
        this.threadManager = threadManager;
    }

    private ClientConnection connection() {
        ClientConnection connection = cm.connection;
        if (connection == null) {
            throw new IllegalStateException("Client has not been connected yet, or is running in disconnect mode");
        }
        return connection;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void handleMessageReply(ConnectionMessage m, DefaultConnectionFuture<?> f) {
        if (m instanceof RegisterServiceResult) {
            serviceRegisteredAck((RegisterServiceResult) m, (DefaultConnectionFuture<RegisterServiceResult>) f);
        } else if (m instanceof FindServiceResult) {
            serviceFindAck((FindServiceResult) m, (DefaultConnectionFuture<FindServiceResult>) f);
        } else {
            ((DefaultConnectionFuture) f).complete(m);
        }
    }

    public void onMsg(ConnectionMessage m) {
        if (m instanceof ServerResponseMessage) {
            ServerResponseMessage am = (ServerResponseMessage) m;
            DefaultConnectionFuture<?> f = acks.remove(am.getMessageAck());
            if (f == null) {
                System.err.println("Orphaned packet with id " + am.getMessageAck() + " registered " + acks.keySet()
                        + ", local " + "" + " p = ");
                // TODO close connection with error
            } else {
                try {
                    handleMessageReply(m, f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // System.out.println("RELEASING " + am.getMessageAck() + ", remaining " + acks.keySet());
        } else {
            try {
                for (MessageConsumer c : consumers) {
                    if (c.type.isAssignableFrom(m.getClass())) {
                        c.process(m);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public OutstandingMessage sendConnectionMessage(ConnectionMessage b) {
        return connection().messageSend(b);
    }

    public <T extends ServerResponseMessage> DefaultConnectionFuture<T> sendMessage(ServerRequestMessage<T> m) {
        // we need to send the messages in the same order as they are numbered for now
        synchronized (ai) {
            long id = ai.incrementAndGet();

            DefaultConnectionFuture<T> f = threadManager.create();
            acks.put(id, f);
            m.setReplyTo(id);
            sendConnectionMessage(m);
            return f;
        }
    }

    /** {@inheritDoc} */
    private void serviceFindAck(FindServiceResult a, DefaultConnectionFuture<FindServiceResult> f) {
        f.complete(a);
    }

    /** {@inheritDoc} */
    private void serviceRegisteredAck(RegisterServiceResult a, DefaultConnectionFuture<RegisterServiceResult> f) {
        f.complete(a);
    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        for (Object o : container.getComponents()) {
            for (Method m : o.getClass().getMethods()) {
                if (m.isAnnotationPresent(OnMessage.class)) {
                    @SuppressWarnings("rawtypes")
                    Class messageType = m.getParameterTypes()[0];
                    consumers.add(new MessageConsumer(messageType, o, m));
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {}


    static class MessageConsumer {

        final Method m;

        final Object o;

        final Class<?> type;

        MessageConsumer(Class<?> type, Object o, Method m) {
            this.type = requireNonNull(type);
            this.o = requireNonNull(o);
            this.m = m;
        }

        void process(Object message) {
            try {
                m.invoke(o, message);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
