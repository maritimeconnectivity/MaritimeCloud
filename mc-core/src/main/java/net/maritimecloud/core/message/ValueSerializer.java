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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class ValueSerializer<T> {

    public static final ValueSerializer<Binary> BINARY = new ValueSerializer<Binary>() {
        public Binary read(ValueReader reader) throws IOException {
            return reader.readBinary();
        }

        @Override
        public void write(Binary t, ValueWriter writer) throws IOException {
            writer.writeBinary(t);
        }
    };

    public static final ValueSerializer<Boolean> BOOLEAN = new ValueSerializer<Boolean>() {
        public Boolean read(ValueReader reader) throws IOException {
            return reader.readBoolean();
        }

        @Override
        public void write(Boolean t, ValueWriter writer) throws IOException {
            writer.writeBoolean(t);
        }
    };

    public static final ValueSerializer<BigDecimal> DECIMAL = new ValueSerializer<BigDecimal>() {
        public BigDecimal read(ValueReader reader) throws IOException {
            return reader.readDecimal();
        }

        @Override
        public void write(BigDecimal t, ValueWriter writer) throws IOException {
            writer.writeDecimal(t);
        }
    };

    public static final ValueSerializer<Double> DOUBLE = new ValueSerializer<Double>() {
        public Double read(ValueReader reader) throws IOException {
            return reader.readDouble();
        }

        @Override
        public void write(Double t, ValueWriter writer) throws IOException {
            writer.writeDouble(t);
        }
    };

    public static final ValueSerializer<Float> FLOAT = new ValueSerializer<Float>() {
        public Float read(ValueReader reader) throws IOException {
            return reader.readFloat();
        }

        @Override
        public void write(Float t, ValueWriter writer) throws IOException {
            writer.writeFloat(t);
        }
    };

    public static final ValueSerializer<Integer> INT = new ValueSerializer<Integer>() {
        public Integer read(ValueReader reader) throws IOException {
            return reader.readInt();
        }

        @Override
        public void write(Integer t, ValueWriter writer) throws IOException {
            writer.writeInt(t);
        }
    };

    public static final ValueSerializer<Long> INT64 = new ValueSerializer<Long>() {
        public Long read(ValueReader reader) throws IOException {
            return reader.readInt64();
        }

        @Override
        public void write(Long t, ValueWriter writer) throws IOException {
            writer.writeInt64(t);
        }
    };


    public static final ValueSerializer<Position> POSITION = new ValueSerializer<Position>() {
        public Position read(ValueReader reader) throws IOException {
            return reader.readPosition();
        }

        @Override
        public void write(Position t, ValueWriter writer) throws IOException {
            writer.writePosition(t);
        }
    };

    public static final ValueSerializer<PositionTime> POSITION_TIME = new ValueSerializer<PositionTime>() {
        public PositionTime read(ValueReader reader) throws IOException {
            return reader.readPositionTime();
        }

        @Override
        public void write(PositionTime t, ValueWriter writer) throws IOException {
            writer.writePositionTime(t);
        }
    };

    public static final ValueSerializer<String> TEXT = new ValueSerializer<String>() {
        public String read(ValueReader reader) throws IOException {
            return reader.readText();
        }

        @Override
        public void write(String t, ValueWriter writer) throws IOException {
            writer.writeText(t);
        }
    };

    public static final ValueSerializer<Date> TIMESTAMP = new ValueSerializer<Date>() {
        public Date read(ValueReader reader) throws IOException {
            return reader.readTimestamp();
        }

        @Override
        public void write(Date t, ValueWriter writer) throws IOException {
            writer.writeTimestamp(t);
        }
    };

    public static final ValueSerializer<BigInteger> VARINT = new ValueSerializer<BigInteger>() {
        public BigInteger read(ValueReader reader) throws IOException {
            return reader.readVarInt();
        }

        @Override
        public void write(BigInteger t, ValueWriter writer) throws IOException {
            writer.writeVarInt(t);
        }
    };

    public final ValueSerializer<List<T>> listOf() {
        return new ListSerializer<>(this);
    }

    public final <V> ValueSerializer<Map<T, V>> mappingTo(ValueSerializer<V> valueParser) {
        return new MapSerializer<>(this, valueParser);
    }

    /**
     * Parses a message from the specified reader
     *
     * @param reader
     *            the serializer to create the from
     * @return the message that was constructed from the reader
     * @throws IOException
     *             if the message could not be read
     */
    public abstract T read(ValueReader reader) throws IOException;

    public final ValueSerializer<Set<T>> setOf() {
        return new SetSerializer<>(this);
    }

    public abstract void write(T t, ValueWriter writer) throws IOException;

    // public static ValueSerializer<?> serializerOf(Class<?> type) {
    // if (type == String.class) {
    // return TEXT;
    // }
    // throw new UnsupportedOperationException();
    // }

    static class ListSerializer<E> extends ValueSerializer<List<E>> {
        final ValueSerializer<E> serializer;

        ListSerializer(ValueSerializer<E> serializer) {
            this.serializer = requireNonNull(serializer);
        }

        /** {@inheritDoc} */
        @Override
        public List<E> read(ValueReader reader) throws IOException {
            return reader.readList(serializer);
        }

        /** {@inheritDoc} */
        @Override
        public void write(List<E> t, ValueWriter writer) throws IOException {
            writer.writeList(t, serializer);
        }
    }

    static class MapSerializer<K, V> extends ValueSerializer<Map<K, V>> {
        final ValueSerializer<K> keyParser;

        final ValueSerializer<V> valueParser;

        MapSerializer(ValueSerializer<K> keyParser, ValueSerializer<V> valueParser) {
            this.keyParser = requireNonNull(keyParser);
            this.valueParser = requireNonNull(valueParser);
        }

        /** {@inheritDoc} */
        @Override
        public Map<K, V> read(ValueReader reader) throws IOException {
            return reader.readMap(keyParser, valueParser);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Map<K, V> t, ValueWriter writer) throws IOException {
            writer.writeMap(t, keyParser, valueParser);
        }
    }

    static class SetSerializer<E> extends ValueSerializer<Set<E>> {
        final ValueSerializer<E> serializer;

        SetSerializer(ValueSerializer<E> serializer) {
            this.serializer = requireNonNull(serializer);
        }

        /** {@inheritDoc} */
        @Override
        public Set<E> read(ValueReader reader) throws IOException {
            return reader.readSet(serializer);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Set<E> t, ValueWriter writer) throws IOException {
            writer.writeSet(t, serializer);
        }
    }
}
//
// static class MessageValueParser<T extends MessageSerializable> extends ValueParser<T> {
// private final MessageParser<T> serializer;
//
// MessageValueParser(MessageParser<T> serializer) {
// this.serializer = requireNonNull(serializer);
// }
//
// /** {@inheritDoc} */
// @Override
// public T parse(ValueReader reader) throws IOException {
// return reader.readMessage(serializer);
// }
// }
