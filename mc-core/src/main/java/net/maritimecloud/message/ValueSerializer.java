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

import static java.util.Objects.requireNonNull;

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
 * @param <T>
 *            the type of values to serialize
 * @author Kasper Nielsen
 */
public abstract class ValueSerializer<T> {

    /** A value serializer that can serialize instances of {@link Binary}. */
    public static final ValueSerializer<Binary> BINARY = new ValueSerializer<Binary>() {

        /** {@inheritDoc} */
        @Override
        public Binary read(ValueReader reader) throws IOException {
            return reader.readBinary();
        }

        /** {@inheritDoc} */
        @Override
        public void write(Binary t, ValueWriter writer) throws IOException {
            writer.writeBinary(t);
        }

        @Override
        public void write(int tag, String name, Binary t, MessageWriter writer) throws IOException {
            writer.writeBinary(tag, name, t);
        }
    };

    /** A value serializer that can serialize booleans. */
    public static final ValueSerializer<Boolean> BOOLEAN = new ValueSerializer<Boolean>() {

        /** {@inheritDoc} */
        @Override
        public Boolean read(ValueReader reader) throws IOException {
            return reader.readBoolean();
        }


        /** {@inheritDoc} */
        @Override
        public void write(Boolean t, ValueWriter writer) throws IOException {
            writer.writeBoolean(t);
        }


        @Override
        public void write(int tag, String name, Boolean t, MessageWriter writer) throws IOException {
            writer.writeBoolean(tag, name, t);
        }
    };

    /** A value serializer that can serialize instances of {@link BigDecimal}. */
    public static final ValueSerializer<BigDecimal> DECIMAL = new ValueSerializer<BigDecimal>() {

        /** {@inheritDoc} */
        @Override
        public BigDecimal read(ValueReader reader) throws IOException {
            return reader.readDecimal();
        }

        /** {@inheritDoc} */
        @Override
        public void write(BigDecimal t, ValueWriter writer) throws IOException {
            writer.writeDecimal(t);
        }

        @Override
        public void write(int tag, String name, BigDecimal t, MessageWriter writer) throws IOException {
            writer.writeDecimal(tag, name, t);
        }
    };

    /** A value serializer that can serialize doubles. */
    public static final ValueSerializer<Double> DOUBLE = new ValueSerializer<Double>() {

        /** {@inheritDoc} */
        @Override
        public Double read(ValueReader reader) throws IOException {
            return reader.readDouble();
        }

        /** {@inheritDoc} */
        @Override
        public void write(Double t, ValueWriter writer) throws IOException {
            writer.writeDouble(t);
        }

        @Override
        public void write(int tag, String name, Double t, MessageWriter writer) throws IOException {
            writer.writeDouble(tag, name, t);
        }
    };

    /** A value serializer that can serialize floats. */
    public static final ValueSerializer<Float> FLOAT = new ValueSerializer<Float>() {

        /** {@inheritDoc} */
        @Override
        public Float read(ValueReader reader) throws IOException {
            return reader.readFloat();
        }

        /** {@inheritDoc} */
        @Override
        public void write(Float t, ValueWriter writer) throws IOException {
            writer.writeFloat(t);
        }

        @Override
        public void write(int tag, String name, Float t, MessageWriter writer) throws IOException {
            writer.writeFloat(tag, name, t);
        }
    };

    /** A value serializer that can serialize ints. */
    public static final ValueSerializer<Integer> INT = new ValueSerializer<Integer>() {

        /** {@inheritDoc} */
        @Override
        public Integer read(ValueReader reader) throws IOException {
            return reader.readInt();
        }

        /** {@inheritDoc} */
        @Override
        public void write(Integer t, ValueWriter writer) throws IOException {
            writer.writeInt(t);
        }

        @Override
        public void write(int tag, String name, Integer t, MessageWriter writer) throws IOException {
            writer.writeInt(tag, name, t);
        }
    };

