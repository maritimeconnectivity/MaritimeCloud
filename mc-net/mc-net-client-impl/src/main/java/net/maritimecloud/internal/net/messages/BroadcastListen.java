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

public class BroadcastListen implements Message, net.maritimecloud.internal.net.messages.spi.RequestMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<BroadcastListen> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long replyTo;

    /** Hey */
    private net.maritimecloud.util.geometry.Area area;

    /** Hey */
    private String channel;

    /** Creates a new BroadcastListen. */
    public BroadcastListen() {}

    /**
     * Creates a new BroadcastListen by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    BroadcastListen(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.replyTo = reader.readInt64(3, "replyTo", null);
        this.area = reader.readMessage(4, "area", net.maritimecloud.util.geometry.Area.PARSER);
        this.channel = reader.readString(5, "channel", null);
    }

    /**
     * Creates a new BroadcastListen by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    BroadcastListen(BroadcastListen instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.replyTo = instance.replyTo;
        this.area = MessageHelper.immutable(instance.area);
        this.channel = instance.channel;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.replyTo);
        result = 31 * result + Hashing.hashcode(this.area);
        return 31 * result + Hashing.hashcode(this.channel);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof BroadcastListen) {
            BroadcastListen o = (BroadcastListen) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(replyTo, o.replyTo) &&
                   Objects.equals(area, o.area) &&
                   Objects.equals(channel, o.channel);
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
        w.writeString(5, "channel", channel);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public BroadcastListen setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public BroadcastListen setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public boolean hasReplyTo() {
        return replyTo != null;
    }

    public BroadcastListen setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public net.maritimecloud.util.geometry.Area getArea() {
        return area;
    }

    public boolean hasArea() {
        return area != null;
    }

    public BroadcastListen setArea(net.maritimecloud.util.geometry.Area area) {
        this.area = area;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public boolean hasChannel() {
        return channel != null;
    }

    public BroadcastListen setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastListen immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of BroadcastListen. */
    static class Parser extends MessageParser<BroadcastListen> {

        /** {@inheritDoc} */
        @Override
        public BroadcastListen parse(MessageReader reader) throws IOException {
            return new BroadcastListen(reader);
        }
    }

    /** An immutable version of BroadcastListen. */
    static class Immutable extends BroadcastListen {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(BroadcastListen instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastListen immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastListen setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastListen setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastListen setReplyTo(Long replyTo) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastListen setArea(net.maritimecloud.util.geometry.Area area) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastListen setChannel(String channel) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
