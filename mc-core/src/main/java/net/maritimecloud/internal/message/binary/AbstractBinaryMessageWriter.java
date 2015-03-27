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
package net.maritimecloud.internal.message.binary;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageEnum;
import net.maritimecloud.message.MessageFormatType;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 * An abstract class for a binary message writer.
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractBinaryMessageWriter implements MessageWriter {

    /** {@inheritDoc} */
    @Override
    public final MessageFormatType getFormatType() {
        return MessageFormatType.MACHINE_READABLE;
    }

    protected abstract void writeBinary(int tag, byte[] bin) throws IOException;

    /** {@inheritDoc} */
    @Override
    public void writeBinary(int tag, String name, Binary binary) throws IOException {
        if (binary != null && !binary.isEmpty()) {
            writeBinary(tag, binary.toByteArray());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeDecimal(int tag, String name, BigDecimal value) throws IOException {
        if (value != null) {
            writeDecimal0(tag, value);
        }
    }

    protected abstract void writeDecimal0(int tag, BigDecimal bd) throws IOException;

    /** {@inheritDoc} */
    @Override
    public final void writeDouble(int tag, String name, Double value) throws IOException {
        if (value != null) {
            writeDouble0(tag, MessageHelper.checkDouble(value));
        }
    }

    protected void writeDouble0(int tag, double value) throws IOException {
        long val = Double.doubleToLongBits(MessageHelper.checkDouble(value));
        writeInt640(tag, val);
    }

    /** {@inheritDoc} */
    @Override
    public final void writeEnum(int tag, String name, MessageEnum serializable) throws IOException {
        if (serializable != null) {
            writeInt0(tag, serializable.getValue());
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void writeFloat(int tag, String name, Float value) throws IOException {
        if (value != null) {
            writeFloat0(tag, MessageHelper.checkFloat(value));
        }
    }

    protected void writeFloat0(int tag, float value) throws IOException {
        int val = Float.floatToIntBits(MessageHelper.checkFloat(value));
        writeInt0(tag, val);
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt(int tag, String name, Integer value) throws IOException {
        if (value != null) {
            writeInt0(tag, value);
        }
    }

    protected abstract void writeInt0(int tag, int value) throws IOException;

    /** {@inheritDoc} */
    @Override
    public final void writeInt64(int tag, String name, Long value) throws IOException {
        if (value != null) {
            writeInt640(tag, value);
        }
    }

    protected abstract void writeInt640(int tag, long value) throws IOException;

    protected abstract void writeInt640(int tag, String name, long value) throws IOException;

    /** {@inheritDoc} */
    @Override
    public final <T> void writeList(int tag, String name, List<T> list, ValueSerializer<T> serializer)
            throws IOException {
        if (list != null && list.size() > 0) {
            writeSetOrList(tag, list, serializer);
        }
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> void writeMap(int tag, String name, Map<K, V> map, ValueSerializer<K> keySerializer,
            ValueSerializer<V> valueSerializer) throws IOException {
        if (map != null && map.size() > 0) {
            writeMap0(tag, map, keySerializer, valueSerializer);
        }
    }


    protected abstract <K, V> void writeMap0(int tag, Map<K, V> map, ValueSerializer<K> keySerializer,
            ValueSerializer<V> valueSerializer) throws IOException;

    /** {@inheritDoc} */
    @Override
    public final <T extends Message> void writeMessage(int tag, String name, T message, MessageSerializer<T> serializer)
            throws IOException {
        if (message != null) {
            writeMessage0(tag, message, serializer);
        }
    }

    protected abstract <T extends Message> void writeMessage0(int tag, T message, MessageSerializer<T> serializer)
            throws IOException;

    /** {@inheritDoc} */
    @Override
    public void writePosition(int tag, String name, Position value) throws IOException {
        if (value != null) {
            writeBinary(tag, name, value.toBinary());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writePositionTime(int tag, String name, PositionTime value) throws IOException {
        if (value != null) {
            writeBinary(tag, name, value.toBinary());
        }
    }

    /** {@inheritDoc} */
    @Override
    public final <T> void writeSet(int tag, String name, Set<T> set, ValueSerializer<T> serializer) throws IOException {
        if (set != null && set.size() > 0) {
            writeSetOrList(tag, set, serializer);
        }
    }

    protected abstract <T> void writeSetOrList(int tag, Collection<T> set, ValueSerializer<T> serializer)
            throws IOException;

    /** {@inheritDoc} */
    @Override
    public void writeText(int tag, String name, String value) throws IOException {
        if (value != null) {
            byte[] text = value.getBytes("UTF-8");
            writeBinary(tag, text);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void writeTimestamp(int tag, String name, Timestamp value) throws IOException {
        if (value != null) {
            writeInt640(tag, name, value.getTime());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeVarInt(int tag, String name, BigInteger value) throws IOException {
        if (value != null) {
            // We write it as an integer if it can be represented as a long, otherwise as a byte array
            if (value.bitLength() < 64) {
                writeInt640(tag, value.longValueExact());
            } else {
                writeBinary(tag, name, BinaryUtils.encodeBigInteger(value));
            }
        }
    }
}
