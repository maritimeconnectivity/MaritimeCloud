package net.maritimecloud.internal.mms.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.util.geometry.PositionTime;

public class PositionReport implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.mms.messages.PositionReport";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<PositionReport> SERIALIZER = new Serializer();

    /** Field definition. */
    private PositionTime positionTime;

    /** Creates a new PositionReport. */
    public PositionReport() {}

    /**
     * Creates a new PositionReport by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    PositionReport(MessageReader reader) throws IOException {
        this.positionTime = reader.readPositionTime(3, "positionTime", null);
    }

    /**
     * Creates a new PositionReport by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    PositionReport(PositionReport instance) {
        this.positionTime = instance.positionTime;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writePositionTime(3, "positionTime", positionTime);
    }

    public PositionTime getPositionTime() {
        return positionTime;
    }

    public boolean hasPositionTime() {
        return positionTime != null;
    }

    public PositionReport setPositionTime(PositionTime positionTime) {
        this.positionTime = positionTime;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public PositionReport immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static PositionReport fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Hashing.hashcode(this.positionTime);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof PositionReport) {
            PositionReport o = (PositionReport) other;
            return Objects.equals(positionTime, o.positionTime);
        }
        return false;
    }

    /** A serializer for reading and writing instances of PositionReport. */
    static class Serializer extends MessageSerializer<PositionReport> {

        /** {@inheritDoc} */
        @Override
        public PositionReport read(MessageReader reader) throws IOException {
            return new PositionReport(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(PositionReport message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of PositionReport. */
    static class Immutable extends PositionReport {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(PositionReport instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public PositionReport immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public PositionReport setPositionTime(PositionTime positionTime) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
