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

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.maritimecloud.mms.server.connection.client.Client.State;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import org.cakeframework.container.concurrent.ScheduleAtFixedRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A client reaper takes care of removing stale clients
 *
 * @author Kasper Nielsen
 */
public class ClientReaper {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTransport.class);

    /** The number of nanoseconds before a stale connection is detected. */
    private final long timeoutNanos = TimeUnit.MINUTES.toNanos(5);

    /** The client manager that maintains a list of connected clients. */
    private final ClientManager clientManager;

    /** The transport listener, used for reaping clients that do not send hello messages. */
    private final DefaultTransportListener transportListener;

    /**
     * @param clientManager
     */
    public ClientReaper(ClientManager clientManager, DefaultTransportListener transportListener) {
        this.clientManager = requireNonNull(clientManager);
        this.transportListener = requireNonNull(transportListener);
    }

    /** Cleans up and remove stale clients. */
    @ScheduleAtFixedRate(value = 30, unit = TimeUnit.SECONDS)
    public void cleanudp() {
        try {
            cleanup();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    public void cleanup() {
        long now = System.nanoTime();

        // We start by reaping missing Helloes from clients. That is clients that have received the welcome message.
        // But who for some reason do not ever reply to it but keeps the connection open.
        Iterator<ServerTransport> transports = transportListener.missingHellos.iterator();
        while (transports.hasNext()) {
            ServerTransport t = transports.next();
            if (t.getTimeOfCreation()+ timeoutNanos < now) {
                t.close(MmsConnectionClosingCode.CLIENT_TIMEOUT);
                transports.remove();
            }
        }

        Iterator<Client> clients = clientManager.clients.values().iterator(); // clients.iterator() is immutable
        while (clients.hasNext()) {
            Client ic = clients.next();
            ReentrantReadWriteLock.WriteLock lock = ic.lock.writeLock();

            // We use tryLock() in the following instead of lock() because we do not perform any critical operations.
            // And if we do not succeed in acquiring the lock this time. We most likely will the next time this method
            // is run.

            // TODO maybe check for no messages in 2 minutes, send pong or something like it
            ClientInternalState state = ic.state;

            // state.session != null -> connected or disconnected

            if (state.session != null && ic.getTimeOfLatestReceivedMessage() + timeoutNanos < now && lock.tryLock()) {
                try {
                    state = ic.state; // refresh after we have locked
                    if (state.session != null && ic.getTimeOfLatestReceivedMessage() + timeoutNanos < now) {
                        LOGGER.info("Killing client " + ic.getId());
                        ic.close(MmsConnectionClosingCode.CLIENT_TIMEOUT);
                        ic.state = ClientInternalState.TERMINATED;
                        state = ic.state;// refresh state
                    }
                } finally {
                    lock.unlock();
                }
            }

            if (state.state == State.TERMINATED && lock.tryLock()) {
                try {
                    clients.remove(); // A client will never transition from the terminated state
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
