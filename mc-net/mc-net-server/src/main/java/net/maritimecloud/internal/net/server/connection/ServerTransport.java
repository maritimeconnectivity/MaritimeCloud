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
package net.maritimecloud.internal.net.server.connection;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.locks.ReentrantLock;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCode;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.maritimecloud.core.id.ServerId;
import net.maritimecloud.internal.net.messages.ConnectionMessage;
import net.maritimecloud.internal.net.messages.TransportMessage;
import net.maritimecloud.internal.net.messages.auxiliary.WelcomeMessage;
import net.maritimecloud.internal.net.server.InternalServer;
import net.maritimecloud.net.ClosingCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Kasper Nielsen
 */
@ServerEndpoint(value = "/")
public class ServerTransport {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(ServerTransport.class);

    final ConnectionManager cm;

    /** Whether or not we have received the first hello message from the client. */
    ServerConnectFuture connectFuture = new ServerConnectFuture(this);

    /** The connection this transport is attached to, or null, if it is not attached to one. */
    volatile ServerConnection connection;

    private final ReentrantLock readLock = new ReentrantLock();

    final InternalServer server;

    /** The websocket session. */
    private volatile Session session;

    private final ReentrantLock writeLock = new ReentrantLock();

    ServerTransport(InternalServer server) {
        this.cm = requireNonNull(server.getService(ConnectionManager.class));
        this.server = requireNonNull(server);
    }

    /** {@inheritDoc} */
    void doClose(final ClosingCode reason) {
        fullyLock();
        try {
            Session session = this.session;
            if (session != null) {
                CloseReason cr = new CloseReason(new CloseCode() {
                    public int getCode() {
                        return reason.getId();
                    }
                }, reason.getMessage());

                try {
                    session.close(cr);
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
    @OnClose
    public void onClose(CloseReason closeReason) {
        fullyLock();
        try {
            session = null;
            ClosingCode reason = ClosingCode
                    .create(closeReason.getCloseCode().getCode(), closeReason.getReasonPhrase());
            if (connection != null) {
                connection.transportDisconnected(this, reason);
            }
        } finally {
            fullyUnlock();
        }

    }

    @OnOpen
    public void onOpen(Session session) {
        fullyLock();
        try {
            this.session = session;
            // send a Welcome message to the client as the first thing
            ServerId id = cm.server.getServerId();
            sendText(new WelcomeMessage(1, id, "enavServer/1.0").toJSON());
        } finally {
            fullyUnlock();
        }
    }

    @OnMessage
    public void onTextMessage(String textMessage) {
        readLock.lock();
        try {
            TransportMessage msg;
            System.out.println("Received: " + textMessage);
            try {
                msg = TransportMessage.parseMessage(textMessage);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("Failed to parse incoming message", e);
                doClose(ClosingCode.WRONG_MESSAGE.withMessage(e.getMessage()));
                return;
            }
            if (connectFuture != null) {
                connectFuture.onMessage(msg);
            } else if (msg instanceof ConnectionMessage) {
                ConnectionMessage m = (ConnectionMessage) msg;
                connection.messageReceive(this, m);
            } else {
                String err = "Unknown messageType " + msg.getClass().getSimpleName();
                LOG.error(err);
                doClose(ClosingCode.WRONG_MESSAGE.withMessage(err));
            }
        } finally {
            readLock.unlock();
        }
    }

    public void sendText(String text) {
        writeLock.lock();
        try {
            Session session = this.session;
            if (session != null) {
                if (text.length() < 1000) {
                    System.out.println("Sending " + text);
                }
                session.getAsyncRemote().sendText(text);
            }
        } finally {
            writeLock.unlock();
        }
    }
}
