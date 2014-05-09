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
package net.maritimecloud.internal.net.client.broadcast.stubs;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.message.util.Hashing;
import net.maritimecloud.net.broadcast.BroadcastMessage;

/**
 *
 * @author Kasper Nielsen
 */
public class BroadcastTestMessage implements BroadcastMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<BroadcastTestMessage> PARSER = new Parser();

    /** Hey */
    private String msg;

    /** Creates a new BroadcastTestMessage. */
    public BroadcastTestMessage() {}

    /**
     * Creates a new BroadcastTestMessage by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    BroadcastTestMessage(MessageReader reader) throws IOException {
        this.msg = reader.readString(1, "msg", null);
    }

    /**
     * Creates a new BroadcastTestMessage by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    BroadcastTestMessage(BroadcastTestMessage instance) {
        this.msg = instance.msg;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Hashing.hashcode(this.msg);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof BroadcastTestMessage) {
            BroadcastTestMessage o = (BroadcastTestMessage) other;
            return Objects.equals(msg, o.msg);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeString(1, "msg", msg);
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

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastTestMessage immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of BroadcastTestMessage. */
    static class Parser extends MessageParser<BroadcastTestMessage> {

        /** {@inheritDoc} */
        @Override
        public BroadcastTestMessage parse(MessageReader reader) throws IOException {
            return new BroadcastTestMessage(reader);
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
    }
}
