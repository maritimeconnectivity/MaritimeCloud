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

public class MethodInvoke implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.net.messages.MethodInvoke";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<MethodInvoke> SERIALIZER = new Serializer();

    /** Field definition. */
    private Binary messageId;

    /** Field definition. */
    private String endpointMethod;

    /** Field definition. */
    private String senderId;

    /** Field definition. */
    private Timestamp senderTimestamp;

    /** Field definition. */
    private Position senderPosition;

    /** Field definition. */
    private String receiverId;

    /** Field definition. */
    private String parameters;

    /** Field definition. */
    private Binary signature;

    /** Creates a new MethodInvoke. */
    public MethodInvoke() {}

    /**
     * Creates a new MethodInvoke by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    MethodInvoke(MessageReader reader) throws IOException {
        this.messageId = reader.readBinary(1, "messageId", null);
        this.endpointMethod = reader.readText(2, "endpointMethod", null);
        this.senderId = reader.readText(3, "senderId", null);
        this.senderTimestamp = reader.readTimestamp(4, "senderTimestamp", null);
        this.senderPosition = reader.readPosition(5, "senderPosition", null);
        this.receiverId = reader.readText(6, "receiverId", null);
        this.parameters = reader.readText(7, "parameters", null);
        this.signature = reader.readBinary(15, "signature", null);
    }

    /**
     * Creates a new MethodInvoke by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    MethodInvoke(MethodInvoke instance) {
        this.messageId = instance.messageId;
        this.endpointMethod = instance.endpointMethod;
        this.senderId = instance.senderId;
        this.senderTimestamp = instance.senderTimestamp;
        this.senderPosition = instance.senderPosition;
        this.receiverId = instance.receiverId;
        this.parameters = instance.parameters;
        this.signature = instance.signature;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeBinary(1, "messageId", messageId);
        w.writeText(2, "endpointMethod", endpointMethod);
        w.writeText(3, "senderId", senderId);
        w.writeTimestamp(4, "senderTimestamp", senderTimestamp);
        w.writePosition(5, "senderPosition", senderPosition);
        w.writeText(6, "receiverId", receiverId);
        w.writeText(7, "parameters", parameters);
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

    public MethodInvoke setMessageId(Binary messageId) {
        this.messageId = messageId;
        return this;
    }

    /** Returns the name of the endpoint method that should be invoked. */
    public String getEndpointMethod() {
        return endpointMethod;
    }

    public boolean hasEndpointMethod() {
        return endpointMethod != null;
    }

    public MethodInvoke setEndpointMethod(String endpointMethod) {
        this.endpointMethod = endpointMethod;
        return this;
    }

    /** Returns the id of the sender of endpoint invocation request. */
    public String getSenderId() {
        return senderId;
    }

    public boolean hasSenderId() {
        return senderId != null;
    }

    public MethodInvoke setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    /** Returns the current time of the sender when invoking the service. */
    public Timestamp getSenderTimestamp() {
        return senderTimestamp;
    }

    public boolean hasSenderTimestamp() {
        return senderTimestamp != null;
    }

    public MethodInvoke setSenderTimestamp(Timestamp senderTimestamp) {
        this.senderTimestamp = senderTimestamp;
        return this;
    }

    /** Returns the current position of the sender when invoking the service (Optional). */
    public Position getSenderPosition() {
        return senderPosition;
    }

    public boolean hasSenderPosition() {
        return senderPosition != null;
    }

    public MethodInvoke setSenderPosition(Position senderPosition) {
        this.senderPosition = senderPosition;
        return this;
    }

    /** Returns the id of the receiver of the endpoint invocation request. */
    public String getReceiverId() {
        return receiverId;
    }

    public boolean hasReceiverId() {
        return receiverId != null;
    }

    public MethodInvoke setReceiverId(String receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    /** Returns the parameters for the endpoint invocation. (Optional) */
    public String getParameters() {
        return parameters;
    }

    public boolean hasParameters() {
        return parameters != null;
    }

    public MethodInvoke setParameters(String parameters) {
        this.parameters = parameters;
        return this;
    }

    public Binary getSignature() {
        return signature;
    }

    public boolean hasSignature() {
        return signature != null;
    }

    public MethodInvoke setSignature(Binary signature) {
        this.signature = signature;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MethodInvoke immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static MethodInvoke fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.endpointMethod);
        result = 31 * result + Hashing.hashcode(this.senderId);
        result = 31 * result + Hashing.hashcode(this.senderTimestamp);
        result = 31 * result + Hashing.hashcode(this.senderPosition);
        result = 31 * result + Hashing.hashcode(this.receiverId);
        result = 31 * result + Hashing.hashcode(this.parameters);
        return 31 * result + Hashing.hashcode(this.signature);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof MethodInvoke) {
            MethodInvoke o = (MethodInvoke) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(endpointMethod, o.endpointMethod) &&
                   Objects.equals(senderId, o.senderId) &&
                   Objects.equals(senderTimestamp, o.senderTimestamp) &&
                   Objects.equals(senderPosition, o.senderPosition) &&
                   Objects.equals(receiverId, o.receiverId) &&
                   Objects.equals(parameters, o.parameters) &&
                   Objects.equals(signature, o.signature);
        }
        return false;
    }

    /** A serializer for reading and writing instances of MethodInvoke. */
    static class Serializer extends MessageSerializer<MethodInvoke> {

        /** {@inheritDoc} */
        @Override
        public MethodInvoke read(MessageReader reader) throws IOException {
            return new MethodInvoke(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(MethodInvoke message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of MethodInvoke. */
    static class Immutable extends MethodInvoke {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(MethodInvoke instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvoke immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvoke setMessageId(Binary messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvoke setEndpointMethod(String endpointMethod) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvoke setSenderId(String senderId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvoke setSenderTimestamp(Timestamp senderTimestamp) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvoke setSenderPosition(Position senderPosition) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvoke setReceiverId(String receiverId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvoke setParameters(String parameters) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvoke setSignature(Binary signature) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
