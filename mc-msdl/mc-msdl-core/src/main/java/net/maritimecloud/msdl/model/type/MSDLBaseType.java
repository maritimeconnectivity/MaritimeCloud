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
package net.maritimecloud.msdl.model.type;


/**
 *
 * @author Kasper Nielsen
 */
public enum MSDLBaseType {

    /** A Single-precision floating-point number as defined by IEEE 754. */
    FLOAT,

    /** A Double-precision floating-point number as defined by IEEE 754. */
    DOUBLE,

    /** A variable length zigzag encoded 32-bit integer. */
    INT32,

    /** A variable length zigzag encoded 64-bit integer. */
    INT64,

    /** A boolean value, can either be true or false. */
    BOOL,

    /** A UTF-8 String. */
    STRING,

    /** A list of bytes. */
    BINARY,

    /** Another message. */
    MESSAGE,

    /** An enum value */
    ENUM,

    /** A list type. */
    LIST,

    /** A set type. */
    SET,

    /** A map type. */
    MAP;

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
