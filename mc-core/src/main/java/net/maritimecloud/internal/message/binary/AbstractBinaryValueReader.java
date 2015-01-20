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

import net.maritimecloud.message.MessageEnum;
import net.maritimecloud.message.MessageEnumSerializer;
import net.maritimecloud.message.MessageFormatType;
import net.maritimecloud.message.ValueReader;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 * An abstract implementation of {@link ValueReader} that reads from binary streams.
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractBinaryValueReader implements ValueReader {

    /** {@inheritDoc} */
    @Override
    public final Boolean readBoolean() throws IOException {
        Integer i = readInt();
        return i == 0 ? false : true;
    }

    /** {@inheritDoc} */
    @Override
    public final Double readDouble() throws IOException {
        // Hvad hvis int er variable encoded. Det giver jo ikke mening for doubles.
        // da vi saa vil gemme som 64 bit
        long l = readInt64();
        return Double.longBitsToDouble(l);
    }

    /** {@inheritDoc} */
    @Override
    public final <T extends /* Enum<T> & */MessageEnum> T readEnum(MessageEnumSerializer<T> factory) throws IOException {
        Integer i = readInt();
        return factory.from(i);
    }

    /** {@inheritDoc} */
    @Override
    public final Float readFloat() throws IOException {
        int i = readInt();
        return Float.intBitsToFloat(i);
    }

    /** {@inheritDoc} */
    @Override
    public final MessageFormatType getFormatType() {
        return MessageFormatType.MACHINE_READABLE;
    }

    /** {@inheritDoc} */
    @Override
    public final Position readPosition() throws IOException {
        return Position.fromBinary(readBinary());
    }

    /** {@inheritDoc} */
    @Override
    public final PositionTime readPositionTime() throws IOException {
        return PositionTime.fromBinary(readBinary());
    }

    /** {@inheritDoc} */
    @Override
    public final String readText() throws IOException {
        return readBinary().toStringUtf8();
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readVarInt() throws IOException {
        Binary b = readBinary();
        return BinaryUtils.decodeBigInteger(b);
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal() throws IOException {
        Binary b = readBinary();
        return BinaryUtils.decodeBigDecimal(b);
    }
}
