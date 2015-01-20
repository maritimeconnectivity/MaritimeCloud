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

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 * Interface for reading messages.
 *
 * @author Kasper Nielsen
 */
public interface MessageReader extends Closeable {

    MessageFormatType getFormatType();

    default void close() throws IOException {};

    boolean isNext(int tag, String name) throws IOException;

    Binary readBinary(int tag, String name, Binary defaultValue) throws IOException;

    Boolean readBoolean(int tag, String name, Boolean defaultValue) throws IOException;

    BigDecimal readDecimal(int tag, String name) throws IOException;


    BigDecimal readDecimal(int tag, String name, BigDecimal defaultValue) throws IOException;

    double readDouble(int tag, String name) throws IOException;

    Double readDouble(int tag, String name, Double defaultValue) throws IOException;

    <T extends MessageEnum> T readEnum(int tag, String name, MessageEnumSerializer<T> factory) throws IOException;

    float readFloat(int tag, String name) throws IOException;

    Float readFloat(int tag, String name, Float defaultValue) throws IOException;

    int readInt(int tag, String name) throws IOException;

    Integer readInt(int tag, String name, Integer defaultValue) throws IOException;

    long readInt64(int tag, String name) throws IOException;

    Long readInt64(int tag, String name, Long defaultValue) throws IOException;

    <T> List<T> readList(int tag, String name, ValueSerializer<T> parser) throws IOException;

    <K, V> Map<K, V> readMap(int tag, String name, ValueSerializer<K> keyParser, ValueSerializer<V> valueParser)
            throws IOException;

    <T extends Message> T readMessage(int tag, String name, MessageSerializer<T> parser) throws IOException;

    Position readPosition(int tag, String name) throws IOException;

    Position readPosition(int tag, String name, Position defaultValue) throws IOException;

    PositionTime readPositionTime(int tag, String name) throws IOException;

    PositionTime readPositionTime(int tag, String name, PositionTime defaultValue) throws IOException;

    <T> Set<T> readSet(int tag, String name, ValueSerializer<T> parser) throws IOException;

    String readText(int tag, String name, String defaultValue) throws IOException;

    Timestamp readTimestamp(int tag, String name) throws IOException;

    Timestamp readTimestamp(int tag, String name, Timestamp defaultValue) throws IOException;

    BigInteger readVarInt(int tag, String name) throws IOException;

    BigInteger readVarInt(int tag, String name, BigInteger defaultValue) throws IOException;
}

// int readInt32(int tag, String name, int defaultValue) throws IOException;


// float readRequiredFloat(int tag, String name) throws IOException;
//
// int readRequiredInt32(int tag, String name) throws IOException;
//
// double readRequiredDouble(int tag, String name) throws IOException;
//
// <T extends Message> T readRequiredMessage(int tag, String name, MessageParser<T> parser)
// throws IOException;

// <K, V> Map<K, V> readMap(int tag, String name, MsdlParser<K> keyParser, MsdlParser<K> valueParser);
// <T> List<T> readList(int tag, String name, Class<T> type);
