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
import java.util.LinkedHashSet;
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
public interface ValueReader {
    MessageFormatType getFormatType();

    Integer readInt() throws IOException;

    Long readInt64() throws IOException;

    BigInteger readVarInt() throws IOException;

    Float readFloat() throws IOException;

    Double readDouble() throws IOException;

    BigDecimal readDecimal() throws IOException;

    Boolean readBoolean() throws IOException;

    Binary readBinary() throws IOException;

    String readText() throws IOException;

    default Timestamp readTimestamp() throws IOException {
        long timestamp = readInt64();
        if (timestamp < 0) {
            throw new SerializationException("Read a negative timestamp, must be positive, was " + timestamp);
        }
        return Timestamp.create(timestamp);
    }

    Position readPosition() throws IOException;

    PositionTime readPositionTime() throws IOException;


    <T extends /* Enum<T> & */MessageEnum> T readEnum(MessageEnumSerializer<T> factory) throws IOException;

    <T extends Message> T readMessage(MessageSerializer<T> parser) throws IOException;

    <T> List<T> readList(ValueSerializer<T> parser) throws IOException;

    default <T> Set<T> readSet(ValueSerializer<T> parser) throws IOException {
        return new LinkedHashSet<>(readList(parser));
    }

    <K, V> Map<K, V> readMap(ValueSerializer<K> keyParser, ValueSerializer<V> valueParser) throws IOException;
}
