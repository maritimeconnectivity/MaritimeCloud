package net.maritimecloud.internal.mms.messages.services;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;

public class ClientList implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.mms.messages.services.ClientList";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<ClientList> SERIALIZER = new Serializer();

    /** Field definition. */
    private final List<ClientInfo> clients;

    /** Creates a new ClientList. */
    public ClientList() {
        clients = new java.util.ArrayList<>();
    }

    /**
     * Creates a new ClientList by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    ClientList(MessageReader reader) throws IOException {
        this.clients = MessageHelper.readList(1, "clients", reader, ClientInfo.SERIALIZER);
    }

    /**
     * Creates a new ClientList by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    ClientList(ClientList instance) {
        this.clients = MessageHelper.immutableCopy(instance.clients);
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeList(1, "clients", clients, ClientInfo.SERIALIZER);
    }

    /**
     * Returns the id (hash) of the broadcast (Optional, can be calculated from the rest of the fields).
     */
    public List<ClientInfo> getClients() {
        return java.util.Collections.unmodifiableList(clients);
    }

    public boolean hasClients() {
        return clients != null;
    }

    public ClientList addClients(ClientInfo clients) {
        java.util.Objects.requireNonNull(clients, "clients is null");
        this.clients.add(clients);
        return this;
    }

    public ClientList addAllClients(Collection<? extends ClientInfo> clients) {
        for (ClientInfo e : clients) {
            addClients(e);
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ClientList immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static ClientList fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Hashing.hashcode(this.clients);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof ClientList) {
            ClientList o = (ClientList) other;
            return Objects.equals(clients, o.clients);
        }
        return false;
    }

    /** A serializer for reading and writing instances of ClientList. */
    static class Serializer extends MessageSerializer<ClientList> {

        /** {@inheritDoc} */
        @Override
        public ClientList read(MessageReader reader) throws IOException {
            return new ClientList(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(ClientList message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of ClientList. */
    static class Immutable extends ClientList {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(ClientList instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public ClientList immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public ClientList addClients(ClientInfo clients) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
