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

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import org.cakeframework.container.concurrent.ScheduleAtFixedRate;

/**
 * A client reaper takes care of removing stale clients
 *
 * @author Kasper Nielsen
 */
public class ClientReaper {

    final long timeoutNanos = TimeUnit.MINUTES.toNanos(5);

    /** The client manager that maintains a list of connected clients. */
    private final ClientManager clientManager;

    /**
     * @param clientManager
     */
    public ClientReaper(ClientManager clientManager) {
        this.clientManager = requireNonNull(clientManager);
    }

    /** Cleans up and remove stale clients. */
    @ScheduleAtFixedRate(value = 30, unit = TimeUnit.SECONDS)
    public void cleanup() {
        long now = System.nanoTime();

        // Reap missing Helloes in DefaultTransport

        Iterator<InternalClient> iter = clientManager.clients.values().iterator();
        for (InternalClient ic = iter.next(); iter.hasNext(); ic = iter.next()) {

            // check for no messages in 2 minutes, send pong or something like

            if (ic.state instanceof InternalStateConnected) {
                InternalStateConnected isc = (InternalStateConnected) ic.state;
                if (isc.transport.getLatestReceivedMessage() + timeoutNanos < now) {
                    ReentrantReadWriteLock.WriteLock lock = ic.lock.writeLock();
                    // We use tryLock instead of lock because we do not want to block. And if we cannot acquire the
                    // lock.
                    // We will most likely get it the next time this method iterates through the clients.
                    if (lock.tryLock()) {
                        try {
                            if (isc.transport.getLatestReceivedMessage() + timeoutNanos < now) {
                                ic.close(MmsConnectionClosingCode.CLIENT_TIMEOUT);
                            }
                        } finally {
                            lock.unlock();
                        }
                    }
                }
            }

            if (ic.state instanceof InternalStateTerminated) {
                ReentrantReadWriteLock.WriteLock lock = ic.lock.writeLock();

                // We use tryLock instead of lock because we do not want to block. And if we cannot acquire the lock.
                // We will most likely get it the next time this method iterates through the clients.
                if (lock.tryLock()) {
                    try {
                        // make sure we are still terminated
                        if (ic.state instanceof InternalStateTerminated) {
                            iter.remove();
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }

        }
    }

    // private void markClientAsStale(InternalClient ic) {
    // ReentrantReadWriteLock.WriteLock lock = ic.lock.writeLock();
    //
    // // We use tryLock instead of lock because we do not want to block. And if we cannot acquire the lock.
    // // We will most likely get it the next time this method iterates through the clients.
    // if (lock.tryLock()) {
    // try {
    // // make sure we are still connected
    // if (ic.latestReceivedMessage + timeoutNanos < now) {
    // if (ic.state instanceof InternalStateConnected) {
    // ic.close(MmsConnectionClosingCode.CLIENT_TIMEOUT);
    // }}
    // } finally {
    // lock.unlock();
    // }
    //
    // }
    // // @ScheduleAtFixedRate(value = 1, unit = TimeUnit.MINUTES)
    // // public void checkForTimeout() {
    // //
    // // }
}
