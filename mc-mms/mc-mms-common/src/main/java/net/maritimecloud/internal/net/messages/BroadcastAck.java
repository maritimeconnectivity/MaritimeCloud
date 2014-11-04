package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

public class BroadcastAck implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.net.messages.BroadcastAck";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<BroadcastAck> SERIALIZER = new Serializer();

    /** Field definition. */
    private Binary messageId;

    /** Field definition. */
    private Binary ackForMessageId;

    /** Field definition. */
    private String originalSenderId;

    /** Field definition. */
    private String receiverId;

    /** Field definition. */
    private Timestamp receiverTimestamp;

    /** Field definition. */
    private Position receiverPosition;

    /** Field definition. */
    private Binary signature;

    /** Creates a new BroadcastAck. */
    public BroadcastAck() {}

    /**
     * Creates a new BroadcastAck by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    BroadcastAck(MessageReader reader) throws IOException {
        this.messageId = reader.readBinary(1, "messageId", null);
        this.ackForMessageId = reader.readBinary(2, "ackForMessageId", null);
        this.originalSenderId = reader.readText(3, "originalSenderId", null);
        this.receiverId = reader.readText(4, "receiverId", null);
        this.receiverTimestamp = reader.readTimestamp(5, "receiverTimestamp", null);
        this.receiverPosition = reader.readPosition(6, "receiverPosition", null);
        this.signature = reader.readBinary(15, "signature", null);
    }

    /**
     * Creates a new BroadcastAck by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    BroadcastAck(BroadcastAck instance) {
        this.messageId = instance.messageId;
        this.ackForMessageId = instance.ackForMessageId;
        this.originalSenderId = instance.originalSenderId;
        this.receiverId = instance.receiverId;
        this.receiverTimestamp = instance.receiverTimestamp;
        this.receiverPosition = instance.receiverPosition;
        this.signature = instance.signature;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeBinary(1, "messageId", messageId);
        w.writeBinary(2, "ackForMessageId", ackForMessageId);
        w.writeText(3, "originalSenderId", originalSenderId);
        w.writeText(4, "receiverId", receiverId);
        w.writeTimestamp(5, "receiverTimestamp", receiverTimestamp);
        w.writePosition(6, "receiverPosition", receiverPosition);
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

    public BroadcastAck setMessageId(Binary messageId) {
        this.messageId = messageId;
        return this;
    }

    /** Returns the broadcast id of the broadcast being acknowledged. */
    public Binary getAckForMessageId() {
        return ackForMessageId;
    }

    public boolean hasAckForMessageId() {
        return ackForMessageId != null;
    }

    public BroadcastAck setAckForMessageId(Binary ackForMessageId) {
        this.ackForMessageId = ackForMessageId;
        return this;
    }

    /** Returns the maritime id of the original sender of the broadcast (optional). */
    public String getOriginalSenderId() {
        return originalSenderId;
    }

    public boolean hasOriginalSenderId() {
        return originalSenderId != null;
    }

    public BroadcastAck setOriginalSenderId(String originalSenderId) {
        this.originalSenderId = originalSenderId;
        return this;
    }

    /** Returns the maritime id of the receiver of the broadcast. */
    public String getReceiverId() {
        return receiverId;
    }

    public boolean hasReceiverId() {
        return receiverId != null;
    }

    public BroadcastAck setReceiverId(String receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    /** Returns the local time at the receiver when the broadcast was received (optional). */
    public Timestamp getReceiverTimestamp() {
        return receiverTimestamp;
    }

    public boolean hasReceiverTimestamp() {
        return receiverTimestamp != null;
    }

    public BroadcastAck setReceiverTimestamp(Timestamp receiverTimestamp) {
        this.receiverTimestamp = receiverTimestamp;
        return this;
    }

    /** Returns the position of the receiver when the broadcast was received (optional). */
    public Position getReceiverPosition() {
        return receiverPosition;
    }

    public boolean hasReceiverPosition() {
        return receiverPosition != null;
    }

    public BroadcastAck setReceiverPosition(Position receiverPosition) {
        this.receiverPosition = receiverPosition;
        return this;
    }

    /** Returns signature of this message by the receiver of the broadcast (optional). */
    public Binary getSignature() {
        return signature;
    }

    public boolean hasSignature() {
        return signature != null;
    }

    public BroadcastAck setSignature(Binary signature) {
        this.signature = signature;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastAck immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static BroadcastAck fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.ackForMessageId);
        result = 31 * result + Hashing.hashcode(this.originalSenderId);
        result = 31 * result + Hashing.hashcode(this.receiverId);
        result = 31 * result + Hashing.hashcode(this.receiverTimestamp);
        result = 31 * result + Hashing.hashcode(this.receiverPosition);
        return 31 * result + Hashing.hashcode(this.signature);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof BroadcastAck) {
            BroadcastAck o = (BroadcastAck) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(ackForMessageId, o.ackForMessageId) &&
                   Objects.equals(originalSenderId, o.originalSenderId) &&
                   Objects.equals(receiverId, o.receiverId) &&
                   Objects.equals(receiverTimestamp, o.receiverTimestamp) &&
                   Objects.equals(receiverPosition, o.receiverPosition) &&
                   Objects.equals(signature, o.signature);
        }
        return false;
    }

    /** A serializer for reading and writing instances of BroadcastAck. */
    static class Serializer extends MessageSerializer<BroadcastAck> {

        /** {@inheritDoc} */
        @Override
        public BroadcastAck read(MessageReader reader) throws IOException {
            return new BroadcastAck(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(BroadcastAck message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of BroadcastAck. */
    static class Immutable extends BroadcastAck {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(BroadcastAck instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastAck immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastAck setMessageId(Binary messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastAck setAckForMessageId(Binary ackForMessageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastAck setOriginalSenderId(String originalSenderId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastAck setReceiverId(String receiverId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastAck setReceiverTimestamp(Timestamp receiverTimestamp) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastAck setReceiverPosition(Position receiverPosition) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastAck setSignature(Binary signature) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
