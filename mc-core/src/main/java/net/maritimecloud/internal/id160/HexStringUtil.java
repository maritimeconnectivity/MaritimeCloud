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

import static net.maritimecloud.internal.id160.Checks.checkIntIsOneOrGreater;

import java.util.Random;

/**
 * Various hex util methods.
 *
 * @author Kasper Nielsen
 */
public class HexStringUtil {

    /** All hexadecimal chars. */
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    /** We use a simple byte table to map characters to byte values, */
    private static final byte[] TABLE;

    static {
        TABLE = new byte['f' + 1];
        // we start by filling table with -1, which indicates a illegal char
        for (int i = 0; i <= 'f'; i++) {
            TABLE[i] = -1;
        }
        // Map numbers
        for (int i = '0'; i <= '9'; i++) {
            TABLE[i] = (byte) (i - '0');
        }
        // Valid lower case characters
        for (int i = 'a'; i <= 'f'; i++) {
            TABLE[i] = (byte) (10 + i - 'a');
        }
        // Valid upper case characters
        for (int i = 'A'; i <= 'F'; i++) {
            TABLE[i] = (byte) (10 + i - 'A');
        }
    }

    /**
     * Converts the specified byte array to a hex string.
     *
     * @param b
     *            the byte array to convert to a hex string
     * @return the string representation
     * @throws NullPointerException
     *             if the specified array is null
     * @see #toByteArray(String)
     */
    public static String from(byte... b) {
        char[] result = new char[b.length * 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            int s = b[i];
            result[j++] = HEX_CHARS[(s & 0xf0) >>> 4];
            result[j++] = HEX_CHARS[s & 0x0f];
        }
        return new String(result);
    }

    /**
     * Converts the specified int array to a hex string.
     *
     * @param b
     *            the int array to convert to a hex string
     * @return the string representation
     * @throws NullPointerException
     *             if the specified array is null
     * @see #toIntArray(String)
     */
    public static String from(int... b) {
        char[] result = new char[b.length * 8];
        // sb.append("0x");
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            int s = b[i];
            result[j++] = HEX_CHARS[(s & 0xf0000000) >>> 28];
            result[j++] = HEX_CHARS[(s & 0x0f000000) >>> 24];
            result[j++] = HEX_CHARS[(s & 0x00f00000) >>> 20];
            result[j++] = HEX_CHARS[(s & 0x000f0000) >>> 16];
            result[j++] = HEX_CHARS[(s & 0x0000f000) >>> 12];
            result[j++] = HEX_CHARS[(s & 0x00000f00) >>> 8];
            result[j++] = HEX_CHARS[(s & 0x000000f0) >>> 4];
            result[j++] = HEX_CHARS[s & 0x0000000f];
        }
        return new String(result);
    }

    /**
     * Creates a hex string of the specified length using the specified {@link Random}.
     *
     * @param random
     *            the random source used for generating the string
     * @param length
     *            the length of the generated string
     * @return the random hex string
     */
    public static String next(Random random, int length) {
        byte[] nextBytes = new byte[(checkIntIsOneOrGreater(length, "length") + 1) / 2];
        random.nextBytes(nextBytes);//
        return HexStringUtil.from(nextBytes).substring(0, length);
    }

    /**
     * Converts the two specified chars to a single byte. For example, will toByte('7', 'f') return 127.
     *
     * @param c1
     *            the first char
     * @param c2
     *            the second char
     * @return the converted byte
     */
    private static byte toByte(char c1, char c2) {
        return (byte) ((value(c1) << 4) | value(c2));
    }

    /**
     * Converts the specified hexadecimal string to a byte array. The specified string must have an
     * {@link String#length() even length}. Also, all characters in the string must be either either the numerals
     * <tt>0-9</tt> or the ASCII letters <tt>A-F</tt> or <tt>a-f</tt>.
     * <p>
     * Examples of Strings:
     *
     * <pre>
     * fromHexString("00")  -&gt; [0]
     * fromHexString("7f")  -&gt; [127]
     * fromHexString("7F")  -&gt; [127]
     * fromHexString("c0")  -&gt; [-64]
     * fromHexString("c0a") -&gt; throws IllegalArgumentException() because of an uneven number of characters
     * fromHexString("ca201a3d")  -&gt; [-54, 32, 26, 61]
     * </pre>
     *
     * @param hexString
     *            the hex string to convert
     *
     * @throws IllegalArgumentException
     *             if the string does not have an even number of characters. Or if the string contains a non valid
     *             character (anything except 0-9, a-f or A-F)
     * @return corresponding byte array
     */
    public static byte[] toByteArray(String hexString) {
        int length = hexString.length();
        if ((length & 0x1) != 0) {
            throw new IllegalArgumentException("fromHexString requires an even number of hex characters, [length = "
                    + length + ", hexstring = '" + hexString + "']");
        }
        byte[] result = new byte[length >> 1];
        for (int i = 0; i < length;) {
            result[i >> 1] = toByte(hexString.charAt(i++), hexString.charAt(i++));
        }
        return result;
    }

    /**
     * Converts the 8 specified chars to a single byte.
     *
     * @return the converted int
     */
    private static int toInt(char c1, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        return value(c1) << 28 | value(c2) << 24 | value(c3) << 20 | value(c4) << 16 | value(c5) << 12 | value(c6) << 8
                | value(c7) << 4 | value(c8);
    }

    /**
     * Converts the specified hex string to an integer array.
     *
     * @param hexString
     *            the hex string to convert
     * @throws IllegalArgumentException
     *             if the string does not have a number of characters divisible by 8. Or if the string contains a non
     *             valid character (anything except 0-9, a-f or A-F)
     * @return corresponding int array
     */
    public static int[] toIntArray(String hexString) {
        int length = hexString.length();
        if ((length & 0x7) != 0) {
            throw new IllegalArgumentException(
                    "fromHexString requires an number of hex characters divisible by 8, [length = " + length
                    + ", hexstring = '" + hexString + "']");
        }
        int[] result = new int[length >> 3];
        for (int i = 0; i < length;) {
            result[i >> 3] = toInt(hexString.charAt(i++), hexString.charAt(i++), hexString.charAt(i++),
                    hexString.charAt(i++), hexString.charAt(i++), hexString.charAt(i++), hexString.charAt(i++),
                    hexString.charAt(i++));
        }
        return result;
    }

    /**
     * Converts the specified char to the corresponding value between 0 and 15.
     *
     * @param c
     *            the char to convert
     * @return the converted value
     */
    private static int value(char c) {
        if (c > 'f') {
            throw new IllegalArgumentException("Invalid hex character, character = '" + c + "'");
        }
        byte result = TABLE[c];
        if (result < 0) {
            throw new IllegalArgumentException("Invalid hex character, character = '" + c + "'");
        }
        return result;
    }
}
