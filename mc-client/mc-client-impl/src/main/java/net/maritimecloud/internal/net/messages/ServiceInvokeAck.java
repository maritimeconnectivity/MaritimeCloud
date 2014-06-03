package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.message.util.Hashing;

public class ServiceInvokeAck implements Message, net.maritimecloud.internal.net.messages.spi.RelayMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<ServiceInvokeAck> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private String source;

    /** Hey */
    private String destination;

    /** Hey */
    private String msg;

    /** Hey */
    private String replyType;

    /** Hey */
    private String uuid;

    /** Creates a new ServiceInvokeAck. */
    public ServiceInvokeAck() {}

    /**
     * Creates a new ServiceInvokeAck by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    ServiceInvokeAck(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.source = reader.readString(3, "source", null);
        this.destination = reader.readString(4, "destination", null);
        this.msg = reader.readString(5, "msg", null);
        this.replyType = reader.readString(6, "replyType", null);
        this.uuid = reader.readString(7, "uuid", null);
    }

    /**
     * Creates a new ServiceInvokeAck by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    ServiceInvokeAck(ServiceInvokeAck instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.source = instance.source;
        this.destination = instance.destination;
        this.msg = instance.msg;
        this.replyType = instance.replyType;
        this.uuid = instance.uuid;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.source);
        result = 31 * result + Hashing.hashcode(this.destination);
        result = 31 * result + Hashing.hashcode(this.msg);
        result = 31 * result + Hashing.hashcode(this.replyType);
        return 31 * result + Hashing.hashcode(this.uuid);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof ServiceInvokeAck) {
            ServiceInvokeAck o = (ServiceInvokeAck) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(source, o.source) &&
                   Objects.equals(destination, o.destination) &&
                   Objects.equals(msg, o.msg) &&
                   Objects.equals(replyType, o.replyType) &&
                   Objects.equals(uuid, o.uuid);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeString(3, "source", source);
        w.writeString(4, "destination", destination);
        w.writeString(5, "msg", msg);
        w.writeString(6, "replyType", replyType);
        w.writeString(7, "uuid", uuid);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public ServiceInvokeAck setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public ServiceInvokeAck setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public String getSource() {
        return source;
    }

    public boolean hasSource() {
        return source != null;
    }

    public ServiceInvokeAck setSource(String source) {
        this.source = source;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public boolean hasDestination() {
        return destination != null;
    }

    public ServiceInvokeAck setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public boolean hasMsg() {
        return msg != null;
    }

    public ServiceInvokeAck setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getReplyType() {
        return replyType;
    }

    public boolean hasReplyType() {
        return replyType != null;
    }

    public ServiceInvokeAck setReplyType(String replyType) {
        this.replyType = replyType;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean hasUuid() {
        return uuid != null;
    }

    public ServiceInvokeAck setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static ServiceInvokeAck fromJSON(CharSequence c) {
        return MessageSerializers.readFromJSON(PARSER, c);
    }

    /** {@inheritDoc} */
    @Override
    public ServiceInvokeAck immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of ServiceInvokeAck. */
    static class Parser extends MessageParser<ServiceInvokeAck> {

        /** {@inheritDoc} */
        @Override
        public ServiceInvokeAck parse(MessageReader reader) throws IOException {
            return new ServiceInvokeAck(reader);
        }
    }

    /** An immutable version of ServiceInvokeAck. */
    static class Immutable extends ServiceInvokeAck {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(ServiceInvokeAck instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvokeAck immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvokeAck setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvokeAck setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvokeAck setSource(String source) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvokeAck setDestination(String destination) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvokeAck setMsg(String msg) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvokeAck setReplyType(String replyType) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvokeAck setUuid(String uuid) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
