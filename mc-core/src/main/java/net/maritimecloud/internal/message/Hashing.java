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

import static java.util.Objects.requireNonNull;

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

    /** @return a MessageDigest that uses SHA-1 */
    private static MessageDigest getSHA1MessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // /CLOVER:OFF
            throw new Error("All java implementations should be able to create SHA-1 digests", e);
            // /CLOVER:ON
        }
    }

    /** @return a MessageDigest that uses SHA-256 */
    private static MessageDigest getSHA256MessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // /CLOVER:OFF
            throw new Error("All java implementations should be able to create SHA-256 digests", e);
            // /CLOVER:ON
        }
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
     * @return a SHA-1 hash of the specified byte array
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
     * @return a SHA-256 hash of the specified byte array
     */
    public static byte[] SHA256(byte[] bytes) {
        requireNonNull(bytes, "bytes is null");
        MessageDigest md = getSHA256MessageDigest();
        return md.digest(bytes);
    }
}
