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
public abstract class MessageParser<T> {

    public static final MessageParser<Long> OF_INT64 = null;

    public static final MessageParser<Integer> OF_INT32 = null;

    public static final MessageParser<Double> OF_DOUBLE = null;

    public static final MessageParser<Float> OF_FLOAT = null;

    public static final MessageParser<Binary> OF_BINARY = null;

    public static final MessageParser<String> OF_STRING = null;

    public static final MessageParser<Boolean> OF_BOOL = null;

    /**
     * Parses a message from the specified reader
     *
     * @param reader
     *            the parser to create the from
     * @return the message that was constructed from the reader
     * @throws IOException
     *             if the message could not be read
     */
    public abstract T parse(MessageReader reader) throws IOException;

    public static <E> MessageParser<Set<E>> ofSet(MessageParser<E> elementParser) {
        return null;
    }

    public static <E> MessageParser<List<E>> ofList(MessageParser<E> elementParser) {
        return null;
    }

    public static <K, V> MessageParser<Map<K, V>> ofMap(MessageParser<K> keyParser, MessageParser<V> valueParser) {
        return null;
    }

    static class MapParser<K, V> extends MessageParser<Map<K, V>> {
        final MessageParser<K> keyParser;

        final MessageParser<K> valueParser;

        MapParser(MessageParser<K> keyParser, MessageParser<K> valueParser) {
            this.keyParser = requireNonNull(keyParser);
            this.valueParser = requireNonNull(valueParser);
        }

        /** {@inheritDoc} */
        @Override
        public Map<K, V> parse(MessageReader reader) throws IOException {

            return null;
            // return reader.readList(-1, null, parser);
        }
    }

    static class ListParser<E> extends MessageParser<List<E>> {
        final MessageParser<E> parser;

        ListParser(MessageParser<E> parser) {
            this.parser = requireNonNull(parser);
        }

        /** {@inheritDoc} */
        @Override
        public List<E> parse(MessageReader reader) throws IOException {
            return reader.readList(-1, null, parser);
        }
    }
}
