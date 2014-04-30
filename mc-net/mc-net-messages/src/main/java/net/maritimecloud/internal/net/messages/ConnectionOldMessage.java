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

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import net.maritimecloud.internal.messages.ConnectionMessage;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class ConnectionOldMessage implements ConnectionMessage {

    /** The id of the message. */
    private long messageId;

    /** The last message id that was received by the remote end. */
    private long latestReceivedId;

    // options
    // boolean fastack <- receiver should send some kind of ack immediatly

    /** The type of message. */
    private final MessageType messageType;

    public String toText() {
        TextMessageWriter w = new TextMessageWriter();
        // w.writeInt(getMessageType().type);
        write(w);
        String s = w.sb.append("]").toString();
        s = messageType.type + ":" + s;
        return s;
    }


    /**
     * @param messageType
     */
    public ConnectionOldMessage(MessageType messageType) {
        this.messageType = requireNonNull(messageType);
    }

    public ConnectionOldMessage(MessageType messageType, TextMessageReader pr) throws IOException {
        this.messageType = requireNonNull(messageType);
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

    public ConnectionMessage setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public ConnectionMessage setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    /** {@inheritDoc} */
    protected final void write(TextMessageWriter w) {
        w.writeLong(messageId);
        w.writeLong(latestReceivedId);
        write0(w);
    }

    protected abstract void write0(TextMessageWriter w);
}
