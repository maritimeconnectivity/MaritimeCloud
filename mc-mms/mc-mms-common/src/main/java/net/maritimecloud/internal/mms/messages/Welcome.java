/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.maritimecloud.internal.mms.messages;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;

public class Welcome implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.mms.messages.Welcome";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<Welcome> SERIALIZER = new Serializer();

    /** Field definition. */
    private final List<Integer> protocolVersion;

    /** Field definition. */
    private String serverId;

    /** Field definition. */
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
        this.protocolVersion = MessageHelper.readList(1, "protocolVersion", reader, ValueSerializer.INT);
        this.serverId = reader.readText(2, "serverId", null);
        this.properties = MessageHelper.readMap(3, "properties", reader, ValueSerializer.TEXT, ValueSerializer.TEXT);
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

    void writeTo(MessageWriter w) throws IOException {
        w.writeList(1, "protocolVersion", protocolVersion, ValueSerializer.INT);
        w.writeText(2, "serverId", serverId);
        w.writeMap(3, "properties", properties, ValueSerializer.TEXT, ValueSerializer.TEXT);
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

    public Welcome addAllProtocolVersion(Collection<? extends Integer> protocolVersion) {
        for (Integer e : protocolVersion) {
            addProtocolVersion(e);
        }
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

    public Welcome putProperties(String key, String value) {
        java.util.Objects.requireNonNull(key, "key is null");
        java.util.Objects.requireNonNull(value, "value is null");
        this.properties.put(key, value);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Welcome immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static Welcome fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
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

    /** A serializer for reading and writing instances of Welcome. */
    static class Serializer extends MessageSerializer<Welcome> {

        /** {@inheritDoc} */
        @Override
        public Welcome read(MessageReader reader) throws IOException {
            return new Welcome(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Welcome message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
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
        public Welcome putProperties(String key, String value) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
