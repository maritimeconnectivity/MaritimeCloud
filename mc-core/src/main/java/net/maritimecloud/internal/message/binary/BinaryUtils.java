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
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

import net.maritimecloud.util.Binary;


/**
 *
 * @author Kasper Nielsen
 */
public class BinaryUtils {

    /**
     * Compute the number of bytes that would be needed to encode a varint. {@code value} is treated as unsigned, so it
     * won't be sign-extended if negative.
     */
    public static int computeVarInt32(int value) {
        if ((value & 0xffffffff << 7) == 0) {
            return 1;
        } else if ((value & 0xffffffff << 14) == 0) {
            return 2;
        } else if ((value & 0xffffffff << 21) == 0) {
            return 3;
        } else if ((value & 0xffffffff << 28) == 0) {
            return 4;
        }
        return 5;
    }

    /**
     * Decode a ZigZag-encoded 32-bit value. ZigZag encodes signed integers into values that can be efficiently encoded
     * with varint. (Otherwise, negative values must be sign-extended to 64 bits to be varint encoded, thus always
     * taking 10 bytes on the wire.)
     *
     * @param n
     *            An unsigned 32-bit integer, stored in a signed int because Java has no explicit unsigned support.
     * @return A signed 32-bit integer.
     */
    public static int decodeZigZag32(final int n) {
        return n >>> 1 ^ -(n & 1);
    }

    /**
     * Decode a ZigZag-encoded 64-bit value. ZigZag encodes signed integers into values that can be efficiently encoded
     * with varint. (Otherwise, negative values must be sign-extended to 64 bits to be varint encoded, thus always
     * taking 10 bytes on the wire.)
     *
     * @param n
     *            An unsigned 64-bit integer, stored in a signed int because Java has no explicit unsigned support.
     * @return A signed 64-bit integer.
     */
    public static long decodeZigZag64(final long n) {
        return n >>> 1 ^ -(n & 1);
    }

    public static Binary encodeBigDecimal(BigDecimal value) {
        byte[] b = BinaryUtils.encodeVarInt32(value.scale());
        Binary bin = Binary.copyFrom(b);
        return bin.concat(encodeBigInteger(value.unscaledValue()));
    }

    public static Binary encodeBigInteger(BigInteger bi) {
        return Binary.copyFrom(bi.toByteArray());
    }

    public static BigDecimal decodeBigDecimal(Binary b) throws IOException {
        InputStream is = b.newInput();
        int scale = ReaderUtils.readRawVarint32(is);
        Binary bin = Binary.readFrom(is);
        BigInteger unscaled = new BigInteger(bin.toByteArray());
        return new BigDecimal(unscaled, scale);
    }

    public static BigInteger decodeBigInteger(Binary b) {
        return new BigInteger(b.toByteArray());
    }

    /**
     * Compute the number of bytes that would be needed to encode a varint. {@code value} is treated as unsigned, so it
     * won't be sign-extended if negative.
     */
    public static byte[] encodeVarInt32(int value) {
        int size = computeVarInt32(value);
        byte[] b = new byte[size];
        int i = 0;
        while (true) {
            if ((value & ~0x7F) == 0) {
                b[i] = (byte) value;
                return b;
            } else {
                b[i] = (byte) (value & 0x7F | 0x80);
                value >>>= 7;
            }
        }
    }

    /**
     * Encode a ZigZag-encoded 32-bit value. ZigZag encodes signed integers into values that can be efficiently encoded
     * with varint. (Otherwise, negative values must be sign-extended to 64 bits to be varint encoded, thus always
     * taking 10 bytes on the wire.)
     *
     * @param n
     *            A signed 32-bit integer.
     * @return An unsigned 32-bit integer, stored in a signed int because Java has no explicit unsigned support.
     */
    public static int encodeZigZag32(final int n) {
        // Note: the right-shift must be arithmetic
        return n << 1 ^ n >> 31;
    }

    /**
     * Encode a ZigZag-encoded 64-bit value. ZigZag encodes signed integers into values that can be efficiently encoded
     * with varint. (Otherwise, negative values must be sign-extended to 64 bits to be varint encoded, thus always
     * taking 10 bytes on the wire.)
     *
     * @param n
     *            A signed 64-bit integer.
     * @return An unsigned 64-bit integer, stored in a signed int because Java has no explicit unsigned support.
     */
    public static long encodeZigZag64(final long n) {
        // Note: the right-shift must be arithmetic
        return n << 1 ^ n >> 63;
    }

}
