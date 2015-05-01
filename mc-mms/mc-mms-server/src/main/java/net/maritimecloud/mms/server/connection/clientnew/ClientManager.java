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
package net.maritimecloud.mms.server.connection.clientnew;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;

/**
 *
 * @author Kasper Nielsen
 */
public class ClientManager implements Iterable<InternalClient> {

    /** A list of all currently connected clients. Clients will be removed after xx time */
    final ConcurrentHashMap<String, InternalClient> clients = new ConcurrentHashMap<>();

    final SessionListener sessionLister = null;

    public void forEachTarget(Consumer<InternalClient> consumer) {
        clients.forEachValue(10, requireNonNull(consumer));
    }

    public SessionMessageFuture sendMessage(MmsMessage m, String destinationId) {
        InternalClient ic = clients.get(destinationId);
        if (ic == null) {
            return SessionMessageFuture.notConnected(m);
        }
        return sendMessage(ic, null, m);
    }

    SessionMessageFuture sendMessage(InternalClient client, Session requireSession, MmsMessage m) {
        client.lock.readLock().lock();
        try {
            InternalState state = client.state;
            if (state instanceof InternalStateConnecting || state instanceof InternalStateTerminated) {
                return SessionMessageFuture.notConnected(m);
            }
            final Session session;
            if (state instanceof InternalStateConnected) {
                session = ((InternalStateConnected) state).session;
            } else {
                session = ((InternalStateDisconnected) state).session;
            }
            if (requireSession != null && requireSession != session) {
                return SessionMessageFuture.wrongSession(m);
            }
            // lets send the mother fucker
            return session.enqueueMessageWithReadLock(m);
        } finally {
            client.lock.readLock().unlock();
        }
    }

    public InternalClient get(MaritimeId id) {
        return clients.get(id.toString());
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<InternalClient> iterator() {
        return Collections.unmodifiableCollection(clients.values()).iterator();
    }

    public InternalClient onHello(String id, Hello hello, ServerTransport transport) {
        // this method is written is typical non-blocking style, with an infinite loop.
        // Where each invocation is live free lock bla bla.
        for (;;) {
            InternalClient c = clients.get(id);
            if (c == null) { // no existing client
                c = new InternalClient(this, transport, id);
                c.latest = hello.getPositionTime();
                // we need to lock it before we insert it into the hash map so other threads won't attempt to send any
                // messages before we have sent a Connected message
                // We can probably remove it at some point, just need to figure out how
                c.lock.writeLock().lock();
                try {
                    // Try and see if we can insert as current client. Otherwise let for(;;) loop retry
                    if (clients.putIfAbsent(id, c) == null) {
                        Session session = new Session(c);
                        MmsMessage mm = new MmsMessage(new Connected().setSessionId(session.getSessionId())
                                .setLastReceivedMessageId(0L));
                        transport.sendMessage(mm); // Send connected message

                        c.state = new InternalStateConnected(transport, session);
                        session.onConnectWithWriteLock(transport);
                        return c;
                    }
                } finally {
                    c.lock.writeLock().unlock();
                }
            } else {
                // A client is already connecting, connected or is stale. Since we will most likely change state.
                // we start by locking the client, preparing an update.
                c.lock.writeLock().lock();
                try {
                    c.latest = hello.getPositionTime();
                    InternalState state = c.state;
                    if (state instanceof InternalStateConnecting) {
                        // The current implementation does not allow this state.
                        // Because right now the above code will always hold the write lock
                        // while the state is InternalStateConnecting.
                        // This might change with a distributed implementation.
                        throw new IllegalStateException();

                    } else if (state instanceof InternalStateConnected) {
                        InternalStateConnected isc = (InternalStateConnected) state;
                        Session session = isc.session;
                        session.onConnectWithWriteLock(transport);
                        //
                    } else if (state instanceof InternalStateDisconnected) {
                        InternalStateDisconnected failed = (InternalStateDisconnected) state;
                        Session session = failed.session;
                        session.onConnectWithWriteLock(transport);
                        // check session id
                        // Try reconnect

                    } else if (state instanceof InternalStateTerminated) {
                        clients.remove(id, c);// remove it, and let for(;;) handle the new connection
                    }
                } finally {
                    c.lock.writeLock().unlock();
                }
            }
        }
    }

    public Stream<InternalClient> parallelStream() {
        return clients.values().parallelStream();
    }

    public Stream<InternalClient> stream() {
        return clients.values().stream();
    }
}
