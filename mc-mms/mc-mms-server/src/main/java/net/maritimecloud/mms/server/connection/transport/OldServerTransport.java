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
package net.maritimecloud.mms.server.connection.transport;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.core.id.ServerId;
import net.maritimecloud.internal.mms.client.connection.transport.ClientTransportListener;
import net.maritimecloud.internal.mms.messages.Welcome;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.mms.server.MmsServer;
import net.maritimecloud.mms.server.connectionold.ConnectionManager;
import net.maritimecloud.mms.server.connectionold.ServerConnectFuture;
import net.maritimecloud.mms.server.connectionold.ServerConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kasper Nielsen
 */
public class OldServerTransport implements ServerTransportListener {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(OldServerTransport.class);

    public final ConnectionManager cm;

    /** Whether or not we have received the first hello message from the client. */
    public ServerConnectFuture connectFuture = new ServerConnectFuture(this);

    /** The connection this transport is attached to, or null, if it is not attached to one. */
    public volatile ServerConnection connection;

    private final ReentrantLock readLock = new ReentrantLock();

    public final MmsServer server;

    final ClientTransportListener transportListener;

    /** The websocket session. */
    private volatile ServerTransport transport;

    private final ReentrantLock writeLock = new ReentrantLock();

    /** Constructor */
    public OldServerTransport(MmsServer server, ClientTransportListener transportListener) {
        this.cm = requireNonNull(server.getService(ConnectionManager.class));
        this.server = requireNonNull(server);
        this.transportListener = requireNonNull(transportListener);
    }

    /** {@inheritDoc} */
    public void doClose(final MmsConnectionClosingCode reason) {
        fullyLock();
        try {
            ServerTransport transport = this.transport;
            if (transport != null) {
                try {
                    transport.close(reason);
                } catch (Exception e) {
                    LOG.error("Failed to close connection", e);
                }
            }
        } finally {
            fullyUnlock();
        }
    }

    /**
     * Locks to prevent both puts and takes.
     */
    void fullyLock() {
        writeLock.lock();
        readLock.lock();
    }

    /**
     * Unlocks to allow both puts and takes.
     */
    void fullyUnlock() {
        readLock.unlock();
        writeLock.unlock();
    }

    /** {@inheritDoc} */
    public void onClose(ServerTransport t, MmsConnectionClosingCode reason) {
        fullyLock();
        try {
            transport = null;
            if (connection != null) {
                connection.transportDisconnected(this, reason);
            }
            transportListener.onClose(reason);
        } finally {
            fullyUnlock();
        }

    }

    public void onOpen(ServerTransport t) {
        fullyLock();
        try {
            this.transport = t;
            // send a Welcome message to the client as the first thing
            ServerId id = cm.server.getServerId();
            sendMessage(new MmsMessage(new Welcome().addProtocolVersion(1).setServerId(id.toString())
                    .putProperties("implementation", "mmsServer/0.2")));
            transportListener.onOpen();
        } finally {
            fullyUnlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onMessage(ServerTransport t, MmsMessage msg) {
        readLock.lock();
        try {
            if (connectFuture != null) {
                connectFuture.onMessage(msg.getM());
            } else if (msg.isConnectionMessage()) {
                connection.messageReceive(this, msg);
            } else {
                String err = "Unknown messageType " + msg.getClass().getSimpleName();
                LOG.error(err);
                doClose(MmsConnectionClosingCode.WRONG_MESSAGE.withMessage(err));
            }
            transportListener.onMessageReceived(msg);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Sends the given message over the wire in text or binary format
     * 
     * @param msg
     *            the message to sen
     */
    public void sendMessage(MmsMessage msg) {
        writeLock.lock();
        try {
            ServerTransport transport = this.transport;
            if (transport != null) {
                transport.sendMessage(msg);
                transportListener.onMessageSent(msg);
            }
        } finally {
            writeLock.unlock();
        }
    }
}
