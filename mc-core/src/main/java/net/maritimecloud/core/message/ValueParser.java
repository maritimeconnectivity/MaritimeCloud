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
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class ValueParser<T> {

    public static final ValueParser<Binary> BINARY = new ValueParser<Binary>() {
        public Binary parse(ValueReader reader) throws IOException {
            return reader.readBinary();
        }
    };

    public static final ValueParser<Boolean> BOOL = new ValueParser<Boolean>() {
        public Boolean parse(ValueReader reader) throws IOException {
            return reader.readBool();
        }
    };;

    public static final ValueParser<Double> DOUBLE = new ValueParser<Double>() {
        public Double parse(ValueReader reader) throws IOException {
            return reader.readDouble();
        }
    };;

    public static final ValueParser<Float> FLOAT = new ValueParser<Float>() {
        public Float parse(ValueReader reader) throws IOException {
            return reader.readFloat();
        }
    };;

    public static final ValueParser<Integer> INT32 = new ValueParser<Integer>() {
        public Integer parse(ValueReader reader) throws IOException {
            return reader.readInt32();
        }
    };;

    public static final ValueParser<Long> INT64 = new ValueParser<Long>() {
        public Long parse(ValueReader reader) throws IOException {
            return reader.readInt64();
        }
    };

    public static final ValueParser<String> STRING = new ValueParser<String>() {
        public String parse(ValueReader reader) throws IOException {
            return reader.readString();
        }
    };;

    public final ValueParser<List<T>> listOf() {
        return new ListParser<>(this);
    }

    public final <V> ValueParser<Map<T, V>> mappingTo(ValueParser<V> valueParser) {
        return new MapParser<>(this, valueParser);
    }

    /**
     * Parses a message from the specified reader
     *
     * @param reader
     *            the parser to create the from
     * @return the message that was constructed from the reader
     * @throws IOException
     *             if the message could not be read
     */
    public abstract T parse(ValueReader reader) throws IOException;

    public static ValueParser<?> parserOf(Class<?> type) {
        if (type == String.class) {
            return STRING;
        }
        throw new UnsupportedOperationException();
    }

    public final ValueParser<Set<T>> setOf() {
        return new SetParser<>(this);
    }

    static class ListParser<E> extends ValueParser<List<E>> {
        final ValueParser<E> parser;

        ListParser(ValueParser<E> parser) {
            this.parser = requireNonNull(parser);
        }

        /** {@inheritDoc} */
        @Override
        public List<E> parse(ValueReader reader) throws IOException {
            return reader.readList(parser);
        }
    }

    static class MapParser<K, V> extends ValueParser<Map<K, V>> {
        final ValueParser<K> keyParser;

        final ValueParser<V> valueParser;

        MapParser(ValueParser<K> keyParser, ValueParser<V> valueParser) {
            this.keyParser = requireNonNull(keyParser);
            this.valueParser = requireNonNull(valueParser);
        }

        /** {@inheritDoc} */
        @Override
        public Map<K, V> parse(ValueReader reader) throws IOException {
            return reader.readMap(keyParser, valueParser);
        }
    }

    static class SetParser<E> extends ValueParser<Set<E>> {
        final ValueParser<E> parser;

        SetParser(ValueParser<E> parser) {
            this.parser = requireNonNull(parser);
        }

        /** {@inheritDoc} */
        @Override
        public Set<E> parse(ValueReader reader) throws IOException {
            return reader.readSet(parser);
        }
    }
}
//
// static class MessageValueParser<T extends MessageSerializable> extends ValueParser<T> {
// private final MessageParser<T> parser;
//
// MessageValueParser(MessageParser<T> parser) {
// this.parser = requireNonNull(parser);
// }
//
// /** {@inheritDoc} */
// @Override
// public T parse(ValueReader reader) throws IOException {
// return reader.readMessage(parser);
// }
// }
