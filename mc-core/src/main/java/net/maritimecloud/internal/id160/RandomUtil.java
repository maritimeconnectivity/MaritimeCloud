/*
 * Copyright (c) 2008 Kasper Nielsen.
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
package net.maritimecloud.internal.id160;

import static net.maritimecloud.internal.id160.Checks.checkLongIsZeroOrGreater;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * Various random utility methods. No methods perform any kind of bound checks.
 *
 * @author Kasper Nielsen
 */
public final class RandomUtil {

    /**
     * Takes two random longs and converts them to an {@link UUID}. By setting the version flag and the variant.
     *
     * @param mostSignificantBits
     *            the most significant bits of the uuid
     * @param leastSignificantBits
     *            the least significant bits of the uuid
     * @return the new uuid
     */
    public static UUID longsToType4UUID(long mostSignificantBits, long leastSignificantBits) {
        mostSignificantBits &= 0xffffffffffff0fffL; /* clear version */
        mostSignificantBits |= 0x4000;/* set to version 4 */
        leastSignificantBits &= 0x3fffffffffffffffL; /* clear variant */
        leastSignificantBits |= 0x8000000000000000L; /* set to IETF variant */
        return new UUID(mostSignificantBits, leastSignificantBits);
    }

    /**
     * Populates the specified byte array with random data.
     *
     * @param random
     *            the random data source
     * @param bytes
     *            The array into which bytes are to be written
     * @return a random byte array
     */
    public static byte[] nextByteArray(Random random, byte[] bytes) {
        return nextByteArray(random, bytes, 0, bytes.length);
    }

    /**
     * Fills the specified byte array with random data using the supplied random source.
     *
     * @param random
     *            the random data source
     * @param bytes
     *            The array into which bytes are to be written
     * @param offset
     *            The offset within the array of the first byte to be written; must be non-negative and no larger than
     *            <tt>target.length</tt>
     * @param length
     *            The maximum number of bytes to be written to the given array; must be non-negative and no larger than
     *            <tt>target.length - offset</tt>
     * @return the specified byte array
     */
    public static byte[] nextByteArray(Random random, byte[] bytes, int offset, int length) {
        int i = offset;
        while (i < ((length >> 2) << 2)) {
            int rnd = random.nextInt();
            bytes[i++] = (byte) (rnd >>> 24);
            bytes[i++] = (byte) (rnd >>> 16);
            bytes[i++] = (byte) (rnd >>> 8);
            bytes[i++] = (byte) rnd;
        }
        int j = length + (offset - i);
        if (j != 0) {
            int rnd = random.nextInt();
            switch (j) {
            case 3:
                bytes[i++] = (byte) (rnd >>> 16);
            case 2:
                bytes[i++] = (byte) (rnd >>> 8);
            }
            bytes[i++] = (byte) rnd;
        }
        return bytes;
    }

    /**
     * Creates a random byte array of the specified length using the specified random.
     *
     * @param random
     *            the random data source
     * @param length
     *            the length of the returned byte array
     * @return a random byte array
     * @throws IllegalArgumentException
     *             if the specified length is negative
     */
    public static byte[] nextByteArray(Random random, int length) {
        checkLongIsZeroOrGreater(length, "length");
        return nextByteArray(random, new byte[length], 0, length);
    }

    /**
     * Fills the specified array with (secure) random data.
     *
     * @param bytes
     *            the array to fill
     * @return the specified array
     */
    public static byte[] secureRandom(byte[] bytes) {
        SecureHolder.RANDOM.nextBytes(bytes);
        return bytes;
    }

    /**
     * Returns a secure random byte array of the specified length
     *
     * @param length
     *            the length of the returned byte array
     * @return a secure random byte array
     */
    public static byte[] secureRandom(int length) {
        return secureRandom(new byte[length]);
    }

    /** A holder class to defer initialization until needed. */
    private static class SecureHolder {
        static final SecureRandom RANDOM = new SecureRandom();
    }
}
