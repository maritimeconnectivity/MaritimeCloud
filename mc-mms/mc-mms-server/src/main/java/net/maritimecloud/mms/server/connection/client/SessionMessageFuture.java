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
package net.maritimecloud.mms.server.connection.client;

import java.util.concurrent.CompletableFuture;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.message.Message;

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

    /** When the future was, and the initial was first attempted to be sent. */
    final long creationTime = System.nanoTime();

    /** The message that should be send, might be null for certain error conditions. */
    final MmsMessage message;

    /** The message id. */
    final long messageId;

    SessionMessageFuture(MmsMessage cm, long messageId) {
        this.message = cm;
        this.messageId = messageId;
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

    static SessionMessageFuture notConnected(Message message) {
        return null;
    }

    static SessionMessageFuture wrongSession(Message message) {
        return null;
    }

    public enum FailToSendReason {
        NOT_CONNECTED, OK, SESSION_EXPIRED;
    }

    public enum State {
        ACKED, CLIENT_GONE, OTHER_SESSION, SEND;
    }
}
