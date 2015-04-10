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
package net.maritimecloud.internal.message.binary.protobuf;

import net.maritimecloud.internal.core.com.google.protobuf.CodedOutputStream;
import net.maritimecloud.internal.message.binary.AbstractBinaryValueWriter;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * A compact Protobuf value writer.
 *
 * @author Kasper Nielsen
 */
public class ProtobufValueWriter extends AbstractBinaryValueWriter {

    /** The output stream to write to. */
    final CodedOutputStream cos;

    /**
     * Constructor
     * @param cos the coded output stream
     */
    ProtobufValueWriter(CodedOutputStream cos) {
        this.cos = requireNonNull(cos);
    }

    /**
     * Constructor
     * @param os the output stream
     */
    public ProtobufValueWriter(OutputStream os) {
        this(CodedOutputStream.newInstance(os));
    }

    /** {@inheritDoc} */
    @Override
    public void flush() throws IOException {
        cos.flush();
    }

    /** {@inheritDoc} */
    @Override
    public void writeBinary(Binary binary) throws IOException {
        byte[] b = binary.toByteArray();
        cos.writeByteArrayNoTag(b);
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt(Integer value) throws IOException {
        cos.writeSInt32NoTag(value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt64(Long value) throws IOException {
        cos.writeSInt64NoTag(value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeDecimal(BigDecimal value) throws IOException {
        writeInt(value.scale());
        writeBinary(Binary.copyFrom(value.unscaledValue().toByteArray()));
    }

    /** {@inheritDoc} */
    @Override
    public <T> void writeList(List<T> list, ValueSerializer<T> serializer) throws IOException {
        for (T t : list) {
            if (t != null) {
                serializer.write(t, this);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> void writeMap(Map<K, V> map, ValueSerializer<K> keySerializer, ValueSerializer<V> valueSerializer)
            throws IOException {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (key != null && value != null) {
                keySerializer.write(key, this);
                valueSerializer.write(value, this);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Message> void writeMessage(T message, MessageSerializer<T> serializer) throws IOException {
        byte[] b = writeWithMessageWriter(e -> serializer.write(message, e));
        writeBinary(Binary.copyFrom(b));
    }

    /**
     * Helper function. Generate the Protobuf byte array for the value writer consumer
     * @param w the value writer consumer
     * @return the Protobuf bytes
     */
    static byte[] writeWithValueWriter(IOEConsumer<ProtobufValueWriter> w) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CodedOutputStream bos = CodedOutputStream.newInstance(baos);
        ProtobufValueWriter bvw = new ProtobufValueWriter(bos);
        w.consume(bvw);
        bvw.flush();
        return baos.toByteArray();
    }

    /**
     * Helper function. Generate the Protobuf byte array for the message writer consumer
     * @param w the message writer consumer
     * @return the Protobuf bytes
     */
    static byte[] writeWithMessageWriter(IOEConsumer<ProtobufMessageWriter> w) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProtobufMessageWriter bmw = new ProtobufMessageWriter(baos);
        w.consume(bmw);
        bmw.flush();
        return baos.toByteArray();
    }

    interface IOEConsumer<T> {
        void consume(T t) throws IOException;
    }
}
