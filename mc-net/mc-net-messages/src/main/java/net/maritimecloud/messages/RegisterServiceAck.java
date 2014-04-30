package net.maritimecloud.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.internal.util.Hashing;

public class RegisterServiceAck implements Message, net.maritimecloud.internal.messages.ReplyMessage {

    /** A message parser that can create new instances of this class. */
    public static final MessageParser<RegisterServiceAck> PARSER = new Parser();

    /** Hey */
    private Long messageId;

    /** Hey */
    private Long latestReceivedId;

    /** Hey */
    private Long messageAck;

    /** Creates a new RegisterServiceAck. */
    public RegisterServiceAck() {}

    /**
     * Creates a new RegisterServiceAck by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    RegisterServiceAck(MessageReader reader) throws IOException {
        this.messageId = reader.readInt64(1, "messageId", null);
        this.latestReceivedId = reader.readInt64(2, "latestReceivedId", null);
        this.messageAck = reader.readInt64(3, "messageAck", null);
    }

    /**
     * Creates a new RegisterServiceAck by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    RegisterServiceAck(RegisterServiceAck instance) {
        this.messageId = instance.messageId;
        this.latestReceivedId = instance.latestReceivedId;
        this.messageAck = instance.messageAck;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.messageId);
        result = 31 * result + Hashing.hashcode(this.latestReceivedId);
        return 31 * result + Hashing.hashcode(this.messageAck);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof RegisterServiceAck) {
            RegisterServiceAck o = (RegisterServiceAck) other;
            return Objects.equals(messageId, o.messageId) &&
                   Objects.equals(latestReceivedId, o.latestReceivedId) &&
                   Objects.equals(messageAck, o.messageAck);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt64(1, "messageId", messageId);
        w.writeInt64(2, "latestReceivedId", latestReceivedId);
        w.writeInt64(3, "messageAck", messageAck);
    }

    public Long getMessageId() {
        return messageId;
    }

    public boolean hasMessageId() {
        return messageId != null;
    }

    public RegisterServiceAck setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Long getLatestReceivedId() {
        return latestReceivedId;
    }

    public boolean hasLatestReceivedId() {
        return latestReceivedId != null;
    }

    public RegisterServiceAck setLatestReceivedId(Long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public Long getMessageAck() {
        return messageAck;
    }

    public boolean hasMessageAck() {
        return messageAck != null;
    }

    public RegisterServiceAck setMessageAck(Long messageAck) {
        this.messageAck = messageAck;
        return this;
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    /** {@inheritDoc} */
    @Override
    public RegisterServiceAck immutable() {
        return new Immutable(this);
    }

    /** A parser for parsing instances of RegisterServiceAck. */
    static class Parser extends MessageParser<RegisterServiceAck> {

        /** {@inheritDoc} */
        @Override
        public RegisterServiceAck parse(MessageReader reader) throws IOException {
            return new RegisterServiceAck(reader);
        }
    }

    /** An immutable version of RegisterServiceAck. */
    static class Immutable extends RegisterServiceAck {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(RegisterServiceAck instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public RegisterServiceAck immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public RegisterServiceAck setMessageId(Long messageId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public RegisterServiceAck setLatestReceivedId(Long latestReceivedId) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public RegisterServiceAck setMessageAck(Long messageAck) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
