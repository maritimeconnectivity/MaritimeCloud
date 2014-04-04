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
package net.maritimecloud.internal.util;

import static java.util.Objects.requireNonNull;

import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Various utility method for hashing objects and primitives.
 * 
 * @author Kasper Nielsen
 */
public class Hashing {

    /** Keeps a per-thread message digest. */
    private static final ThreadLocal<SoftReference<MessageDigest>> SHA1 = new ThreadLocal<>();

    /** Keeps a per-thread message digest. */
    private static final ThreadLocal<SoftReference<MessageDigest>> SHA256 = new ThreadLocal<>();

    /** @return a MessageDigest that uses SHA-1 */
    private static MessageDigest getSHA1MessageDigest() {
        SoftReference<MessageDigest> ref = SHA1.get();
        if (ref != null) {
            MessageDigest result = ref.get();
            if (result != null) {
                return result;
            }
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            ref = new SoftReference<>(md);
            SHA1.set(ref);
            return md;
        } catch (NoSuchAlgorithmException e) {
            // /CLOVER:OFF
            throw new Error("All java implementations should be able to create SHA-1 digests", e);
            // /CLOVER:ON
        }
    }

    /** @return a MessageDigest that uses SHA-256 */
    private static MessageDigest getSHA256MessageDigest() {
        SoftReference<MessageDigest> ref = SHA256.get();
        if (ref != null) {
            MessageDigest result = ref.get();
            if (result != null) {
                return result;
            }
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            ref = new SoftReference<>(md);
            SHA256.set(ref);
            return md;
        } catch (NoSuchAlgorithmException e) {
            // /CLOVER:OFF
            throw new Error("All java implementations should be able to create SHA-256 digests", e);
            // /CLOVER:ON
        }
    }

    /**
     * Returns the hash code of the specified boolean using the same contract as {@link Boolean}.
     * 
     * @param value
     *            the boolean to calculate the hash code for
     * @return the hash code of the specified value
     */
    public static int hashcode(boolean value) {
        return value ? 1231 : 1237;
    }

    /**
     * Returns the hash code of the specified byte using the same contract as {@link Byte}.
     * 
     * @param value
     *            the byte to calculate the hash code for
     * @return the hash code of the specified value
     */
    public static int hashcode(byte value) {
        return value;
    }

    /**
     * Returns the hash code of the specified char using the same contract as {@link Character}.
     * 
     * @param value
     *            the char to calculate the hash code for
     * @return the hash code of the specified value
     */
    public static int hashcode(char value) {
        return value;
    }

    /**
     * Returns the hash code of the specified double using the same contract as {@link Double}.
     * 
     * @param value
     *            the double to calculate the hash code for
     * @return the hash code of the specified value
     */
    public static int hashcode(double value) {
        long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ bits >>> 32);
    }

    /**
     * Returns the hash code of the specified float using the same contract as {@link Float}.
     * 
     * @param value
     *            the float to calculate the hash code for
     * @return the hash code of the specified value
     */
    public static int hashcode(float value) {
        return Float.floatToIntBits(value);
    }

    /**
     * Returns the hash code of the specified long using the same contract as {@link Long}.
     * 
     * @param value
     *            the long to calculate the hash code for
     * @return the hash code of the specified value
     */
    public static int hashcode(long value) {
        return (int) (value ^ value >>> 32);
    }

    /**
     * Returns the hash code of the specified object, returning 0 for the {@code null} object. This method is equivalent
     * to {@link Objects#hashCode(Object)}
     * 
     * @param o
     *            the object to calculate the hash code for
     * @return the hash code of the specified object
     */
    public static int hashcode(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    /**
     * Returns the hash code of the specified short using the same contract as {@link Short}.
     * 
     * @param value
     *            the short to calculate the hash code for
     * @return the hash code of the specified value
     */
    public static int hashcode(short value) {
        return value;
    }

    /**
     * Applies the supplementary hash function used by {@link IdentityHashMap} to the specified integer.
     * 
     * @param h
     *            the integer to hash
     * @return the hashed integer
     */
    public static int identityHash(int h) {
        return (h << 1) - (h << 8);
    }

    /**
     * Applies the supplementary hash function used by {@link HashMap} to the specified integer.
     * 
     * @param h
     *            the integer to hash
     * @return the hashed integer
     */
    public static int juHashMapHash(int h) {
        h ^= h >>> 20 ^ h >>> 12;
        return h ^ h >>> 7 ^ h >>> 4;
    }

    /**
     * Applies the supplementary hash function used by {@link ConcurrentHashMap} to the specified integer.
     * 
     * @param h
     *            the integer to hash
     * @return the hashed integer
     */

    public static int wangJenkinsHash(int h) {
        // FOREIGN_CODE
        // Taken from ConcurrentHashMap
        // http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/main/java/util/concurrent/ConcurrentHashMap.java?view=markup
        // Spread bits to regularize both segment and index locations,
        // using variant of single-word Wang/Jenkins hash.
        h += h << 15 ^ 0xffffcd7d;
        h ^= h >>> 10;
        h += h << 3;
        h ^= h >>> 6;
        h += (h << 2) + (h << 14);
        return h ^ h >>> 16;
    }

    /**
     * Applies a supplemental hash function to a given hashCode, which defends against poor quality hash functions. The
     * result must be non-negative, and for reasonable performance must have good avalanche properties; i.e., that each
     * bit of the argument affects each bit (except sign bit) of the result.
     */
    public static final int murmur3BaseStep(int h) {
        // FOREIGN_CODE
        // Taken from ConcurrentHashMap
        // http://http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/jsr166e/ConcurrentHashMapV8.java?revision=1.23&view=markup
        // Apply base step of MurmurHash; see http://code.google.com/p/smhasher/
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        return h >>> 16; // mask out sign bit
    }

    /**
     * Returns a SHA-1 hash of the specified byte array.
     * 
     * @param bytes
     *            the array to calculate the hash of
     * @return a SHA-1 hash
     */
    public static byte[] SHA1(byte[] bytes) {
        requireNonNull(bytes, "bytes is null");
        MessageDigest md = getSHA1MessageDigest();
        return md.digest(bytes);
    }

    /**
     * Returns a SHA-256 hash of the specified byte array.
     * 
     * @param bytes
     *            the array to calculate the hash of
     * @return a SHA-256 hash
     */
    public static byte[] SHA256(byte[] bytes) {
        requireNonNull(bytes, "bytes is null");
        MessageDigest md = getSHA256MessageDigest();
        return md.digest(bytes);
    }
}
