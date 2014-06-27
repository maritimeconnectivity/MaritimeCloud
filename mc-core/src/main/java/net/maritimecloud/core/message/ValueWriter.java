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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public interface ValueWriter {

    public abstract void writeBinary(Binary binary) throws IOException;

    /**
     * Writes a boolean.
     * 
     * @param value
     *            the boolean value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    public abstract void writeBoolean(Boolean value) throws IOException;

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
    public abstract void writeDouble(Double value) throws IOException;

    public abstract void writeEnum(MessageEnum serializable) throws IOException;

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
    public abstract void writeFloat(Float value) throws IOException;

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
    public abstract void writeInt(Integer value) throws IOException;

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
    public abstract void writeInt64(Long value) throws IOException;

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
    public abstract void writeList(List<?> list) throws IOException;

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
    public abstract void writeMap(Map<?, ?> map) throws IOException;

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
    public abstract void writeMessage(MessageSerializable message) throws IOException;

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
    public abstract void writeSet(Set<?> set) throws IOException;

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
    public abstract void writeText(String value) throws IOException;
}
