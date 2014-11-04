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
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;

public class TestMessage implements Message {

    /** The full name of this message */
    public static final String NAME ="net.maritimecloud.mms.stubs.TestMessage";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<TestMessage> SERIALIZER = new Serializer();

    /** Hey */
    private final List<Long> f1;

    /** Creates a new TestMessage. */
    public TestMessage() {
        f1 = new java.util.ArrayList<>();
    }

    /**
     * Creates a new TestMessage by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    TestMessage(MessageReader reader) throws IOException {
        this.f1 = MessageHelper.readList(1, "f1", reader, ValueSerializer.INT64);
    }

    /**
     * Creates a new TestMessage by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    TestMessage(TestMessage instance) {
        this.f1 = MessageHelper.immutableCopy(instance.f1);
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeList(1, "f1", f1, ValueSerializer.INT64);
    }

    public List<Long> getF1() {
        return java.util.Collections.unmodifiableList(f1);
    }

    public boolean hasF1() {
        return f1 != null;
    }

    public TestMessage addF1(Long f1) {
        java.util.Objects.requireNonNull(f1, "f1 is null");
        this.f1.add(f1);
        return this;
    }

    public TestMessage addAllF1(Collection<? extends Long> f1) {
        for (Long e : f1) {
            addF1(e);
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public TestMessage immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static TestMessage fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Hashing.hashcode(this.f1);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof TestMessage) {
            TestMessage o = (TestMessage) other;
            return Objects.equals(f1, o.f1);
        }
        return false;
    }

    /** A serializer for reading and writing instances of TestMessage. */
    static class Serializer extends MessageSerializer<TestMessage> {

        /** {@inheritDoc} */
        @Override
        public TestMessage read(MessageReader reader) throws IOException {
            return new TestMessage(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(TestMessage message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of TestMessage. */
    static class Immutable extends TestMessage {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(TestMessage instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public TestMessage immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public TestMessage addF1(Long f1) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
