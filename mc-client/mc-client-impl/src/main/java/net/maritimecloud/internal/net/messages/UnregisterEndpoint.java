package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.message.util.Hashing;

public class UnregisterEndpoint implements Message, net.maritimecloud.internal.net.messages.spi.RequestMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<UnregisterEndpoint> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long replyTo;

    /** Hey */
    private String EndpointName;

    /** Creates a new UnregisterEndpoint. */
    public UnregisterEndpoint() {}

    /**
     * Creates a new UnregisterEndpoint by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    UnregisterEndpoint(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.replyTo = reader.readInt64(3, "replyTo", null);
        this.EndpointName = reader.readString(4, "EndpointName", null);
    }

    /**
     * Creates a new UnregisterEndpoint by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    UnregisterEndpoint(UnregisterEndpoint instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.replyTo = instance.replyTo;
        this.EndpointName = instance.EndpointName;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.replyTo);
        return 31 * result + Hashing.hashcode(this.EndpointName);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof UnregisterEndpoint) {
            UnregisterEndpoint o = (UnregisterEndpoint) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(replyTo, o.replyTo) &&
                   Objects.equals(EndpointName, o.EndpointName);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeInt64(3, "replyTo", replyTo);
        w.writeString(4, "EndpointName", EndpointName);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public UnregisterEndpoint setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public UnregisterEndpoint setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public boolean hasReplyTo() {
        return replyTo != null;
    }

    public UnregisterEndpoint setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public String getEndpointName() {
        return EndpointName;
    }

    public boolean hasEndpointName() {
        return EndpointName != null;
    }

    public UnregisterEndpoint setEndpointName(String EndpointName) {
        this.EndpointName = EndpointName;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static UnregisterEndpoint fromJSON(CharSequence c) {
        return MessageSerializers.readFromJSON(PARSER, c);
    }

    /** {@inheritDoc} */
    @Override
    public UnregisterEndpoint immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of UnregisterEndpoint. */
    static class Parser extends MessageParser<UnregisterEndpoint> {

        /** {@inheritDoc} */
        @Override
        public UnregisterEndpoint parse(MessageReader reader) throws IOException {
            return new UnregisterEndpoint(reader);
        }
    }

    /** An immutable version of UnregisterEndpoint. */
    static class Immutable extends UnregisterEndpoint {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(UnregisterEndpoint instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterEndpoint immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterEndpoint setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterEndpoint setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterEndpoint setReplyTo(Long replyTo) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterEndpoint setEndpointName(String EndpointName) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
