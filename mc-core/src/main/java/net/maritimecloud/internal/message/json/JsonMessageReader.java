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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.spi.JsonProvider;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageEnum;
import net.maritimecloud.core.message.MessageEnumSerializer;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializer;
import net.maritimecloud.core.message.SerializationException;
import net.maritimecloud.core.message.ValueSerializer;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public class JsonMessageReader extends MessageReader {

    final JsonIterator iter;

    final JsonReader r;

    JsonMessageReader(JsonMessageReader r, JsonObject o) {
        this.r = r.r;
        iter = new JsonIterator(o);
    }

    JsonMessageReader(JsonObject o) {
        this.r = null;
        this.iter = new JsonIterator(o);
    }

    public JsonMessageReader(JsonReader r) {
        this.r = requireNonNull(r);
        iter = new JsonIterator(r.readObject());
    }

    public JsonMessageReader(CharSequence s) {
        this(JsonProvider.provider().createReader(new StringReader(s.toString())));
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

    private JsonValueReader read(String name) throws SerializationException {
        if (isNext(-1, name)) {
            return new JsonValueReader(iter.next().getValue());
        }
        throw new SerializationException("Could not find tag '" + name + "'");
    }

    /** {@inheritDoc} */
    @Override
    public Binary readBinary(int tag, String name, Binary defaultValue) throws IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readBinary();
    }

    /** {@inheritDoc} */
    @Override
    public Boolean readBoolean(int tag, String name, Boolean defaultValue) throws IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readBoolean();
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal(int tag, String name) throws IOException {
        return read(name).readDecimal();
    }


    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal(int tag, String name, BigDecimal defaultValue) throws IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readDecimal();
    }

    /** {@inheritDoc} */
    @Override
    public Double readDouble(int tag, String name, Double defaultValue) throws IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readDouble();
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Enum<T> & MessageEnum> T readEnum(int tag, String name, MessageEnumSerializer<T> factory)
            throws IOException {
        return factory.from(name);
    }

    /** {@inheritDoc} */
    @Override
    public Float readFloat(int tag, String name, Float defaultValue) throws SerializationException, IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readFloat();
    }

    /** {@inheritDoc} */
    @Override
    public int readInt(int tag, String name) throws IOException {
        return read(name).readInt();
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt(int tag, String name, Integer defaultValue) throws IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readInt();
    }

    /** {@inheritDoc} */
    @Override
    public long readInt64(int tag, String name) throws IOException {
        return read(name).readInt64();
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64(int tag, String name, Long defaultValue) throws IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readInt64();
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> List<T> readList(int tag, String name, ValueSerializer<T> parser) throws IOException {
        JsonValueReader r = readOpt(name);
        // The list cast shoudn't be necessary but javac complains for some reason
        return r == null ? (List) Collections.emptyList() : r.readList(parser);
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> Map<K, V> readMap(int tag, String name, ValueSerializer<K> keyParser, ValueSerializer<V> valueParser)
            throws IOException {
        if (isNext(-1, name)) {
            Entry<String, JsonValue> next = iter.next();
            return new JsonValueReader(next.getValue()).readMap(keyParser, valueParser);
        }
        return Collections.emptyMap();
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Message> T readMessage(int tag, String name, MessageSerializer<T> parser)
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

    private JsonValueReader readOpt(String name) {
        if (isNext(-1, name)) {
            return new JsonValueReader(iter.next().getValue());
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Position readPosition(int tag, String name, Position defaultValue) throws IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readPosition();

    }

    /** {@inheritDoc} */
    @Override
    public PositionTime readPositionTime(int tag, String name) throws IOException {
        return read(name).readPositionTime();
    }

    /** {@inheritDoc} */
    @Override
    public PositionTime readPositionTime(int tag, String name, PositionTime defaultValue) throws IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readPositionTime();
    }

    /** {@inheritDoc} */
    @Override
    public Position readPostion(int tag, String name) throws IOException {
        return read(name).readPosition();
    }


    /** {@inheritDoc} */
    @Override
    public double readDouble(int tag, String name) throws IOException {
        return read(name).readDouble();
    }

    /** {@inheritDoc} */
    @Override
    public float readFloat(int tag, String name) throws IOException {
        return read(name).readFloat();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public <T> Set<T> readSet(int tag, String name, ValueSerializer<T> parser) throws IOException {
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
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readText();
    }

    /** {@inheritDoc} */
    @Override
    public Date readTimestamp(int tag, String name) throws IOException {
        return read(name).readTimestamp();
    }

    /** {@inheritDoc} */
    @Override
    public Date readTimestamp(int tag, String name, Date defaultValue) throws IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readTimestamp();
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readVarInt(int tag, String name) throws IOException {
        return read(name).readVarInt();
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readVarInt(int tag, String name, BigInteger defaultValue) throws IOException {
        JsonValueReader r = readOpt(name);
        return r == null ? defaultValue : r.readVarInt();
    }

    public static JsonValueReader readFromString(String value) {
        String ss = " { \"x\": " + value + "}";
        JsonReader reader = JsonProvider.provider().createReader(new StringReader(ss));
        JsonObject o = reader.readObject();
        JsonValue val = o.get("x");
        return new JsonValueReader(val);
    }

    // A Quick hack
    public static <T> T readFromString(String value, ValueSerializer<T> parser) throws IOException {
        String ss = " { \"x\": " + value + "}";
        JsonReader reader = JsonProvider.provider().createReader(new StringReader(ss));
        JsonObject o = reader.readObject();
        JsonValue val = o.get("x");
        JsonValueReader r = new JsonValueReader(val);
        return parser.read(r);
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
}

final class JsonStringImpl implements JsonString {
    private final String value;

    JsonStringImpl(String value) {
        this.value = value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof JsonString)) {
            return false;
        }
        JsonString other = (JsonString) obj;
        return getString().equals(other.getString());
    }

    public CharSequence getChars() {
        return this.value;
    }

    public String getString() {
        return this.value;
    }

    public JsonValue.ValueType getValueType() {
        return JsonValue.ValueType.STRING;
    }

    public int hashCode() {
        return getString().hashCode();
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
