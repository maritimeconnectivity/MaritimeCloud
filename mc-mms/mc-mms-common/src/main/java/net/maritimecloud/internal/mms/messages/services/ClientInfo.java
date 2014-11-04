package net.maritimecloud.internal.mms.messages.services;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

public class ClientInfo implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.mms.messages.services.ClientInfo";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<ClientInfo> SERIALIZER = new Serializer();

    /** Field definition. */
    private String id;

    /** Field definition. */
    private Position latestPosition;

    /** Field definition. */
    private Timestamp lastSeen;

    /** Field definition. */
    private String name;

    /** Field definition. */
    private String description;

    /** Field definition. */
    private String organization;

    /** Creates a new ClientInfo. */
    public ClientInfo() {}

    /**
     * Creates a new ClientInfo by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    ClientInfo(MessageReader reader) throws IOException {
        this.id = reader.readText(1, "id", null);
        this.latestPosition = reader.readPosition(2, "latestPosition", null);
        this.lastSeen = reader.readTimestamp(3, "lastSeen", null);
        this.name = reader.readText(4, "name", null);
        this.description = reader.readText(5, "description", null);
        this.organization = reader.readText(6, "organization", null);
    }

    /**
     * Creates a new ClientInfo by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    ClientInfo(ClientInfo instance) {
        this.id = instance.id;
        this.latestPosition = instance.latestPosition;
        this.lastSeen = instance.lastSeen;
        this.name = instance.name;
        this.description = instance.description;
        this.organization = instance.organization;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeText(1, "id", id);
        w.writePosition(2, "latestPosition", latestPosition);
        w.writeTimestamp(3, "lastSeen", lastSeen);
        w.writeText(4, "name", name);
        w.writeText(5, "description", description);
        w.writeText(6, "organization", organization);
    }

    public String getId() {
        return id;
    }

    public boolean hasId() {
        return id != null;
    }

    public ClientInfo setId(String id) {
        this.id = id;
        return this;
    }

    public Position getLatestPosition() {
        return latestPosition;
    }

    public boolean hasLatestPosition() {
        return latestPosition != null;
    }

    public ClientInfo setLatestPosition(Position latestPosition) {
        this.latestPosition = latestPosition;
        return this;
    }

    public Timestamp getLastSeen() {
        return lastSeen;
    }

    public boolean hasLastSeen() {
        return lastSeen != null;
    }

    public ClientInfo setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        return name != null;
    }

    public ClientInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public ClientInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOrganization() {
        return organization;
    }

    public boolean hasOrganization() {
        return organization != null;
    }

    public ClientInfo setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ClientInfo immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static ClientInfo fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.id);
        result = 31 * result + Hashing.hashcode(this.latestPosition);
        result = 31 * result + Hashing.hashcode(this.lastSeen);
        result = 31 * result + Hashing.hashcode(this.name);
        result = 31 * result + Hashing.hashcode(this.description);
        return 31 * result + Hashing.hashcode(this.organization);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof ClientInfo) {
            ClientInfo o = (ClientInfo) other;
            return Objects.equals(id, o.id) &&
                   Objects.equals(latestPosition, o.latestPosition) &&
                   Objects.equals(lastSeen, o.lastSeen) &&
                   Objects.equals(name, o.name) &&
                   Objects.equals(description, o.description) &&
                   Objects.equals(organization, o.organization);
        }
        return false;
    }

    /** A serializer for reading and writing instances of ClientInfo. */
    static class Serializer extends MessageSerializer<ClientInfo> {

        /** {@inheritDoc} */
        @Override
        public ClientInfo read(MessageReader reader) throws IOException {
            return new ClientInfo(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(ClientInfo message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of ClientInfo. */
    static class Immutable extends ClientInfo {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(ClientInfo instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public ClientInfo immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public ClientInfo setId(String id) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ClientInfo setLatestPosition(Position latestPosition) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ClientInfo setLastSeen(Timestamp lastSeen) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ClientInfo setName(String name) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ClientInfo setDescription(String description) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ClientInfo setOrganization(String organization) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
