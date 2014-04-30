package net.maritimecloud.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.util.Hashing;

public class UnregisterService implements Message, net.maritimecloud.internal.messages.RequestMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<UnregisterService> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long replyTo;

    /** Hey */
    private String serviceName;

    /** Creates a new UnregisterService. */
    public UnregisterService() {}

    /**
     * Creates a new UnregisterService by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    UnregisterService(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.replyTo = reader.readInt64(3, "replyTo", null);
        this.serviceName = reader.readString(4, "serviceName", null);
    }

    /**
     * Creates a new UnregisterService by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    UnregisterService(UnregisterService instance) {
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
        } else if (other instanceof UnregisterService) {
            UnregisterService o = (UnregisterService) other;
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

    public UnregisterService setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public UnregisterService setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public boolean hasReplyTo() {
        return replyTo != null;
    }

    public UnregisterService setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean hasServiceName() {
        return serviceName != null;
    }

    public UnregisterService setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public UnregisterService immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of UnregisterService. */
    static class Parser extends MessageParser<UnregisterService> {

        /** {@inheritDoc} */
        @Override
        public UnregisterService parse(MessageReader reader) throws IOException {
            return new UnregisterService(reader);
        }
    }

    /** An immutable version of UnregisterService. */
    static class Immutable extends UnregisterService {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(UnregisterService instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterService immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterService setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterService setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterService setReplyTo(Long replyTo) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterService setServiceName(String serviceName) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
