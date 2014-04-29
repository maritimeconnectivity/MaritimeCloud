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

public class BroadcastPublish implements Message, net.maritimecloud.internal.messages.RequestMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<BroadcastPublish> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long requestId;

    /** Hey */
    private net.maritimecloud.util.geometry.Area area;

    /** Hey */
    private net.maritimecloud.util.geometry.PositionTime position;

    /** Hey */
    private Boolean receiveIndividualAcks;

    /** Hey */
    private String broadcastId;

    /** Hey */
    private String msg;

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
        this.requestId = reader.readInt64(3, "requestId", null);
        this.area = reader.readMessage(4, "area", net.maritimecloud.util.geometry.Area.PARSER);
        this.position = reader.readMessage(5, "position", net.maritimecloud.util.geometry.PositionTime.PARSER);
        this.receiveIndividualAcks = reader.readBool(6, "receiveIndividualAcks", null);
        this.broadcastId = reader.readString(7, "broadcastId", null);
        this.msg = reader.readString(8, "msg", null);
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
        this.requestId = instance.requestId;
        this.area = MessageHelper.immutable(instance.area);
        this.position = MessageHelper.immutable(instance.position);
        this.receiveIndividualAcks = instance.receiveIndividualAcks;
        this.broadcastId = instance.broadcastId;
        this.msg = instance.msg;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.requestId);
        result = 31 * result + Hashing.hashcode(this.area);
        result = 31 * result + Hashing.hashcode(this.position);
        result = 31 * result + Hashing.hashcode(this.receiveIndividualAcks);
        result = 31 * result + Hashing.hashcode(this.broadcastId);
        return 31 * result + Hashing.hashcode(this.msg);
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
                   Objects.equals(requestId, o.requestId) &&
                   Objects.equals(area, o.area) &&
                   Objects.equals(position, o.position) &&
                   Objects.equals(receiveIndividualAcks, o.receiveIndividualAcks) &&
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
        w.writeMessage(4, "area", area);
        w.writeMessage(5, "position", position);
        w.writeBool(6, "receiveIndividualAcks", receiveIndividualAcks);
        w.writeString(7, "broadcastId", broadcastId);
        w.writeString(8, "msg", msg);
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

    public Long getRequestId() {
        return requestId;
    }

    public boolean hasRequestId() {
        return requestId != null;
    }

    public BroadcastPublish setRequestId(Long requestId) {
        this.requestId = requestId;
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

    public net.maritimecloud.util.geometry.PositionTime getPosition() {
        return position;
    }

    public boolean hasPosition() {
        return position != null;
    }

    public BroadcastPublish setPosition(net.maritimecloud.util.geometry.PositionTime position) {
        this.position = position;
        return this;
    }

    public Boolean getReceiveIndividualAcks() {
        return receiveIndividualAcks;
    }

    public boolean hasReceiveIndividualAcks() {
        return receiveIndividualAcks != null;
    }

    public BroadcastPublish setReceiveIndividualAcks(Boolean receiveIndividualAcks) {
        this.receiveIndividualAcks = receiveIndividualAcks;
        return this;
    }

    public String getBroadcastId() {
        return broadcastId;
    }

    public boolean hasBroadcastId() {
        return broadcastId != null;
    }

    public BroadcastPublish setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
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

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
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
        public BroadcastPublish setRequestId(Long requestId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setArea(net.maritimecloud.util.geometry.Area area) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setPosition(net.maritimecloud.util.geometry.PositionTime position) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setReceiveIndividualAcks(Boolean receiveIndividualAcks) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setBroadcastId(String broadcastId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastPublish setMsg(String msg) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
