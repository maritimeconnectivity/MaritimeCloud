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
package net.maritimecloud.mms.stubs;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.net.BroadcastMessage;

public class BroadcastTestMessage implements BroadcastMessage {

    /** The full name of this message */
    public static final String NAME ="net.maritimecloud.mms.stubs.BroadcastTestMessage";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<BroadcastTestMessage> SERIALIZER = new Serializer();

    /** Hey */
    private String msg;

    /** Hey */
    private Integer id;

    /** Creates a new BroadcastTestMessage. */
    public BroadcastTestMessage() {}

    /**
     * Creates a new BroadcastTestMessage by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    BroadcastTestMessage(MessageReader reader) throws IOException {
        this.msg = reader.readText(1, "msg", null);
        this.id = reader.readInt(2, "id", null);
    }

    /**
     * Creates a new BroadcastTestMessage by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    BroadcastTestMessage(BroadcastTestMessage instance) {
        this.msg = instance.msg;
        this.id = instance.id;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeText(1, "msg", msg);
        w.writeInt(2, "id", id);
    }

    public String getMsg() {
        return msg;
    }

    public boolean hasMsg() {
        return msg != null;
    }

    public BroadcastTestMessage setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public boolean hasId() {
        return id != null;
    }

    public BroadcastTestMessage setId(Integer id) {
        this.id = id;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastTestMessage immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static BroadcastTestMessage fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.msg);
        return 31 * result + Hashing.hashcode(this.id);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof BroadcastTestMessage) {
            BroadcastTestMessage o = (BroadcastTestMessage) other;
            return Objects.equals(msg, o.msg) &&
                   Objects.equals(id, o.id);
        }
        return false;
    }

    /** A serializer for reading and writing instances of BroadcastTestMessage. */
    static class Serializer extends MessageSerializer<BroadcastTestMessage> {

        /** {@inheritDoc} */
        @Override
        public BroadcastTestMessage read(MessageReader reader) throws IOException {
            return new BroadcastTestMessage(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(BroadcastTestMessage message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of BroadcastTestMessage. */
    static class Immutable extends BroadcastTestMessage {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(BroadcastTestMessage instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastTestMessage immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastTestMessage setMsg(String msg) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastTestMessage setId(Integer id) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
