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

public class FindService implements Message, net.maritimecloud.internal.messages.RequestMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<FindService> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long requestId;

    /** Hey */
    private String serviceName;

    /** Hey */
    private net.maritimecloud.util.geometry.Area area;

    /** Creates a new FindService. */
    public FindService() {}

    /**
     * Creates a new FindService by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    FindService(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.requestId = reader.readInt64(3, "requestId", null);
        this.serviceName = reader.readString(4, "serviceName", null);
        this.area = reader.readMessage(5, "area", net.maritimecloud.util.geometry.Area.PARSER);
    }

    /**
     * Creates a new FindService by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    FindService(FindService instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.requestId = instance.requestId;
        this.serviceName = instance.serviceName;
        this.area = MessageHelper.immutable(instance.area);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        result = 31 * result + Hashing.hashcode(this.requestId);
        result = 31 * result + Hashing.hashcode(this.serviceName);
        return 31 * result + Hashing.hashcode(this.area);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof FindService) {
            FindService o = (FindService) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(requestId, o.requestId) &&
                   Objects.equals(serviceName, o.serviceName) &&
                   Objects.equals(area, o.area);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeInt64(3, "requestId", requestId);
        w.writeString(4, "serviceName", serviceName);
        w.writeMessage(5, "area", area);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public FindService setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public FindService setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getRequestId() {
        return requestId;
    }

    public boolean hasRequestId() {
        return requestId != null;
    }

    public FindService setRequestId(Long requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean hasServiceName() {
        return serviceName != null;
    }

    public FindService setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public net.maritimecloud.util.geometry.Area getArea() {
        return area;
    }

    public boolean hasArea() {
        return area != null;
    }

    public FindService setArea(net.maritimecloud.util.geometry.Area area) {
        this.area = area;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public FindService immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of FindService. */
    static class Parser extends MessageParser<FindService> {

        /** {@inheritDoc} */
        @Override
        public FindService parse(MessageReader reader) throws IOException {
            return new FindService(reader);
        }
    }

    /** An immutable version of FindService. */
    static class Immutable extends FindService {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(FindService instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public FindService immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public FindService setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindService setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindService setRequestId(Long requestId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindService setServiceName(String serviceName) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public FindService setArea(net.maritimecloud.util.geometry.Area area) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
