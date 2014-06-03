package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.message.util.Hashing;
import net.maritimecloud.internal.message.util.MessageHelper;

public class BroadcastPublish implements Message, net.maritimecloud.internal.net.messages.spi.RequestMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<BroadcastPublish> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long replyTo;

    /** Hey */
    private net.maritimecloud.util.geometry.Area area;

    /** Hey */
    private net.maritimecloud.util.geometry.PositionTime positionTime;

    /** Hey */
    private Boolean receiverAck;

    /** Hey */
    private String channel;

    /** Hey */
    private String msg;

    /** Hey */
    private String id;

    /** Creates a new BroadcastPublish. */
    public BroadcastPublish() {}

    /**
     * Creates a new BroadcastPublish by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    BroadcastPublish(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.replyTo = reader.readInt64(3, "replyTo", null);
        this.area = reader.readMessage(4, "area", net.maritimecloud.util.geometry.Area.PARSER);
        this.positionTime = reader.readMessage(5, "positionTime", net.maritimecloud.util.geometry.PositionTime.PARSER);
        this.receiverAck = reader.readBool(6, "receiverAck", null);
        this.channel = reader.readString(7, "channel", null);
        this.msg = reader.readString(8, "msg", null);
        this.id = reader.readString(9, "id", null);
    }

    /**
     * Creates a new BroadcastPublish by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    BroadcastPublish(BroadcastPublish instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.replyTo = instance.replyTo;
        this.area = MessageHelper.immutable(instance.area);
        this.positionTime = MessageHelper.immutable(instance.positionTime);
        this.receiverAck = instance.receiverAck;
        this.channel = instance.channel;
        this.msg = instance.msg;
        this.id = instance.id;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.replyTo);
        result = 31 * result + Hashing.hashcode(this.area);
        result = 31 * result + Hashing.hashcode(this.positionTime);
        result = 31 * result + Hashing.hashcode(this.receiverAck);
        result = 31 * result + Hashing.hashcode(this.channel);
        result = 31 * result + Hashing.hashcode(this.msg);
        return 31 * result + Hashing.hashcode(this.id);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof BroadcastPublish) {
            BroadcastPublish o = (BroadcastPublish) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(replyTo, o.replyTo) &&
                   Objects.equals(area, o.area) &&
                   Objects.equals(positionTime, o.positionTime) &&
                   Objects.equals(receiverAck, o.receiverAck) &&
                   Objects.equals(channel, o.channel) &&
                   Objects.equals(msg, o.msg) &&
                   Objects.equals(id, o.id);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeInt64(3, "replyTo", replyTo);
        w.writeMessage(4, "area", area);
        w.writeMessage(5, "positionTime", positionTime);
        w.writeBool(6, "receiverAck", receiverAck);
        w.writeString(7, "channel", channel);
        w.writeString(8, "msg", msg);
        w.writeString(9, "id", id);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public BroadcastPublish setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public BroadcastPublish setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public boolean hasReplyTo() {
        return replyTo != null;
    }

    public BroadcastPublish setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public net.maritimecloud.util.geometry.Area getArea() {
        return area;
    }

    public boolean hasArea() {
        return area != null;
    }

    public BroadcastPublish setArea(net.maritimecloud.util.geometry.Area area) {
        this.area = area;
        return this;
    }

    public net.maritimecloud.util.geometry.PositionTime getPositionTime() {
        return positionTime;
    }

    public boolean hasPositionTime() {
        return positionTime != null;
    }

    public BroadcastPublish setPositionTime(net.maritimecloud.util.geometry.PositionTime positionTime) {
        this.positionTime = positionTime;
        return this;
    }

    public Boolean getReceiverAck() {
        return receiverAck;
    }

    public boolean hasReceiverAck() {
        return receiverAck != null;
    }

    public BroadcastPublish setReceiverAck(Boolean receiverAck) {
        this.receiverAck = receiverAck;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public boolean hasChannel() {
        return channel != null;
    }

    public BroadcastPublish setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public boolean hasMsg() {
        return msg != null;
    }

    public BroadcastPublish setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getId() {
        return id;
    }

    public boolean hasId() {
        return id != null;
    }

    public BroadcastPublish setId(String id) {
        this.id = id;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static BroadcastPublish fromJSON(CharSequence c) {
        return MessageSerializers.readFromJSON(PARSER, c);
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastPublish immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of BroadcastPublish. */
    static class Parser extends MessageParser<BroadcastPublish> {

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish parse(MessageReader reader) throws IOException {
            return new BroadcastPublish(reader);
        }
    }

    /** An immutable version of BroadcastPublish. */
    static class Immutable extends BroadcastPublish {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(BroadcastPublish instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setReplyTo(Long replyTo) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setArea(net.maritimecloud.util.geometry.Area area) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setPositionTime(net.maritimecloud.util.geometry.PositionTime positionTime) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setReceiverAck(Boolean receiverAck) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setChannel(String channel) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setMsg(String msg) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setId(String id) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
