package net.maritimecloud.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.util.Hashing;

public class Connected implements Message, net.maritimecloud.internal.messages.TransportMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<Connected> PARSER = new Parser();

    /** Hey */
    private String connectionId;

    /** Hey */
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
        this.connectionId = reader.readString(1, "connectionId", null);
        this.lastReceivedMessageId = reader.readInt64(2, "lastReceivedMessageId", null);
    }

    /**
     * Creates a new Connected by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    Connected(Connected instance) {
        this.connectionId = instance.connectionId;
        this.lastReceivedMessageId = instance.lastReceivedMessageId;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.connectionId);
        return 31 * result + Hashing.hashcode(this.lastReceivedMessageId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Connected) {
            Connected o = (Connected) other;
            return Objects.equals(connectionId, o.connectionId) &&
                   Objects.equals(lastReceivedMessageId, o.lastReceivedMessageId);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeString(1, "connectionId", connectionId);
        w.writeInt64(2, "lastReceivedMessageId", lastReceivedMessageId);
    }

    public String getConnectionId() {
        return connectionId;
    }

    public boolean hasConnectionId() {
        return connectionId != null;
    }

    public Connected setConnectionId(String connectionId) {
        this.connectionId = connectionId;
        return this;
    }

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

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public Connected immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of Connected. */
    static class Parser extends MessageParser<Connected> {

        /** {@inheritDoc} */
        @Override
        public Connected parse(MessageReader reader) throws IOException {
            return new Connected(reader);
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
        public Connected setConnectionId(String connectionId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Connected setLastReceivedMessageId(Long lastReceivedMessageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
