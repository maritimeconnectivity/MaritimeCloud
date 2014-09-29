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

import static java.util.Objects.requireNonNull;
import static net.maritimecloud.internal.id160.BinaryUtil.readInt;
import static net.maritimecloud.internal.id160.Checks.checkIntIsZeroOrGreater;

import java.io.Serializable;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * A class that represents an immutable 160-bit identifier.
 *
 * The minimum value is {@link #ZERO} and the maximum value is {@link #MAX}.
 *
 * When adding IDs overflow is handled by wrapping around keeping the least significant representable bits of the
 * result. So, for example, MAX_VALUE + ONE = ZERO. This make Id160 ideal to use for consistent hashing,
 *
 * @author Kasper Nielsen
 */
public class Id160 implements Comparable<Id160>, Serializable {

    /** The Id160 constant half. Represented as 0x8000000000000000000000000000000000000000 */
    public static final Id160 HALF = new Id160(Integer.MIN_VALUE, 0, 0, 0, 0);

    /** This mask is used to obtain the value of an integer as if it were unsigned. */
    private static final long LONG_MASK = 0xffffffffL;

    /** The Id160 constant max. Represented as 0xffffffffffffffffffffffffffffffffffffffff */
    public static final Id160 MAX = new Id160(-1, -1, -1, -1, -1);

    /** The Id160 constant one. Represented as 0x0000000000000000000000000000000000000001 */
    public static final Id160 ONE = new Id160(0, 0, 0, 0, 1);

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** The Id160 constant zero. Represented as 0x0000000000000000000000000000000000000000 */
    public static final Id160 ZERO = new Id160(0, 0, 0, 0, 0);

    /** The actual data. */
    private final int i1, i2, i3, i4, i5;

    /**
     * Creates a new Id160 by reading 20 bytes (5 ints) from the specified byte source
     *
     * @param src
     *            the source to read from
     * @param offset
     *            the offset to begin reading
     */
    private Id160(byte[] src, int offset) {
        this(readInt(src, offset), readInt(src, offset + 4), readInt(src, offset + 8), readInt(src, offset + 12),
                readInt(src, offset + 16));
    }

    /** Creates a new Id160 using 5 32-bit integers */
    Id160(int i1, int i2, int i3, int i4, int i5) {
        this.i1 = i1;
        this.i2 = i2;
        this.i3 = i3;
        this.i4 = i4;
        this.i5 = i5;
    }

    /**
     * Adds this id with the specified id without a carry flag. Thereby retaining only the least significant 160 bits.
     * <p>
     * Adding without a carry flag means that, for example, <code>MAX.add(MAX) = MAX.previous()</code>.
     *
     * @param other
     *            the id to add to this id
     * @return the result of the addition
     */
    public Id160 add(Id160 other) {
        if (other == Id160.ZERO) {
            return this;
        }
        long a5 = (i5 & LONG_MASK) + (other.i5 & LONG_MASK);
        long a4 = (i4 & LONG_MASK) + (other.i4 & LONG_MASK) + (a5 >> 32L);
        long a3 = (i3 & LONG_MASK) + (other.i3 & LONG_MASK) + (a4 >> 32L);
        long a2 = (i2 & LONG_MASK) + (other.i2 & LONG_MASK) + (a3 >> 32L);
        long a1 = (i1 & LONG_MASK) + (other.i1 & LONG_MASK) + (a2 >> 32L);
        return new Id160((int) a1, (int) a2, (int) a3, (int) a4, (int) a5);
    }

    /**
     * Compares this id to another id. The id's are considered equal if their bitwise representation as returned by
     * {@link #toByteArray()} are identical.
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Id160 o) {
        if (o == this) {
            return 0;
        }
        return i1 == o.i1 ? (i2 == o.i2 ? (i3 == o.i3 ? (i4 == o.i4 ? (i5 == o.i5 ? 0 : unsignedCompare(i5, o.i5))
                : unsignedCompare(i4, o.i4)) : unsignedCompare(i3, o.i3)) : unsignedCompare(i2, o.i2))
                : unsignedCompare(i1, o.i1);
    }

    /**
     * Adds the specified scalar to this id. If positive will return the result of applying {@link #next()} the
     * specified <code>scalar</code> number of times. If negative will return the result of applying {@link #previous()}
     * the specified <code>scalar</code> number of times.
     *
     * @param scalar
     *            the scalar to add (or substract)
     * @return the result of the addition
     */
    public Id160 add(int scalar) {
        if (scalar > 0) {
            return add(new Id160(0, 0, 0, 0, scalar));
        } else if (scalar < 0) {
            return add(new Id160(-1, -1, -1, -1, scalar));
        } else { // scalar==0
            return this;
        }
    }

    /**
     * Two id's are considered equal if their bitwise representation as returned by {@link #toByteArray()} are
     * identical.
     *
     * @param id
     *            the id to compare with
     * @see Object#equals(Object)
     * @return true if equals, other false
     */
    public boolean equals(Id160 id) {
        if (id == this) {
            return true;
        } else if (id == null) {
            return false;
        }
        return id.i1 == i1 && id.i2 == i2 && id.i3 == i3 && id.i4 == i4 && id.i5 == i5;
    }

    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        return (obj instanceof Id160) && equals((Id160) obj);
    }

    /**
     * Returns the number of zero bits preceding the highest-order ("leftmost") one-bit in the binary representation of
     * this id. Returns 160 if this id is equals to {@link #ZERO}.
     *
     * @return the number of zero bits preceding the highest-order ("leftmost") one-bit in the binary representation of
     *         this id, or 160 if the value is equal to zero.
     */
    public int getNumberOfLeadingZeroes() {
        if (i1 != 0) {
            return Integer.numberOfLeadingZeros(i1);
        } else if (i2 != 0) {
            return 32 + Integer.numberOfLeadingZeros(i2);
        } else if (i3 != 0) {
            return 64 + Integer.numberOfLeadingZeros(i3);
        } else if (i4 != 0) {
            return 96 + Integer.numberOfLeadingZeros(i4);
        } else {
            return 128 + Integer.numberOfLeadingZeros(i5);
        }
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return i1 ^ i2 ^ i3 ^ i4 ^ i5;
    }

    /** @return the result of mirroring this id in an vertical axis */
    Id160 inverse() {
        return new Id160(~i1, ~i2, ~i3, ~i4, ~i5);
    }

    /**
     * Returns the next id after this id. If this id is {@link #MAX} returns {@link #ZERO}.
     *
     * @return the next id
     * @see #previous()
     */
    public Id160 next() {
        if (i5 != -1) {
            return new Id160(i1, i2, i3, i4, i5 + 1);
        } else if (i4 != -1) {
            return new Id160(i1, i2, i3, i4 + 1, 0);
        } else if (i3 != -1) {
            return new Id160(i1, i2, i3 + 1, 0, 0);
        } else if (i2 != -1) {
            return new Id160(i1, i2 + 1, 0, 0, 0);
        } else if (i1 != -1) {
            return new Id160(i1 + 1, 0, 0, 0, 0);
        } else {
            return new Id160(0, 0, 0, 0, 0);
        }
    }

    /**
     *
     * Returns the next <tt>count</tt> number of ID160s.
     *
     * @param count
     *            the amount of ID160s to return
     * @return an new array of the next <tt>count</tt> number of ID160s
     * @throws IllegalArgumentException
     *             if count is negative
     */
    public Id160[] next(int count) {
        Id160[] ids = new Id160[checkIntIsZeroOrGreater(count, "count")];
        Id160 next = this;
        for (int i = 0; i < count; i++) {
            next = ids[i] = next.next();
        }
        return ids;
    }

    /**
     * If all ids are placed on a circle, this method will returns the id opposite this id. So the following holds
     * <code>id.subtract(id.opposite()) = HALF</code>
     * <p>
     * Basically this method just an id representation where the most significant bit is flipped.
     *
     * @return the id that is opposite this id
     */
    public Id160 opposite() {
        return new Id160(i1 ^ (1 << 31), i2, i3, i4, i5);// flip the first bit
    }

    /**
     * Creates a new pseudorandom, uniformly distributed {@link Id160}.
     * <p>
     * Unlike, for example, {@link UUID#randomUUID()} the id returned by this method is <strong>not</strong> guaranteed
     * to be cryptographically secure. {@link #secureRandom()} can be used instead for generating a cryptographically
     * secure random id.
     * <p>
     * The current implementation delegates this call to {@link ThreadLocalMersenneTwisterRandom#nextId160()}. This
     * might change in future versions.
     *
     * @return a new pseudorandom Id160
     *
     * @see #secureRandom()
     */
    public static Id160 random() {
        return ThreadLocalMersenneTwisterRandom.current().nextId160();
    }

    public static Stream<Id160> randomStream() {
        return Stream.generate(() -> random());
    }

    /**
     * Equivalent to {@link #random()} except this method will create an array of Id160s.
     *
     * @param count
     *            the number of random Id160s to create.
     * @return an array of pseudorandom Id160s
     */
    public static Id160[] random(int count) {
        return random(ThreadLocalMersenneTwisterRandom.current(), count);
    }

    /**
     * Creates a new <strong>secure</strong> pseudorandom, uniformly distributed {@link Id160}.
     * <p>
     * The current implementation delegates this call to {@link ThreadLocalAESRandom#nextId160()}. This might change in
     * future versions.
     *
     * @return a new secure random Id160
     * @see #random()
     */
    public static Id160 secureRandom() {
        return ThreadLocalAESRandom.current().nextId160();
    }

    /**
     * Equivalent to {@link #secureRandom()} except this method will create an array of Id160s.
     *
     * @param count
     *            the number of random Id160s to create.
     * @return an array of secure pseudorandom Id160s
     */
    public static Id160[] secureRandom(int count) {
        return random(ThreadLocalAESRandom.current(), count);
    }

    /**
     * Returns the id before this id. If this id is {@link #ZERO} returns {@link #MAX}.
     *
     * @return the previous id
     * @see #next()
     */
    public Id160 previous() {
        if (i5 != 0) {
            return new Id160(i1, i2, i3, i4, i5 - 1);
        } else if (i4 != 0) {
            return new Id160(i1, i2, i3, i4 - 1, -1);
        } else if (i3 != 0) {
            return new Id160(i1, i2, i3 - 1, -1, -1);
        } else if (i2 != 0) {
            return new Id160(i1, i2 - 1, -1, -1, -1);
        } else if (i1 != 0) {
            return new Id160(i1 - 1, -1, -1, -1, -1);
        }
        return new Id160(-1, -1, -1, -1, -1);
    }

    /**
     * Writes the bit representation of this id to the specified array starting at index 0.
     *
     * @param target
     *            the byte array to write the bit representation to
     * @return the specified byte array
     * @throws IndexOutOfBoundsException
     *             if the specified array has less than 20 elements
     * @throws NullPointerException
     *             if the specified array is null
     */
    public byte[] put(byte[] target) {
        checkByteArraySize(target);
        return put0(target, 0);
    }

    /**
     * Writes this bit representation of this id to the specified array starting at the specified index.
     *
     * @param target
     *            the byte array to write the but representation to
     * @param offset
     *            the index to start at
     * @return the specified byte array
     * @throws IndexOutOfBoundsException
     *             if the specified offset is negative or if there less than 20 bytes available from the specified
     *             offset
     * @throws NullPointerException
     *             if the specified array is null
     */
    public byte[] put(byte[] target, int offset) {
        if (offset < 0) {
            throw new IndexOutOfBoundsException("" + offset);
        } else if (target.length < 20 + offset) {
            throw new IndexOutOfBoundsException(
                    "the specified target array must be able to hold at least 20 bytes, length=" + target.length
                            + ", offset=" + offset);
        }
        return put0(target, offset);
    }

    /**
     * Write this the bit representation of this id to the specified byte buffer.
     *
     * @param buffer
     *            the buffer to write to
     * @return the specified buffer
     * @throws BufferOverflowException
     *             If there are fewer than 20 bytes remaining in the specified buffer
     * @throws ReadOnlyBufferException
     *             If the specified buffer is read-only
     * @throws NullPointerException
     *             if the specified buffer is null
     */
    public ByteBuffer put(ByteBuffer buffer) {
        return buffer.putInt(i1).putInt(i2).putInt(i3).putInt(i4).putInt(i5);
    }

    /** Equivalent to {@link #put(byte[], int)} except making no checks. */
    private byte[] put0(byte[] target, int offset) {
        BinaryUtil.writeInt(i1, target, offset);
        BinaryUtil.writeInt(i2, target, offset + 4);
        BinaryUtil.writeInt(i3, target, offset + 8);
        BinaryUtil.writeInt(i4, target, offset + 12);
        BinaryUtil.writeInt(i5, target, offset + 16);
        return target;
    }

    /**
     * Returns the distance (clockwise) from this id to the specified id.
     *
     * @param other
     *            the other id
     * @return the distance from this id to the specified id
     */
    public Id160 substract(Id160 other) {
        switch (compareTo(other)) {
        case -1:
            // This can be optimized
            return other.substract0(this).inverse().next();
        case 0:
            return Id160.ZERO;
        default:
            return substract0(other);
        }
    }

    private Id160 substract0(Id160 other) {
        long a5 = (i5 & LONG_MASK) - (other.i5 & LONG_MASK);
        long a4 = (i4 & LONG_MASK) - (other.i4 & LONG_MASK) + (a5 >> 32L);
        long a3 = (i3 & LONG_MASK) - (other.i3 & LONG_MASK) + (a4 >> 32L);
        long a2 = (i2 & LONG_MASK) - (other.i2 & LONG_MASK) + (a3 >> 32L);
        long a1 = (i1 & LONG_MASK) - (other.i1 & LONG_MASK) + (a2 >> 32L);
        return new Id160((int) a1, (int) a2, (int) a3, (int) a4, (int) a5);
    }

    /**
     * Returns a byte array with length 20 containing a bitwise representation of this id. The byte array will be in
     * <i>big-endian</i> byte-order: the most significant byte is in the zeroth element. (This representation is
     * compatible with {@link Id160#from(byte[])}.)
     * <p>
     * The following holds true <code>id.equals(Id160.fromByteArray(id.toByteArray()));</code>
     *
     * @return a byte array with length 20 containing a bitwise representation of this id.
     * @see #from(byte[])
     * @see #from(byte[], int)
     * @see #put(byte[])
     * @see #put(byte[], int)
     */
    public byte[] toByteArray() {
        return put0(new byte[20], 0);
    }

    /**
     * Returns a 40 char hexadecimal string representation of this id.
     *
     * @return a 40 char long String representation of this id
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return HexStringUtil.from(i1, i2, i3, i4, i5);
    }

    /**
     * Returns a 8 char hexadecimal string representation of the highest 32 bits of this id.
     *
     * @return a 8 char long String representation of the highest 32 bits of this id
     * @see java.lang.Object#toString()
     */
    public String toStringShort() {
        return HexStringUtil.from(i1);
    }

    /**
     * Checks that the specified arrays length is at least 20.
     *
     * @param a
     *            the array to check
     */
    private static void checkByteArraySize(byte[] a) {
        if (a.length < 20) {
            throw new IndexOutOfBoundsException("the specified array must have a length of at least 20 bytes, length="
                    + a.length);
        }
    }

    /**
     * Creates a new Id160 by reading the first 20 bytes of the array.
     *
     * @param src
     *            the array to read the bit representation from
     * @return a new id corresponding to the specified bit representation
     * @throws IndexOutOfBoundsException
     *             if the specified array is less than 20 bytes
     */
    public static Id160 from(byte[] src) {
        checkByteArraySize(src);
        return new Id160(src, 0);
    }

    /**
     * Creates a new Id160 by reading 20 bytes of the array from the specified offset.
     *
     * @param src
     *            the array to read from
     * @param offset
     *            the offset to read from
     * @return a new id corresponding to the specified bit representation
     * @throws IndexOutOfBoundsException
     *             if the length of specified array from the specified offset is less than 20 bytes
     * @throws IllegalArgumentException
     *             if the specified offset is negative
     */
    public static Id160 from(byte[] src, int offset) {
        if (offset < 0) {
            throw new IndexOutOfBoundsException("offset is negative, offset = " + offset);
        } else if ((src.length - offset) < 20) {
            throw new IndexOutOfBoundsException(
                    "the specified array must have a length of at least 20 bytes from the specified offset, [length = "
                            + src.length + ", offset = " + offset + "]");
        }
        return new Id160(src, offset);
    }

    /**
     * Reads 20 bytes from the specified buffer and returns an Id160 corresponding to the bit representation that was
     * read.
     *
     * @param buffer
     *            the buffer to read from
     * @return the corresponding id
     * @throws BufferUnderflowException
     *             If there are fewer than 20 bytes remaining in this buffer
     */
    public static Id160 from(ByteBuffer buffer) {
        return new Id160(buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt());
    }

    /**
     * Reads a hexadecimal string of length 40 equivalent to that returned by {@link #toString()}. And returns the
     * corresponding id.
     *
     * @param id
     *            A hexadecimal string of length exactly 40 representation an Id160
     * @return the Id160 representation
     * @throws IllegalArgumentException
     *             if the specified string representation does not have an exact length of 40. Or if the string contains
     *             other the hexadecimal characters
     */
    public static Id160 from(String id) {
        if (id.length() != 40) {
            throw new IllegalArgumentException("requires 160 bit, was " + id.length() * 4L);
        }
        int[] i = HexStringUtil.toIntArray(id);
        return new Id160(i[0], i[1], i[2], i[3], i[4]);
    }

    /**
     * Creates a new random Id160 using the specified random source.
     *
     * @param r
     *            the random source
     * @return the new random Id160
     */
    public static Id160 random(Random r) {
        return new Id160(r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt());
    }

    /**
     * Equivalent to {@link #random(Random)} except this method will create an array of Id160s.
     *
     * @param random
     *            the random source
     * @param count
     *            the number of random Id160s to create.
     * @return a array of pseudorandom Id160s
     * @throws IllegalArgumentException
     *             if count is negative
     */
    public static Id160[] random(Random random, int count) {
        requireNonNull(random, "random is null");
        Id160[] ids = new Id160[checkIntIsZeroOrGreater(count, "count")];
        for (int i = 0; i < count; i++) {
            ids[i] = random(random);
        }
        return ids;
    }

    private static int unsignedCompare(int a, int b) {
        return (a & LONG_MASK) > (b & LONG_MASK) ? 1 : -1;
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between {@link Id160#ZERO} (inclusive) and the specified
     * value (exclusive).
     *
     * @param random
     *            the random source
     * @param bound
     *            the bound on the random number to be returned.
     * @return the next value
     * @see Id160#random(Random, Id160)
     * @throws NullPointerException
     *             if the specified bound or random is null
     */
    public static Id160 random(Random random, Id160 bound) {
        requireNonNull(random, "random is null");
        int bitsToGenerate = 160 - requireNonNull(bound, "bound is null").previous().getNumberOfLeadingZeroes();

        // basic idea is to keep generating an ID until it is valid.
        // The id we generate is less than 2 times as big as the bound

        // perhaps we can do one better,
        // generate highest 8 bits, if bits==highest bits in bound
        // we take a slow route, otherwise the rest of the bits can safely be
        // generate because there is no need to compare anything, we know it is less
        // than
        for (;;) {
            final Id160 id;
            if (bitsToGenerate == 0) {
                return Id160.ZERO;
            } else if (bitsToGenerate <= 32) {
                id = new Id160(0, 0, 0, 0, random.nextInt() >>> (32 - bitsToGenerate));
            } else if (bitsToGenerate <= 64) {
                id = new Id160(0, 0, 0, random.nextInt() >>> (64 - bitsToGenerate), random.nextInt());
            } else if (bitsToGenerate <= 96) {
                id = new Id160(0, 0, random.nextInt() >>> (96 - bitsToGenerate), random.nextInt(), random.nextInt());
            } else if (bitsToGenerate <= 128) {
                id = new Id160(0, random.nextInt() >>> (128 - bitsToGenerate), random.nextInt(), random.nextInt(),
                        random.nextInt());
            } else {
                if (bound.equals(Id160.ZERO)) {
                    throw new IllegalArgumentException("bound cannot be Id160.ZERO");
                }
                id = new Id160(random.nextInt() >>> (160 - bitsToGenerate), random.nextInt(), random.nextInt(),
                        random.nextInt(), random.nextInt());
            }
            if (id.compareTo(bound) < 0) {
                return id;
            }
        }
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between the given least value (inclusive) and bound
     * (exclusive).
     *
     * @param random
     *            the random source
     * @param least
     *            the least value returned
     * @param bound
     *            the upper bound (exclusive)
     * @return a random bounded value
     * @throws IllegalArgumentException
     *             if least is greater than or equal to bound
     * @throws NullPointerException
     *             if the specified random, bound or least is null
     */
    public static Id160 random(Random random, Id160 least, Id160 bound) {
        if (least.compareTo(bound) >= 0) {
            throw new IllegalArgumentException("least must be less than bound, [least = " + least + ", bound = "
                    + bound);
        }
        return random(random, bound.substract(least)).add(least);
    }
}
