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

public class BroadcastPublicRemoteAck implements Message, net.maritimecloud.internal.net.messages.spi.ConnectionMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<BroadcastPublicRemoteAck> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private String id;

    /** Hey */
    private net.maritimecloud.util.geometry.PositionTime positionTime;

    /** Hey */
    private Long broadcastId;

    /** Creates a new BroadcastPublicRemoteAck. */
    public BroadcastPublicRemoteAck() {}

    /**
     * Creates a new BroadcastPublicRemoteAck by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    BroadcastPublicRemoteAck(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.id = reader.readString(3, "id", null);
        this.positionTime = reader.readMessage(4, "positionTime", net.maritimecloud.util.geometry.PositionTime.PARSER);
        this.broadcastId = reader.readInt64(5, "broadcastId", null);
    }

    /**
     * Creates a new BroadcastPublicRemoteAck by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    BroadcastPublicRemoteAck(BroadcastPublicRemoteAck instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.id = instance.id;
        this.positionTime = MessageHelper.immutable(instance.positionTime);
        this.broadcastId = instance.broadcastId;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.id);
        result = 31 * result + Hashing.hashcode(this.positionTime);
        return 31 * result + Hashing.hashcode(this.broadcastId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof BroadcastPublicRemoteAck) {
            BroadcastPublicRemoteAck o = (BroadcastPublicRemoteAck) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(id, o.id) &&
                   Objects.equals(positionTime, o.positionTime) &&
                   Objects.equals(broadcastId, o.broadcastId);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeString(3, "id", id);
        w.writeMessage(4, "positionTime", positionTime);
        w.writeInt64(5, "broadcastId", broadcastId);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public BroadcastPublicRemoteAck setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public BroadcastPublicRemoteAck setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public String getId() {
        return id;
    }

    public boolean hasId() {
        return id != null;
    }

    public BroadcastPublicRemoteAck setId(String id) {
        this.id = id;
        return this;
    }

    public net.maritimecloud.util.geometry.PositionTime getPositionTime() {
        return positionTime;
    }

    public boolean hasPositionTime() {
        return positionTime != null;
    }

    public BroadcastPublicRemoteAck setPositionTime(net.maritimecloud.util.geometry.PositionTime positionTime) {
        this.positionTime = positionTime;
        return this;
    }

    public Long getBroadcastId() {
        return broadcastId;
    }

    public boolean hasBroadcastId() {
        return broadcastId != null;
    }

    public BroadcastPublicRemoteAck setBroadcastId(Long broadcastId) {
        this.broadcastId = broadcastId;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastPublicRemoteAck immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of BroadcastPublicRemoteAck. */
    static class Parser extends MessageParser<BroadcastPublicRemoteAck> {

        /** {@inheritDoc} */
        @Override
        public BroadcastPublicRemoteAck parse(MessageReader reader) throws IOException {
            return new BroadcastPublicRemoteAck(reader);
        }
    }

    /** An immutable version of BroadcastPublicRemoteAck. */
    static class Immutable extends BroadcastPublicRemoteAck {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(BroadcastPublicRemoteAck instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublicRemoteAck immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublicRemoteAck setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublicRemoteAck setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublicRemoteAck setId(String id) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublicRemoteAck setPositionTime(net.maritimecloud.util.geometry.PositionTime positionTime) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublicRemoteAck setBroadcastId(Long broadcastId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
