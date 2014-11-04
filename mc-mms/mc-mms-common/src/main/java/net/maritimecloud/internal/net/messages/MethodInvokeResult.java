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

public class MethodInvokeResult implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.net.messages.MethodInvokeResult";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<MethodInvokeResult> SERIALIZER = new Serializer();

    /** Field definition. */
    private Binary messageId;

    /** Field definition. */
    private Binary resultForMessageId;

    /** Field definition. */
    private String originalSenderId;

    /** Field definition. */
    private String receiverId;

    /** Field definition. */
    private Timestamp receiverTimestamp;

    /** Field definition. */
    private Binary result;

    /** Field definition. */
    private MethodInvokeFailure failure;

    /** Field definition. */
    private Binary signature;

    /** Creates a new MethodInvokeResult. */
    public MethodInvokeResult() {}

    /**
     * Creates a new MethodInvokeResult by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    MethodInvokeResult(MessageReader reader) throws IOException {
        this.messageId = reader.readBinary(1, "messageId", null);
        this.resultForMessageId = reader.readBinary(2, "resultForMessageId", null);
        this.originalSenderId = reader.readText(3, "originalSenderId", null);
        this.receiverId = reader.readText(4, "receiverId", null);
        this.receiverTimestamp = reader.readTimestamp(5, "receiverTimestamp", null);
        this.result = reader.readBinary(7, "result", null);
        this.failure = reader.readMessage(8, "failure", MethodInvokeFailure.SERIALIZER);
        this.signature = reader.readBinary(15, "signature", null);
    }

    /**
     * Creates a new MethodInvokeResult by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    MethodInvokeResult(MethodInvokeResult instance) {
        this.messageId = instance.messageId;
        this.resultForMessageId = instance.resultForMessageId;
        this.originalSenderId = instance.originalSenderId;
        this.receiverId = instance.receiverId;
        this.receiverTimestamp = instance.receiverTimestamp;
        this.result = instance.result;
        this.failure = MessageHelper.immutable(instance.failure);
        this.signature = instance.signature;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeBinary(1, "messageId", messageId);
        w.writeBinary(2, "resultForMessageId", resultForMessageId);
        w.writeText(3, "originalSenderId", originalSenderId);
        w.writeText(4, "receiverId", receiverId);
        w.writeTimestamp(5, "receiverTimestamp", receiverTimestamp);
        w.writeBinary(7, "result", result);
        w.writeMessage(8, "failure", failure, MethodInvokeFailure.SERIALIZER);
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

    public MethodInvokeResult setMessageId(Binary messageId) {
        this.messageId = messageId;
        return this;
    }

    /** Returns the id of the original service invocation. */
    public Binary getResultForMessageId() {
        return resultForMessageId;
    }

    public boolean hasResultForMessageId() {
        return resultForMessageId != null;
    }

    public MethodInvokeResult setResultForMessageId(Binary resultForMessageId) {
        this.resultForMessageId = resultForMessageId;
        return this;
    }

    /** Returns the id of the sender of endpoint invocation request. */
    public String getOriginalSenderId() {
        return originalSenderId;
    }

    public boolean hasOriginalSenderId() {
        return originalSenderId != null;
    }

    public MethodInvokeResult setOriginalSenderId(String originalSenderId) {
        this.originalSenderId = originalSenderId;
        return this;
    }

    /** Returns the id of the receiver of the endpoint invocation request. */
    public String getReceiverId() {
        return receiverId;
    }

    public boolean hasReceiverId() {
        return receiverId != null;
    }

    public MethodInvokeResult setReceiverId(String receiverId) {
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

    public MethodInvokeResult setReceiverTimestamp(Timestamp receiverTimestamp) {
        this.receiverTimestamp = receiverTimestamp;
        return this;
    }

    /** Returns the result of the computation or empty if failed or void method. (Optional) */
    public Binary getResult() {
        return result;
    }

    public boolean hasResult() {
        return result != null;
    }

    public MethodInvokeResult setResult(Binary result) {
        this.result = result;
        return this;
    }

    /** Returns an exceptional result. (Optional) */
    public MethodInvokeFailure getFailure() {
        return failure;
    }

    public boolean hasFailure() {
        return failure != null;
    }

    public MethodInvokeResult setFailure(MethodInvokeFailure failure) {
        this.failure = failure;
        return this;
    }

    /** Returns the signature of this message (Optional). */
    public Binary getSignature() {
        return signature;
    }

    public boolean hasSignature() {
        return signature != null;
    }

    public MethodInvokeResult setSignature(Binary signature) {
        this.signature = signature;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MethodInvokeResult immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static MethodInvokeResult fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.resultForMessageId);
        result = 31 * result + Hashing.hashcode(this.originalSenderId);
        result = 31 * result + Hashing.hashcode(this.receiverId);
        result = 31 * result + Hashing.hashcode(this.receiverTimestamp);
        result = 31 * result + Hashing.hashcode(this.result);
        result = 31 * result + Hashing.hashcode(this.failure);
        return 31 * result + Hashing.hashcode(this.signature);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof MethodInvokeResult) {
            MethodInvokeResult o = (MethodInvokeResult) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(resultForMessageId, o.resultForMessageId) &&
                   Objects.equals(originalSenderId, o.originalSenderId) &&
                   Objects.equals(receiverId, o.receiverId) &&
                   Objects.equals(receiverTimestamp, o.receiverTimestamp) &&
                   Objects.equals(result, o.result) &&
                   Objects.equals(failure, o.failure) &&
                   Objects.equals(signature, o.signature);
        }
        return false;
    }

    /** A serializer for reading and writing instances of MethodInvokeResult. */
    static class Serializer extends MessageSerializer<MethodInvokeResult> {

        /** {@inheritDoc} */
        @Override
        public MethodInvokeResult read(MessageReader reader) throws IOException {
            return new MethodInvokeResult(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(MethodInvokeResult message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of MethodInvokeResult. */
    static class Immutable extends MethodInvokeResult {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(MethodInvokeResult instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeResult immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeResult setMessageId(Binary messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeResult setResultForMessageId(Binary resultForMessageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeResult setOriginalSenderId(String originalSenderId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeResult setReceiverId(String receiverId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeResult setReceiverTimestamp(Timestamp receiverTimestamp) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeResult setResult(Binary result) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeResult setFailure(MethodInvokeFailure failure) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeResult setSignature(Binary signature) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
