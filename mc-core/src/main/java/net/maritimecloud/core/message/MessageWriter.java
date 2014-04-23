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
package net.maritimecloud.core.message;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.util.Binary;


/**
 * Abstract class for writing to message streams.
 *
 * @author Kasper Nielsen
 */
public abstract class MessageWriter implements Closeable, Flushable {

    /** {@inheritDoc} */
    public void flush() throws IOException {}

    public boolean isCompact() {
        return true;
    }

    public abstract void writeBinary(int tag, String name, Binary binary) throws IOException;

    public final void writeBinary(int tag, String name, byte[] bytes) throws IOException {
        writeBinary(tag, name, bytes, 0, bytes.length);
    }

    public void writeBinary(int tag, String name, byte[] bytes, int offset, int length) throws IOException {
        writeBinary(tag, name, Binary.copyFrom(bytes, offset, length));
    }

    public void writeBinary(int tag, String name, ByteBuffer buffer) throws IOException {
        writeBinary(tag, name, Binary.copyFrom(buffer));
    }

    /**
     * Writes a boolean.
     *
     * @param tag
     *            the tag value
     * @param name
     *            the tag name
     * @param value
     *            the boolean value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeBool(int tag, String name, Boolean value) throws IOException;

    /**
     * Writes a double.
     *
     * @param tag
     *            the tag value
     * @param name
     *            the tag name
     * @param value
     *            the double value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeDouble(int tag, String name, Double value) throws IOException;

    public abstract void writeEnum(int tag, String name, MessageEnum serializable) throws IOException;

    /**
     * Writes a float.
     *
     * @param tag
     *            the tag value
     * @param name
     *            the tag name
     * @param value
     *            the float value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeFloat(int tag, String name, Float value) throws IOException;

    /**
     * Writes an integer.
     *
     * @param tag
     *            the tag value
     * @param name
     *            the tag name
     * @param value
     *            the integer value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeInt32(int tag, String name, Integer value) throws IOException;

    /**
     * Writes a long.
     *
     * @param tag
     *            the tag value
     * @param name
     *            the tag name
     * @param value
     *            the long value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeInt64(int tag, String name, Long value) throws IOException;

    /**
     * Writes a list.
     *
     * @param tag
     *            the tag value
     * @param name
     *            the tag name
     * @param list
     *            the list to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeList(int tag, String name, List<?> list) throws IOException;

    /**
     * Writes a map.
     *
     * @param tag
     *            the tag value
     * @param name
     *            the tag name
     * @param list
     *            the map to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeMap(int tag, String name, Map<?, ?> map) throws IOException;

    /**
     * Writes the specified message if it is non-null.
     *
     * @param tag
     *            the tag value
     * @param name
     *            the tag name
     * @param value
     *            the message to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeMessage(int tag, String name, MessageSerializable message) throws IOException;

    /**
     * Writes a set.
     *
     * @param tag
     *            the tag value
     * @param name
     *            the tag name
     * @param list
     *            the set to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeSet(int tag, String name, Set<?> set) throws IOException;

    /**
     * Writes a string.
     *
     * @param tag
     *            the tag value
     * @param name
     *            the tag name
     * @param value
     *            the string value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeString(int tag, String name, String value) throws IOException;
}
//
//
// public <T extends List<?> & MessageList> void writeList2(int tag, String name, T t) {
//
// }
//
// public <T> void writeList(int tag, String name, List<? extends T> list, MLS<T> mls) {
//
// List<Integer> ll = new ArrayList<>();
// writeList(tag, name, ll, MLS.TO_INT);
// }

