package net.maritimecloud.internal.mms.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;

public class Close implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.mms.messages.Close";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<Close> SERIALIZER = new Serializer();

    /** Field definition. */
    private Integer closeCode;

    /** Field definition. */
    private Long lastReceivedMessageId;

    /** Creates a new Close. */
    public Close() {}

    /**
     * Creates a new Close by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    Close(MessageReader reader) throws IOException {
        this.closeCode = reader.readInt(1, "closeCode", null);
        this.lastReceivedMessageId = reader.readInt64(3, "lastReceivedMessageId", null);
    }

    /**
     * Creates a new Close by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    Close(Close instance) {
        this.closeCode = instance.closeCode;
        this.lastReceivedMessageId = instance.lastReceivedMessageId;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeInt(1, "closeCode", closeCode);
        w.writeInt64(3, "lastReceivedMessageId", lastReceivedMessageId);
    }

    /** Returns a closing code. */
    public Integer getCloseCode() {
        return closeCode;
    }

    public boolean hasCloseCode() {
        return closeCode != null;
    }

    public Close setCloseCode(Integer closeCode) {
        this.closeCode = closeCode;
        return this;
    }

    public Long getLastReceivedMessageId() {
        return lastReceivedMessageId;
    }

    public boolean hasLastReceivedMessageId() {
        return lastReceivedMessageId != null;
    }

    public Close setLastReceivedMessageId(Long lastReceivedMessageId) {
        this.lastReceivedMessageId = lastReceivedMessageId;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Close immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static Close fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.closeCode);
        return 31 * result + Hashing.hashcode(this.lastReceivedMessageId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Close) {
            Close o = (Close) other;
            return Objects.equals(closeCode, o.closeCode) &&
                   Objects.equals(lastReceivedMessageId, o.lastReceivedMessageId);
        }
        return false;
    }

    /** A serializer for reading and writing instances of Close. */
    static class Serializer extends MessageSerializer<Close> {

        /** {@inheritDoc} */
        @Override
        public Close read(MessageReader reader) throws IOException {
            return new Close(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Close message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of Close. */
    static class Immutable extends Close {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(Close instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public Close immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Close setCloseCode(Integer closeCode) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public Close setLastReceivedMessageId(Long lastReceivedMessageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
