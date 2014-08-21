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
package net.maritimecloud.internal.message.binary;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Kasper Nielsen
 */
public class ReaderUtils {

    /**
     * Reads a varint from the input one byte at a time, so that it does not read any bytes after the end of the varint.
     * If you simply wrapped the stream in a CodedInputStream and used {@link #readRawVarint32(InputStream)} then you
     * would probably end up reading past the end of the varint since CodedInputStream buffers its input.
     */
    static int readRawVarint32(InputStream input) throws IOException {
        final int firstByte = input.read();
        if (firstByte == -1) {
            throw Exceptions.truncatedMessage();
        }
        return readRawVarint32(firstByte, input);
    }

    /**
     * Like {@link #readRawVarint32(InputStream)}, but expects that the caller has already read one byte. This allows
     * the caller to determine if EOF has been reached before attempting to read.
     */
    public static int readRawVarint32(int firstByte, InputStream input) throws IOException {
        if ((firstByte & 0x80) == 0) {
            return firstByte;
        }

        int result = firstByte & 0x7f;
        int offset = 7;
        for (; offset < 32; offset += 7) {
            final int b = input.read();
            if (b == -1) {
                throw Exceptions.truncatedMessage();
            }
            result |= (b & 0x7f) << offset;
            if ((b & 0x80) == 0) {
                return result;
            }
        }
        // Keep reading up to 64 bits.
        for (; offset < 64; offset += 7) {
            final int b = input.read();
            if (b == -1) {
                throw Exceptions.truncatedMessage();
            }
            if ((b & 0x80) == 0) {
                return result;
            }
        }
        throw Exceptions.malformedVarint();
    }
}
