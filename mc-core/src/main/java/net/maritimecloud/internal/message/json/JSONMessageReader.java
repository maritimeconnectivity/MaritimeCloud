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
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.spi.JsonProvider;

import net.maritimecloud.core.message.MessageEnum;
import net.maritimecloud.core.message.MessageEnumParser;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
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
public class JSONMessageReader extends MessageReader {

    JsonIterator iter;

    final JsonReader r;

    JSONMessageReader(JSONMessageReader r, JsonObject o) {
        this.r = r.r;
        iter = new JsonIterator(o);
    }

    JSONMessageReader(JsonObject o) {
        this.r = null;
        this.iter = new JsonIterator(o);
    }

    public JSONMessageReader(String s) {
        this(JsonProvider.provider().createReader(new StringReader(s)));
    }

    public JSONMessageReader(JsonReader r) {
        this.r = requireNonNull(r);
        iter = new JsonIterator(r.readObject());
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {}

    /** {@inheritDoc} */
    @Override
    public boolean isNext(int tag, String name) {
        requireNonNull(name, "name is null");
        if (iter.hasNext()) {
            return name.equals(iter.peek().getKey());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public Binary readBinary(int tag, String name, Binary defaultValue) throws IOException {
        if (isNext(-1, name)) {
            JsonString val = (JsonString) iter.next().getValue();
            return Binary.copyFromBase64(val.getString());
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public Boolean readBoolean(int tag, String name, Boolean defaultValue) {
        if (isNext(-1, name)) {
            JsonValue val = iter.next().getValue();
            return Boolean.parseBoolean(val.toString());
        }
        return defaultValue;
    }

    // A Quick hack
    public static <T> T readFromString(String value, ValueParser<T> parser) throws IOException {
        String ss = " { \"x\": " + value + "}";
        JsonReader reader = JsonProvider.provider().createReader(new StringReader(ss));
        JsonObject o = reader.readObject();
        JsonValue val = o.get("x");
        JsonValueReader r = new JsonValueReader(val);
        return parser.parse(r);
    }

    public static JsonValueReader readFromString(String value) {
        String ss = " { \"x\": " + value + "}";
        JsonReader reader = JsonProvider.provider().createReader(new StringReader(ss));
        JsonObject o = reader.readObject();
        JsonValue val = o.get("x");
        return new JsonValueReader(val);
    }

    /** {@inheritDoc} */
    @Override
    public Double readDouble(int tag, String name, Double defaultValue) {
        if (isNext(-1, name)) {
            JsonNumber val = (JsonNumber) iter.next().getValue();
            return val.doubleValue();
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Enum<T> & MessageEnum> T readEnum(int tag, String name, MessageEnumParser<T> factory)
            throws IOException {
        return factory.from(name);
    }

    /** {@inheritDoc} */
    @Override
    public Float readFloat(int tag, String name, Float defaultValue) {
        if (isNext(-1, name)) {
            JsonNumber val = (JsonNumber) iter.next().getValue();
            return (float) val.doubleValue();
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public int readInt(int tag, String name) throws IOException {
        if (isNext(-1, name)) {
            JsonNumber val = (JsonNumber) iter.next().getValue();
            return val.intValue();
        }
        throw new MessageSerializationException("Could not find tag '" + name + "'");
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt(int tag, String name, Integer defaultValue) throws IOException {
        if (isNext(-1, name)) {
            JsonNumber val = (JsonNumber) iter.next().getValue();
            return val.intValue();
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64(int tag, String name, Long defaultValue) throws IOException {
        if (isNext(-1, name)) {
            JsonNumber val = (JsonNumber) iter.next().getValue();
            return val.longValue();
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public long readInt64(int tag, String name) throws IOException {
        if (isNext(-1, name)) {
            JsonNumber val = (JsonNumber) iter.next().getValue();
            return val.longValue();
        }
        throw new MessageSerializationException("Could not find tag '" + name + "'");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public <T> List<T> readList(int tag, String name, ValueParser<T> parser) throws IOException {
        if (isNext(-1, name)) {
            Entry<String, JsonValue> next = iter.next();
            return new JsonValueReader(next.getValue()).readList(parser);
        }
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public <K, V> Map<K, V> readMap(int tag, String name, ValueParser<K> keyParser, ValueParser<V> valueParser)
            throws IOException {
        if (isNext(-1, name)) {
            Entry<String, JsonValue> next = iter.next();
            return new JsonValueReader(next.getValue()).readMap(keyParser, valueParser);
        }
        return Collections.emptyMap();
    }

    /** {@inheritDoc} */
    @Override
    public <T extends MessageSerializable> T readMessage(int tag, String name, MessageParser<T> parser)
            throws IOException {
        if (isNext(-1, name)) {
            Entry<String, JsonValue> next = iter.next();
            return new JsonValueReader(next.getValue()).readMessage(parser);
            //
            // JsonIterator existing = iter;
            // Entry<String, JsonValue> next = iter.next();
            // JsonObject a = (JsonObject) next.getValue();
            // iter = new JsonIterator(a);
            // T result = parser.parse(this);
            // iter = existing;
            // return result;
        }
        return null;

        // parser.parse(null)
        // stack.push(o);
        // o = (JSONObject) o.get(name);
        // T result = parser.parse(this);
        // o = stack.pop();
        // return result;
    }

    /**
     * {@inheritDoc}
     *
     * @throws MessageSerializationException
     */
    @Override
    public double readRequiredDouble(int tag, String name) throws MessageSerializationException {
        if (isNext(-1, name)) {
            JsonNumber val = (JsonNumber) iter.next().getValue();
            return val.doubleValue();
        }
        throw new MessageSerializationException("Could not find tag '" + tag + "'");
    }

    @Override
    public float readRequiredFloat(int tag, String name) throws MessageSerializationException {
        if (isNext(-1, name)) {
            JsonNumber val = (JsonNumber) iter.next().getValue();
            return (float) val.doubleValue();
        }
        throw new MessageSerializationException("Could not find tag '" + tag + "'");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public <T> Set<T> readSet(int tag, String name, ValueParser<T> parser) throws IOException {
        // parser.
        //
        // for (xxxx) {
        // elementParser.
        // }
        // parser.parse(this);
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String readText(int tag, String name, String defaultValue) throws IOException {
        if (isNext(-1, name)) {
            JsonString val = (JsonString) iter.next().getValue();
            return val.getString();
        }
        return defaultValue;
    }


    static class JsonIterator {
        Iterator<Entry<String, javax.json.JsonValue>> iter;

        private Entry<String, javax.json.JsonValue> next;

        final JsonObject o;

        JsonIterator(JsonObject o) {
            this.o = o;
            iter = o.entrySet().iterator();
            if (iter.hasNext()) {
                next = iter.next();
            }
        }

        boolean hasNext() {
            return next != null;
        }

        Entry<String, javax.json.JsonValue> next() {
            Entry<String, javax.json.JsonValue> n = next;
            if (iter.hasNext()) {
                next = iter.next();
            } else {
                next = null;
            }
            return n;
        }

        Entry<String, javax.json.JsonValue> peek() {
            return next;
        }
    }

    static class JsonValueReader extends ValueReader {
        JsonValue value;

        JsonValueReader(JsonValue value) {
            this.value = requireNonNull(value);
        }

        /** {@inheritDoc} */
        @Override
        public Boolean readBoolean() throws IOException {
            return Boolean.parseBoolean(value.toString());
        }

        /** {@inheritDoc} */
        @Override
        public Float readFloat() throws IOException {
            return Float.parseFloat(value.toString());
        }

        /** {@inheritDoc} */
        @Override
        public Double readDouble() throws IOException {
            return Double.parseDouble(value.toString());
        }

        /** {@inheritDoc} */
        @Override
        public Integer readInt() throws IOException {
            return Integer.parseInt(value.toString());
        }

        /** {@inheritDoc} */
        @Override
        public Long readInt64() throws IOException {
            return Long.parseLong(value.toString());
        }

        /** {@inheritDoc} */
        @Override
        public Binary readBinary() throws IOException {
            return Binary.copyFromBase64(((JsonString) value).getString());
        }

        /** {@inheritDoc} */
        @Override
        public String readText() throws IOException {
            String val = ((JsonString) value).getString();
            return val;
        }

        /** {@inheritDoc} */
        @Override
        public <T extends Enum<T> & MessageEnum> T readEnum(MessageEnumParser<T> factory) throws IOException {
            return factory.from(value.toString());
        }

        /** {@inheritDoc} */
        @Override
        public <T extends MessageSerializable> T readMessage(MessageParser<T> parser) throws IOException {
            JsonObject o = (JsonObject) value;
            JSONMessageReader r = new JSONMessageReader(o);
            return parser.parse(r);
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
        public <T> Set<T> readSet(ValueParser<T> parser) throws IOException {
            return new HashSet<>(readList(parser));
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
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readVarInt(int tag, String name) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal(int tag, String name) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Position readPostion(int tag, String name) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public PositionTime readPositionTime(int tag, String name) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Date readTimestamp(int tag, String name) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal(int tag, String name, BigDecimal defaultValue) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Date readTimestamp(int tag, String name, Date defaultValue) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Position readPosition(int tag, String name, Position defaultValue) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public PositionTime readPositionTime(int tag, String name, PositionTime defaultValue) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readVarInt(int tag, String name, BigInteger defaultValue) throws IOException {
        return null;
    }
}

final class JsonStringImpl implements JsonString {
    private final String value;

    JsonStringImpl(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }

    public CharSequence getChars() {
        return this.value;
    }

    public JsonValue.ValueType getValueType() {
        return JsonValue.ValueType.STRING;
    }

    public int hashCode() {
        return getString().hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof JsonString)) {
            return false;
        }
        JsonString other = (JsonString) obj;
        return getString().equals(other.getString());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('"');

        for (int i = 0; i < this.value.length(); ++i) {
            char c = this.value.charAt(i);

            if (c >= ' ' && c <= 1114111 && c != '"' && c != '\\') {
                sb.append(c);
            } else {
                switch (c) {
                case '"':
                case '\\':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '\b':
                    sb.append('\\');
                    sb.append('b');
                    break;
                case '\f':
                    sb.append('\\');
                    sb.append('f');
                    break;
                case '\n':
                    sb.append('\\');
                    sb.append('n');
                    break;
                case '\r':
                    sb.append('\\');
                    sb.append('r');
                    break;
                case '\t':
                    sb.append('\\');
                    sb.append('t');
                    break;
                default:
                    String hex = new StringBuilder().append("000").append(Integer.toHexString(c)).toString();
                    sb.append("\\u").append(hex.substring(hex.length() - 4));
                }
            }
        }

        sb.append('"');
        return sb.toString();
    }
}
