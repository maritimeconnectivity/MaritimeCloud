package net.maritimecloud.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.message.util.MessageHelper;
import net.maritimecloud.internal.util.Hashing;

public class BroadcastSend implements Message {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<BroadcastSend> PARSER = new Parser();

    /** Hey */
    private String broadcastName;

    /** Hey */
    private Integer broadcastRadius;

    /** Hey */
    private net.maritimecloud.util.geometry.Area broadcastArea;

    /** Hey */
    private Boolean broadcastAck;

    /** Hey */
    private String msg;

    /** Creates a new BroadcastSend. */
    public BroadcastSend() {}

    /**
     * Creates a new BroadcastSend by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    BroadcastSend(MessageReader reader) throws IOException {
        this.broadcastName = reader.readString(1, "broadcastName", null);
        this.broadcastRadius = reader.readInt32(2, "broadcastRadius", null);
        this.broadcastArea = reader.readMessage(3, "broadcastArea", net.maritimecloud.util.geometry.Area.PARSER);
        this.broadcastAck = reader.readBool(4, "broadcastAck", null);
        this.msg = reader.readString(15, "msg", null);
    }

    /**
     * Creates a new BroadcastSend by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    BroadcastSend(BroadcastSend instance) {
        this.broadcastName = instance.broadcastName;
        this.broadcastRadius = instance.broadcastRadius;
        this.broadcastArea = MessageHelper.immutable(instance.broadcastArea);
        this.broadcastAck = instance.broadcastAck;
        this.msg = instance.msg;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.broadcastName);
        result = 31 * result + Hashing.hashcode(this.broadcastRadius);
        result = 31 * result + Hashing.hashcode(this.broadcastArea);
        result = 31 * result + Hashing.hashcode(this.broadcastAck);
        return 31 * result + Hashing.hashcode(this.msg);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof BroadcastSend) {
            BroadcastSend o = (BroadcastSend) other;
            return Objects.equals(broadcastName, o.broadcastName) &&
                   Objects.equals(broadcastRadius, o.broadcastRadius) &&
                   Objects.equals(broadcastArea, o.broadcastArea) &&
                   Objects.equals(broadcastAck, o.broadcastAck) &&
                   Objects.equals(msg, o.msg);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeString(1, "broadcastName", broadcastName);
        w.writeInt32(2, "broadcastRadius", broadcastRadius);
        w.writeMessage(3, "broadcastArea", broadcastArea);
        w.writeBool(4, "broadcastAck", broadcastAck);
        w.writeString(15, "msg", msg);
    }

    /** Returns //C2S */
    public String getBroadcastName() {
        return broadcastName;
    }

    public boolean hasBroadcastName() {
        return broadcastName != null;
    }

    public BroadcastSend setBroadcastName(String broadcastName) {
        this.broadcastName = broadcastName;
        return this;
    }

    public Integer getBroadcastRadius() {
        return broadcastRadius;
    }

    public boolean hasBroadcastRadius() {
        return broadcastRadius != null;
    }

    public BroadcastSend setBroadcastRadius(Integer broadcastRadius) {
        this.broadcastRadius = broadcastRadius;
        return this;
    }

    public net.maritimecloud.util.geometry.Area getBroadcastArea() {
        return broadcastArea;
    }

    public boolean hasBroadcastArea() {
        return broadcastArea != null;
    }

    public BroadcastSend setBroadcastArea(net.maritimecloud.util.geometry.Area broadcastArea) {
        this.broadcastArea = broadcastArea;
        return this;
    }

    public Boolean getBroadcastAck() {
        return broadcastAck;
    }

    public boolean hasBroadcastAck() {
        return broadcastAck != null;
    }

    public BroadcastSend setBroadcastAck(Boolean broadcastAck) {
        this.broadcastAck = broadcastAck;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public boolean hasMsg() {
        return msg != null;
    }

    public BroadcastSend setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastSend immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of BroadcastSend. */
    static class Parser extends MessageParser<BroadcastSend> {

        /** {@inheritDoc} */
        @Override
        public BroadcastSend parse(MessageReader reader) throws IOException {
            return new BroadcastSend(reader);
        }
    }

    /** An immutable version of BroadcastSend. */
    static class Immutable extends BroadcastSend {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(BroadcastSend instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastSend immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastSend setBroadcastName(String broadcastName) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastSend setBroadcastRadius(Integer broadcastRadius) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastSend setBroadcastArea(net.maritimecloud.util.geometry.Area broadcastArea) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastSend setBroadcastAck(Boolean broadcastAck) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastSend setMsg(String msg) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
