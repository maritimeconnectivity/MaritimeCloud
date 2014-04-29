package net.maritimecloud.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.message.util.MessageHelper;
import net.maritimecloud.internal.util.Hashing;

public class BroadcastRelay implements Message, net.maritimecloud.internal.messages.ConnectionMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<BroadcastRelay> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long requestId;

    /** Hey */
    private String senderId;

    /** Hey */
    private net.maritimecloud.util.geometry.PositionTime senderPosition;

    /** Hey */
    private String broadcastId;

    /** Hey */
    private String msg;

    /** Creates a new BroadcastRelay. */
    public BroadcastRelay() {}

    /**
     * Creates a new BroadcastRelay by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    BroadcastRelay(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.requestId = reader.readInt64(3, "requestId", null);
        this.senderId = reader.readString(4, "senderId", null);
        this.senderPosition = reader.readMessage(5, "senderPosition", net.maritimecloud.util.geometry.PositionTime.PARSER);
        this.broadcastId = reader.readString(6, "broadcastId", null);
        this.msg = reader.readString(7, "msg", null);
    }

    /**
     * Creates a new BroadcastRelay by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    BroadcastRelay(BroadcastRelay instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.requestId = instance.requestId;
        this.senderId = instance.senderId;
        this.senderPosition = MessageHelper.immutable(instance.senderPosition);
        this.broadcastId = instance.broadcastId;
        this.msg = instance.msg;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.requestId);
        result = 31 * result + Hashing.hashcode(this.senderId);
        result = 31 * result + Hashing.hashcode(this.senderPosition);
        result = 31 * result + Hashing.hashcode(this.broadcastId);
        return 31 * result + Hashing.hashcode(this.msg);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof BroadcastRelay) {
            BroadcastRelay o = (BroadcastRelay) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(requestId, o.requestId) &&
                   Objects.equals(senderId, o.senderId) &&
                   Objects.equals(senderPosition, o.senderPosition) &&
                   Objects.equals(broadcastId, o.broadcastId) &&
                   Objects.equals(msg, o.msg);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeInt64(3, "requestId", requestId);
        w.writeString(4, "senderId", senderId);
        w.writeMessage(5, "senderPosition", senderPosition);
        w.writeString(6, "broadcastId", broadcastId);
        w.writeString(7, "msg", msg);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public BroadcastRelay setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public BroadcastRelay setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getRequestId() {
        return requestId;
    }

    public boolean hasRequestId() {
        return requestId != null;
    }

    public BroadcastRelay setRequestId(Long requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public boolean hasSenderId() {
        return senderId != null;
    }

    public BroadcastRelay setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public net.maritimecloud.util.geometry.PositionTime getSenderPosition() {
        return senderPosition;
    }

    public boolean hasSenderPosition() {
        return senderPosition != null;
    }

    public BroadcastRelay setSenderPosition(net.maritimecloud.util.geometry.PositionTime senderPosition) {
        this.senderPosition = senderPosition;
        return this;
    }

    public String getBroadcastId() {
        return broadcastId;
    }

    public boolean hasBroadcastId() {
        return broadcastId != null;
    }

    public BroadcastRelay setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public boolean hasMsg() {
        return msg != null;
    }

    public BroadcastRelay setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastRelay immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of BroadcastRelay. */
    static class Parser extends MessageParser<BroadcastRelay> {

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay parse(MessageReader reader) throws IOException {
            return new BroadcastRelay(reader);
        }
    }

    /** An immutable version of BroadcastRelay. */
    static class Immutable extends BroadcastRelay {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(BroadcastRelay instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay setRequestId(Long requestId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay setSenderId(String senderId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay setSenderPosition(net.maritimecloud.util.geometry.PositionTime senderPosition) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay setBroadcastId(String broadcastId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay setMsg(String msg) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
