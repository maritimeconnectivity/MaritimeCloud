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
package net.maritimecloud.internal.mms.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.util.Binary;

public class Connected implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.mms.messages.Connected";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<Connected> SERIALIZER = new Serializer();

    /** Field definition. */
    private Binary sessionId;

    /** Field definition. */
    private Long lastReceivedMessageId;

    /** Creates a new Connected. */
    public Connected() {}

    /**
     * Creates a new Connected by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    Connected(MessageReader reader) throws IOException {
        this.sessionId = reader.readBinary(1, "sessionId", null);
        this.lastReceivedMessageId = reader.readInt64(2, "lastReceivedMessageId", null);
    }

    /**
     * Creates a new Connected by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    Connected(Connected instance) {
        this.sessionId = instance.sessionId;
        this.lastReceivedMessageId = instance.lastReceivedMessageId;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeBinary(1, "sessionId", sessionId);
        w.writeInt64(2, "lastReceivedMessageId", lastReceivedMessageId);
    }

    /** Returns the session id, if successfully reconnected identical to Hello.sessionId. */
    public Binary getSessionId() {
        return sessionId;
    }

    public boolean hasSessionId() {
        return sessionId != null;
    }

    public Connected setSessionId(Binary sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    /**
     * Returns if successfully reconnected, the latest message id that was succesfully received by server (Optional).
     */
    public Long getLastReceivedMessageId() {
        return lastReceivedMessageId;
    }

    public boolean hasLastReceivedMessageId() {
        return lastReceivedMessageId != null;
    }

    public Connected setLastReceivedMessageId(Long lastReceivedMessageId) {
        this.lastReceivedMessageId = lastReceivedMessageId;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Connected immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static Connected fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.sessionId);
        return 31 * result + Hashing.hashcode(this.lastReceivedMessageId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Connected) {
            Connected o = (Connected) other;
            return Objects.equals(sessionId, o.sessionId) &&
                   Objects.equals(lastReceivedMessageId, o.lastReceivedMessageId);
        }
        return false;
    }

    /** A serializer for reading and writing instances of Connected. */
    static class Serializer extends MessageSerializer<Connected> {

        /** {@inheritDoc} */
        @Override
        public Connected read(MessageReader reader) throws IOException {
            return new Connected(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Connected message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of Connected. */
    static class Immutable extends Connected {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(Connected instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public Connected immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Connected setSessionId(Binary sessionId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Connected setLastReceivedMessageId(Long lastReceivedMessageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
