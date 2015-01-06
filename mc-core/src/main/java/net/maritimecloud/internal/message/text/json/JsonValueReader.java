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
package net.maritimecloud.internal.message.text.json;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.maritimecloud.internal.core.javax.json.JsonArray;
import net.maritimecloud.internal.core.javax.json.JsonNumber;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonString;
import net.maritimecloud.internal.core.javax.json.JsonValue;
import net.maritimecloud.internal.message.text.AbstractTextValueReader;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.SerializationException;
import net.maritimecloud.message.ValueSerializer;

/**
 *
 * @author Kasper Nielsen
 */
public class JsonValueReader extends AbstractTextValueReader {

    final JsonValue value;

    JsonValueReader(JsonValue value) {
        this.value = requireNonNull(value);
    }

    /** {@inheritDoc} */
    @Override
    public Boolean readBoolean() throws IOException {
        if (value == JsonValue.TRUE) {
            return Boolean.TRUE;
        } else if (value == JsonValue.FALSE) {
            return Boolean.FALSE;
        }
        throw new SerializationException("Was not a boolean " + value.getClass());
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return v.bigDecimalValue();
        }
        throw new SerializationException("Was not a number");
    }

    /** {@inheritDoc} */
    @Override
    public Double readDouble() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return v.doubleValue();
        }
        throw new SerializationException("Was not a number");
    }

    /** {@inheritDoc} */
    @Override
    public Float readFloat() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return (float) v.doubleValue();
        }
        throw new SerializationException("Was not a number");
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return v.intValue();
        }
        throw new SerializationException("Was not a number");
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return v.longValue();
        }
        throw new SerializationException("Was not a number");
    }

    /** {@inheritDoc} */
    @Override
    public <T> List<T> readList(ValueSerializer<T> parser) throws IOException {
        ArrayList<T> result = new ArrayList<>();
        JsonArray a = (JsonArray) value;
        for (int i = 0; i < a.size(); i++) {
            T t = parser.read(new JsonValueReader(a.get(i)));
            result.add(t);
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> Map<K, V> readMap(ValueSerializer<K> keyParser, ValueSerializer<V> valueParser) throws IOException {
        Map<K, V> result = new HashMap<>();
        JsonObject a = (JsonObject) value;
        for (Map.Entry<String, JsonValue> e : a.entrySet()) {
            JsonStringImpl kImpl = new JsonStringImpl(e.getKey());
            K key = keyParser.read(new JsonValueReader(kImpl));
            V value = valueParser.read(new JsonValueReader(e.getValue()));
            result.put(key, value);
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Message> T readMessage(MessageSerializer<T> parser) throws IOException {
        JsonObject o = (JsonObject) value;
        JsonMessageReader r = new JsonMessageReader(o);
        return parser.read(r);
    }

    /** {@inheritDoc} */
    @Override
    protected String readString() throws IOException {
        if (value instanceof JsonString) {
            JsonString v = (JsonString) value;
            return v.getString();
        }
        throw new SerializationException("Was not a string");
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readVarInt() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return v.bigIntegerValue();
        }
        throw new SerializationException("Was not a number");
    }
}
