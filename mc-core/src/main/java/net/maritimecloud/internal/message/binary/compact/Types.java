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
package net.maritimecloud.internal.message.binary.compact;

/**
 *
 * @author Kasper Nielsen
 */
class Types {

    /** The constant 0. */
    static final int WIRETYPE_CONSTANT_0 = 0;

    /** The constant 1. */
    static final int WIRETYPE_CONSTANT_1 = 1;

    /** The constant 2. */
    static final int WIRETYPE_CONSTANT_2 = 2;

    /** The constant -1. */
    static final int WIRETYPE_CONSTANT_MINUS_1 = 3;

    /** A fixed 8 bit number/string.. */
    static final int WIRETYPE_FIXED_8 = 4;

    /** A fixed 16 bit number/string.. */
    static final int WIRETYPE_FIXED_16 = 5;

    /** A fixed 32 bit number/string.. */
    static final int WIRETYPE_FIXED_32 = 6;

    /** A fixed 64 bit number/string.. */
    static final int WIRETYPE_FIXED_64 = 7;

    static final int WIRETYPE_VARINT_NEGATIVE = 8;

    static final int WIRETYPE_VARINT_POSITIVE = 9;

    static final int WIRETYPE_NUMBER_OF_BYTES = 10;

    static final int TAG_TYPE_BITS = 4;

    static final int TAG_TYPE_MASK = (1 << TAG_TYPE_BITS) - 1;

    static final int MAXIMUM_FIELD_ID = 1 << 29;

    public static void main(String[] args) {
        System.out.println(TAG_TYPE_MASK);
        System.out.println(~TAG_TYPE_MASK);
    }

    static boolean isConstantWireType(int wireType) {
        return wireType >= 0 && wireType <= 3;
    }

    static int getWireType(int tag) {
        return tag & TAG_TYPE_MASK;
    }

    /** Given a tag value, determines the field number (the upper 29 bits). */
    static int getTagFieldNumber(int tag) {
        return tag >>> TAG_TYPE_BITS;
    }

    /** Makes a tag value given a field number and wire type. */
    static int makeTag(int wireType, int fieldNumber) {
        return fieldNumber << TAG_TYPE_BITS | wireType;
    }
}
