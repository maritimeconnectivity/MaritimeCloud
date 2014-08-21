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
// ============================================================================
//   Copyright 2006, 2007, 2008, 2008 Daniel W. Dyer
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
// ============================================================================
package net.maritimecloud.internal.id160;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ThreadLocalRandom;

import net.maritimecloud.util.Id160;

/**
 * A random number generator isolated to the current thread. This generator is similar to {@link ThreadLocalRandom}
 * except this generator uses a <a href="http://en.wikipedia.org/wiki/Mersenne_twister">Mersenne twister pseudorandom
 * number generator</a> for higher quality values.
 * <p>
 * {@code ThreadLocalMersenneTwister} is initialized with an internally generated seed that may not otherwise be
 * modified. When applicable, use of {@code ThreadLocalMersenneTwister} rather than shared {@code Random} objects in
 * concurrent programs will typically encounter much less overhead and contention. Use of
 * {@code ThreadLocalMersenneTwister} is particularly appropriate when multiple tasks (for example, each a
 * {@link ForkJoinTask}) use random numbers in parallel in thread pools.
 * <p>
 * Usages of this class should typically be of the form: {@code ThreadLocalMersenneTwister.current().nextX(...)} (where
 * {@code X} is {@code Int}, {@code Long}, etc). When all usages are of this form, it is never possible to accidently
 * share a {@code ThreadLocalMersenneTwister} across multiple threads.
 * <p>
 * This class also provides additional commonly used bounded random generation methods and methods that generate various
 * types and ID's.
 * <p>
 * Like {@link Random}, {@code ThreadLocalMersenneTwister} instances does not generate cryptographically secure numbers.
 * Consider instead using {@link ThreadLocalAESRandom} to get a cryptographically secure pseudo-random number generator
 * for use by security-sensitive applications.
 * <p>
 * This implementation is based on the c-based version available at <a
 * href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c"
 * >http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c</a>
 *
 * @author Kasper Nielsen
 * @see ThreadLocalRandom
 * @see ThreadLocalAESRandom
 * @see <a href="http://en.wikipedia.org/wiki/Mersenne_twister">http://en.wikipedia.org/wiki/Mersenne_twister</a>
 */
public class ThreadLocalMersenneTwisterRandom extends Random {

    /** Magic constant 1. */
    private static final int M = 397;

    /** Magic constant 2. */
    private static final int[] MAG01 = { 0, 0x9908b0df };

    /** Size of state cache. */
    private static final int N = 624;

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** The ThreadLocal containing references to local ThreadLocalMersenneTwisterRandom instance */
    private static final ThreadLocal<ThreadLocalMersenneTwisterRandom> THREAD_LOCAL = new ThreadLocal<ThreadLocalMersenneTwisterRandom>() {
        protected ThreadLocalMersenneTwisterRandom initialValue() {
            return new ThreadLocalMersenneTwisterRandom();
        }
    };

    /** Generated state cache. */
    private final int[] mt = new int[N];

    // TODO test if we need padding for index
    /** Current index in state cache. */
    private int mti;

    private final boolean initialized;
    // Padding to help avoid memory contention among seed updates in
    // different TLMTRs in the common case that they are located near
    // each other.
    @SuppressWarnings("unused")
    private int pad1, pad2, pad3, pad4, pad5, pad6, pad7, pad8, pad9, pad10, pad11, pad12, pad13, pad14, pad15, pad16;

    /** Whether or not we have cached the next gaussian value. */
    private boolean haveNextGaussian;

    /** If haveNextGaussian == true, the next gaussian value to return from {@link #nextGaussian()}. */
    private double nextGaussian;

    /** Creates a new ThreadLocalMersenneTwisterRandom. */
    ThreadLocalMersenneTwisterRandom() {
        seed(BinaryUtil.readInts(RandomUtil.secureRandom(256 / 8)));
        initialized = true;
    }

