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
package net.maritimecloud.mms.tck.stubs;

import java.io.IOException;

import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.LocalEndpoint;

public final class TestEndpoint extends LocalEndpoint {

    /** The name of the endpoint. */
    public static final String NAME = "TestEndpoint";

    public TestEndpoint(LocalEndpoint.Invocator ei) {
        super(ei);
    }

    public EndpointInvocationFuture<Long> hello(Long testId) {
        Hello arguments = new Hello();
        arguments.setTestId(testId);
        return invokeRemote("TestEndpoint.hello", arguments, Hello.SERIALIZER, ValueSerializer.INT64);
    }

    public static class Hello implements Message {

        /** The full name of this message */
        public static final String NAME = "net.maritimecloud.mms.tck.stubs.Hello";

        /** A message serializer that can read and write instances of this class. */
        public static final MessageSerializer<Hello> SERIALIZER = new HelloSerializer();

        /** Hey */
        private Long testId;

        /** Creates a new Hello. */
        public Hello() {}

        /**
         * Creates a new Hello by reading from a message reader.
         *
         * @param reader
         *            the message reader
         */
        Hello(MessageReader reader) throws IOException {
            this.testId = reader.readInt64(1, "testId", null);
        }

        void writeTo(MessageWriter w) throws IOException {
            w.writeInt64(1, "testId", testId);
        }

        public Long getTestId() {
            return testId;
        }

        public boolean hasTestId() {
            return testId != null;
        }

        public Hello setTestId(Long testId) {
            this.testId = testId;
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Hello immutable() {
            throw new UnsupportedOperationException("method not supported");
        }

        /** Returns a JSON representation of this message */
        public String toJSON() {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    /** A serializer for reading and writing instances of Hello. */
    static class HelloSerializer extends MessageSerializer<Hello> {

        /** {@inheritDoc} */
        @Override
        public Hello read(MessageReader reader) throws IOException {
            return new Hello(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Hello message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }
}
