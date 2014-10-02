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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.maritimecloud.internal.message.binary.AbstractBinaryValueReader;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;

import com.google.protobuf.CodedInputStream;

/**
 *
 * @author Kasper Nielsen
 */
public class ProtobufValueReader extends AbstractBinaryValueReader {

    CodedInputStream bis;

    ProtobufValueReader(CodedInputStream bis) {
        this.bis = requireNonNull(bis);
    }

    /** {@inheritDoc} */
    @Override
    public Binary readBinary() throws IOException {
        byte[] a = bis.readByteArray();
        return Binary.copyFrom(a);
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt() throws IOException {
        return bis.readInt32();
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
        byte[] b = bis.readByteArray();

        CodedInputStream cis = CodedInputStream.newInstance(b);
        ArrayList<T> list = new ArrayList<>();
        ProtobufValueReader r = new ProtobufValueReader(cis);
        while (!cis.isAtEnd()) {
            T t = parser.read(r);
            if (t != null) {
                list.add(t);
            }
        }
        return list;
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> Map<K, V> readMap(ValueSerializer<K> keyParser, ValueSerializer<V> valueParser) throws IOException {
        return null;
    }

}
