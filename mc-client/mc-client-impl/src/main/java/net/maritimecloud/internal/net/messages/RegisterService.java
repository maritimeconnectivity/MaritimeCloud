package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.message.util.Hashing;

public class RegisterService implements Message, net.maritimecloud.internal.net.messages.spi.RequestMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<RegisterService> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long replyTo;

    /** Hey */
    private String serviceName;

    /** Creates a new RegisterService. */
    public RegisterService() {}

    /**
     * Creates a new RegisterService by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    RegisterService(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.replyTo = reader.readInt64(3, "replyTo", null);
        this.serviceName = reader.readString(4, "serviceName", null);
    }

    /**
     * Creates a new RegisterService by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    RegisterService(RegisterService instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.replyTo = instance.replyTo;
        this.serviceName = instance.serviceName;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.replyTo);
        return 31 * result + Hashing.hashcode(this.serviceName);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof RegisterService) {
            RegisterService o = (RegisterService) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(replyTo, o.replyTo) &&
                   Objects.equals(serviceName, o.serviceName);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeInt64(3, "replyTo", replyTo);
        w.writeString(4, "serviceName", serviceName);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public RegisterService setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public RegisterService setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public boolean hasReplyTo() {
        return replyTo != null;
    }

    public RegisterService setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean hasServiceName() {
        return serviceName != null;
    }

    public RegisterService setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static RegisterService fromJSON(CharSequence c) {
        return MessageSerializers.readFromJSON(PARSER, c);
    }

    /** {@inheritDoc} */
    @Override
    public RegisterService immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of RegisterService. */
    static class Parser extends MessageParser<RegisterService> {

        /** {@inheritDoc} */
        @Override
        public RegisterService parse(MessageReader reader) throws IOException {
            return new RegisterService(reader);
        }
    }

    /** An immutable version of RegisterService. */
    static class Immutable extends RegisterService {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(RegisterService instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public RegisterService immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public RegisterService setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public RegisterService setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public RegisterService setReplyTo(Long replyTo) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public RegisterService setServiceName(String serviceName) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
