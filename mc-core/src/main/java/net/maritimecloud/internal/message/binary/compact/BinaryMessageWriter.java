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
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import net.maritimecloud.internal.message.binary.AbstractBinaryMessageWriter;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;

/**
 *
 * @author Kasper Nielsen
 */
public class BinaryMessageWriter extends AbstractBinaryMessageWriter {

    final BinaryOutputStream os;

    BinaryMessageWriter(BinaryOutputStream bos) {
        this.os = requireNonNull(bos);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {}

    /** {@inheritDoc} */
    @Override
    public void flush() throws IOException {
        os.flush();
    }

    @Override
    protected void writeBinary(int tag, byte[] bin) throws IOException {
        os.writeBytes(tag, bin);
    }

    /** {@inheritDoc} */
    @Override
    public void writeBoolean(int tag, String name, Boolean value) throws IOException {
        if (value != null) {
            os.writeBoolean(tag, name, value);
        }
    }

    @Override
    protected void writeDecimal0(int tag, BigDecimal bd) throws IOException {
        os.encodeAndWriteInteger(tag, bd.scale());
        os.writeBytes(bd.unscaledValue().toByteArray());
    }

    /** {@inheritDoc} */
    @Override
    protected void writeInt0(int tag, int value) throws IOException {
        os.encodeAndWriteInteger(tag, value);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeInt640(int tag, long value) throws IOException {
        os.encodeAndWriteInteger(tag, (int) value);
    }

    /** {@inheritDoc} */
    @Override
    protected <K, V> void writeMap0(int tag, Map<K, V> map, ValueSerializer<K> keySerializer,
            ValueSerializer<V> valueSerializer) throws IOException {
        byte[] bytes = BinaryValueWriter.writeWithWriter(w -> {
            for (Map.Entry<K, V> e : map.entrySet()) {
                keySerializer.write(e.getKey(), w);
                valueSerializer.write(e.getValue(), w);
            }
        });
        os.writeBytes(tag, bytes);
    }

    /** {@inheritDoc} */
    @Override
    protected <T extends Message> void writeMessage0(int tag, T message, MessageSerializer<T> serializer)
            throws IOException {
        byte[] bytes = BinaryValueWriter.writeWithWriter(w -> {
            serializer.write(message, w);
        });
        os.writeBytes(tag, bytes);
    }

    /** {@inheritDoc} */
    @Override
    protected <T> void writeSetOrList(int tag, Collection<T> col, ValueSerializer<T> serializer) throws IOException {
        /*
         * if (col.size() == 1) { T t = col.iterator().next(); if (t != null) { serializer.write(tag, "tagIsIgnored", t,
         * this); } } else {
         */
        byte[] bytes = BinaryValueWriter.writeWithWriter(e -> {
            for (T t : col) {
                if (t != null) {
                    serializer.write(t, e);
                }
            }
        });
        os.writeBytes(tag, bytes);
        // }
    }

    public static <T extends Message> byte[] write(T message, MessageSerializer<T> serializer) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryOutputStream bos = new BinaryOutputStream(baos);
        BinaryMessageWriter bvw = new BinaryMessageWriter(bos);
        serializer.write(message, bvw);
        bvw.flush();
        return baos.toByteArray();
    }

    /** {@inheritDoc} */
    @Override
    protected void writeInt640(int tag, String name, long value) throws IOException {}
}
