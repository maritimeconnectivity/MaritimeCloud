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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageEnum;
import net.maritimecloud.core.message.MessageSerializer;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.core.message.ValueSerializer;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
class JsonMessageWriter extends MessageWriter {

    final JsonValueWriter w;

    JsonMessageWriter(JsonValueWriter w) {
        this.w = w;
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {}

    /** {@inheritDoc} */
    @Override
    public void flush() throws IOException {
        w.w.flush();
    }

    /** {@inheritDoc} */
    @Override
    public void writeBinary(int tag, String name, Binary binary) throws IOException {
        if (binary != null) {
            w.writeTag(tag, name).writeBinary(binary);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeBoolean(int tag, String name, Boolean value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writeBoolean(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeDecimal(int tag, String name, BigDecimal value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writeDecimal(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeDouble(int tag, String name, Double value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writeDouble(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeEnum(int tag, String name, MessageEnum value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writeEnum(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeFloat(int tag, String name, Float value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writeFloat(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt(int tag, String name, Integer value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writeInt(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt64(int tag, String name, Long value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writeInt64(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public <T> void writeList(int tag, String name, List<T> list, ValueSerializer<T> serializer) throws IOException {
        if (list != null && list.size() > 0) {
            w.writeTag(tag, name).writeList(list, serializer);
        }
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> void writeMap(int tag, String name, Map<K, V> map, ValueSerializer<K> keySerializer,
            ValueSerializer<V> valueSerializer) throws IOException {
        if (map != null && map.size() > 0) {
            w.writeTag(tag, name).writeMap(map, keySerializer, valueSerializer);
        }
    }


    /** {@inheritDoc} */
    @Override
    public <T extends Message> void writeMessage(int tag, String name, T message,
            MessageSerializer<T> serializer) throws IOException {
        if (message != null) {
            w.writeTag(tag, name).writeMessage(message, serializer);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writePosition(int tag, String name, Position value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writePosition(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writePositionTime(int tag, String name, PositionTime value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writePositionTime(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public <T> void writeSet(int tag, String name, Set<T> value, ValueSerializer<T> serializer) throws IOException {
        if (value != null && value.size() > 0) {
            w.writeTag(tag, name).writeSet(value, serializer);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeText(int tag, String name, String value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writeText(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeTimestamp(int tag, String name, Date value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writeTimestamp(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeVarInt(int tag, String name, BigInteger value) throws IOException {
        if (value != null) {
            w.writeTag(tag, name).writeVarInt(value);
        }
    }


}
