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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
public abstract class BinaryValueReader extends AbstractBinaryValueReader {

    BinaryInputStream bis;

    BinaryValueReader(BinaryInputStream bis) {
        this.bis = requireNonNull(bis);
    }

    /** {@inheritDoc} */
    @Override
    public Binary readBinary() throws IOException {
        int length = bis.readVarint32();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return Binary.copyFrom(baos.toByteArray());
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt() throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64() throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Message> T readMessage(MessageSerializer<T> parser) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public <T> List<T> readList(ValueSerializer<T> parser) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> Map<K, V> readMap(ValueSerializer<K> keyParser, ValueSerializer<V> valueParser) throws IOException {
        return null;
    }

}
