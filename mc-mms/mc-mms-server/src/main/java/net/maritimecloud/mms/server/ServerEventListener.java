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
package net.maritimecloud.mms.server;

import static java.util.Objects.requireNonNull;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.Binary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class ServerEventListener {

    /** An event listener that does nothing. */
    public static ServerEventListener EMPTY = new ServerEventListener() {};

    /** The logger. */
    static final Logger LOGGER = LoggerFactory.getLogger(ServerEventListener.class);

    /**
     * Invoked whenever the connection is lost to the MMS server. It will automatically connect again unless disabled or
     * shutdown.
     *
     * @param closeReason
     *            the reason for the closing
     */
    public void disconnected(MmsConnectionClosingCode closeReason) {}

    public void sessionCreated(ServerTransport transport, Binary sessionID) {}


    /**
     * The binary message received over the connection
     *
     * @param message
     *            the message
     */
    public void transportBinaryMessageReceived(ServerTransport transport, byte[] message) {}

    /**
     * The binary message sent over the connection
     *
     * @param message
     *            the message
     */
    public void transportBinaryMessageSend(ServerTransport transport, byte[] message) {}

    public void transportMessageReceived(ServerTransport transport, MmsMessage message) {}

    public void transportMessageSend(ServerTransport transport, MmsMessage message) {}

    /**
     * The text message received over the connection
     *
     * @param message
     *            the message
     */
    public void transportTextMessageReceived(ServerTransport transport, String message) {}

    /**
     * The text message sent over the connection
     *
     * @param message
     *            the message
     */
    public void transportTextMessageSend(ServerTransport transport, String message) {}

    public static ServerEventListener create(ServerEventListener... listeners) {
        return new AggegatedEventListener(listeners);
    }

    /**
     * Aggregates one or more event listeners. And makes sure not to propagate any exception that a listener might
     * raise.
     */
    static class AggegatedEventListener extends ServerEventListener {

        /** An array of listeners */
        private final ServerEventListener[] listeners;

        AggegatedEventListener(ServerEventListener[] listeners) {
            this.listeners = new ServerEventListener[listeners.length];
            for (int i = 0; i < listeners.length; i++) {
                this.listeners[i] = requireNonNull(listeners[i], "listener at index " + i + " is null");
            }
        }

        /** {@inheritDoc} */
        @Override
        public void disconnected(MmsConnectionClosingCode closeReason) {
            for (ServerEventListener listener : listeners) {
                try {
                    listener.disconnected(closeReason);
                } catch (RuntimeException e) {
                    LOGGER.error("Event listener failed", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void sessionCreated(ServerTransport transport, Binary sessionID) {
            for (ServerEventListener listener : listeners) {
                try {
                    listener.sessionCreated(transport, sessionID);
                } catch (RuntimeException e) {
                    LOGGER.error("Event listener failed", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void transportBinaryMessageReceived(ServerTransport transport, byte[] message) {
            for (ServerEventListener listener : listeners) {
                try {
                    listener.transportBinaryMessageReceived(transport, message);
                } catch (RuntimeException e) {
                    LOGGER.error("Event listener failed", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void transportBinaryMessageSend(ServerTransport transport, byte[] message) {
            for (ServerEventListener listener : listeners) {
                try {
                    listener.transportBinaryMessageSend(transport, message);
                } catch (RuntimeException e) {
                    LOGGER.error("Event listener failed", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void transportMessageReceived(ServerTransport transport, MmsMessage message) {
            for (ServerEventListener listener : listeners) {
                try {
                    listener.transportMessageReceived(transport, message);
                } catch (RuntimeException e) {
                    LOGGER.error("Event listener failed", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void transportMessageSend(ServerTransport transport, MmsMessage message) {
            for (ServerEventListener listener : listeners) {
                try {
                    listener.transportMessageSend(transport, message);
                } catch (RuntimeException e) {
                    LOGGER.error("Event listener failed", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void transportTextMessageReceived(ServerTransport transport, String message) {
            for (ServerEventListener listener : listeners) {
                try {
                    listener.transportTextMessageReceived(transport, message);
                } catch (RuntimeException e) {
                    LOGGER.error("Event listener failed", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void transportTextMessageSend(ServerTransport transport, String message) {
            for (ServerEventListener listener : listeners) {
                try {
                    listener.transportTextMessageSend(transport, message);
                } catch (RuntimeException e) {
                    LOGGER.error("Event listener failed", e);
                }
            }
        }
    }
}
