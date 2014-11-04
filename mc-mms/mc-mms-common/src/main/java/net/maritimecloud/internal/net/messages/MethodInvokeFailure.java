package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;

public class MethodInvokeFailure implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.net.messages.MethodInvokeFailure";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<MethodInvokeFailure> SERIALIZER = new Serializer();

    /** Field definition. */
    private Integer errorCode;

    /** Field definition. */
    private String exceptionType;

    /** Field definition. */
    private String description;

    /** Creates a new MethodInvokeFailure. */
    public MethodInvokeFailure() {}

    /**
     * Creates a new MethodInvokeFailure by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    MethodInvokeFailure(MessageReader reader) throws IOException {
        this.errorCode = reader.readInt(1, "errorCode", null);
        this.exceptionType = reader.readText(2, "exceptionType", null);
        this.description = reader.readText(3, "description", null);
    }

    /**
     * Creates a new MethodInvokeFailure by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    MethodInvokeFailure(MethodInvokeFailure instance) {
        this.errorCode = instance.errorCode;
        this.exceptionType = instance.exceptionType;
        this.description = instance.description;
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeInt(1, "errorCode", errorCode);
        w.writeText(2, "exceptionType", exceptionType);
        w.writeText(3, "description", description);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public boolean hasErrorCode() {
        return errorCode != null;
    }

    public MethodInvokeFailure setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    /** Returns the name of the exception. */
    public String getExceptionType() {
        return exceptionType;
    }

    public boolean hasExceptionType() {
        return exceptionType != null;
    }

    public MethodInvokeFailure setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
        return this;
    }

    /** Returns a textual description. */
    public String getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public MethodInvokeFailure setDescription(String description) {
        this.description = description;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MethodInvokeFailure immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static MethodInvokeFailure fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.errorCode);
        result = 31 * result + Hashing.hashcode(this.exceptionType);
        return 31 * result + Hashing.hashcode(this.description);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof MethodInvokeFailure) {
            MethodInvokeFailure o = (MethodInvokeFailure) other;
            return Objects.equals(errorCode, o.errorCode) &&
                   Objects.equals(exceptionType, o.exceptionType) &&
                   Objects.equals(description, o.description);
        }
        return false;
    }

    /** A serializer for reading and writing instances of MethodInvokeFailure. */
    static class Serializer extends MessageSerializer<MethodInvokeFailure> {

        /** {@inheritDoc} */
        @Override
        public MethodInvokeFailure read(MessageReader reader) throws IOException {
            return new MethodInvokeFailure(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(MethodInvokeFailure message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of MethodInvokeFailure. */
    static class Immutable extends MethodInvokeFailure {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(MethodInvokeFailure instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeFailure immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeFailure setErrorCode(Integer errorCode) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeFailure setExceptionType(String exceptionType) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public MethodInvokeFailure setDescription(String description) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