    /** {@inheritDoc} */
    @Override
    protected final int next(int bits) {
        int y;
        if (mti >= N) { /* generate N ints at one time */
            int mtNext = mt[0];
            for (int k = 0; k < N - M; ++k) {
                int mtCurr = mtNext;
                mtNext = mt[k + 1];
                y = (mtCurr & 0x80000000) | (mtNext & 0x7fffffff);
                mt[k] = mt[k + M] ^ (y >>> 1) ^ MAG01[y & 0x1];
            }
            for (int k = N - M; k < N - 1; ++k) {
                int mtCurr = mtNext;
                mtNext = mt[k + 1];
                y = (mtCurr & 0x80000000) | (mtNext & 0x7fffffff);
                mt[k] = mt[k + (M - N)] ^ (y >>> 1) ^ MAG01[y & 0x1];
            }
            y = (mtNext & 0x80000000) | (mt[0] & 0x7fffffff);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ MAG01[y & 0x1];

            mti = 0;
        }
        y = mt[mti++];

        y ^= y >>> 11;
        y ^= (y << 7) & 0x9d2c5680;
        y ^= (y << 15) & 0xefc60000;
        y ^= y >>> 18;

        return y >>> (32 - bits);
    }

    /**
     * Returns a pseudorandom, uniformly distributed {@code double} value between 0 (inclusive) and the specified value
     * (exclusive).
     *
     * @param n
     *            the bound on the random number to be returned. Must be positive.
     * @return the next value
     * @throws IllegalArgumentException
     *             if n is not positive
     */
    public double nextDouble(double n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        return nextDouble() * n;
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between the given least value (inclusive) and bound
     * (exclusive).
     *
     * @param least
     *            the least value returned
     * @param bound
     *            the upper bound (exclusive)
     * @return the next value
     * @throws IllegalArgumentException
     *             if least greater than or equal to bound
     */
    public double nextDouble(double least, double bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return nextDouble() * (bound - least) + least;
    }

    /** {@inheritDoc} */
    public double nextGaussian() {
        // FOREIGN_CODE
        // Based on AOCP, Volume 2: Seminumerical Algorithms</i>, section 3.4.1, subsection C, algorithm P.
        // Based on svn version from apache harmorny
        // http://svn.apache.org/viewvc/harmony/enhanced/java/trunk/classlib/modules/luni/src/main/java/java/util/Random.java?revision=929253
        if (haveNextGaussian) { // if X1 has been returned, return the second Gaussian
            haveNextGaussian = false;
            return nextGaussian;
        }
        double v1, v2, s;
        do {
            v1 = 2 * nextDouble() - 1; // Generates two independent random variables U1, U2
            v2 = 2 * nextDouble() - 1;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1);
        double norm = Math.sqrt(-2 * Math.log(s) / s);
        nextGaussian = v2 * norm;
        haveNextGaussian = true;
        return v1 * norm;
    }

    /**
     * Returns a random string consisting only of numerals <tt>0-9</tt> and the Latin letters <tt>A-F</tt>
     *
     * @param length
     *            the length of the string to generate
     * @return the random string
     */
    public String nextHexString(int length) {
        return HexStringUtil.next(this, length);
    }

    /**
     * Returns a pseudorandom, uniformly distributed {@link Id160} value using this as a random source.
     *
     * @return the new pseudorandom, uniformly distributed Id160
     * @see Id160#random(Random)
     */
    public Id160 nextId160() {
        return Id160.random(this);
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between {@link Id160#ZERO} (inclusive) and the specified
     * value (exclusive).
     *
     * @param bound
     *            the bound on the random number to be returned.
     * @return the next value
     * @see Id160#random(Random, Id160)
     * @throws NullPointerException
     *             if the specified bound is null
     */
    public Id160 nextId160(Id160 bound) {
        return Id160.random(this, bound);
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between the given least value (inclusive) and bound
     * (exclusive).
     *
     * @param least
     *            the least value returned
     * @param bound
     *            the upper bound (exclusive)
     * @return a random bounded value
     * @throws IllegalArgumentException
     *             if least is greater than or equal to bound
     * @throws NullPointerException
     *             if the specified bound or least is null
     */
    public Id160 nextId160(Id160 least, Id160 bound) {
        return Id160.random(this, least, bound);
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between the given least value (inclusive) and bound
     * (exclusive).
     *
     * @param least
     *            the least value returned
     * @param bound
     *            the upper bound (exclusive)
     * @throws IllegalArgumentException
     *             if least greater than or equal to bound
     * @return the next value
     */
    public int nextInt(int least, int bound) {
        if (least >= bound || least <= 0) {
            throw new IllegalArgumentException();
        }
        return nextInt(bound - least) + least;
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between 0 (inclusive) and the specified value (exclusive).
     *
     * @param n
     *            the bound on the random number to be returned. Must be positive.
     * @return the next value
     * @throws IllegalArgumentException
     *             if n is not positive
     */
    public long nextLong(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive, was " + n);
        }
        if (n <= Integer.MAX_VALUE) {
            return nextInt((int) n);
        } else if (n <= ((long) Integer.MAX_VALUE) << 1L + 1) { // No more than 32 bits are needed
            for (;;) {
                long val = next(32) & 0xffffffffL;
                if (val < n) {
                    return val;
                }
            }
        }
        int bits = 32 - Integer.numberOfLeadingZeros((int) ((n - 1) >>> 32));
        for (;;) {
            long msb = ((long) next(bits)) << 32L;
            if (msb < n) {
                long val = msb | (next(32) & 0xffffffffL);
                if (val < n) {
                    return val;
                }
            }

        }
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between the given least value (inclusive) and bound
     * (exclusive).
     *
     * @param least
     *            the least value returned
     * @param bound
     *            the upper bound (exclusive)
     * @return the next value
     * @throws IllegalArgumentException
     *             if least is greater than or equal to bound, or if least is negative
     */
    public long nextLong(long least, long bound) {
        if (least >= bound || least <= 0) {
            throw new IllegalArgumentException();
        }
        return nextLong(bound - least) + least;
    }

    /**
     * Returns a new type 4 (pseudo randomly generated) UUID.
     * <p>
     * <strong>Note that, unlike {@link UUID#randomUUID()}, this implementation is not cryptographically
     * secure.</strong> This means that UUIDs created by this method should not be used in situations where if multiple
     * UUIDs has been revealed (or guessed correctly), it must be impossible to reconstruct the stream of random UUIDs
     * prior to the revelation.
     *
     * @return the new UUID
     * @see <a
     *      href="http://en.wikipedia.org/wiki/Cryptographically_secure_pseudorandom_number_generator">http://en.wikipedia.org/wiki/Cryptographically_secure_pseudorandom_number_generator</a>
     */
    public UUID nextUUID() {
        return RandomUtil.longsToType4UUID(nextLong(), nextLong());
    }

    /**
     * Initializes the random with the specified seed.
     *
     * @param seedInts
     *            the seed (seedInts.length must be 8)
     */
    private void seed(int[] seedInts) {
        long longMT = 19650218;
        mt[0] = (int) longMT;
        for (mti = 1; mti < N; ++mti) {
            longMT = (1812433253L * (longMT ^ (longMT >> 30)) + mti) & 0xffffffffL;
            mt[mti] = (int) longMT;
        }

        // FOREIGN_CODE
        // This section is translated from the init_by_array code in the Makoto Matsumoto version.
        int i = 1;
        int j = 0;
        for (int k = Math.max(N, seedInts.length); k > 0; k--) {
            mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * 1664525)) + seedInts[j] + j;
            i++;
            j++;
            if (i >= N) {
                mt[0] = mt[N - 1];
                i = 1;
            }
            if (j >= seedInts.length) {
                j = 0;
            }
        }
        for (int k = N - 1; k > 0; k--) {
            mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * 1566083941)) - i;
            i++;
            if (i >= N) {
                mt[0] = mt[N - 1];
                i = 1;
            }
        }
        mt[0] = 0x80000000;
    }

    /**
     * Throws {@code UnsupportedOperationException}. Setting seeds in this random is not supported.
     *
     * @throws UnsupportedOperationException
     *             always
     */
    public void setSeed(long seed) {
        if (initialized) {// This is needed for the first release of 1.7 which calls setSeed from Random() constructor
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the current thread's {@code ThreadLocalMersenneTwister}.
     *
     * @return the current thread's {@code ThreadLocalMersenneTwister}
     */
    public static ThreadLocalMersenneTwisterRandom current() {
        return THREAD_LOCAL.get();
    }

    // @Override
    // public IntStream ints() {
    // return IntStream.generate(new IntSupplier() {
    // public int getAsInt() {
    // return nextInt();
    // }
    // });
    // }
    //
    // @Override
    // public LongStream longs() {
    // return LongStream.generate(new LongSupplier() {
    // public long getAsLong() {
    // return nextLong();
    // }
    // });
    // }
    //
    // @Override
    // public DoubleStream doubles() {
    // return DoubleStream.generate(new DoubleSupplier() {
    // public double getAsDouble() {
    // return nextInt();
    // }
    // });
    // }
    //
    // @Override
    // public DoubleStream gaussians() {
    // return DoubleStream.generate(new DoubleSupplier() {
    // public double getAsDouble() {
    // return nextGaussian();
    // }
    // });
    // }
}
