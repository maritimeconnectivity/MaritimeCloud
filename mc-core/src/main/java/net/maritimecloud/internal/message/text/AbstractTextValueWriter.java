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
package net.maritimecloud.internal.message.text;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageEnum;
import net.maritimecloud.message.MessageFormatType;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractTextValueWriter implements ValueWriter {

    protected String escape(String value) {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public final MessageFormatType getFormatType() {
        return MessageFormatType.HUMAN_READABLE;
    }

    /** {@inheritDoc} */
    @Override
    public final void writeBinary(Binary binary) throws IOException {
        writeEscapedString(binary.base64encode());
    }

    /** {@inheritDoc} */
    @Override
    public final void writeDecimal(BigDecimal value) throws IOException {
        writeNumber(value.toString());
    }

    /** {@inheritDoc} */
    @Override
    public void writeDouble(Double value) throws IOException {
        writeNumber(Double.toString(MessageHelper.checkDouble(value)));
    }

    /** {@inheritDoc} */
    @Override
    public final void writeEnum(MessageEnum serializable) throws IOException {
        writeEscapedString(serializable.getName());
    }

    protected abstract void writeEscapedString(String value) throws IOException;

    /** {@inheritDoc} */
    @Override
    public void writeFloat(Float value) throws IOException {
        writeNumber(Float.toString(MessageHelper.checkFloat(value)));
    }

    /** {@inheritDoc} */
    @Override
    public final void writeInt(Integer value) throws IOException {
        writeNumber(value.toString());
    }

    /** {@inheritDoc} */
    @Override
    public final void writeInt64(Long value) throws IOException {
        writeNumber(value.toString());
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> void writeMap(Map<K, V> map, ValueSerializer<K> keySerializer, ValueSerializer<V> valueSerializer)
            throws IOException {}

    /** {@inheritDoc} */
    @Override
    public <T extends Message> void writeMessage(T message, MessageSerializer<T> serializer) throws IOException {}

    protected abstract void writeNumber(String value) throws IOException;

    /** {@inheritDoc} */
    @Override
    public void writePosition(Position value) throws IOException {
        writeMessage(value, Position.SERIALIZER);
    }

    /** {@inheritDoc} */
    @Override
    public void writePositionTime(PositionTime value) throws IOException {
        writeMessage(value, PositionTime.SERIALIZER);
    }

    /** {@inheritDoc} */
    @Override
    public final void writeText(String value) throws IOException {
        requireNonNull(value, "value is null");
        writeEscapedString(escape(value));
    }

    /** {@inheritDoc} */
    @Override
    public final void writeVarInt(BigInteger value) throws IOException {
        writeNumber(value.toString());
    }
}
