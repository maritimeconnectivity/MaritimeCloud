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

/**
 * A listener that can used to listen for updates to the connection status to the maritime network.
 */
public interface MmsConnectionListener {

    /**
     * Invoked whenever the client is trying to connect/reconnect to a remote MMS server.
     *
     * @param host The MMS server we are connecting to
     */
    default void connecting(URI host) {}

    /**
     * Invoked when the client has successfully connected to a MMS server.
     *
     * @param host The MMS server we are connecting to
     */
    default void connected(URI host) {}

    /**
     * Invoked whenever the connection is lost to the MMS server. It will automatically connect again unless
     * disabled or shutdown.
     *
     * @param closeReason the reason for the closing
     */
    default void disconnected(MmsConnectionClosingCode closeReason) {}

    /**
     * The binary message received over the connection
     * @param message the message
     */
    default void binaryMessageReceived(byte[] message) {}

    /**
     * The binary message sent over the connection
     * @param message the message
     */
    default void binaryMessageSend(byte[] message) {}

    /**
     * The text message received over the connection
     * @param message the message
     */
    default void textMessageReceived(String message) {}

    /**
     * The text message sent over the connection
     * @param message the message
     */
    default void textMessageSend(String message) {}
}
