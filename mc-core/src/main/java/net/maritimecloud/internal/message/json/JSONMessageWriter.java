/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.maritimecloud.internal.message.json;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.core.message.MessageEnum;
import net.maritimecloud.core.message.MessageSerializable;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class JSONMessageWriter extends MessageWriter {

    /** The Unix line separator. */
    static final String LS = "\n";

    /** The current number of indents. */
    private int indent;

    /** The print writer to write to */
    private final PrintWriter pw;

    public JSONMessageWriter(PrintWriter pw) {
        this.pw = requireNonNull(pw);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        pw.close();
    }

    /** {@inheritDoc} */
    @Override
    public void flush() throws IOException {
        pw.flush();
    }

    /**
     * Adds the specified count of spaces to the specified string builder.
     *
     * @param sb
     *            the string builder to add to
     * @param count
     *            the number of spaces to add
     * @return the specified string builder
     */
    void indent() {
        for (int i = 0; i < indent; i++) {
            pw.append("  ");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeBinary(int tag, String name, byte[] bytes, int offset, int length) throws IOException {
        byte[] b = Arrays.copyOfRange(bytes, offset, length);
        writeString(tag, name, SimpleBase64.encode(b));
    }

    void writeBool(boolean value) {
        pw.write(Boolean.toString(value));
    }

    /** {@inheritDoc} */
    @Override
    public void writeBool(int tag, String name, boolean value) throws IOException {
        writeTag(tag, name);
        writeBool(value);
    }

    void writeDouble(double value) throws IOException {
        if (!Double.isFinite(value)) {
            throw new IOException("Cannot write double value " + value);
        }
        pw.write(Double.toString(value));
    }

    /** {@inheritDoc} */
    @Override
    public void writeDouble(int tag, String name, double value) throws IOException {
        writeTag(tag, name);
        writeDouble(value);
    }

    void writeElement(Object value) throws IOException {
        if (value instanceof MessageSerializable) {
            MessageSerializable message = (MessageSerializable) value;
            pw.println("{");
            indent++;
            message.writeTo(this);
            indent--;
            pw.println();
            indent();
            pw.println("}");
        } else if (value instanceof List || value instanceof Set) {
            writeListOrSet((Collection<?>) value);
        } else if (value instanceof MessageEnum) {
            MessageEnum e = (MessageEnum) value;
            pw.print("\"" + e.getName() + "\"");
        } else if (value instanceof String) {
            writeString((String) value);
        } else if (value instanceof Binary) {
            byte[] b = ((Binary) value).toByteArray();
            pw.print("\"" + SimpleBase64.encode(b) + "\"");
        } else if (value instanceof Long) {
            writeInt64((Long) value);
        } else if (value instanceof Integer) {
            writeInt32((Integer) value);
        } else if (value instanceof Double) {
            writeDouble((Double) value);
        } else if (value instanceof Float) {
            writeFloat((Float) value);
        } else if (value instanceof Boolean) {
            writeBool((Boolean) value);
        } else {
            throw new IOException("Don't know how to write instances of " + value.getClass());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeEnum(int tag, String name, MessageEnum enumValue) throws IOException {
        writeString(tag, name, enumValue.getName());
    }

    void writeFloat(float value) throws IOException {
        if (!Float.isFinite(value)) {
            throw new IOException("Cannot write float value " + value);
        }
        pw.write(Float.toString(value));
    }

    /** {@inheritDoc} */
    @Override
    public void writeFloat(int tag, String name, float value) throws IOException {
        writeTag(tag, name);
        writeFloat(value);
    }

    void writeInt32(int value) {
        pw.write(Integer.toString(value));
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt32(int tag, String name, int value) throws IOException {
        writeTag(tag, name);
        writeInt32(value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt64(int tag, String name, long value) throws IOException {
        writeTag(tag, name);
        writeInt64(value);
    }

    void writeInt64(long value) {
        pw.write(Long.toString(value));
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public void writeList(int tag, String name, List<?> list) throws IOException {
        writeTag(tag, name);
        writeListOrSet(requireNonNull(list, "list is null"));
    }

    void writeListOrSet(Collection<?> list) throws IOException {
        pw.print("[");
        pw.println();
        indent++;
        boolean isFirst = true;
        for (Object o : list) {
            if (!isFirst) {
                pw.println(",");
            }
            indent();
            writeElement(o);
            isFirst = false;
        }
        pw.println();
        indent--;
        indent();
        pw.append("]");
    }

    /** {@inheritDoc} */
    @Override
    public void writeMap(int tag, String name, Map<?, ?> map) {}

    /** {@inheritDoc} */
    @Override
    public void writeMessage(int tag, String name, MessageSerializable message) throws IOException {
        if (message != null) {
            indent();
            pw.println("\"" + name + "\": {");
            indent++;
            message.writeTo(this);
            indent--;
            pw.println();
            indent();
            pw.print("}");
            pw.flush();
        }
    }

    public void writeMessage(MessageSerializable message) throws IOException {
        writeElement(message);
    }

    @Override
    public void writeSet(int tag, String name, Set<?> set) throws IOException {
        writeTag(tag, name);
        writeListOrSet(requireNonNull(set, "set is null"));
    }

    /** {@inheritDoc} */
    @Override
    public void writeString(int tag, String name, String value) throws IOException {
        writeTag(tag, name);
        writeString(requireNonNull(value, "value is null"));
    }

    void writeString(String value) {
        pw.write('"');
        pw.write(value);
        pw.write('"');
    }

    private void writeTag(int tag, String name) {
        indent();
        pw.print("\"" + name + "\": ");
    }
}
