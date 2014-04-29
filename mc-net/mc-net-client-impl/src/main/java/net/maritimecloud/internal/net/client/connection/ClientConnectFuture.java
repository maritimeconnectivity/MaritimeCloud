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

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.net.client.ClientContainer;
import net.maritimecloud.internal.net.messages.TransportMessage;
import net.maritimecloud.internal.net.messages.auxiliary.HelloMessage;
import net.maritimecloud.internal.net.messages.auxiliary.WelcomeMessage;
import net.maritimecloud.messages.Connected;
import net.maritimecloud.net.ClosingCode;
import net.maritimecloud.net.MaritimeCloudConnection.Listener;
import net.maritimecloud.util.function.Consumer;
import net.maritimecloud.util.geometry.PositionTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class takes of connecting and handshaking with a remote server.
 *
 * @author Kasper Nielsen
 */
class ClientConnectFuture implements Runnable {

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ClientConnectFuture.class);

    private final ClientConnection connection;

    private final long reconnectId;

    private final CountDownLatch cancelled = new CountDownLatch(1);

    private final ConnectionTransport transport;

    private boolean receivedHelloMessage /* = false */;

    /** The thread during the actual connect. */
    private volatile Thread thread;

    String connectedId;

    ClientConnectFuture(ClientConnection connection, long reconnectId) {
        this.connection = requireNonNull(connection);
        this.reconnectId = reconnectId;
        transport = connection.connectionManager.ctm.create(connection, this);
    }

    public void run() {
        // RECONNECTIN
        // [MaritimeCloud-ConnectingThread] INFO net.maritimecloud.internal.net.client.connection.ClientConnectFuture -
        // Trying to connect to ws://localhost:30000/
        // [Thread-15] ERROR
        // net.maritimecloud.internal.net.client.connection.ConnectionTransportManagerJsr356$ConnectionTransportJsr356 -
        // Connect timed out after 10 seconds
        // [MaritimeCloud-ConnectingThread] ERROR net.maritimecloud.internal.net.client.connection.ClientConnectFuture -
        // Could not connect to ws://localhost:30000/, will try again later
        // RECONNECTIN
        // [Thread-16] ERROR
        // net.maritimecloud.internal.net.client.connection.ConnectionTransportManagerJsr356$ConnectionTransportJsr356 -
        // Connect timed out after 10 seconds
        // [MaritimeCloud-ConnectingThread] ERROR net.maritimecloud.internal.net.client.connection.ClientConnectFuture -
        // Could not connect to ws://localhost:30000/, will try again later
        // RECONNECTIN
        // [Thread-17] ERROR
        // net.maritimecloud.internal.net.client.connection.ConnectionTransportManagerJsr356$ConnectionTransportJsr356 -
        // Connect timed out after 10 seconds
        // [MaritimeCloud-ConnectingThread] ERROR net.maritimecloud.internal.net.client.connection.ClientConnectFuture -
        // Could not connect to ws://localhost:30000/, will try again later
        // RECONNECTIN
        // [Thread-18] ERROR
        // net.maritimecloud.internal.net.client.connection.ConnectionTransportManagerJsr356$ConnectionTransportJsr356 -
        // Connect timed out after 10 seconds
        // [MaritimeCloud-ConnectingThread] ERROR net.maritimecloud.internal.net.client.connection.ClientConnectFuture -
        // Could not connect to ws://localhost:30000/, will try again later
        // RECONNECTIN
        // Bye connect
        // [WebSocketClient@1644443712-21] ERROR net.maritimecloud.internal.net.client.connection.ClientConnectFuture -
        // Expected a connected message, but was: WelcomeMessage
        // [WebSocketClient@1644443712-21] ERROR net.maritimecloud.internal.net.client.connection.ClientConnection -
        // Connection to MaritimeCloud was lost: 'Expected a connected message, but was: WelcomeMessage' will try and
        // reconnected
        // [MaritimeCloud-ConnectingThread] INFO net.maritimecloud.internal.net.client.connection.ClientConnectFuture -
        // Trying to connect to ws://localhost:30000/
        // RECONNECTIN
        // Bye connect
        try {
            run0();
            System.out.println("Bye connect");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    void run0() {
        final ConnectionManager cm = connection.connectionManager;
        cm.forEachListener(new Consumer<Listener>() {
            public void accept(Listener t) {
                t.connecting(cm.uri);
            }
        });
        LOG.info("Trying to connect to " + cm.uri);
        thread = Thread.currentThread();
        while (cancelled.getCount() > 0) {
            System.out.println("RECONNECTIN");
            try {
                transport.connect(cm.uri);
                System.out.println("FFFFF");
                return;
            } catch (IllegalStateException e) {
                LOG.error("A serious internal error", e);
            } catch (IOException e) {
                if (cancelled.getCount() > 0) {// Only log the error if we are not cancelled
                    LOG.error("Could not connect to " + cm.uri + ", will try again later");
                }
            }
            try {
                cancelled.await(2, TimeUnit.SECONDS); // exponential backoff?
            } catch (InterruptedException ignore) {}
        }
    }

    /**
     * Takes care of the connection handshake.
     *
     * @param m
     *            the message we have received
     */
    void onMessage(TransportMessage m) {
        if (!receivedHelloMessage) {
            if (m instanceof WelcomeMessage) {
                ClientContainer client = connection.connectionManager.client;
                PositionTime pt = client.readCurrentPosition();
                String connectName = connection.connectionId == null ? "" : connection.connectionId;
                transport.sendText(new HelloMessage(client.getLocalId(), connection.connectionManager.client
                        .getClientConnectString(), connectName, reconnectId, pt.getLatitude(), pt.getLongitude())
                        .toText());
                receivedHelloMessage = true;
            } else {
                String err = "Expected a welcome message, but was: " + m.getClass().getSimpleName();
                LOG.error(err);
                transport.doClose(ClosingCode.WRONG_MESSAGE.withMessage(err));
            }
        } else {
            if (m instanceof Connected) {
                Connected cm = (Connected) m;
                boolean isReconnected = Objects.equals(cm.getConnectionId(), connection.connectionId);
                connection.connectionId = cm.getConnectionId();
                // if (cm.getLastReceivedMessageId() >= 0) {
                // List<OutstandingMessage> os = connection.rq.reConnected(cm);
                //
                // for (OutstandingMessage o : os) {
                // transport.sendText(o.msg);
                // }
                // // Okay lets send the outstanding message
                // }
                connectedId = cm.getConnectionId();
                connection.connected(this, transport);

                connection.worker.onConnect(transport, cm.getLastReceivedMessageId(), isReconnected);
                // We need to retransmit messages
                transport.connectFuture = null; // make sure we do not get any more messages

                connection.connectionManager.forEachListener(new Consumer<Listener>() {
                    public void accept(Listener t) {
                        t.connected();
                    }
                });
            } else {
                String err = "Expected a connected message, but was: " + m.getClass().getSimpleName();
                LOG.error(err);
                transport.doClose(ClosingCode.WRONG_MESSAGE.withMessage(err));
            }
        }
    }

    void cancelConnectUnderLock() {
        cancelled.countDown();
        Thread t = thread;
        if (t != null) {
            t.interrupt();
            transport.doClose(ClosingCode.CONNECT_CANCELLED.withMessage("connect cancelled"));
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
