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
package net.maritimecloud.internal.message.binary.compact;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 *
 * @author Kasper Nielsen
 */
public class BinaryOutputStream extends Types {

    final OutputStream os;

    BinaryOutputStream(OutputStream os) {
        this.os = requireNonNull(os);
    }

    public BinaryOutputStream encodeAndWriteInteger(int tag, int value) throws IOException {
        switch (value) {
        case 0:
            return writeTypeTag(WIRETYPE_CONSTANT_0, tag);
        case 1:
            return writeTypeTag(WIRETYPE_CONSTANT_1, tag);
        case 2:
            return writeTypeTag(WIRETYPE_CONSTANT_2, tag);
        case -1:
            return writeTypeTag(WIRETYPE_CONSTANT_MINUS_1, tag);
        default:
            if (value > 0) { // POSITIVE
                if (value <= Byte.MAX_VALUE) {
                    return writeTypeTag(WIRETYPE_FIXED_8, tag).write08Bits(value);
                } else if (value <= Short.MAX_VALUE) {
                    return writeTypeTag(WIRETYPE_FIXED_16, tag).write16Bits(value);
                } else if ((value & 0xffffffff << 21) == 0) {
                    return writeTypeTag(WIRETYPE_VARINT_POSITIVE, tag).writeRawVarint32(value);
                }
            } else {
                if (value >= Byte.MIN_VALUE) {
                    return writeTypeTag(WIRETYPE_FIXED_8, tag).write08Bits(value);
                } else if (value >= Short.MIN_VALUE) {
                    return writeTypeTag(WIRETYPE_FIXED_16, tag).write16Bits(value);
                } else if ((value & 0xffffffff << 21) == 0) {
                    return writeTypeTag(WIRETYPE_VARINT_NEGATIVE, tag).writeRawVarint32Negative(value);
                }
            }
            return writeTypeTag(WIRETYPE_FIXED_32, tag).write32Bits(value);
        }
    }

    public void flush() throws IOException {
        os.flush();
    }

    public BinaryOutputStream write08Bits(int value) throws IOException {
        return writeRawByte(value & 0xFF);
    }

    public BinaryOutputStream write16Bits(int value) throws IOException {
        writeRawByte(value >> 8 & 0xFF);
        return writeRawByte(value & 0xFF);
    }

    public BinaryOutputStream write24Bits(int value) throws IOException {
        writeRawByte(value >> 16 & 0xFF);
        writeRawByte(value >> 8 & 0xFF);
        return writeRawByte(value & 0xFF);
    }

    public BinaryOutputStream write32Bits(int value) throws IOException {
        writeRawByte(value >> 24 & 0xFF);
        writeRawByte(value >> 16 & 0xFF);
        writeRawByte(value >> 8 & 0xFF);
        return writeRawByte(value & 0xFF);
    }

    public BinaryOutputStream write64Bits(long value) throws IOException {
        writeRawByte((int) (value >> 56) & 0xFF);
        writeRawByte((int) (value >> 48) & 0xFF);
        writeRawByte((int) (value >> 40) & 0xFF);
        writeRawByte((int) (value >> 32) & 0xFF);
        writeRawByte((int) (value >> 24) & 0xFF);
        writeRawByte((int) (value >> 16) & 0xFF);
        writeRawByte((int) (value >> 8) & 0xFF);
        return writeRawByte((int) value & 0xFF);
    }

    public void writeBigDecimal(BigDecimal value) throws IOException {
        writeRawVarint32(value.scale());
        // writeBigInteger(value.unscaledValue());
    }

    public BinaryOutputStream writeBoolean(int tag, String name, boolean value) throws IOException {
        return writeTypeTag(value ? WIRETYPE_CONSTANT_1 : WIRETYPE_CONSTANT_0, tag);
    }

    public void writeBytes(byte[] value) throws IOException {
        os.write(value);
    }

    public void writeBytes(int tag, byte[] value) throws IOException {
        if (value.length == 1) {
            encodeAndWriteInteger(tag, value[0]);
        } else if (value.length == 2) {
            writeTypeTag(WIRETYPE_FIXED_16, tag).writeRawByte(value[0]).writeRawByte(value[1]);
        } else if (value.length == 4) {
            writeTypeTag(WIRETYPE_FIXED_32, tag).writeRawByte(value[0]).writeRawByte(value[1]).writeRawByte(value[2])
            .writeRawByte(value[3]);
        } else if (value.length == 8) {
            writeTypeTag(WIRETYPE_FIXED_64, tag).writeRawByte(value[0]).writeRawByte(value[1]).writeRawByte(value[2])
                    .writeRawByte(value[3]).writeRawByte(value[4]).writeRawByte(value[5]).writeRawByte(value[6])
                    .writeRawByte(value[7]);
        } else {
            writeTypeTag(WIRETYPE_NUMBER_OF_BYTES, tag);
            writeVarInt32Positive(value.length);
            os.write(value);
        }
    }

    public BinaryOutputStream writeLong(int tag, long value) {
        return this;
    }

    /** Write a single byte. */
    BinaryOutputStream writeRawByte(final byte value) throws IOException {
        os.write(value);
        return this;
    }

    /** Write a single byte, represented by an integer value. */
    BinaryOutputStream writeRawByte(final int value) throws IOException {
        writeRawByte((byte) value);
        return this;
    }

    /**
     * Encode and write a varint. {@code value} is treated as unsigned, so it won't be sign-extended if negative.
     */
    BinaryOutputStream writeRawVarint32(int value) throws IOException {
        while (true) {
            if ((value & ~0x7F) == 0) {
                writeRawByte(value);
                return this;
            } else {
                writeRawByte(value & 0x7F | 0x80);
                value >>>= 7;
            }
        }
    }

    BinaryOutputStream writeRawVarint32Negative(int value) throws IOException {
        while (true) {
            if ((value & ~0x7F) == 0) {
                writeRawByte(value);
                return this;
            } else {
                writeRawByte(value & 0x7F | 0x80);
                value >>>= 7;
            }
        }
    }

    /** Encode and write a varint. */
    BinaryOutputStream writeRawVarint64(long value) throws IOException {
        while (true) {
            if ((value & ~0x7FL) == 0) {
                return writeRawByte((int) value);
            } else {
                writeRawByte((int) value & 0x7F | 0x80);
                value >>>= 7;
            }
        }
    }

    BinaryOutputStream writeTypeTag(int wireType, int fieldNumber) throws IOException {
        if (fieldNumber <= 15) {
            writeRawByte(makeTag(wireType, fieldNumber));
        } else {
            writeRawByte(makeTag(wireType, 0));
            writeRawVarint32(fieldNumber);
        }
        return this;
    }

    void writeVarInt32(int value) {

    }

    void writeVarInt32Positive(int value) throws IOException {
        writeRawVarint32(value);
    }


}
