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
import net.maritimecloud.internal.core.com.google.protobuf.CodedOutputStream;
import net.maritimecloud.internal.message.AbstractMessageReader;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageEnum;
import net.maritimecloud.message.MessageEnumSerializer;
import net.maritimecloud.message.MessageFormatType;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.SerializationException;
import net.maritimecloud.message.ValueReader;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of a message reader that uses the Google Protobuf wire format.
 * <p>
 * Because of underlying differences in the way the MessageReader and the
 * Protobuf CodedMessageReader is used, the message is initially read into
 * a {@code fieldValues} map, that maps the field tags to ValueReaders.
 */
@SuppressWarnings("unused")
public class ProtobufMessageReader extends AbstractMessageReader {

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageFormatType getFormatType() {
        return MessageFormatType.MACHINE_READABLE;
    }

    /** Maps the message tags to the corresponding ValueReader instance */
    final Map<Integer, ValueReader> fieldValues = new ConcurrentHashMap<>();

    /** Keep track of the currently read tag */
    final Integer[] tags;
    int tagIndex = 0;

    /**
     * Constructor
     * @param in the input stream
     */
    public ProtobufMessageReader(InputStream in) throws IOException {
        this(CodedInputStream.newInstance(in));
    }

    /**
     * Constructor
     * @param cis the coded input stream
     */
    public ProtobufMessageReader(CodedInputStream cis) throws IOException {

        // Read the message field by field
        while (!cis.isAtEnd()) {
            int pbTag = cis.readTag();
            int tag = ProtobufWireFormat.getTagFieldNumber(pbTag);
            int wireType = ProtobufWireFormat.getTagWireType(pbTag);
            // We only use two of the supported Protobuf wire types
            switch (wireType) {
                case ProtobufWireFormat.WIRETYPE_VARINT:
                    fieldValues.put(tag, new ProtobufValueReader(cis.readSInt64()));
                    break;
                case ProtobufWireFormat.WIRETYPE_LENGTH_DELIMITED:
                    fieldValues.put(tag, new ProtobufValueReader(CodedInputStream.newInstance(readByteArray(cis))));
                    break;
                default:
                    throw new SerializationException("Invalid protobuf wire type " + wireType);
            }
        }

        tags = fieldValues.keySet().stream().toArray(Integer[]::new);
    }

    /**
     * Prepends the byte-array at the current tag with the length
     * @param cis the coded input stream
     * @return the byte array prepended with the length
     */
    private byte[] readByteArray(CodedInputStream cis) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CodedOutputStream cout = CodedOutputStream.newInstance(bout);
        cout.writeByteArrayNoTag(cis.readByteArray());
        cout.flush();
        return bout.toByteArray();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNext(int tag, String name) {
        return tagIndex <= tags.length - 1 && tags[tagIndex].equals(tag);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends MessageEnum> T readEnum(int tag, String name, MessageEnumSerializer<T> factory)
            throws IOException {
        int enumValue = readInt(tag, name);
        return factory.from(enumValue);
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> Map<K, V> readMap(int tag, String name, ValueSerializer<K> keyParser, ValueSerializer<V> valueParser)
            throws IOException {
        Binary bin = readBinary(tag, name, Binary.EMPTY);
        if (bin != null) {
            CodedInputStream mis = CodedInputStream.newInstance(bin.toByteArray());
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
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Message> T readMessage(int tag, String name, MessageSerializer<T> parser) throws IOException {
        Binary bin = readBinary(tag, name, Binary.EMPTY);
        if (bin != null) {
            CodedInputStream mis = CodedInputStream.newInstance(bin.toByteArray());
            ProtobufMessageReader bmr = new ProtobufMessageReader(mis);
            return parser.read(bmr);
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected ValueReader find(int tag, String name) throws SerializationException {
        ValueReader valueReader = findOptional(tag, name);
        if (valueReader == null) {
            throw new SerializationException("Could not find tag " + tag);
        }
        return valueReader;
    }

    /** {@inheritDoc} */
    @Override
    protected ValueReader findOptional(int tag, String name) {
        if (isNext(tag, name)) {
            tagIndex++;
            return fieldValues.get(tag);
        }
        return null;
    }

    /**
     * Reads a message from a byte array using the given message serializer
     * @param message the message byte array
     * @param serializer the message serializer
     * @return the message
     */
    public static <T extends Message> T read(byte[] message, MessageSerializer<T> serializer) throws IOException {
        CodedInputStream bin = CodedInputStream.newInstance(message);
        ProtobufMessageReader reader = new ProtobufMessageReader(bin);
        return serializer.read(reader);
    }
}

