package net.maritimecloud.mms.stubs;

import java.io.IOException;

import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.LocalEndpoint;

public final class ShipEndpoint extends LocalEndpoint {

    /** The name of the endpoint. */
    public static final String NAME = "ShipEndpoint";

    public ShipEndpoint(LocalEndpoint.Invocator ei) {
        super(ei);
    }

    public EndpointInvocationFuture<Void> hello(Long foo, String fff) {
        Hello arguments = new Hello();
        arguments.setFoo(foo);
        arguments.setFff(fff);
        return invokeRemote("ShipEndpoint.hello", arguments, Hello.SERIALIZER, null);
    }

    public EndpointInvocationFuture<Long> fff() {
        Fff arguments = new Fff();
        return invokeRemote("ShipEndpoint.fff", arguments, Fff.SERIALIZER, ValueSerializer.INT64);
    }

    public static class Hello implements Message {

        /** The full name of this message */
        public static final String NAME = "net.maritimecloud.mms.stubs.Hello";

        /** A message serializer that can read and write instances of this class. */
        public static final MessageSerializer<Hello> SERIALIZER = new HelloSerializer();

        /** Hey */
        private Long foo;

        /** Hey */
        private String fff;

        /** Creates a new Hello. */
        public Hello() {}

        /**
         * Creates a new Hello by reading from a message reader.
         *
         * @param reader
         *            the message reader
         */
        Hello(MessageReader reader) throws IOException {
            this.foo = reader.readInt64(1, "foo", null);
            this.fff = reader.readText(2, "fff", null);
        }

        void writeTo(MessageWriter w) throws IOException {
            w.writeInt64(1, "foo", foo);
            w.writeText(2, "fff", fff);
        }

        public Long getFoo() {
            return foo;
        }

        public boolean hasFoo() {
            return foo != null;
        }

        public Hello setFoo(Long foo) {
            this.foo = foo;
            return this;
        }

        public String getFff() {
            return fff;
        }

        public boolean hasFff() {
            return fff != null;
        }

        public Hello setFff(String fff) {
            this.fff = fff;
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

    public static class Fff implements Message {

        /** The full name of this message */
        public static final String NAME = "net.maritimecloud.mms.stubs.Fff";

        /** A message serializer that can read and write instances of this class. */
        public static final MessageSerializer<Fff> SERIALIZER = new FffSerializer();

        /** {@inheritDoc} */
        @Override
        public Fff immutable() {
            throw new UnsupportedOperationException("method not supported");
        }

        /** Returns a JSON representation of this message */
        public String toJSON() {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    /** A serializer for reading and writing instances of Fff. */
    static class FffSerializer extends MessageSerializer<Fff> {

        /** {@inheritDoc} */
        @Override
        public Fff read(MessageReader reader) throws IOException {
            return new Fff();
        }

        /** {@inheritDoc} */
        @Override
        public void write(Fff message, MessageWriter writer) throws IOException {}
    }
}
