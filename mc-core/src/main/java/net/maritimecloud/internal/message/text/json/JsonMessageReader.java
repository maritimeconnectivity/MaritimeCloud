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
import java.io.StringReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonReader;
import net.maritimecloud.internal.core.javax.json.JsonString;
import net.maritimecloud.internal.core.javax.json.JsonValue;
import net.maritimecloud.internal.core.javax.json.spi.JsonProvider;
import net.maritimecloud.internal.message.AbstractMessageReader;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageEnum;
import net.maritimecloud.message.MessageEnumSerializer;
import net.maritimecloud.message.MessageFormatType;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.SerializationException;
import net.maritimecloud.message.ValueReader;
import net.maritimecloud.message.ValueSerializer;

/**
 *
 * @author Kasper Nielsen
 */
public class JsonMessageReader extends AbstractMessageReader {

    final JsonIterator iter;

    final JsonReader r;

    public JsonMessageReader(CharSequence s) {
        this(JsonProvider.provider().createReader(new StringReader(s.toString())));
    }

    /** {@inheritDoc} */
    @Override
    public final MessageFormatType getFormatType() {
        return MessageFormatType.HUMAN_READABLE;
    }

    public JsonMessageReader(JsonObject o) {
        this.r = null;
        this.iter = new JsonIterator(o);
    }

    public JsonMessageReader(JsonReader r) {
        this.r = requireNonNull(r);
        iter = new JsonIterator(r.readObject());
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {}

    protected ValueReader find(int tag, String name) throws SerializationException {
        if (isNext(-1, name)) {
            return new JsonValueReader(iter.next().getValue());
        }
        throw new SerializationException("Could not find tag '" + name + "'");
    }

    protected ValueReader findOptional(int tag, String name) {
        if (isNext(-1, name)) {
            return new JsonValueReader(iter.next().getValue());
        }
        return null;
    }

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
    public <T extends MessageEnum> T readEnum(int tag, String name, MessageEnumSerializer<T> factory)
            throws IOException {
        ValueReader r = find(tag, name);

        return factory.from(r.readText());
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
    public <T extends Message> T readMessage(int tag, String name, MessageSerializer<T> parser) throws IOException {
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

    // A Quick hack
    public static <T> T readFromString(String value, ValueSerializer<T> parser) throws IOException {
        String ss = " { \"x\": " + value + "}";
        JsonReader reader = JsonProvider.provider().createReader(new StringReader(ss));
        JsonObject o = reader.readObject();
        JsonValue val = o.get("x");
        ValueReader r = new JsonValueReader(val);
        return parser.read(r);
    }

    static class JsonIterator {
        Iterator<Entry<String, net.maritimecloud.internal.core.javax.json.JsonValue>> iter;

        private Entry<String, net.maritimecloud.internal.core.javax.json.JsonValue> next;

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

        Entry<String, net.maritimecloud.internal.core.javax.json.JsonValue> next() {
            Entry<String, net.maritimecloud.internal.core.javax.json.JsonValue> n = next;
            if (iter.hasNext()) {
                next = iter.next();
            } else {
                next = null;
            }
            return n;
        }

        Entry<String, net.maritimecloud.internal.core.javax.json.JsonValue> peek() {
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
// public static ValueReader readFromString(String value) {
// String ss = " { \"x\": " + value + "}";
// JsonReader reader = JsonProvider.provider().createReader(new StringReader(ss));
// JsonObject o = reader.readObject();
// JsonValue val = o.get("x");
// return new JsonValueReader(val);
// }
