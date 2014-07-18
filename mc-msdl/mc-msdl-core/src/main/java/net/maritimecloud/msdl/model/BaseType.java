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
package net.maritimecloud.msdl.model;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.message.Message;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 * The base types that are available for a MSDL file.
 *
 * @author Kasper Nielsen
 */
public enum BaseType {

    /** A list of bytes. */
    BINARY(Binary.class),

    /** A boolean value, can either be true or false. */
    BOOLEAN(Boolean.class),

    /** An arbitrary-precision decimal. */
    DECIMAL(BigDecimal.class),

    /** A Double-precision floating-point number as defined by IEEE 754. */
    DOUBLE(Double.class),

    /** An enum value */
    ENUM(Enum.class),

    /** A Single-precision floating-point number as defined by IEEE 754. */
    FLOAT(Float.class),

    /** A 32-bit integer. */
    INT(Integer.class),

    /** A 64-bit integer. */
    INT64(Long.class),

    /** A list type. */
    LIST(List.class),

    /** A map type. */
    MAP(Map.class),

    /** Another message. */
    MESSAGE(Message.class),

    /** A position. */
    POSITION(Position.class),

    /** A position coupled with a timestamp. */
    POSITION_TIME(PositionTime.class),

    /** A set type. */
    SET(Set.class),

    /** A UTF-8 String. */
    TEXT(String.class),

    /** A 64-bit timestamp type. */
    TIMESTAMP(Timestamp.class),

    /** An arbitrary-precision integer type. */
    VARINT(BigInteger.class);

    final Class<?> javaType;

    BaseType(Class<?> javaType) {
        this.javaType = requireNonNull(javaType);
    }

    /**
     * @return the javaType
     */
    public Class<?> getJavaType() {
        return javaType;
    }

    /**
     * Returns <tt>true</tt> if the state is any of the specified states, otherwise false.
     *
     * @param states
     *            the states to check if this state is in
     * @return <tt>true</tt> if the state is any of the specified states, otherwise false
     */
    public boolean isAnyOf(BaseType... states) {
        for (BaseType s : states) {
            if (s == this) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether or not this type is complex type. {@link #LIST}, {@link #SET} and {@link #MAP} are considered
     * complex types.
     *
     * @return whether or not this type is complex type
     */
    public boolean isComplexType() {
        return this == LIST || this == SET || this == MAP;
    }

    public boolean isPrimitive() {
        return !isComplexType() && !isReferenceType();
    }

    public boolean isReferenceType() {
        return this == ENUM || this == MESSAGE;
    }
}
