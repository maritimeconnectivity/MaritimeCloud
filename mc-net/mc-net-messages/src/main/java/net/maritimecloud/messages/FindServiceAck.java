package net.maritimecloud.messages;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.core.message.ValueParser;
import net.maritimecloud.internal.message.util.MessageHelper;
import net.maritimecloud.internal.util.Hashing;

public class FindServiceAck implements Message, net.maritimecloud.internal.messages.ReplyMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<FindServiceAck> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long replyId;

    /** Hey */
    private final List<String> remoteIDS;

    /** Hey */
    private Integer errorCode;

    /** Creates a new FindServiceAck. */
    public FindServiceAck() {
        remoteIDS = new java.util.ArrayList<>();
    }

    /**
     * Creates a new FindServiceAck by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    FindServiceAck(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.replyId = reader.readInt64(3, "replyId", null);
        this.remoteIDS = MessageHelper.readList(reader, 4, "remoteIDS", ValueParser.STRING);
        this.errorCode = reader.readInt32(5, "errorCode", null);
    }

    /**
     * Creates a new FindServiceAck by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    FindServiceAck(FindServiceAck instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.replyId = instance.replyId;
        this.remoteIDS = MessageHelper.immutableCopy(instance.remoteIDS);
        this.errorCode = instance.errorCode;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.replyId);
        result = 31 * result + Hashing.hashcode(this.remoteIDS);
        return 31 * result + Hashing.hashcode(this.errorCode);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof FindServiceAck) {
            FindServiceAck o = (FindServiceAck) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(replyId, o.replyId) &&
                   Objects.equals(remoteIDS, o.remoteIDS) &&
                   Objects.equals(errorCode, o.errorCode);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeInt64(3, "replyId", replyId);
        w.writeList(4, "remoteIDS", remoteIDS);
        w.writeInt32(5, "errorCode", errorCode);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public FindServiceAck setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public FindServiceAck setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getReplyId() {
        return replyId;
    }

    public boolean hasReplyId() {
        return replyId != null;
    }

    public FindServiceAck setReplyId(Long replyId) {
        this.replyId = replyId;
        return this;
    }

    public List<String> getRemoteIDS() {
        return java.util.Collections.unmodifiableList(remoteIDS);
    }

    public boolean hasRemoteIDS() {
        return remoteIDS != null;
    }

    public FindServiceAck addRemoteIDS(String remoteIDS) {
        java.util.Objects.requireNonNull(remoteIDS, "remoteIDS is null");
        this.remoteIDS.add(remoteIDS);
        return this;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public boolean hasErrorCode() {
        return errorCode != null;
    }

    public FindServiceAck setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public FindServiceAck immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of FindServiceAck. */
    static class Parser extends MessageParser<FindServiceAck> {

        /** {@inheritDoc} */
        @Override
        public FindServiceAck parse(MessageReader reader) throws IOException {
            return new FindServiceAck(reader);
        }
    }

    /** An immutable version of FindServiceAck. */
    static class Immutable extends FindServiceAck {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(FindServiceAck instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public FindServiceAck immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public FindServiceAck setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindServiceAck setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindServiceAck setReplyId(Long replyId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindServiceAck addRemoteIDS(String remoteIDS) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindServiceAck setErrorCode(Integer errorCode) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
