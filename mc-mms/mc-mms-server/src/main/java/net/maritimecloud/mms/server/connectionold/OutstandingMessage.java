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
package net.maritimecloud.mms.server.connectionold;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.message.Message;

/**
 *
 * @author Kasper Nielsen
 */
public class OutstandingMessage {

    private final CompletableFuture<Void> acked = new CompletableFuture<>();

    volatile boolean isSent;

    final MmsMessage cm;

    long id;

    public final UUID uuid = UUID.randomUUID();

    OutstandingMessage(Message tm) {
        this.cm = new MmsMessage();
        cm.setM(tm);
    }

    OutstandingMessage(MmsMessage cm) {
        this.cm = cm;
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

    public boolean isSent() {
        return isSent;
    }
}
