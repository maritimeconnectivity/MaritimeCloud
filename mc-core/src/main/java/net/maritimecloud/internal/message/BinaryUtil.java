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
package net.maritimecloud.internal.message;

/**
 * Various binary utility methods. No methods perform any kind of bound checks.
 *
 * @author Kasper Nielsen
 */
public class BinaryUtil {

    /**
     * Reads a int using big-endian convention from the specified offset.
     *
     * @param bytes
     *            The array to read from
     * @param offset
     *            the position to start reading from
     * @return the integer corresponding to the 4 bytes that was read
     */
    public static int readInt(byte[] bytes, int offset) {
        return (bytes[offset] << 24) + ((bytes[offset + 1] & 0xff) << 16) + ((bytes[offset + 2] & 0xff) << 8)
                + (bytes[offset + 3] & 0xff);
    }

    /**
     * Convert an array of bytes into an array of ints. 4 bytes from the input data map to a single int in the output
     * data.
     *
     * @param bytes
     *            The data to read from.
     * @return An array of integers corresponding to the specified byte array
     * @throws IllegalArgumentException
     *             if the length of the array is not divisible by 4
     */
    public static int[] readInts(byte[] bytes) {
        if ((bytes.length & 3) != 0) { // & 3 = % 4
            throw new IllegalArgumentException("Number of bytes must be a multiple of 4.");
        }
        int[] ints = new int[bytes.length >> 2];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = readInt(bytes, i << 2);
        }
        return ints;
    }

    /**
     * Writes 4 bytes containing the given int value. The conversion is done using the big-endian convention.
     *
     * @param value
     *            the value to convert
     * @param offset
     *            the offset in the byte array to write the int
     * @param bytes
     *            the array to write the value into
     * @return the specified byte array
     */
    public static byte[] writeInt(int value, byte[] bytes, int offset) {
        bytes[offset] = (byte) (value >>> 24);
        bytes[offset + 1] = (byte) (value >>> 16);
        bytes[offset + 2] = (byte) (value >>> 8);
        bytes[offset + 3] = (byte) value;
        return bytes;
    }

    /**
     * Writes 4 bytes containing the given int value. The conversion is done using the big-endian convention.
     *
     * @param value
     *            the value to convert
     * @param offset
     *            the offset in the byte array to write the int
     * @param bytes
     *            the array to write the value into
     * @return the specified byte array
     */
    public static byte[] writeLong(long value, byte[] bytes, int offset) {
        bytes[offset] = (byte) (value >>> 56);
        bytes[offset + 1] = (byte) (value >>> 48);
        bytes[offset + 2] = (byte) (value >>> 40);
        bytes[offset + 3] = (byte) (value >>> 32);
        bytes[offset + 4] = (byte) (value >>> 24);
        bytes[offset + 5] = (byte) (value >>> 16);
        bytes[offset + 6] = (byte) (value >>> 8);
        bytes[offset + 7] = (byte) value;
        return bytes;
    }

    /**
     * Reads a long using big-endian convention from the specified offset.
     *
     * @param bytes
     *            The array to read from
     * @param offset
     *            the position to start reading from
     * @return the long corresponding to the 4 bytes that was read
     */
    public static long readLong(byte[] bytes, int offset) {
        return ((long)bytes[offset] << 56) +
                (((long)bytes[offset + 1] & 0xff) << 48) +
                (((long)bytes[offset + 2] & 0xff) << 40) +
                (((long)bytes[offset + 3] & 0xff) << 32) +
                (((long)bytes[offset + 4] & 0xff) << 24) +
                (((long)bytes[offset + 5] & 0xff) << 16) +
                (((long)bytes[offset + 6] & 0xff) << 8) +
                ((long)bytes[offset + 7] & 0xff);
    }

}
