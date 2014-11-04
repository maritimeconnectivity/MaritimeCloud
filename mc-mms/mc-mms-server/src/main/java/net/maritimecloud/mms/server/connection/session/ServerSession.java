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
package net.maritimecloud.mms.server.connection.session;

import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;
import net.maritimecloud.mms.server.connection.transport.ServerTransportListener;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class ServerSession {

    final ReentrantLock readLock = new ReentrantLock();

    final ReentrantLock writeLock = new ReentrantLock();

    ServerTransport transport;

    final ServerTransportListener stl = new ServerTransportListener() {};

    private final Binary sessionId = Binary.random(32);

    public Binary getSessionId() {
        return sessionId;
    }

    /**
     *
     */
    public void invalidate(MmsConnectionClosingCode c) {
        // lock

        // lock
    }

    public void sendMessage(MmsMessage message) {

    }

    public boolean reconnect(long lastReceivedId) {
        return false;
    }

    public void close(MmsMessage message) {

    }

    public void onMessage(ServerTransport t, MmsMessage message) {

    }

    /**
     * Invoked whenever the transport has been closed. Either remote or locally.
     *
     * @param closingCode
     *            the closing code
     */
    public void onClose(ServerTransport t, MmsConnectionClosingCode closingCode) {

    }


    public static ServerSession create() {
        return null;
    }
}
