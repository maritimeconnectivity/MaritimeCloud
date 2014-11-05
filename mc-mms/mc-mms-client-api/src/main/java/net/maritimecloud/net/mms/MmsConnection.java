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
package net.maritimecloud.net.mms;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Details about the connection to the MMS server.
 *
 * @author Kasper Nielsen
 */
public interface MmsConnection {

    /**
     * Blocks until there is a valid connection to the MMS server, or the client is closed, or the timeout occurs, or
     * the current thread is interrupted, whichever happens first.
     *
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return <tt>true</tt> if the client is connected to the MMS server and <tt>false</tt> if the timeout elapsed
     *         before a connection was made or the client was closed
     * @throws InterruptedException
     *             if interrupted while waiting
     */
    boolean awaitConnected(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Blocks until there is no connection to the MMS server, or the client is closed, or the timeout occurs, or the
     * current thread is interrupted, whichever happens first.
     *
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return <tt>true</tt> if the connection is nont connected and <tt>false</tt> if the timeout elapsed before a
     *         connection was made or the client was closed
     * @throws InterruptedException
     *             if interrupted while waiting
     */
    boolean awaitDisconnected(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Enables the connection to the MMS server. If the client is not already connected the client will automatically
     * connect.
     *
     * @throws MmsClientClosedException
     *             if the client has been shutdown
     */
    void enable();

    /**
     * Disconnects from the MMS server. Unlike {@link MmsClient#close()} invoking this method allows for connecting
     * again later via {@link #enable()}.
     */
    void disable();

    /**
     * Returns whether or not we are currently connected to the MMS server.
     *
     * @return whether or not we are currently connected to the MMS server
     */
    boolean isConnected();

    /**
     * Returns whether or not the connection to the MMS server is enabled.
     *
     * @return whether or not the connection to the MMS server is enabled
     */
    boolean isEnabled();

    /** A listener that can used to listen for updates to the connection status to the maritime network. */
    interface Listener {

        /**
         * Invoked whenever the client is trying to connect/reconnect to a remote MMS server.
         *
         * @param host
         *            The MMS server we are connecting to
         */
        default void connecting(URI host) {}

        /** Invoked when the client has successfully connected to a MMS server. */
        default void connected(URI host) {}

        default void binaryMessageReceived(byte[] message) {}

        default void binaryMessageSend(byte[] message) {}

        default void textMessageReceived(String message) {}

        default void textMessageSend(String message) {}

        /**
         * Invoked whenever the connection is lost to the MMS server. It will automatically connect again unless
         * disabled or shutdown.
         *
         * @param closeReason
         *            the reason for the closing
         */
        default void disconnected(MmsConnectionClosingCode closeReason) {}
    }
}
