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
package net.maritimecloud.internal.net.messages;

import java.io.IOException;

import net.maritimecloud.internal.messages.ConnectionMessage;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class ConnectionOldMessage extends AbstractTransportMessage implements ConnectionMessage {

    /** The id of the message. */
    private long messageId;

    /** The last message id that was received by the remote end. */
    private long latestReceivedId;

    // options
    // boolean fastack <- receiver should send some kind of ack immediatly
    /**
     * @param messageType
     */
    public ConnectionOldMessage(MessageType messageType) {
        super(messageType);
    }

    public ConnectionOldMessage(MessageType messageType, TextMessageReader pr) throws IOException {
        super(messageType);
        this.messageId = pr.takeLong();
        this.latestReceivedId = pr.takeLong();
    }

    /**
     * @return the destination
     */
    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    /**
     * @return the src
     */
    public Long getMessageId() {
        return messageId;
    }

    public ConnectionOldMessage setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public ConnectionOldMessage setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    protected final void write(TextMessageWriter w) {
        w.writeLong(messageId);
        w.writeLong(latestReceivedId);
        write0(w);
    }

    protected abstract void write0(TextMessageWriter w);
}