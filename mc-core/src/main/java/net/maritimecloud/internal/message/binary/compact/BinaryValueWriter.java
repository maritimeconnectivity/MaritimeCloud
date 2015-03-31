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

import net.maritimecloud.internal.message.binary.AbstractBinaryValueWriter;
import net.maritimecloud.internal.message.binary.BinaryUtils;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * A compact binary value writer.
 * 
 * @author Kasper Nielsen
 */
public class BinaryValueWriter extends AbstractBinaryValueWriter {

    /** The output stream to write to. */
    final BinaryOutputStream bos;

    BinaryValueWriter(BinaryOutputStream bos) {
        this.bos = requireNonNull(bos);
    }

    /** {@inheritDoc} */
    @Override
    public void writeBinary(Binary binary) throws IOException {
        byte[] b = binary.toByteArray();
        bos.writeRawVarint32(b.length);
        bos.writeBytes(b);
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt(Integer value) throws IOException {
        bos.writeRawVarint32(BinaryUtils.encodeZigZag32(value));
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt64(Long value) throws IOException {
        bos.writeRawVarint64(BinaryUtils.encodeZigZag64(value));
    }

    /** {@inheritDoc} */
    @Override
    public <T> void writeList(List<T> list, ValueSerializer<T> serializer) throws IOException {
        byte[] b = writeWithWriter(e -> {
            for (T t : list) {
                if (t != null) {
                    serializer.write(t, e);
                }
            }
        });
        writeBinary(Binary.copyFrom(b));
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> void writeMap(Map<K, V> map, ValueSerializer<K> keySerializer, ValueSerializer<V> valueSerializer)
            throws IOException {
        byte[] b = writeWithWriter(e -> {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                if (key != null && value != null) {
                    keySerializer.write(key, e);
                    valueSerializer.write(value, e);
                }
            }
        });
        writeBinary(Binary.copyFrom(b));
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Message> void writeMessage(T message, MessageSerializer<T> serializer) throws IOException {
        byte[] b = writeWithWriter(e -> serializer.write(message, e));
        writeBinary(Binary.copyFrom(b));
    }

    static byte[] writeWithWriter(IOEConsumer<BinaryValueWriter> w) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryOutputStream bos = new BinaryOutputStream(baos);
        BinaryValueWriter bvw = new BinaryValueWriter(bos);
        w.consume(bvw);
        bvw.flush();
        return baos.toByteArray();
    }

    interface IOEConsumer<T> {
        void consume(T t) throws IOException;
    }
}
