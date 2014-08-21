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
public abstract class TReader extends AbstractBinaryValueReader {

    /** {@inheritDoc} */
    @Override
    public Binary readBinary() throws IOException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal() throws IOException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt() throws IOException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64() throws IOException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> Map<K, V> readMap(ValueSerializer<K> keyParser, ValueSerializer<V> valueParser) throws IOException {
        throw new UnsupportedOperationException();
    }


    /** {@inheritDoc} */
    @Override
    public <T extends Message> T readMessage(MessageSerializer<T> parser) throws IOException {
        throw new UnsupportedOperationException();
    }


}
