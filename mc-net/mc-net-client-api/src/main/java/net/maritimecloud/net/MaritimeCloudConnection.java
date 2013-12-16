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

import java.util.concurrent.TimeUnit;

/**
 * A connection to the cloud.
 * 
 * @author Kasper Nielsen
 */
public interface MaritimeCloudConnection {

    void addListener(Listener listener);

    boolean awaitConnected(long timeout, TimeUnit unit) throws InterruptedException;

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

    boolean isConnected();

    /** A listener that can used to listen for updates to the connection status to the maritime network. */
    abstract class Listener {

        /** Invoked when the client has fully connected to the server. */
        public void connected() {}

        public void disconnected(ClosingCode closeReason) {}
    }
}
