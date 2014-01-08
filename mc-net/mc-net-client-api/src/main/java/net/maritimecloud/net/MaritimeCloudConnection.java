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
package net.maritimecloud.net;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * A connection to the cloud.
 * 
 * @author Kasper Nielsen
 */
public interface MaritimeCloudConnection {

    // /**
    // * Adds a connection listener that can be notified whenever the client connects or disconnects.
    // *
    // * @param listener
    // * the listener
    // */
    // void addListener(MaritimeCloudConnection.Listener listener);

    /**
     * Blocks until there is a valid connection to the maritime cloud, or the client is closed, or the timeout occurs,
     * or the current thread is interrupted, whichever happens first.
     * 
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return <tt>true</tt> if the connection is connected and <tt>false</tt> if the timeout elapsed before a
     *         connection was made or the client was closed
     * @throws InterruptedException
     *             if interrupted while waiting
     */
    boolean awaitConnected(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Blocks until there is a valid connection to the maritime cloud, or the client is closed, or the timeout occurs,
     * or the current thread is interrupted, whichever happens first.
     * 
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return <tt>true</tt> if the connection is connected and <tt>false</tt> if the timeout elapsed before a
     *         connection was made or the client was closed
     * @throws InterruptedException
     *             if interrupted while waiting
     */
    boolean awaitDisconnected(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Connects to the cloud. If the client is already connected to the cloud this call is ignored.
     * 
     * @throws ConnectionClosedException
     *             if the client has been shutdown
     */
    void connect();

    /**
     * Disconnects from the cloud. Unlike {@link #close()} invoking this method allows for connecting again later via
     * {@link #connect()}.
     */
    void disconnect();

    // /**
    // * Returns an unique connection id after the connection has been made. Returns <code>null</code> until connected.
    // *
    // * @return an unique connection id
    // */
    // String getId();

    // boolean isConnecting()
    /**
     * Returns whether or not we are currently connected to the cloud.
     * 
     * @return whether or not we are currently connected to the cloud
     */
    boolean isConnected();

    /** A listener that can used to listen for updates to the connection status to the maritime network. */
    abstract class Listener {

        /** Invoked whenever the client is trying to connect/reconnect. */
        public void connecting(URI host) {}

        /** Invoked when the client has fully connected to the server. */
        public void connected() {}

        /**
         * Invoked when the client has been disconnected from the cloud server
         * 
         * @param closeReason
         *            the reason for the closing
         */
        public void disconnected(ClosingCode closeReason) {}
    }
}
