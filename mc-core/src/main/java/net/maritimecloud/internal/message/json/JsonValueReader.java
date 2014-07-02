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
package net.maritimecloud.internal.message.json;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import net.maritimecloud.core.message.MessageEnum;
import net.maritimecloud.core.message.MessageEnumParser;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageSerializable;
import net.maritimecloud.core.message.MessageSerializationException;
import net.maritimecloud.core.message.ValueParser;
import net.maritimecloud.core.message.ValueReader;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public class JsonValueReader extends ValueReader {

    final JsonValue value;

    JsonValueReader(JsonValue value) {
        this.value = requireNonNull(value);
    }

    /** {@inheritDoc} */
    @Override
    public Binary readBinary() throws IOException {
        if (value instanceof JsonString) {
            JsonString v = (JsonString) value;
            return Binary.copyFromBase64(v.getString());
        }
        throw new MessageSerializationException("Was not a string");
    }

    /** {@inheritDoc} */
    @Override
    public Boolean readBoolean() throws IOException {
        if (value == JsonValue.TRUE) {
            return Boolean.TRUE;
        } else if (value == JsonValue.FALSE) {
            return Boolean.FALSE;
        }
        throw new MessageSerializationException("Was not a boolean " + value.getClass());
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return v.bigDecimalValue();
        }
        throw new MessageSerializationException("Was not a number");
    }

    /** {@inheritDoc} */
    @Override
    public Double readDouble() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return v.doubleValue();
        }
        throw new MessageSerializationException("Was not a number");
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Enum<T> & MessageEnum> T readEnum(MessageEnumParser<T> factory) throws IOException {
        return factory.from(value.toString());
    }

    /** {@inheritDoc} */
    @Override
    public Float readFloat() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return (float) v.doubleValue();
        }
        throw new MessageSerializationException("Was not a number");
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return v.intValue();
        }
        throw new MessageSerializationException("Was not a number");
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return v.longValue();
        }
        throw new MessageSerializationException("Was not a number");
    }

    /** {@inheritDoc} */
    @Override
    public <T> List<T> readList(ValueParser<T> parser) throws IOException {
        ArrayList<T> result = new ArrayList<>();
        JsonArray a = (JsonArray) value;
        for (int i = 0; i < a.size(); i++) {
            T t = parser.parse(new JsonValueReader(a.get(i)));
            result.add(t);
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> Map<K, V> readMap(ValueParser<K> keyParser, ValueParser<V> valueParser) throws IOException {
        Map<K, V> result = new HashMap<>();
        JsonObject a = (JsonObject) value;
        for (Map.Entry<String, JsonValue> e : a.entrySet()) {
            JsonStringImpl kImpl = new JsonStringImpl(e.getKey());
            K key = keyParser.parse(new JsonValueReader(kImpl));
            V value = valueParser.parse(new JsonValueReader(e.getValue()));
            result.put(key, value);
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends MessageSerializable> T readMessage(MessageParser<T> parser) throws IOException {
        JsonObject o = (JsonObject) value;
        JsonMessageReader r = new JsonMessageReader(o);
        return parser.parse(r);
    }

    /** {@inheritDoc} */
    @Override
    public Position readPosition() throws IOException {
        return readMessage(Position.PARSER);
    }

    /** {@inheritDoc} */
    @Override
    public PositionTime readPositionTime() throws IOException {
        return readMessage(PositionTime.PARSER);
    }

    /** {@inheritDoc} */
    @Override
    public <T> Set<T> readSet(ValueParser<T> parser) throws IOException {
        return new HashSet<>(readList(parser));
    }

    /** {@inheritDoc} */
    @Override
    public String readText() throws IOException {
        if (value instanceof JsonString) {
            JsonString v = (JsonString) value;
            return v.getString();
        }
        throw new MessageSerializationException("Was not a string");
    }

    /** {@inheritDoc} */
    @Override
    public Date readTimestamp() throws IOException {
        return new Date(readInt64());
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readVarInt() throws IOException {
        if (value instanceof JsonNumber) {
            JsonNumber v = (JsonNumber) value;
            return v.bigIntegerValue();
        }
        throw new MessageSerializationException("Was not a number");
    }
}
