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
import java.util.List;
import java.util.Map;

import net.maritimecloud.internal.message.binary.AbstractBinaryValueWriter;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;

import com.google.protobuf.CodedOutputStream;

/**
 *
 * @author Kasper Nielsen
 */
public class ProtobufValueWrite extends AbstractBinaryValueWriter {

    final CodedOutputStream cos;


    ProtobufValueWrite(OutputStream os) {
        // this.os = requireNonNull(os);
        cos = CodedOutputStream.newInstance(os);
    }

    /** {@inheritDoc} */
    @Override
    public void writeBinary(Binary binary) throws IOException {
        cos.writeByteArrayNoTag(binary.toByteArray());
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt(Integer value) throws IOException {
        cos.writeInt32NoTag(value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt64(Long value) throws IOException {
        cos.writeInt64NoTag(value);
    }

    /** {@inheritDoc} */
    @Override
    public <T> void writeList(List<T> list, ValueSerializer<T> serializer) throws IOException {}

    /** {@inheritDoc} */
    @Override
    public <K, V> void writeMap(Map<K, V> map, ValueSerializer<K> keySerializer, ValueSerializer<V> valueSerializer)
            throws IOException {}

    /** {@inheritDoc} */
    @Override
    public <T extends Message> void writeMessage(T message, MessageSerializer<T> serializer) throws IOException {

    }
}
