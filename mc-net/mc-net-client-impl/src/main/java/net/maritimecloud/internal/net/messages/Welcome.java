package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.core.message.ValueParser;
import net.maritimecloud.internal.message.util.MessageHelper;
import net.maritimecloud.internal.util.Hashing;

public class Welcome implements Message, net.maritimecloud.internal.net.messages.spi.TransportMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<Welcome> PARSER = new Parser();

    /** Hey */
    private final List<Integer> protocolVersion;

    /** Hey */
    private String serverId;

    /** Hey */
    private final Map<String, String> properties;

    /** Creates a new Welcome. */
    public Welcome() {
        protocolVersion = new java.util.ArrayList<>();
        properties = new java.util.LinkedHashMap<>();
    }

    /**
     * Creates a new Welcome by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    Welcome(MessageReader reader) throws IOException {
        this.protocolVersion = MessageHelper.readList(reader, 1, "protocolVersion", ValueParser.INT32);
        this.serverId = reader.readString(2, "serverId", null);
        this.properties = MessageHelper.readMap(reader, 3, "properties", ValueParser.STRING, ValueParser.STRING);
    }

    /**
     * Creates a new Welcome by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    Welcome(Welcome instance) {
        this.protocolVersion = MessageHelper.immutableCopy(instance.protocolVersion);
        this.serverId = instance.serverId;
        this.properties = MessageHelper.immutableCopy(instance.properties);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.protocolVersion);
        result = 31 * result + Hashing.hashcode(this.serverId);
        return 31 * result + Hashing.hashcode(this.properties);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Welcome) {
            Welcome o = (Welcome) other;
            return Objects.equals(protocolVersion, o.protocolVersion) &&
                   Objects.equals(serverId, o.serverId) &&
                   Objects.equals(properties, o.properties);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeList(1, "protocolVersion", protocolVersion);
        w.writeString(2, "serverId", serverId);
        w.writeMap(3, "properties", properties);
    }

    /** Returns a list of protocol versions this server understands. */
    public List<Integer> getProtocolVersion() {
        return java.util.Collections.unmodifiableList(protocolVersion);
    }

    public boolean hasProtocolVersion() {
        return protocolVersion != null;
    }

    public Welcome addProtocolVersion(Integer protocolVersion) {
        java.util.Objects.requireNonNull(protocolVersion, "protocolVersion is null");
        this.protocolVersion.add(protocolVersion);
        return this;
    }

    /** Returns the id of the server. */
    public String getServerId() {
        return serverId;
    }

    public boolean hasServerId() {
        return serverId != null;
    }

    public Welcome setServerId(String serverId) {
        this.serverId = serverId;
        return this;
    }

    /** Returns a map of properties. */
    public Map<String, String> getProperties() {
        return java.util.Collections.unmodifiableMap(properties);
    }

    public boolean hasProperties() {
        return properties != null;
    }

    public Welcome putPropertie(String key, String value) {
        java.util.Objects.requireNonNull(key, "key is null");
        java.util.Objects.requireNonNull(value, "value is null");
        this.properties.put(key, value);
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public Welcome immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of Welcome. */
    static class Parser extends MessageParser<Welcome> {

        /** {@inheritDoc} */
        @Override
        public Welcome parse(MessageReader reader) throws IOException {
            return new Welcome(reader);
        }
    }

    /** An immutable version of Welcome. */
    static class Immutable extends Welcome {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(Welcome instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public Welcome immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Welcome addProtocolVersion(Integer protocolVersion) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Welcome setServerId(String serverId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Welcome putPropertie(String key, String value) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
