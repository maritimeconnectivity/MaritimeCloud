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

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import net.maritimecloud.internal.message.binary.AbstractBinaryMessageWriter;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;

import com.google.protobuf.CodedOutputStream;

/**
 *
 * @author Kasper Nielsen
 */
public class ProtobufMessageWriter extends AbstractBinaryMessageWriter {

    final CodedOutputStream cos;

    ProtobufMessageWriter(OutputStream os) {
        // this.os = requireNonNull(os);
        cos = CodedOutputStream.newInstance(os);
    }

    /** {@inheritDoc} */
    @Override
    public void writeBoolean(int tag, String name, Boolean value) throws IOException {

    }

    /** {@inheritDoc} */
    @Override
    public void flush() throws IOException {
        cos.flush();
    }

    /** {@inheritDoc} */
    @Override
    protected void writeBinary(int tag, byte[] bin) throws IOException {
        cos.writeByteArray(tag, bin);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeInt640(int tag, long value) throws IOException {}

    /** {@inheritDoc} */
    @Override
    protected <T extends Message> void writeMessage0(int tag, T message, MessageSerializer<T> serializer)
            throws IOException {}

    /** {@inheritDoc} */
    @Override
    protected void writeInt640(int tag, String name, long value) throws IOException {}

    /** {@inheritDoc} */
    @Override
    protected void writeDecimal0(int tag, BigDecimal bd) throws IOException {}

    /** {@inheritDoc} */
    @Override
    protected void writeInt0(int tag, int value) throws IOException {}

    /** {@inheritDoc} */
    @Override
    protected <K, V> void writeMap0(int tag, Map<K, V> map, ValueSerializer<K> keySerializer,
            ValueSerializer<V> valueSerializer) throws IOException {}

    /** {@inheritDoc} */
    @Override
    protected <T> void writeSetOrList(int tag, Collection<T> set, ValueSerializer<T> serializer) throws IOException {}

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {}

}
