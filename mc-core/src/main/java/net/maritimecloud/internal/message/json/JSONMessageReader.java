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
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.maritimecloud.core.message.MessageEnum;
import net.maritimecloud.core.message.MessageEnumParser;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializable;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class JSONMessageReader extends MessageReader {

    JSONObject o;

    ArrayDeque<JSONObject> stack = new ArrayDeque<>();

    final JSONTokener t;

    public JSONMessageReader(JSONTokener t) {
        this.t = requireNonNull(t);
        o = new JSONObject(t);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNext(int tag, String name) {
        System.out.println(name);
        String s = (String) o.keys().next();
        return name.equals(s);
    }

    /** {@inheritDoc} */
    @Override
    public Entry<Integer, String> nextTag() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Enum<T> & MessageEnum> T readEnum(int tag, String name, MessageEnumParser<T> factory)
            throws IOException {
        return factory.from(name);
    }

    /** {@inheritDoc} */
    @Override
    public <T> List<T> readList(int tag, String name, MessageParser<T> parser) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends MessageSerializable> T readMessage(int tag, String name, MessageParser<T> parser)
            throws IOException {
        stack.push(o);
        o = (JSONObject) o.get(name);
        T result = parser.parse(this);
        o = stack.pop();
        return result;
    }

    // /** {@inheritDoc} */
    // @Override
    // public double readRequiredDouble(int tag, String name) throws IOException {
    // Object object = o.get(name);
    // return (Double) object;
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public float readRequiredFloat(int tag, String name) throws IOException {
    // Object object = o.get(name);
    // return ((Double) object).floatValue();
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public int readRequiredInt32(int tag, String name) throws IOException {
    // Object object = o.get(name);
    // return (Integer) object;
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public <T extends MessageSerializable> T readRequiredMessage(int tag, String name, MessageParser<T> parser)
    // throws IOException {
    // return readMessage(tag, name, parser);
    // }

    public static JSONMessageReader create(CharSequence cs) {
        return new JSONMessageReader(new JSONTokener(cs.toString()));
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {}

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
    public <K, V> Map<K, V> readMap(int tag, String name, MessageParser<K> keyParser, MessageParser<V> valueParser) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Boolean readBool(int tag, String name, Boolean defaultValue) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Float readFloat(int tag, String name, Float defaultValue) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Double readDouble(int tag, String name, Double defaultValue) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt32(int tag, String name, Integer defaultValue) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64(int tag, String name, Long defaultValue) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Binary readBinary(int tag, String name, Binary defaultValue) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String readString(int tag, String name, String defaultValue) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public float readRequiredFloat(int tag, String name) {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public double readRequiredDouble(int tag, String name) {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public int readRequiredInt32(int tag, String name) throws IOException {
        return 0;
    }
}
