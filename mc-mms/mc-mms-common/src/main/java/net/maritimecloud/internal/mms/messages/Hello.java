package net.maritimecloud.internal.mms.messages;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.PositionTime;

public class Hello implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.mms.messages.Hello";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<Hello> SERIALIZER = new Serializer();

    /** Field definition. */
    private String clientId;

    /** Field definition. */
    private final Map<String, String> properties;

    /** Field definition. */
    private Binary sessionId;

    /** Field definition. */
    private Long lastReceivedMessageId;

    /** Field definition. */
    private PositionTime positionTime;

    /** Creates a new Hello. */
    public Hello() {
        properties = new java.util.LinkedHashMap<>();
    }

    /**
     * Creates a new Hello by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    Hello(MessageReader reader) throws IOException {
        this.clientId = reader.readText(1, "clientId", null);
        this.properties = MessageHelper.readMap(2, "properties", reader, ValueSerializer.TEXT, ValueSerializer.TEXT);
        this.sessionId = reader.readBinary(4, "sessionId", null);
        this.lastReceivedMessageId = reader.readInt64(5, "lastReceivedMessageId", null);
        this.positionTime = reader.readPositionTime(6, "positionTime", null);
    }

    /**
     * Creates a new Hello by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    Hello(Hello instance) {
        this.clientId = instance.clientId;
        this.properties = MessageHelper.immutableCopy(instance.properties);
        this.sessionId = instance.sessionId;
        this.lastReceivedMessageId = instance.lastReceivedMessageId;
        this.positionTime = instance.positionTime;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeText(1, "clientId", clientId);
        w.writeMap(2, "properties", properties, ValueSerializer.TEXT, ValueSerializer.TEXT);
        w.writeBinary(4, "sessionId", sessionId);
        w.writeInt64(5, "lastReceivedMessageId", lastReceivedMessageId);
        w.writePositionTime(6, "positionTime", positionTime);
    }

    /** Returns the id of the client. */
    public String getClientId() {
        return clientId;
    }

    public boolean hasClientId() {
        return clientId != null;
    }

    public Hello setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /** Returns textual properties of the client. */
    public Map<String, String> getProperties() {
        return java.util.Collections.unmodifiableMap(properties);
    }

    public boolean hasProperties() {
        return properties != null;
    }

    public Hello putProperties(String key, String value) {
        java.util.Objects.requireNonNull(key, "key is null");
        java.util.Objects.requireNonNull(value, "value is null");
        this.properties.put(key, value);
        return this;
    }

    public Binary getSessionId() {
        return sessionId;
    }

    public boolean hasSessionId() {
        return sessionId != null;
    }

    public Hello setSessionId(Binary sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public Long getLastReceivedMessageId() {
        return lastReceivedMessageId;
    }

    public boolean hasLastReceivedMessageId() {
        return lastReceivedMessageId != null;
    }

    public Hello setLastReceivedMessageId(Long lastReceivedMessageId) {
        this.lastReceivedMessageId = lastReceivedMessageId;
        return this;
    }

    public PositionTime getPositionTime() {
        return positionTime;
    }

    public boolean hasPositionTime() {
        return positionTime != null;
    }

    public Hello setPositionTime(PositionTime positionTime) {
        this.positionTime = positionTime;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Hello immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static Hello fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.clientId);
        result = 31 * result + Hashing.hashcode(this.properties);
        result = 31 * result + Hashing.hashcode(this.sessionId);
        result = 31 * result + Hashing.hashcode(this.lastReceivedMessageId);
        return 31 * result + Hashing.hashcode(this.positionTime);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Hello) {
            Hello o = (Hello) other;
            return Objects.equals(clientId, o.clientId) &&
                   Objects.equals(properties, o.properties) &&
                   Objects.equals(sessionId, o.sessionId) &&
                   Objects.equals(lastReceivedMessageId, o.lastReceivedMessageId) &&
                   Objects.equals(positionTime, o.positionTime);
        }
        return false;
    }

    /** A serializer for reading and writing instances of Hello. */
    static class Serializer extends MessageSerializer<Hello> {

        /** {@inheritDoc} */
        @Override
        public Hello read(MessageReader reader) throws IOException {
            return new Hello(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Hello message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of Hello. */
    static class Immutable extends Hello {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(Hello instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public Hello immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Hello setClientId(String clientId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Hello putProperties(String key, String value) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Hello setSessionId(Binary sessionId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Hello setLastReceivedMessageId(Long lastReceivedMessageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Hello setPositionTime(PositionTime positionTime) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
