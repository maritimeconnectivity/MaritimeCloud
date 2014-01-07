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

import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.internal.net.messages.ConnectionMessage;
import net.maritimecloud.net.ClosingCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Kasper Nielsen
 */
public final class ClientConnection {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(ClientConnection.class);

    private volatile ClientConnectFuture connectingFuture;

    volatile String connectionId;

    final ConnectionManager connectionManager;

    private volatile ClientDisconnectFuture disconnectingFuture;

    final ReentrantLock retrieveLock = new ReentrantLock();

    final ReentrantLock sendLock = new ReentrantLock();

    /** We are connected when this is non-null. */
    volatile ConnectionTransport transport;

    final Worker worker = new Worker(this);

    private ClientConnection(ConnectionManager connectionManager) {
        this.connectionManager = requireNonNull(connectionManager);
    }

    void connect() {
        connectionManager.lock.lock();
        try {
            // only connect if are not already connected (transport==null)
            // And another thread is not already trying to connect (connectionFuture==null)
            if (transport == null && connectingFuture == null) {
                LOG.info("Trying to connect to " + connectionManager.uri);
                connectingFuture = new ClientConnectFuture(this, -1);
                connectionManager.threadManager.startConnectingThread(connectingFuture);
            }
        } finally {
            connectionManager.lock.unlock();
        }
    }

    /**
     * Invoked when we have successfully connected to the server.
     * 
     * @param transport
     */
    void connected(ClientConnectFuture future, ConnectionTransport transport) {
        connectionManager.lock.lock();
        try {
            if (future == connectingFuture) {
                this.connectingFuture = null;
                this.transport = transport;
                connectionManager.stateChange.signalAll();
            }
        } finally {
            connectionManager.lock.unlock();
        }
    }

    static ClientConnection create(ConnectionManager cm) {
        ClientConnection cc = new ClientConnection(cm);
        cm.threadManager.startWorkerThread(cc.worker);
        return cc;
    }

    boolean disconnect() {
        connectionManager.lock.lock();
        try {
            if (transport != null && disconnectingFuture == null) {
                LOG.info("Starting Disconnecting");
                disconnectingFuture = new ClientDisconnectFuture(this, transport);
                connectionManager.threadManager.startDisconnectingThread(disconnectingFuture);
            } else if (connectingFuture != null) {
                LOG.info("Cancelling connect");
                connectingFuture.cancelConnectUnderLock(); // In the process of connecting, just cancel the connect
                connectingFuture = null;
                connectionManager.connection = null;
                return true;
            }
            connectionManager.stateChange.signalAll();
        } finally {
            connectionManager.lock.unlock();
        }
        return false;
    }

    void disconnected(ClientDisconnectFuture future) {
        connectionManager.lock.lock();
        try {
            if (future == disconnectingFuture && connectingFuture == null && transport == null) {
                this.disconnectingFuture = null;
            }
            connectionManager.connection = null;
            connectionManager.stateChange.signalAll();
        } finally {
            connectionManager.lock.unlock();
        }
    }

    /**
     * @return the transport
     */
    public ConnectionTransport getTransport() {
        return transport;
    }

    public boolean isConnected() {
        connectionManager.lock.lock();
        try {
            return transport != null && disconnectingFuture == null;
        } finally {
            connectionManager.lock.unlock();
        }
    }

    void messageReceive(ConnectionTransport transport, ConnectionMessage m) {
        retrieveLock.lock();
        try {
            worker.messageReceived(m);
        } finally {
            retrieveLock.unlock();
        }
    }

    public ConnectionMessageBus getBus() {
        return connectionManager.hub;
    }

    /**
     * Invoked whenever we want to send a message
     * 
     * @param message
     *            the message to send
     */
    OutstandingMessage messageSend(ConnectionMessage message) {
        return worker.messageSend(message);
        //
        // sendLock.lock();
        // try {
        //
        // OutstandingMessage m = rq.write(message);
        // if (transport != null) {
        // System.out.println("Sending " + m.msg);
        // transport.sendText(m.msg);
        // } else {
        // System.out.println("Not sending " + m.msg);
        // }
        //
        // return m;
        // } finally {
        // sendLock.unlock();
        // }
    }

    void transportDisconnected(ConnectionTransport transport, ClosingCode cr) {
        connectionManager.lock.lock();
        try {
            this.transport = null;
            if (cr.getId() == 1000) {
                connectionManager.connection = null;
                worker.shutdown();
            } else if (cr.getId() == ClosingCode.DUPLICATE_CONNECT.getId()) {
                System.out.println("Dublicate connect detected, will not reconnect");
                connectionManager.state = ConnectionManager.State.SHOULD_STAY_DISCONNECTED;
            } else {
                System.out.println(cr.getMessage());
                System.out.println("OOPS, lets reconnect");
                connectingFuture = null;// need to clear it if we are already connecting
            }
            connectionManager.stateChange.signalAll();
        } finally {
            connectionManager.lock.unlock();
        }
    }
}
