package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.message.util.Hashing;
import net.maritimecloud.internal.message.util.MessageHelper;

public class PositionReport implements Message, net.maritimecloud.internal.net.messages.spi.ConnectionMessage, net.maritimecloud.internal.net.messages.spi.PositionTimeMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<PositionReport> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private net.maritimecloud.util.geometry.PositionTime positionTime;

    /** Creates a new PositionReport. */
    public PositionReport() {}

    /**
     * Creates a new PositionReport by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    PositionReport(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.positionTime = reader.readMessage(3, "positionTime", net.maritimecloud.util.geometry.PositionTime.PARSER);
    }

    /**
     * Creates a new PositionReport by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    PositionReport(PositionReport instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.positionTime = MessageHelper.immutable(instance.positionTime);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        return 31 * result + Hashing.hashcode(this.positionTime);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof PositionReport) {
            PositionReport o = (PositionReport) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(positionTime, o.positionTime);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeMessage(3, "positionTime", positionTime);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public PositionReport setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public PositionReport setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public net.maritimecloud.util.geometry.PositionTime getPositionTime() {
        return positionTime;
    }

    public boolean hasPositionTime() {
        return positionTime != null;
    }

    public PositionReport setPositionTime(net.maritimecloud.util.geometry.PositionTime positionTime) {
        this.positionTime = positionTime;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static PositionReport fromJSON(CharSequence c) {
        return MessageSerializers.readFromJSON(PARSER, c);
    }

    /** {@inheritDoc} */
    @Override
    public PositionReport immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of PositionReport. */
    static class Parser extends MessageParser<PositionReport> {

        /** {@inheritDoc} */
        @Override
        public PositionReport parse(MessageReader reader) throws IOException {
            return new PositionReport(reader);
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
        public PositionReport setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public PositionReport setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public PositionReport setPositionTime(net.maritimecloud.util.geometry.PositionTime positionTime) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
