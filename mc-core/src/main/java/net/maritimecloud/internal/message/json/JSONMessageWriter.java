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

/**
 *
 * @author Kasper Nielsen
 */
public class JSONMessageWriter extends MessageWriter {

    /** The Unix line separator. */
    static final String LS = "\n";

    private int indent;

    boolean isFirst = true;

    private final PrintWriter pw;

    public JSONMessageWriter(PrintWriter pw) {
        this.pw = requireNonNull(pw);
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

    private void write(int tag, String name, Collection<?> list) throws IOException {
        indent();
        pw.print("\"" + name + "\": [");
        pw.println();
        indent++;
        boolean isFirst = true;
        for (Object o : list) {
            if (!isFirst) {
                pw.println(",");
            }
            if (o instanceof MessageSerializable) {
                indent();
                pw.append("{");
                pw.println();
                indent++;
                this.isFirst = true;
                ((MessageSerializable) o).writeTo(this);
                indent--;
                pw.println();
                indent();
                pw.append("}");
            }
            isFirst = false;
        }
        pw.println();
        indent--;
        indent();
        pw.append("]");
    }


    private void write(int tag, String name, Object value) {
        if (!isFirst) {
            pw.println(",");
        }
        indent();
        if (value instanceof String) {
            pw.print("\"" + name + "\": \"" + value + "\"");
        } else {
            pw.print("\"" + name + "\": " + value);
        }
        isFirst = false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeBinary(int tag, String name, byte[] bytes, int offset, int length) throws IOException {
        byte[] b = Arrays.copyOfRange(bytes, offset, length);
        writeString(tag, name, SimpleBase64.encode(b));
    }

    /** {@inheritDoc} */
    @Override
    public void writeBool(int tag, String name, boolean value) throws IOException {
        write(tag, name, value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeDouble(int tag, String name, double value) throws IOException {
        write(tag, name, value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeEnum(int tag, String name, MessageEnum enumValue) throws IOException {
        write(tag, name, enumValue.getName());
    }

    /** {@inheritDoc} */
    @Override
    public void writeFloat(int tag, String name, float value) throws IOException {
        write(tag, name, value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt32(int tag, String name, int value) throws IOException {
        write(tag, name, value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt64(int tag, String name, long value) throws IOException {
        write(tag, name, value);
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
            isFirst = true;
            indent++;
            message.writeTo(this);
            indent--;
            pw.println();
            indent();
            pw.print("}");
            pw.flush();
        }
    }

    @Override
    public void writeSet(int tag, String name, Set<?> set) throws IOException {
        write(tag, name, set);
    }

    /** {@inheritDoc} */
    @Override
    public void writeString(int tag, String name, String value) throws IOException {
        write(tag, name, value);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        pw.close();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public void writeList(int tag, String name, List<?> list) throws IOException {
        write(tag, name, list);
    }
}