    /** A value serializer that can serialize longs. */
    public static final ValueSerializer<Long> INT64 = new ValueSerializer<Long>() {

        /** {@inheritDoc} */
        @Override
        public Long read(ValueReader reader) throws IOException {
            return reader.readInt64();
        }

        /** {@inheritDoc} */
        @Override
        public void write(Long t, ValueWriter writer) throws IOException {
            writer.writeInt64(t);
        }

        @Override
        public void write(int tag, String name, Long t, MessageWriter writer) throws IOException {
            writer.writeInt64(tag, name, t);
        }
    };

    /** A value serializer that can serialize instances of {@link Position}. */
    public static final ValueSerializer<Position> POSITION = new ValueSerializer<Position>() {

        /** {@inheritDoc} */
        @Override
        public Position read(ValueReader reader) throws IOException {
            return reader.readPosition();
        }

        /** {@inheritDoc} */
        @Override
        public void write(Position t, ValueWriter writer) throws IOException {
            writer.writePosition(t);
        }

        @Override
        public void write(int tag, String name, Position t, MessageWriter writer) throws IOException {
            writer.writePosition(tag, name, t);
        }
    };

    /** A value serializer that can serialize instances of {@link PositionTime}. */
    public static final ValueSerializer<PositionTime> POSITION_TIME = new ValueSerializer<PositionTime>() {

        /** {@inheritDoc} */
        @Override
        public PositionTime read(ValueReader reader) throws IOException {
            return reader.readPositionTime();
        }

        /** {@inheritDoc} */
        @Override
        public void write(PositionTime t, ValueWriter writer) throws IOException {
            writer.writePositionTime(t);
        }

        @Override
        public void write(int tag, String name, PositionTime t, MessageWriter writer) throws IOException {
            writer.writePositionTime(tag, name, t);
        }
    };

    /** A value serializer that can serialize strings. */
    public static final ValueSerializer<String> TEXT = new ValueSerializer<String>() {

        /** {@inheritDoc} */
        @Override
        public String read(ValueReader reader) throws IOException {
            return reader.readText();
        }

        /** {@inheritDoc} */
        @Override
        public void write(String t, ValueWriter writer) throws IOException {
            writer.writeText(t);
        }

        @Override
        public void write(int tag, String name, String t, MessageWriter writer) throws IOException {
            writer.writeText(tag, name, t);
        }
    };

    /** A value serializer that can serialize instances of {@link Timestamp}. */
    public static final ValueSerializer<Timestamp> TIMESTAMP = new ValueSerializer<Timestamp>() {

        /** {@inheritDoc} */
        @Override
        public Timestamp read(ValueReader reader) throws IOException {
            return reader.readTimestamp();
        }

        /** {@inheritDoc} */
        @Override
        public void write(Timestamp t, ValueWriter writer) throws IOException {
            writer.writeTimestamp(t);
        }

        @Override
        public void write(int tag, String name, Timestamp t, MessageWriter writer) throws IOException {
            writer.writeTimestamp(tag, name, t);
        }
    };

    /** A value serializer that can serialize instances of {@link BigInteger}. */
    public static final ValueSerializer<BigInteger> VARINT = new ValueSerializer<BigInteger>() {

        /** {@inheritDoc} */
        @Override
        public BigInteger read(ValueReader reader) throws IOException {
            return reader.readVarInt();
        }

        /** {@inheritDoc} */
        @Override
        public void write(BigInteger t, ValueWriter writer) throws IOException {
            writer.writeVarInt(t);
        }

        @Override
        public void write(int tag, String name, BigInteger t, MessageWriter writer) throws IOException {
            writer.writeVarInt(tag, name, t);
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

    public abstract void write(int tag, String name, T t, MessageWriter writer) throws IOException;

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

        /** {@inheritDoc} */
        @Override
        public void write(int tag, String name, List<E> t, MessageWriter writer) throws IOException {
            writer.writeList(tag, name, t, serializer);
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

        /** {@inheritDoc} */
        @Override
        public void write(int tag, String name, Map<K, V> t, MessageWriter writer) throws IOException {
            writer.writeMap(tag, name, t, keyParser, valueParser);
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

        /** {@inheritDoc} */
        @Override
        public void write(int tag, String name, Set<E> t, MessageWriter writer) throws IOException {
            writer.writeSet(tag, name, t, serializer);
        }
    }
}
