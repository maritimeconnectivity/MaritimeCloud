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
final class TReader8Bit extends TReader {

    private final int constant;

    private TReader8Bit(int constant) {
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
}
