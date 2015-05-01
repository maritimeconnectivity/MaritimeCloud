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
package net.maritimecloud.mms.server.connection.clientnew;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;

/**
 *
 * @author Kasper Nielsen
 */
// Den her kan vel ogsaa vaere i flere states???
// isSent
// isAcked
// failedToSend
public class SessionMessageFuture {

    private final CompletableFuture<Void> acked = new CompletableFuture<>();

    final MmsMessage cm;

    volatile boolean isSent;

    long sessionId;

    final UUID uuid = UUID.randomUUID();


    /** When the future was, and the initial was first attempted to be sent. */
    final long created = System.nanoTime();

    SessionMessageFuture(MmsMessage cm) {
        this.cm = cm;
    }

    public boolean isSent() {
        return isSent;
    }

    /**
     * A future that can be used to find out if a message has been received on the remote side. Via an ack for the
     * message id.
     *
     * @return a future
     */
    public CompletableFuture<Void> protocolAcked() {
        return acked;
    }

    public static SessionMessageFuture wrongSession(MmsMessage message) {
        return null;
    }

    public static SessionMessageFuture notConnected(MmsMessage message) {
        return null;
    }

    public enum State {
        SEND, ACKED, OTHER_SESSION, CLIENT_GONE;
    }

    public enum FailToSendReason {
        OK, SESSION_EXPIRED, NOT_CONNECTED;
    }
}
