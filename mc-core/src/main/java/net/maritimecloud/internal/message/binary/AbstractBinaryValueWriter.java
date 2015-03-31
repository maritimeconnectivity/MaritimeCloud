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

import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.MessageEnum;
import net.maritimecloud.message.MessageFormatType;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractBinaryValueWriter implements ValueWriter {

    /** {@inheritDoc} */
    @Override
    public final void writeBoolean(Boolean value) throws IOException {
        writeInt(value ? 1 : 0);
    }

    /** {@inheritDoc} */
    @Override
    public final void writeDouble(Double value) throws IOException {
        writeInt64(Double.doubleToLongBits(MessageHelper.checkDouble(value)));
    }

    /** {@inheritDoc} */
    @Override
    public final void writeEnum(MessageEnum serializable) throws IOException {
        writeInt(serializable.getValue());
    }

    /** {@inheritDoc} */
    @Override
    public final MessageFormatType getFormatType() {
        return MessageFormatType.MACHINE_READABLE;
    }

    /** {@inheritDoc} */
    @Override
    public void writeDecimal(BigDecimal value) throws IOException {
        writeBinary(BinaryUtils.encodeBigDecimal(value));
    }

    /** {@inheritDoc} */
    @Override
    public final void writePositionTime(PositionTime value) throws IOException {
        writeBinary(value.toBinary());
    }

    /** {@inheritDoc} */
    @Override
    public final void writePosition(Position value) throws IOException {
        writeBinary(value.toBinary());
    }

    /** {@inheritDoc} */
    @Override
    public final void writeFloat(Float value) throws IOException {
        writeInt(Float.floatToIntBits(MessageHelper.checkFloat(value)));
    }

    /** {@inheritDoc} */
    @Override
    public void writeVarInt(BigInteger value) throws IOException {
        writeBinary(BinaryUtils.encodeBigInteger(value));
    }

    /** {@inheritDoc} */
    @Override
    public final void writeText(String value) throws IOException {
        writeBinary(Binary.copyFromUtf8(value));
    }
}
