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

import static java.util.Objects.requireNonNull;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_CONSTANT_0;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_CONSTANT_1;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_CONSTANT_2;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_CONSTANT_MINUS_1;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_FIXED_16;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_FIXED_32;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_FIXED_64;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_FIXED_8;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_NUMBER_OF_BYTES;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_VARINT_NEGATIVE;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_VARINT_POSITIVE;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.maritimecloud.internal.message.binary.AbstractBinaryValueReader;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class FlexibleBinaryValueReader extends AbstractBinaryValueReader {

    private final int wireType;

    private final BinaryInputStream bis;

    FlexibleBinaryValueReader(int wireType, BinaryInputStream bis) {
        this.wireType = wireType;
        this.bis = requireNonNull(bis);
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt() throws IOException {
        long l = readInt64();
        return (int) l;
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64() throws IOException {
        switch (wireType) {
        case WIRETYPE_CONSTANT_0:
            return 0L;
        case WIRETYPE_CONSTANT_1:
            return 1L;
        case WIRETYPE_CONSTANT_2:
            return 2L;
        case WIRETYPE_CONSTANT_MINUS_1:
            return -1L;
        case WIRETYPE_FIXED_8:
            long b = bis.read08Bits();
            return b;
        case WIRETYPE_FIXED_16:
            b = bis.read16Bits();
            return b;
        case WIRETYPE_FIXED_32:
            b = bis.read32Bits();
            return b;
        case WIRETYPE_VARINT_POSITIVE:
            return 1L;
        case WIRETYPE_VARINT_NEGATIVE:
            return 1L;
        default:
            throw new IOException("Illegal wireType");
        }
    }

    /** {@inheritDoc} */
    @Override
    public Binary readBinary() throws IOException {
        switch (wireType) {
        case WIRETYPE_CONSTANT_0:
            return Binary.copyFrom(new byte[] { 0 });
        case WIRETYPE_CONSTANT_1:
            return Binary.copyFrom(new byte[] { 1 });
        case WIRETYPE_CONSTANT_2:
            return Binary.copyFrom(new byte[] { 2 });
        case WIRETYPE_CONSTANT_MINUS_1:
            return Binary.copyFrom(new byte[] { -1 });
        case WIRETYPE_FIXED_8:
            return Binary.copyFrom(bis.read(1));
        case WIRETYPE_FIXED_16:
            return Binary.copyFrom(bis.read(2));
        case WIRETYPE_FIXED_32:
            return Binary.copyFrom(bis.read(4));
        case WIRETYPE_FIXED_64:
            return Binary.copyFrom(bis.read(8));
        case WIRETYPE_NUMBER_OF_BYTES:
            int numberOfBytes = bis.readVarint32();
            return Binary.copyFrom(bis.read(numberOfBytes));
        default:
            throw new IOException("Illegal wireType");
        }
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Message> T readMessage(MessageSerializer<T> parser) throws IOException {
        Binary bin = readBinary();
        BinaryMessageReader bmr = new BinaryMessageReader(new BinaryInputStream(bin));
        return parser.read(bmr);
    }

    /** {@inheritDoc} */
    @Override
    public <T> List<T> readList(ValueSerializer<T> parser) throws IOException {
        return Collections.emptyList();
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> Map<K, V> readMap(ValueSerializer<K> keyParser, ValueSerializer<V> valueParser) throws IOException {
        return Collections.emptyMap();
    }
}
