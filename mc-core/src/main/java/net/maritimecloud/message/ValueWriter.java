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
package net.maritimecloud.message;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public interface ValueWriter {

    MessageFormatType getFormatType();

    @SuppressWarnings("unused")
    default void close() throws IOException {}

    @SuppressWarnings("unused")
    default void flush() throws IOException {}

    void writeBinary(Binary binary) throws IOException;

    /**
     * Writes a boolean.
     *
     * @param value
     *            the boolean value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    void writeBoolean(Boolean value) throws IOException;

    void writeDecimal(BigDecimal value) throws IOException;

    /**
     * Writes a double.
     *
     * @param value
     *            the double value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    void writeDouble(Double value) throws IOException;

    void writeEnum(MessageEnum serializable) throws IOException;

    /**
     * Writes a float.
     *
     * @param value
     *            the float value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    void writeFloat(Float value) throws IOException;

    /**
     * Writes an integer.
     *
     * @param value
     *            the integer value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    void writeInt(Integer value) throws IOException;

    /**
     * Writes a long.
     *
     * @param value
     *            the long value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    void writeInt64(Long value) throws IOException;

    /**
     * Writes a list.
     *
     * @param list
     *            the list to write
     * @param serializer
     *            the serializer for each element
     * @param <T>
     *            the type of elements
     * @throws IOException
     *             If an I/O error occurs
     */
    <T> void writeList(List<T> list, ValueSerializer<T> serializer) throws IOException;

    /**
     * Writes a map.
     *
     * @param map
     *            the map to write
     * @param keySerializer
     *            the serializer for each key
     * @param valueSerializer
     *            the serializer for each value
     * @param <K>
     *            the type of keys in the map
     * @param <V>
     *            the type of values in the map
     * @throws IOException
     *             If an I/O error occurs
     */
    <K, V> void writeMap(Map<K, V> map, ValueSerializer<K> keySerializer, ValueSerializer<V> valueSerializer)
            throws IOException;

    /**
     * Writes the specified message if it is non-null.
     *
     * @param message
     *            the message to write
     * @param serializer
     *            the serializer for the message
     * @param <T>
     *            the type of message
     * @throws IOException
     *             If an I/O error occurs
     */
    <T extends Message> void writeMessage(T message, MessageSerializer<T> serializer) throws IOException;

    void writePosition(Position value) throws IOException;


    void writePositionTime(PositionTime value) throws IOException;

    /**
     * Writes a set.
     *
     * @param set
     *            the set to write
     * @param serializer
     *            the serializer for each element
     * @param <T>
     *            the type of elements
     * @throws IOException
     *             If an I/O error occurs
     */
    default <T> void writeSet(Set<T> set, ValueSerializer<T> serializer) throws IOException {
        writeList(new ArrayList<>(set), serializer);
    }

    /**
     * Writes a string.
     *
     * @param value
     *            the string value to write
     * @throws IOException
     *             If an I/O error occurs
     */
    void writeText(String value) throws IOException;

    default void writeTimestamp(Timestamp value) throws IOException {
        writeInt64(value.getTime());
    }

    void writeVarInt(BigInteger value) throws IOException;
}
