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
package net.maritimecloud.util;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.TimeUnit;


/**
 * The class <code>Date</code> represents a specific instant in time, with millisecond precision.
 *
 * @author Kasper Nielsen
 */
public class Timestamp implements Serializable {

    private static final long serialVersionUID = -1966283025437623663L;

    /** The default clock used for creating timestamps. */
    static final Clock CLOCK = Clock.systemUTC();

    final long value;

    Timestamp(long value) {
        this.value = checkValue(value);
    }

    public Instant asInstant() {
        return Instant.ofEpochMilli(value);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        return other instanceof Timestamp && ((Timestamp) other).value == value;
    }

    public long getTime() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    public Timestamp plus(long value, TimeUnit unit) {
        checkValue(value);
        return new Timestamp(value <= Long.MAX_VALUE - this.value ? value + this.value : Long.MAX_VALUE);
    }


    static long checkValue(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be non-negative, was " + value);
        }
        return value;
    }

    public Binary toBinary() {
        byte[] bytes = ByteBuffer.allocate(8).putLong(getTime()).array();
        return Binary.copyFrom(bytes);
    }

    public static Timestamp create(long value) {
        return new Timestamp(value);
    }

    /**
     * Returns a new timestamp with the current time.
     *
     * @return a new timestamp with the current time
     */
    public static Timestamp now() {
        return new Timestamp(CLOCK.millis());
    }
}
