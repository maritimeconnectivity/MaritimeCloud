package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.core.message.ValueParser;
import net.maritimecloud.internal.message.util.Hashing;
import net.maritimecloud.internal.message.util.MessageHelper;

public class Hello implements Message, net.maritimecloud.internal.net.messages.spi.TransportMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<Hello> PARSER = new Parser();

    /** Hey */
    private String clientId;

    /** Hey */
    private net.maritimecloud.util.geometry.PositionTime position;

    /** Hey */
    private final Map<String, String> properties;

    /** Hey */
    private String reconnectId;

    /** Hey */
    private Long lastReceivedMessageId;

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
        this.clientId = reader.readString(1, "clientId", null);
        this.position = reader.readMessage(2, "position", net.maritimecloud.util.geometry.PositionTime.PARSER);
        this.properties = MessageHelper.readMap(reader, 3, "properties", ValueParser.STRING, ValueParser.STRING);
        this.reconnectId = reader.readString(4, "reconnectId", null);
        this.lastReceivedMessageId = reader.readInt64(5, "lastReceivedMessageId", null);
    }

    /**
     * Creates a new Hello by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    Hello(Hello instance) {
        this.clientId = instance.clientId;
        this.position = MessageHelper.immutable(instance.position);
        this.properties = MessageHelper.immutableCopy(instance.properties);
        this.reconnectId = instance.reconnectId;
        this.lastReceivedMessageId = instance.lastReceivedMessageId;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.clientId);
        result = 31 * result + Hashing.hashcode(this.position);
        result = 31 * result + Hashing.hashcode(this.properties);
        result = 31 * result + Hashing.hashcode(this.reconnectId);
        return 31 * result + Hashing.hashcode(this.lastReceivedMessageId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Hello) {
            Hello o = (Hello) other;
            return Objects.equals(clientId, o.clientId) &&
                   Objects.equals(position, o.position) &&
                   Objects.equals(properties, o.properties) &&
                   Objects.equals(reconnectId, o.reconnectId) &&
                   Objects.equals(lastReceivedMessageId, o.lastReceivedMessageId);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeString(1, "clientId", clientId);
        w.writeMessage(2, "position", position);
        w.writeMap(3, "properties", properties);
        w.writeString(4, "reconnectId", reconnectId);
        w.writeInt64(5, "lastReceivedMessageId", lastReceivedMessageId);
    }

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

    public net.maritimecloud.util.geometry.PositionTime getPosition() {
        return position;
    }

    public boolean hasPosition() {
        return position != null;
    }

    public Hello setPosition(net.maritimecloud.util.geometry.PositionTime position) {
        this.position = position;
        return this;
    }

    public Map<String, String> getProperties() {
        return java.util.Collections.unmodifiableMap(properties);
    }

    public boolean hasProperties() {
        return properties != null;
    }

    public Hello putPropertie(String key, String value) {
        java.util.Objects.requireNonNull(key, "key is null");
        java.util.Objects.requireNonNull(value, "value is null");
        this.properties.put(key, value);
        return this;
    }

    public String getReconnectId() {
        return reconnectId;
    }

    public boolean hasReconnectId() {
        return reconnectId != null;
    }

    public Hello setReconnectId(String reconnectId) {
        this.reconnectId = reconnectId;
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

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public Hello immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of Hello. */
    static class Parser extends MessageParser<Hello> {

        /** {@inheritDoc} */
        @Override
        public Hello parse(MessageReader reader) throws IOException {
            return new Hello(reader);
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
        public Hello setPosition(net.maritimecloud.util.geometry.PositionTime position) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Hello putPropertie(String key, String value) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Hello setReconnectId(String reconnectId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Hello setLastReceivedMessageId(Long lastReceivedMessageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
