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
package net.maritimecloud.internal.mms.client.connection.session;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.connection.transport.ClientTransport;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.Welcome;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.message.Message;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public final class SessionStateConnecting extends SessionState {

    /** The logger. */
    static final Logger LOG = Logger.get(SessionStateConnecting.class);

    /** A count down latch indicated if the connection process has been cancelled. */
    final CountDownLatch cancel = new CountDownLatch(1);

    /** Whether or not we have received a hello message. */
    private boolean receivedHelloMessage /* = false */;

    final ClientTransport transport;

    /** The URI to connect to. */
    final URI uri;

    final Thread connectThread = new Thread(() -> run0(), "MMS-ConnectThread");

    /**
     * @param session
     */
    SessionStateConnecting(Session session, URI uri) {
        super(session);
        this.uri = requireNonNull(uri);
        transport = session.ctm.create(session.tl, session.connectionListener);
        connectThread.setDaemon(true);
    }

    void cancelWhileFullyLocked(MmsConnectionClosingCode reason) {
        cancel.countDown();
        connectThread.interrupt();
        transport.closeTransport(reason);
        try {
            connectThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        session.listener.onSessionClose(reason);
    }

    void connectAsynchronously() {
        connectThread.start();
    }

    void run0() {
        LOG.info("Trying to connect to " + uri);
        session.connectionListener.connecting(uri);

        while (this == session.state && cancel.getCount() > 0) {
            try {
                transport.connectBlocking(uri);
                return;
            } catch (IllegalStateException e) {
                LOG.error("A serious internal error", e);
            } catch (IOException e) {
                if (cancel.getCount() > 0) {// Only log the error if we are not cancelled
                    LOG.error("Could not connect to " + uri + ", will try again later");
                }
            }
            try {
                cancel.await(2, TimeUnit.SECONDS); // exponential backoff?
            } catch (InterruptedException ignore) {}
        }
    }

    public void onMessage(MmsMessage pm) {
        Message m = pm.getM();
        String err = null;
        if (!receivedHelloMessage) {
            if (m instanceof Welcome) {
                onWelcome((Welcome) m);
            } else {
                err = "Expected a welcome message, but was: " + m.getClass().getSimpleName();
            }
        } else if (m instanceof Connected) {
            onConnected((Connected) m);
        } else {
            err = "Expected a connected message, but was: " + m.getClass().getSimpleName();
        }

        if (err != null) {
            LOG.error(err);
            transport.closeTransport(MmsConnectionClosingCode.WRONG_MESSAGE.withMessage(err));
            // TODO Kill session
        }
    }

    private void onConnected(Connected cm) {
        Binary b = session.sessionId;
        SessionStateConnected.connected(this, b, cm.getSessionId(),
                cm.getLastReceivedMessageId() == null ? 0 : cm.getLastReceivedMessageId());
    }

    private void onWelcome(Welcome w) {
        Hello h = new Hello();
        // Client properties
        h.setClientId(session.info.getClientId().toString());
        if (session.info.getClientConnectString() != null) {
            for (Map.Entry<String, String> e : session.info.getClientConnectString().entrySet()) {
                h.putProperties(e.getKey(), e.getValue());
            }
        }

        // Reconnect if we have an ongoing session
        if (session.sessionId != null) { // reconnecting or not
            h.setSessionId(session.sessionId);
            h.setLastReceivedMessageId(session.latestReceivedId);
        }

        // Set current position
        Optional<PositionTime> pr = session.info.getCurrentPosition();
        if (pr.isPresent()) {
            h.setPositionTime(pr.get().withTime(System.currentTimeMillis()));
        }

        transport.sendMessage(new MmsMessage(h));
        receivedHelloMessage = true;
    }
}
