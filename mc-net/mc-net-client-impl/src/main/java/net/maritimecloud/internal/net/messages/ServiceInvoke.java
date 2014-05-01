package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.util.Hashing;

public class ServiceInvoke implements Message, net.maritimecloud.internal.net.messages.spi.RelayMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<ServiceInvoke> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private String source;

    /** Hey */
    private String destination;

    /** Hey */
    private String conversationId;

    /** Hey */
    private String msg;

    /** Hey */
    private String messageType;

    /** Hey */
    private String serviceType;

    /** Hey */
    private Integer status;

    /** Creates a new ServiceInvoke. */
    public ServiceInvoke() {}

    /**
     * Creates a new ServiceInvoke by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    ServiceInvoke(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.source = reader.readString(3, "source", null);
        this.destination = reader.readString(4, "destination", null);
        this.conversationId = reader.readString(5, "conversationId", null);
        this.msg = reader.readString(6, "msg", null);
        this.messageType = reader.readString(7, "messageType", null);
        this.serviceType = reader.readString(8, "serviceType", null);
        this.status = reader.readInt32(9, "status", null);
    }

    /**
     * Creates a new ServiceInvoke by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    ServiceInvoke(ServiceInvoke instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.source = instance.source;
        this.destination = instance.destination;
        this.conversationId = instance.conversationId;
        this.msg = instance.msg;
        this.messageType = instance.messageType;
        this.serviceType = instance.serviceType;
        this.status = instance.status;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.source);
        result = 31 * result + Hashing.hashcode(this.destination);
        result = 31 * result + Hashing.hashcode(this.conversationId);
        result = 31 * result + Hashing.hashcode(this.msg);
        result = 31 * result + Hashing.hashcode(this.messageType);
        result = 31 * result + Hashing.hashcode(this.serviceType);
        return 31 * result + Hashing.hashcode(this.status);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof ServiceInvoke) {
            ServiceInvoke o = (ServiceInvoke) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(source, o.source) &&
                   Objects.equals(destination, o.destination) &&
                   Objects.equals(conversationId, o.conversationId) &&
                   Objects.equals(msg, o.msg) &&
                   Objects.equals(messageType, o.messageType) &&
                   Objects.equals(serviceType, o.serviceType) &&
                   Objects.equals(status, o.status);
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
        w.writeString(5, "conversationId", conversationId);
        w.writeString(6, "msg", msg);
        w.writeString(7, "messageType", messageType);
        w.writeString(8, "serviceType", serviceType);
        w.writeInt32(9, "status", status);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public ServiceInvoke setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public ServiceInvoke setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public String getSource() {
        return source;
    }

    public boolean hasSource() {
        return source != null;
    }

    public ServiceInvoke setSource(String source) {
        this.source = source;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public boolean hasDestination() {
        return destination != null;
    }

    public ServiceInvoke setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getConversationId() {
        return conversationId;
    }

    public boolean hasConversationId() {
        return conversationId != null;
    }

    public ServiceInvoke setConversationId(String conversationId) {
        this.conversationId = conversationId;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public boolean hasMsg() {
        return msg != null;
    }

    public ServiceInvoke setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getMessageType() {
        return messageType;
    }

    public boolean hasMessageType() {
        return messageType != null;
    }

    public ServiceInvoke setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public String getServiceType() {
        return serviceType;
    }

    public boolean hasServiceType() {
        return serviceType != null;
    }

    public ServiceInvoke setServiceType(String serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public boolean hasStatus() {
        return status != null;
    }

    public ServiceInvoke setStatus(Integer status) {
        this.status = status;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public ServiceInvoke immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of ServiceInvoke. */
    static class Parser extends MessageParser<ServiceInvoke> {

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke parse(MessageReader reader) throws IOException {
            return new ServiceInvoke(reader);
        }
    }

    /** An immutable version of ServiceInvoke. */
    static class Immutable extends ServiceInvoke {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(ServiceInvoke instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke setSource(String source) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke setDestination(String destination) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke setConversationId(String conversationId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke setMsg(String msg) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke setMessageType(String messageType) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke setServiceType(String serviceType) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public ServiceInvoke setStatus(Integer status) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
