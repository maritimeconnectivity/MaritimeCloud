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

// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
// http://code.google.com/p/protobuf/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
//     * Neither the name of Google Inc. nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package net.maritimecloud.util;

import java.util.NoSuchElementException;

/**
 * This class is used to represent the substring of a {@link Binary} over a single byte array. In terms of the public
 * API of {@link Binary}, you end up here by calling {@link Binary#copyFrom(byte[])} followed by
 * {@link Binary#substring(int, int)}.
 *
 * <p>
 * This class contains most of the overhead involved in creating a substring from a {@link LiteralBinary}. The overhead
 * involves some range-checking and two extra fields.
 *
 * @author carlanton@google.com (Carl Haverl)
 */
class BoundedBinary extends LiteralBinary {

    private final int bytesOffset;

    private final int bytesLength;

    /**
     * Creates a {@code BoundedBinary} backed by the sub-range of given array, without copying.
     *
     * @param bytes
     *            array to wrap
     * @param offset
     *            index to first byte to use in bytes
     * @param length
     *            number of bytes to use from bytes
     * @throws IllegalArgumentException
     *             if {@code offset < 0}, {@code length < 0}, or if {@code offset + length >
     *                                  bytes.length}.
     */
    BoundedBinary(byte[] bytes, int offset, int length) {
        super(bytes);
        if (offset < 0) {
            throw new IllegalArgumentException("Offset too small: " + offset);
        } else if (length < 0) {
            throw new IllegalArgumentException("Length too small: " + offset);
        } else if ((long) offset + length > bytes.length) {
            throw new IllegalArgumentException("Offset+Length too large: " + offset + "+" + length);
        }
        this.bytesOffset = offset;
        this.bytesLength = length;
    }

    /**
     * Gets the byte at the given index. Throws {@link ArrayIndexOutOfBoundsException} for backwards-compatibility
     * reasons although it would more properly be {@link IndexOutOfBoundsException}.
     *
     * @param index
     *            index of byte
     * @return the value
     * @throws ArrayIndexOutOfBoundsException
     *             {@code index} is < 0 or >= size
     */
    @Override
    public byte byteAt(int index) {
        // We must check the index ourselves as we cannot rely on Java array index
        // checking for substrings.
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index too small: " + index);
        } else if (index >= size()) {
            throw new ArrayIndexOutOfBoundsException("Index too large: " + index + ", " + size());
        }
        return bytes[bytesOffset + index];
    }

    @Override
    public int size() {
        return bytesLength;
    }

    @Override
    protected int getOffsetIntoBytes() {
        return bytesOffset;
    }

    // =================================================================
    // Binary -> byte[]

    @Override
    protected void copyToInternal(byte[] target, int sourceOffset, int targetOffset, int numberToCopy) {
        System.arraycopy(bytes, getOffsetIntoBytes() + sourceOffset, target, targetOffset, numberToCopy);
    }

    // =================================================================
    // ByteIterator

    @Override
    public ByteIterator iterator() {
        return new BoundedByteIterator();
    }

    class BoundedByteIterator implements ByteIterator {

        private int position;

        private final int limit;

        BoundedByteIterator() {
            position = getOffsetIntoBytes();
            limit = position + size();
        }

        public boolean hasNext() {
            return position < limit;
        }

        public Byte next() {
            // Boxing calls Byte.valueOf(byte), which does not instantiate.
            return nextByte();
        }

        public byte nextByte() {
            if (position >= limit) {
                throw new NoSuchElementException();
            }
            return bytes[position++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
