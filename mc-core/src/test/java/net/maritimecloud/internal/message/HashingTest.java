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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.security.MessageDigest;

import org.junit.Test;

/**
 * Test of {@link Hashing}.
 *
 * @author Kasper Nielsen
 */
public class HashingTest {
    static final byte[] A = new byte[] { 1 };

    static final byte[] B = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, -1, -2, -3, 1, 2, 3, 4, 5, 6, 7, 8, -1, -2, -3, 1, 2,
            3, 4, 5, 6, 7, 8, -1, -2, -3, 1, 2, 3, 4, 5, 6, 7, 8, -1, -2, -3, 1, 2, 3, 4, 5, 6, 7, 8, -1, -2, -3, 1, 2,
            3, 4, 5, 6, 7, 8, -1, -2, -3 };

    @Test
    public void hashcode() {
        assertEquals(Boolean.FALSE.hashCode(), Hashing.hashcode(false));
        assertEquals(Boolean.TRUE.hashCode(), Hashing.hashcode(true));

        assertEquals(Byte.valueOf((byte) -1).hashCode(), Hashing.hashcode((byte) -1));

        assertEquals(Character.valueOf('a').hashCode(), Hashing.hashcode('a'));

        assertEquals(new Double(Math.E).hashCode(), Hashing.hashcode(Math.E));
        assertEquals(new Double(Math.PI).hashCode(), Hashing.hashcode(Math.PI));

        assertEquals(new Float(Math.E).hashCode(), Hashing.hashcode((float) Math.E));
        assertEquals(new Float(Math.PI).hashCode(), Hashing.hashcode((float) Math.PI));

        assertEquals(new Double(Math.E).hashCode(), Hashing.hashcode(new Double(Math.E)));
        assertEquals(0, Hashing.hashcode(null));

        assertEquals(Long.valueOf(-1).hashCode(), Hashing.hashcode(-1L));

        assertEquals(-1, Hashing.hashcode((short) -1));
    }

    @Test
    public void supplementaryHash() {
        assertEquals(-31357824, Hashing.identityHash(123456));
        assertEquals(11982078, Hashing.juHashMapHash(12345678));
        assertEquals(495876504, Hashing.wangJenkinsHash(12345678));
        assertEquals(11896, Hashing.murmur3BaseStep(12345678));
    }

    @Test
    public void sha1() throws Exception {
        assertArrayEquals(MessageDigest.getInstance("SHA-1").digest(A), Hashing.SHA1(A));
        assertArrayEquals(MessageDigest.getInstance("SHA-1").digest(B), Hashing.SHA1(B));

    }

    @Test(expected = NullPointerException.class)
    public void sha1NPE() {
        Hashing.SHA1(null);
    }

    @Test(expected = NullPointerException.class)
    public void sha256NPE() {
        Hashing.SHA256(null);
    }

    @Test
    public void sha256() throws Exception {
        assertArrayEquals(MessageDigest.getInstance("SHA-256").digest(A), Hashing.SHA256(A));
        assertArrayEquals(MessageDigest.getInstance("SHA-256").digest(B), Hashing.SHA256(B));

    }

}
