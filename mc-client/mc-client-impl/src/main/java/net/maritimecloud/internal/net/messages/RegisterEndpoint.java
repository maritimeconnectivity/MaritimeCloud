package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.message.util.Hashing;

public class RegisterEndpoint implements Message, net.maritimecloud.internal.net.messages.spi.RequestMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<RegisterEndpoint> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long replyTo;

    /** Hey */
    private String endpointName;

    /** Creates a new RegisterEndpoint. */
    public RegisterEndpoint() {}

    /**
     * Creates a new RegisterEndpoint by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    RegisterEndpoint(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.replyTo = reader.readInt64(3, "replyTo", null);
        this.endpointName = reader.readString(4, "endpointName", null);
    }

    /**
     * Creates a new RegisterEndpoint by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    RegisterEndpoint(RegisterEndpoint instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.replyTo = instance.replyTo;
        this.endpointName = instance.endpointName;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.replyTo);
        return 31 * result + Hashing.hashcode(this.endpointName);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof RegisterEndpoint) {
            RegisterEndpoint o = (RegisterEndpoint) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(replyTo, o.replyTo) &&
                   Objects.equals(endpointName, o.endpointName);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeInt64(3, "replyTo", replyTo);
        w.writeString(4, "endpointName", endpointName);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public RegisterEndpoint setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public RegisterEndpoint setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public boolean hasReplyTo() {
        return replyTo != null;
    }

    public RegisterEndpoint setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public boolean hasEndpointName() {
        return endpointName != null;
    }

    public RegisterEndpoint setEndpointName(String endpointName) {
        this.endpointName = endpointName;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static RegisterEndpoint fromJSON(CharSequence c) {
        return MessageSerializers.readFromJSON(PARSER, c);
    }

    /** {@inheritDoc} */
    @Override
    public RegisterEndpoint immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of RegisterEndpoint. */
    static class Parser extends MessageParser<RegisterEndpoint> {

        /** {@inheritDoc} */
        @Override
        public RegisterEndpoint parse(MessageReader reader) throws IOException {
            return new RegisterEndpoint(reader);
        }
    }

    /** An immutable version of RegisterEndpoint. */
    static class Immutable extends RegisterEndpoint {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(RegisterEndpoint instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public RegisterEndpoint immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public RegisterEndpoint setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public RegisterEndpoint setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public RegisterEndpoint setReplyTo(Long replyTo) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public RegisterEndpoint setEndpointName(String endpointName) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}