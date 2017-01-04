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
package net.maritimecloud.mms.server.connection.client;

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
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.MmsServer;
import net.maritimecloud.mms.server.connection.client.Client.State;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

/**
 *
 * @author Kasper Nielsen
 */
public class ClientManager implements Iterable<Client> {

    /** A list of all currently connected clients. Clients will be removed after xx time */
    final ConcurrentHashMap<String, Client> clients = new ConcurrentHashMap<>();

    /** The MMS Server */
    final MmsServer mmsServer;

    /**
     * Creates a new ClientManager
     *
     * @param mmsServer
     *            the mms server
     */
    public ClientManager(MmsServer mmsServer) {
        this.mmsServer = requireNonNull(mmsServer);
    }

    public void forEachTarget(Consumer<Client> consumer) {
        clients.forEachValue(10, requireNonNull(consumer));
    }

    /**
     * Returns any client with the specified id
     *
     * @param id
     *            the id of the client
     * @return any client with the specified id
     */
    public Client get(MaritimeId id) {
        return clients.get(id.toString());
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Client> iterator() {
        return Collections.unmodifiableCollection(clients.values()).iterator();
    }

    /**
     * A new client if successfully connected.
     *
     * @param hello
     *            the clients hello message
     * @param transport
     *            the transport used for sending and receiving messages
     * @return a new client
     */
    Client onHello(Hello hello, ServerTransport transport) {
        MaritimeId mid = MaritimeId.create(hello.getClientId());
        String id = mid.toString();

        // this method is written is typical non-blocking style, with an infinite loop.
        // Where each invocation is live free lock bla bla.
        for (;;) {
            Client c = clients.get(id);
            if (c == null) { // no existing client
                c = new Client(this, transport, id);
                c.latestPositionAndTime = hello.getPositionTime();
                // we need to lock it before we insert it into the hash map so other threads won't attempt to send any
                // messages before we have sent a Connected message.
                // We can probably remove it at some point, just need to figure out how?
                c.lock.writeLock().lock();
                try {
                    // Try and see if we can insert as current client. Otherwise let for(;;) loop retry
                    if (clients.putIfAbsent(id, c) == null) {
                        return c.connectWithWriteLock(transport);
                    }
                } finally {
                    c.lock.writeLock().unlock();
                }
            } else {
                // A client is already connecting, connected or is stale. Since we will most likely change state.
                // we start by locking the client, preparing an update.
                c.lock.writeLock().lock();
                try {
                    c.latestPositionAndTime = hello.getPositionTime(); // lets start by updating the latest timestamp

                    ClientInternalState state = c.state;
                    if (state.state == State.CONNECTING) {
                        // The current implementation does not allow this state.
                        // Because right now the above code will always hold the write lock
                        // while the state is InternalStateConnecting.
                        // This might change with a distributed implementation.
                        throw new IllegalStateException();

                    } else if (state.state == State.TERMINATED) {
                        clients.remove(id, c);// remove it, and let for(;;) handle the new connection
                    } else {
                        Session existingSession = state.session;
                        if (state.state == State.CONNECTED) {
                            state.transport.close(MmsConnectionClosingCode.DUPLICATE_CONNECT);
                        }

                        if (!existingSession.getSessionId().equals(hello.getSessionId())) {
                            existingSession.disconnectedWithWriteLock(true);
                            // Create a new session
                            return c.connectWithWriteLock(transport);
                        } else {
                            c.state = new ClientInternalState(State.CONNECTED, transport, existingSession);
                            MmsMessage mm = new MmsMessage(new Connected().setSessionId(existingSession.getSessionId())
                                    .setLastReceivedMessageId(existingSession.latestMessageIdReceivedByRemote).setIsCleanConnect(false));
                            transport.sendMessage(mm); // Send connected message

                            existingSession.onConnectWithWriteLock(transport, hello.getLastReceivedMessageId());
                            return c;
                        }
                    }
                } finally {
                    c.lock.writeLock().unlock();
                }
            }
        }
    }

    /**
     * Returns a parallel stream of all connected clients.
     *
     * @return a parallel stream of all connected clients
     */
    public Stream<Client> parallelStream() {
        return clients.values().parallelStream();
    }

    SessionMessageFuture sendMessage(String destinationId, Message m) {
        Client ic = clients.get(destinationId);
        if (ic == null) {
            return SessionMessageFuture.notConnected(m);
        }
        return ic.sendMessage(null, m);
    }

    /**
     * Returns a stream of all connected clients.
     *
     * @return a stream of all connected clients
     */
    public Stream<Client> stream() {
        return clients.values().stream();
    }
}
