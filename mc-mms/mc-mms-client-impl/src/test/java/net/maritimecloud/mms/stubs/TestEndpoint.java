package net.maritimecloud.mms.stubs;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.maritimecloud.internal.message.MessageHelper;
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

    public EndpointInvocationFuture<List<Long>> invokeIt(List<TestMessage> li) {
        InvokeIt arguments = new InvokeIt();
        arguments.addAllLi(li);
        return invokeRemote("TestEndpoint.invokeIt", arguments, InvokeIt.SERIALIZER, ValueSerializer.INT64.listOf());
    }

    public static class InvokeIt implements Message {

        /** The full name of this message */
        public static final String NAME = "net.maritimecloud.mms.stubs.InvokeIt";

        /** A message serializer that can read and write instances of this class. */
        public static final MessageSerializer<InvokeIt> SERIALIZER = new InvokeItSerializer();

        /** Hey */
        private final List<TestMessage> li;

        /** Creates a new InvokeIt. */
        public InvokeIt() {
            li = new java.util.ArrayList<>();
        }

        /**
         * Creates a new InvokeIt by reading from a message reader.
         *
         * @param reader
         *            the message reader
         */
        InvokeIt(MessageReader reader) throws IOException {
            this.li = MessageHelper.readList(1, "li", reader, TestMessage.SERIALIZER);
        }

        void writeTo(MessageWriter w) throws IOException {
            w.writeList(1, "li", li, TestMessage.SERIALIZER);
        }

        public List<TestMessage> getLi() {
            return java.util.Collections.unmodifiableList(li);
        }

        public boolean hasLi() {
            return li != null;
        }

        public InvokeIt addLi(TestMessage li) {
            java.util.Objects.requireNonNull(li, "li is null");
            this.li.add(li);
            return this;
        }

        public InvokeIt addAllLi(Collection<? extends TestMessage> li) {
            for (TestMessage e : li) {
                addLi(e);
            }
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public InvokeIt immutable() {
            throw new UnsupportedOperationException("method not supported");
        }

        /** Returns a JSON representation of this message */
        public String toJSON() {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    /** A serializer for reading and writing instances of InvokeIt. */
    static class InvokeItSerializer extends MessageSerializer<InvokeIt> {

        /** {@inheritDoc} */
        @Override
        public InvokeIt read(MessageReader reader) throws IOException {
            return new InvokeIt(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(InvokeIt message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }
}
