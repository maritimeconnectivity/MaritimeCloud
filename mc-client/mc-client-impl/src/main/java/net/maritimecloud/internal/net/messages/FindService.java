package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.message.util.Hashing;

public class FindService implements Message, net.maritimecloud.internal.net.messages.spi.RequestMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<FindService> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long replyTo;

    /** Hey */
    private String serviceName;

    /** Hey */
    private Integer meters;

    /** Hey */
    private Integer max;

    /** Creates a new FindService. */
    public FindService() {}

    /**
     * Creates a new FindService by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    FindService(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.replyTo = reader.readInt64(3, "replyTo", null);
        this.serviceName = reader.readString(4, "serviceName", null);
        this.meters = reader.readInt32(5, "meters", null);
        this.max = reader.readInt32(6, "max", null);
    }

    /**
     * Creates a new FindService by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    FindService(FindService instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.replyTo = instance.replyTo;
        this.serviceName = instance.serviceName;
        this.meters = instance.meters;
        this.max = instance.max;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.replyTo);
        result = 31 * result + Hashing.hashcode(this.serviceName);
        result = 31 * result + Hashing.hashcode(this.meters);
        return 31 * result + Hashing.hashcode(this.max);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof FindService) {
            FindService o = (FindService) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(replyTo, o.replyTo) &&
                   Objects.equals(serviceName, o.serviceName) &&
                   Objects.equals(meters, o.meters) &&
                   Objects.equals(max, o.max);
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
        w.writeInt32(5, "meters", meters);
        w.writeInt32(6, "max", max);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public FindService setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public FindService setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public boolean hasReplyTo() {
        return replyTo != null;
    }

    public FindService setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean hasServiceName() {
        return serviceName != null;
    }

    public FindService setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public Integer getMeters() {
        return meters;
    }

    public boolean hasMeters() {
        return meters != null;
    }

    public FindService setMeters(Integer meters) {
        this.meters = meters;
        return this;
    }

    public Integer getMax() {
        return max;
    }

    public boolean hasMax() {
        return max != null;
    }

    public FindService setMax(Integer max) {
        this.max = max;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public FindService immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of FindService. */
    static class Parser extends MessageParser<FindService> {

        /** {@inheritDoc} */
        @Override
        public FindService parse(MessageReader reader) throws IOException {
            return new FindService(reader);
        }
    }

    /** An immutable version of FindService. */
    static class Immutable extends FindService {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(FindService instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public FindService immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public FindService setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindService setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindService setReplyTo(Long replyTo) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindService setServiceName(String serviceName) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindService setMeters(Integer meters) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindService setMax(Integer max) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
