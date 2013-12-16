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

import jsr166e.CompletableFuture;
import net.maritimecloud.internal.net.messages.ConnectionMessage;

/**
 * 
 * @author Kasper Nielsen
 */
public class OutstandingMessage {

    private final CompletableFuture<Void> acked = new CompletableFuture<>();

    volatile boolean isSent;

    final ConnectionMessage cm;

    long id;

    OutstandingMessage(ConnectionMessage cm) {
        this.cm = cm;
    }

    /**
     * A future that can be used to find out if a message has been received on the remote side. Via an ack for the
     * message id.
     * 
     * @return
     */
    public CompletableFuture<Void> protocolAcked() {
        return acked;
    }

    public boolean isSent() {
        return isSent;
    }
}
