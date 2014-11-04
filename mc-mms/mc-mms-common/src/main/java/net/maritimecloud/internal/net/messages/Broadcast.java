package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

public class Broadcast implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.net.messages.Broadcast";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<Broadcast> SERIALIZER = new Serializer();

    /** Field definition. */
    private Binary messageId;

    /** Field definition. */
    private String broadcastType;

    /** Field definition. */
    private String senderId;

    /** Field definition. */
    private Timestamp senderTimestamp;

    /** Field definition. */
    private Position senderPosition;

    /** Field definition. */
    private Binary payload;

    /** Field definition. */
    private Boolean ackBroadcast;

    /** Field definition. */
    private net.maritimecloud.util.geometry.Area area;

    /** Field definition. */
    private Integer radius;

    /** Field definition. */
    private Binary signature;

    /** Creates a new Broadcast. */
    public Broadcast() {}

    /**
     * Creates a new Broadcast by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    Broadcast(MessageReader reader) throws IOException {
        this.messageId = reader.readBinary(1, "messageId", null);
        this.broadcastType = reader.readText(2, "broadcastType", null);
        this.senderId = reader.readText(3, "senderId", null);
        this.senderTimestamp = reader.readTimestamp(4, "senderTimestamp", null);
        this.senderPosition = reader.readPosition(5, "senderPosition", null);
        this.payload = reader.readBinary(6, "payload", null);
        this.ackBroadcast = reader.readBoolean(7, "ackBroadcast", null);
        this.area = reader.readMessage(10, "area", net.maritimecloud.util.geometry.Area.SERIALIZER);
        this.radius = reader.readInt(11, "radius", null);
        this.signature = reader.readBinary(15, "signature", null);
    }

    /**
     * Creates a new Broadcast by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    Broadcast(Broadcast instance) {
        this.messageId = instance.messageId;
        this.broadcastType = instance.broadcastType;
        this.senderId = instance.senderId;
        this.senderTimestamp = instance.senderTimestamp;
        this.senderPosition = instance.senderPosition;
        this.payload = instance.payload;
        this.ackBroadcast = instance.ackBroadcast;
        this.area = MessageHelper.immutable(instance.area);
        this.radius = instance.radius;
        this.signature = instance.signature;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeBinary(1, "messageId", messageId);
        w.writeText(2, "broadcastType", broadcastType);
        w.writeText(3, "senderId", senderId);
        w.writeTimestamp(4, "senderTimestamp", senderTimestamp);
        w.writePosition(5, "senderPosition", senderPosition);
        w.writeBinary(6, "payload", payload);
        w.writeBoolean(7, "ackBroadcast", ackBroadcast);
        w.writeMessage(10, "area", area, net.maritimecloud.util.geometry.Area.SERIALIZER);
        w.writeInt(11, "radius", radius);
        w.writeBinary(15, "signature", signature);
    }

    /**
     * Returns the id (hash) of this message (Optional, can be calculated from the rest of the fields).
     */
    public Binary getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public Broadcast setMessageId(Binary messageId) {
        this.messageId = messageId;
        return this;
    }

    /** Returns the type of broadcast as the defined in the maritime service registry. */
    public String getBroadcastType() {
        return broadcastType;
    }

    public boolean hasBroadcastType() {
        return broadcastType != null;
    }

    public Broadcast setBroadcastType(String broadcastType) {
        this.broadcastType = broadcastType;
        return this;
    }

    /** Returns the id of the sender of the broadcast. */
    public String getSenderId() {
        return senderId;
    }

    public boolean hasSenderId() {
        return senderId != null;
    }

    public Broadcast setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    /** Returns the current time of the sender when sending the broadcast. */
    public Timestamp getSenderTimestamp() {
        return senderTimestamp;
    }

    public boolean hasSenderTimestamp() {
        return senderTimestamp != null;
    }

    public Broadcast setSenderTimestamp(Timestamp senderTimestamp) {
        this.senderTimestamp = senderTimestamp;
        return this;
    }

    /** Returns the current position of the sender when sending the broadcast (Optional). */
    public Position getSenderPosition() {
        return senderPosition;
    }

    public boolean hasSenderPosition() {
        return senderPosition != null;
    }

    public Broadcast setSenderPosition(Position senderPosition) {
        this.senderPosition = senderPosition;
        return this;
    }

    /** Returns the actual broadcast message to deliver. */
    public Binary getPayload() {
        return payload;
    }

    public boolean hasPayload() {
        return payload != null;
    }

    public Broadcast setPayload(Binary payload) {
        this.payload = payload;
        return this;
    }

    /**
     * Returns whether or not receivers of the broadcast should acknowledgement it by sending a BroadcastAck message. Default false (Optional)
     */
    public Boolean getAckBroadcast() {
        return ackBroadcast;
    }

    public boolean hasAckBroadcast() {
        return ackBroadcast != null;
    }

    public Broadcast setAckBroadcast(Boolean ackBroadcast) {
        this.ackBroadcast = ackBroadcast;
        return this;
    }

    /** Returns the area to deliver the broadcast in. (Optional) */
    public net.maritimecloud.util.geometry.Area getArea() {
        return area;
    }

    public boolean hasArea() {
        return area != null;
    }

    public Broadcast setArea(net.maritimecloud.util.geometry.Area area) {
        this.area = area;
        return this;
    }

    /** Returns the radius to deliver the broadcast in. (Optional) */
    public Integer getRadius() {
        return radius;
    }

    public boolean hasRadius() {
        return radius != null;
    }

    public Broadcast setRadius(Integer radius) {
        this.radius = radius;
        return this;
    }

    /** Returns the signature of this message (optional). */
    public Binary getSignature() {
        return signature;
    }

    public boolean hasSignature() {
        return signature != null;
    }

    public Broadcast setSignature(Binary signature) {
        this.signature = signature;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Broadcast immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static Broadcast fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.broadcastType);
        result = 31 * result + Hashing.hashcode(this.senderId);
        result = 31 * result + Hashing.hashcode(this.senderTimestamp);
        result = 31 * result + Hashing.hashcode(this.senderPosition);
        result = 31 * result + Hashing.hashcode(this.payload);
        result = 31 * result + Hashing.hashcode(this.ackBroadcast);
        result = 31 * result + Hashing.hashcode(this.area);
        result = 31 * result + Hashing.hashcode(this.radius);
        return 31 * result + Hashing.hashcode(this.signature);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Broadcast) {
            Broadcast o = (Broadcast) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(broadcastType, o.broadcastType) &&
                   Objects.equals(senderId, o.senderId) &&
                   Objects.equals(senderTimestamp, o.senderTimestamp) &&
                   Objects.equals(senderPosition, o.senderPosition) &&
                   Objects.equals(payload, o.payload) &&
                   Objects.equals(ackBroadcast, o.ackBroadcast) &&
                   Objects.equals(area, o.area) &&
                   Objects.equals(radius, o.radius) &&
                   Objects.equals(signature, o.signature);
        }
        return false;
    }

    /** A serializer for reading and writing instances of Broadcast. */
    static class Serializer extends MessageSerializer<Broadcast> {

        /** {@inheritDoc} */
        @Override
        public Broadcast read(MessageReader reader) throws IOException {
            return new Broadcast(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Broadcast message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of Broadcast. */
    static class Immutable extends Broadcast {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(Broadcast instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast setMessageId(Binary messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast setBroadcastType(String broadcastType) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast setSenderId(String senderId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast setSenderTimestamp(Timestamp senderTimestamp) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast setSenderPosition(Position senderPosition) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast setPayload(Binary payload) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast setAckBroadcast(Boolean ackBroadcast) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast setArea(net.maritimecloud.util.geometry.Area area) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast setRadius(Integer radius) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Broadcast setSignature(Binary signature) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
