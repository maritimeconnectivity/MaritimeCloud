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

import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_CONSTANT_0;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_CONSTANT_1;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_CONSTANT_2;
import static net.maritimecloud.internal.message.binary.compact.Types.WIRETYPE_CONSTANT_MINUS_1;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
class TReaderConstant extends TReader {

    private final int constant;

    static final TReaderConstant C0 = new TReaderConstant(0);

    static final TReaderConstant C1 = new TReaderConstant(1);

    static final TReaderConstant C2 = new TReaderConstant(2);

    static final TReaderConstant CM1 = new TReaderConstant(-1);

    TReaderConstant(int constant) {
        this.constant = constant;
    }

    /** {@inheritDoc} */
    @Override
    public Binary readBinary() throws IOException {
        return Binary.copyFrom(new byte[] { (byte) constant });
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal() throws IOException {
        return BigDecimal.valueOf(constant);
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt() throws IOException {
        return constant;
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64() throws IOException {
        return (long) constant;
    }

    /** {@inheritDoc} */
    @Override
    public <T> List<T> readList(ValueSerializer<T> parser) throws IOException {
        T t = parser.read(this);
        return Collections.singletonList(t);
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readVarInt() throws IOException {
        return BigInteger.valueOf(constant);
    }

    static TReaderConstant fromWireType(int wireType) {
        switch (wireType) {
        case WIRETYPE_CONSTANT_0:
            return C0;
        case WIRETYPE_CONSTANT_1:
            return C1;
        case WIRETYPE_CONSTANT_2:
            return C2;
        case WIRETYPE_CONSTANT_MINUS_1:
            return CM1;
        default:
            throw new UnsupportedOperationException();
        }
    }
}
