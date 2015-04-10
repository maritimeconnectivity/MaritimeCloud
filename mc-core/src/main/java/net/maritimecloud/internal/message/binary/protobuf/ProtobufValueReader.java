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

import net.maritimecloud.internal.core.com.google.protobuf.CodedInputStream;
import net.maritimecloud.internal.message.binary.AbstractBinaryValueReader;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * A compact Protobuf value reader.
 * <p>
 * The reader is used by the {@code ProtobufMessageReader}, which pre-loads a map of
 * all field value readers.
 * <p>
 * The reader may be initialized with either a Long value, used as the basis for
 * WIRETYPE_VARINT data blobs, or a CodedInputStream used as the basis for
 * WIRETYPE_LENGTH_DELIMITED data blobs.
 *
 * @author Kasper Nielsen
 */
public class ProtobufValueReader extends AbstractBinaryValueReader {

    CodedInputStream cis;
    Long value;

    /**
     * Constructor
     * @param cis the coded input stream
     */
    ProtobufValueReader(CodedInputStream cis) {
        this.cis = requireNonNull(cis);
    }

    /**
     * Constructor
     * @param value the value
     */
    ProtobufValueReader(Long value) {
        this.value = requireNonNull(value);
    }

    /** Throws an exception unless the value reader was instantiated with a coded input stream */
    private void checkFromInputStream() throws IOException {
        if (cis == null) {
            throw new IOException("The value reader is not based on a CodedInputStream");
        }
    }

    /** {@inheritDoc} */
    @Override
    public Binary readBinary() throws IOException {
        checkFromInputStream();
        byte[] a = cis.readByteArray();
        return Binary.copyFrom(a);
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt() throws IOException {
        if (value != null) {
            return value.intValue();
        }

        checkFromInputStream();
        return cis.readSInt32();
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64() throws IOException {
        if (value != null) {
            return value;
        }

        checkFromInputStream();
        return cis.readSInt64();
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal() throws IOException {
        checkFromInputStream();
        byte[] b = readBinary().toByteArray();
        CodedInputStream bdis = CodedInputStream.newInstance(b);
        int scale = bdis.readSInt32();
        BigInteger unscaled = new BigInteger(bdis.readByteArray());
        return new BigDecimal(unscaled, scale);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Message> T readMessage(MessageSerializer<T> parser) throws IOException {
        checkFromInputStream();
        byte[] b = cis.readByteArray();
        CodedInputStream mis = CodedInputStream.newInstance(b);
        ProtobufMessageReader reader = new ProtobufMessageReader(mis);
        return parser.read(reader);
    }

    /** {@inheritDoc} */
    @Override
    public <T> List<T> readList(ValueSerializer<T> parser) throws IOException {
        checkFromInputStream();
        byte[] b = cis.readByteArray();
        CodedInputStream lis = CodedInputStream.newInstance(b);
        ArrayList<T> list = new ArrayList<>();
        ProtobufValueReader r = new ProtobufValueReader(lis);
        while (!lis.isAtEnd()) {
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
        checkFromInputStream();
        byte[] b = cis.readByteArray();
        CodedInputStream mis = CodedInputStream.newInstance(b);
        Map<K, V> map = new HashMap<>();
        ProtobufValueReader r = new ProtobufValueReader(mis);
        while (!mis.isAtEnd()) {
            K key = keyParser.read(r);
            V value = valueParser.read(r);
            if (key != null && value != null) {
                map.put(key, value);
            }
        }
        return map;
    }

}
