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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.JsonObject;
import javax.json.JsonReader;

import net.maritimecloud.core.message.MessageEnum;
import net.maritimecloud.core.message.MessageEnumParser;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializable;
import net.maritimecloud.core.message.MessageSerializationException;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class JSONMessageReader extends MessageReader {

    final JsonIterator iter;

    final JsonReader r;

    JSONMessageReader(JSONMessageReader r, JsonObject o) {
        this.r = r.r;
        iter = new JsonIterator(o);
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
            return Binary.copyFromBase64(iter.next().getValue().toString());
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public Boolean readBool(int tag, String name, Boolean defaultValue) {
        if (isNext(-1, name)) {
            return Boolean.parseBoolean(iter.next().getValue().toString());
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public Double readDouble(int tag, String name, Double defaultValue) {
        if (isNext(-1, name)) {
            return Double.parseDouble(iter.next().getValue().toString());
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
            return Float.parseFloat(iter.next().getValue().toString());
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt32(int tag, String name, Integer defaultValue) throws IOException {
        if (isNext(-1, name)) {
            return Integer.parseInt(iter.next().getValue().toString());
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64(int tag, String name, Long defaultValue) throws IOException {
        if (isNext(-1, name)) {
            return Long.parseLong(iter.next().getValue().toString());
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public <T> List<T> readList(int tag, String name, MessageParser<T> parser) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> Map<K, V> readMap(int tag, String name, MessageParser<K> keyParser, MessageParser<V> valueParser) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends MessageSerializable> T readMessage(int tag, String name, MessageParser<T> parser)
            throws IOException {
        // stack.push(o);
        // o = (JSONObject) o.get(name);
        // T result = parser.parse(this);
        // o = stack.pop();
        // return result;
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public double readRequiredDouble(int tag, String name) {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public float readRequiredFloat(int tag, String name) {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public int readInt32(int tag, String name) throws IOException {
        if (isNext(-1, name)) {
            return Integer.parseInt(iter.next().getValue().toString());
        }
        throw new MessageSerializationException("Could not find tag '" + name + "'");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public <T> Set<T> readSet(int tag, String name, MessageParser<T> parser) throws IOException {
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
    public String readString(int tag, String name, String defaultValue) throws IOException {
        return null;
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
