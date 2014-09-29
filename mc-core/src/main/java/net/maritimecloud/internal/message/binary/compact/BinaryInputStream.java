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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class BinaryInputStream extends Types {

    int lastWireType;

    int fieldNumber;

    boolean consumed = true;

    final InputStream is;

    public BinaryInputStream(Binary bytes) {
        this(bytes.toByteArray());
    }

    public BinaryInputStream(byte[] bytes) {
        this(new ByteArrayInputStream(bytes));
    }


    public BinaryInputStream(InputStream is) {
        this.is = requireNonNull(is);
    }

    public int readInt() {
        // return encodeAndWriteInteger(tag, name, Float.floatToIntBits(value));
        return 0;
    }

    int getWireType() throws IOException {
        if (consumed) {
            readHeader();
        }
        return lastWireType;
    }

    int getFieldId() throws IOException {
        if (consumed) {
            readHeader();
        }
        return fieldNumber;
    }

    byte[] read(int bytes) throws IOException {
        byte[] b = new byte[bytes];
        for (int i = 0; i < bytes; i++) {
            b[i] = readRawByte();
        }
        consumed = true;
        return b;
    }

    void readHeader() throws IOException {
        int b = readRawByte();
        lastWireType = Types.getWireType(b);
        if ((b & ~TAG_TYPE_MASK) == 0) {
            fieldNumber = readInt();
        } else {
            fieldNumber = Types.getTagFieldNumber(b);
        }
        consumed = false;
    }


    int readVarint32() throws IOException {
        return (int) readRawVarint64SlowPath();
    }

    /** Variant of readRawVarint64 for when uncomfortably close to the limit. */
    /* Visible for testing */
    long readRawVarint64SlowPath() throws IOException {
        long result = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            final byte b = readRawByte();
            result |= (long) (b & 0x7F) << shift;
            if ((b & 0x80) == 0) {
                consumed = true;
                return result;
            }
        }
        throw new RuntimeException();
    }

    private byte readRawByte() throws IOException {
        return (byte) is.read();
    }


    public int read08Bits() throws IOException {
        int result = readRawByte() & 0xff;
        consumed = true;
        return result;
    }

    public int read16Bits() throws IOException {
        int result = (readRawByte() & 0xff) << 8 << 16 >> 16; // <<16>>16 for sign extension
        result |= readRawByte() & 0xff;
        consumed = true;
        return result;
    }

    public int read32Bits() throws IOException {
        int result = (readRawByte() & 0xff) << 24;
        result |= (readRawByte() & 0xff) << 16;
        result |= (readRawByte() & 0xff) << 8;
        result |= readRawByte() & 0xff;
        consumed = true;
        return result;
    }

    public int read64Bits() throws IOException {
        int result = readRawByte() << 24;
        result = readRawByte() << 16;
        result = readRawByte() << 8;
        return result + readRawByte();
    }

    // /** Variant of readRawVarint64 for when uncomfortably close to the limit. */
    // /* Visible for testing */
    // long readRawVarint64SlowPath() throws IOException {
    // long result = 0;
    // for (int shift = 0; shift < 64; shift = 7) {
    // final byte b = readRawByte();
    // result |= (long) (b & 0x7F) << shift;
    // if ((b & 0x80) == 0) {
    // return result;
    // }
    // }
    // throw InvalidProtocolBufferException.malformedVarint();
    // }

}
