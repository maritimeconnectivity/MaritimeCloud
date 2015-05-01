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

import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public class InternalClient {

    final String id;

    volatile PositionTime latest;

    /** A read write lock for the client. */
    final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /** The client manager. */
    final ClientManager manager;


    /** The current state of this client. */
    volatile InternalState state;

    /**
     * @param appLayerManager
     * @param id
     */
    public InternalClient(ClientManager manager, ServerTransport initialTransport, String id) {
        this.manager = manager;
        this.id = id;
        this.state = new InternalStateConnecting(initialTransport);
    }

    /**
     * @return the latest
     */
    public PositionTime getLatestPosition() {
        return latest;
    }

    public void close(MmsConnectionClosingCode closingCode) {
        lock.writeLock().lock();
        try {
            InternalState state = this.state;
            if (!(state instanceof InternalStateTerminated)) {
                ServerTransport t = state.transport;
                t.close(closingCode);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Invoked when socket closes.
    // This can happen for a number of reasons.
    // The client closed it normally
    // The closed the server exception or normally
    // No matter what this method is always invoked.
    void onClose(ServerTransport t, MmsConnectionClosingCode closingCode) {
        lock.writeLock().lock();
        try {
            InternalState state = this.state;
            if (state instanceof InternalStateConnected) {
                InternalStateConnected isc = (InternalStateConnected) state;
                if (closingCode.getId() == 1000) {
                    state = new InternalStateTerminated();
                    isc.session.disconnectedWithWriteLock(true);
                } else {
                    state = new InternalStateDisconnected(t, isc.session);
                    isc.session.disconnectedWithWriteLock(false);
                }
            } else if (state instanceof InternalStateConnecting) {
                state = new InternalStateTerminated();
            } else {
                throw new IllegalStateException();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    void onMessage(ServerTransport t, MmsMessage message) {
        lock.readLock().lock();
        try {
            InternalState state = this.state;
            if (state.transport == t && state instanceof InternalStateConnected) {
                InternalStateConnected isc = (InternalStateConnected) state;
                PositionTime pt = message.getPositionTime();
                // Should we close the client if going back in time??? Think it can happen
                // not for a single session, but inbetween sessions.
                if (pt.getTime() > latest.getTime()) {
                    latest = pt;
                }
                isc.session.onMessageWithReadLock(message);
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}
