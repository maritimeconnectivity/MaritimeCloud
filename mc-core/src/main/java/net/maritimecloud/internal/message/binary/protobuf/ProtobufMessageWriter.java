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
import net.maritimecloud.internal.message.binary.AbstractBinaryMessageWriter;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Implementation of a message writer that uses the Google Protobuf wire format
 * @author Kasper Nielsen
 */
public class ProtobufMessageWriter extends AbstractBinaryMessageWriter {

    final CodedOutputStream cos;

    /**
     * Constructor
     * @param os the nested output stream
     */
    public ProtobufMessageWriter(OutputStream os) {
        cos = CodedOutputStream.newInstance(os);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
    }

    /** {@inheritDoc} */
    @Override
    public void flush() throws IOException {
        cos.flush();
    }

    /** {@inheritDoc} */
    @Override
    public void writeBoolean(int tag, String name, Boolean value) throws IOException {
        if (value != null) {
            cos.writeBool(tag, value);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void writeBinary(int tag, byte[] bin) throws IOException {
        if (bin != null) {
            cos.writeByteArray(tag, bin);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void writeInt640(int tag, long value) throws IOException {
        cos.writeSInt64(tag, value);
    }

    /** {@inheritDoc} */
    @Override
    protected <T extends Message> void writeMessage0(int tag, T message, MessageSerializer<T> serializer)
            throws IOException {
        byte[] bytes = ProtobufValueWriter.writeWithMessageWriter(w -> serializer.write(message, w));
        cos.writeByteArray(tag, bytes);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeInt640(int tag, String name, long value) throws IOException {
        writeInt640(tag, value);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeDecimal0(int tag, BigDecimal bd) throws IOException {
        byte[] bytes = ProtobufValueWriter.writeWithValueWriter(w -> w.writeDecimal(bd));
        cos.writeByteArray(tag, bytes);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeInt0(int tag, int value) throws IOException {
        cos.writeSInt32(tag, value);
    }

    /** {@inheritDoc} */
    @Override
    protected <K, V> void writeMap0(int tag, Map<K, V> map, ValueSerializer<K> keySerializer,
            ValueSerializer<V> valueSerializer) throws IOException {
        byte[] bytes = ProtobufValueWriter.writeWithValueWriter(w -> w.writeMap(map, keySerializer, valueSerializer));
        cos.writeByteArray(tag, bytes);
    }

    /** {@inheritDoc} */
    @Override
    protected <T> void writeSetOrList(int tag, Collection<T> col, ValueSerializer<T> serializer) throws IOException {
        byte[] bytes = ProtobufValueWriter.writeWithValueWriter(w -> w.writeList(new ArrayList<>(col), serializer));
        cos.writeByteArray(tag, bytes);
    }

    /**
     * Writes the message as a byte array in the Protobuf format
     * @param message the message
     * @param serializer the message serializer
     * @return the bytes
     */
    public static <T extends Message> byte[] write(T message, MessageSerializer<T> serializer) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtobufMessageWriter bvw = new ProtobufMessageWriter(out);
        serializer.write(message, bvw);
        bvw.flush();
        return out.toByteArray();
    }

}
