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
    private String id;

    /** Hey */
    private net.maritimecloud.util.geometry.PositionTime positionTime;

    /** Hey */
    private String channel;

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
        this.id = reader.readString(4, "id", null);
        this.positionTime = reader.readMessage(5, "positionTime", net.maritimecloud.util.geometry.PositionTime.PARSER);
        this.channel = reader.readString(6, "channel", null);
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
        this.id = instance.id;
        this.positionTime = MessageHelper.immutable(instance.positionTime);
        this.channel = instance.channel;
        this.msg = instance.msg;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.requestId);
        result = 31 * result + Hashing.hashcode(this.id);
        result = 31 * result + Hashing.hashcode(this.positionTime);
        result = 31 * result + Hashing.hashcode(this.channel);
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
                   Objects.equals(id, o.id) &&
                   Objects.equals(positionTime, o.positionTime) &&
                   Objects.equals(channel, o.channel) &&
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
        w.writeString(4, "id", id);
        w.writeMessage(5, "positionTime", positionTime);
        w.writeString(6, "channel", channel);
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

    public String getId() {
        return id;
    }

    public boolean hasId() {
        return id != null;
    }

    public BroadcastRelay setId(String id) {
        this.id = id;
        return this;
    }

    public net.maritimecloud.util.geometry.PositionTime getPositionTime() {
        return positionTime;
    }

    public boolean hasPositionTime() {
        return positionTime != null;
    }

    public BroadcastRelay setPositionTime(net.maritimecloud.util.geometry.PositionTime positionTime) {
        this.positionTime = positionTime;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public boolean hasChannel() {
        return channel != null;
    }

    public BroadcastRelay setChannel(String channel) {
        this.channel = channel;
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
        public BroadcastRelay setId(String id) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay setPositionTime(net.maritimecloud.util.geometry.PositionTime positionTime) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay setChannel(String channel) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastRelay setMsg(String msg) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
