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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

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

/**
 *
 * @author Kasper Nielsen
 */
public class BinaryMessageReader extends AbstractMessageReader {

    /** {@inheritDoc} */
    @Override
    public MessageFormatType getFormatType() {
        return MessageFormatType.MACHINE_READABLE;
    }

    final BinaryInputStream bis;

    public BinaryMessageReader(BinaryInputStream bis) {
        this.bis = bis;
    }

    @Override
    public boolean isNext(int tag, String name) throws IOException {
        return tag == bis.getFieldId();
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
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Message> T readMessage(int tag, String name, MessageSerializer<T> parser) throws IOException {
        Binary bin = readBinary(tag, name, Binary.EMPTY);
        if (bin != null) {
            BinaryMessageReader bmr = new BinaryMessageReader(new BinaryInputStream(bin));
            return parser.read(bmr);
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected ValueReader find(int tag, String name) throws SerializationException {
        return null;
    }

    @Override
    protected ValueReader findOptional(int tag, String name) throws IOException {
        if (isNext(tag, name)) {
            int wireType = bis.getWireType();
            return new FlexibleBinaryValueReader(wireType, bis);
            // if (Types.isConstantWireType(wireType)) {
            // return TReaderConstant.fromWireType(wireType);
            // } else if (wireType == Types.WIRETYPE_FIXED_8) {
            // byte bal = bis.readRawByte();
            // return new TReaderConstant(bal);
            // } else if (wireType == Types.WIRETYPE_FIXED_16) {
            //
            // } else if (wireType == Types.WIRETYPE_FIXED_32) {
            //
            // } else if (wireType == Types.WIRETYPE_FIXED_64) {
            //
            // } else if (wireType == Types.WIRETYPE_NUMBER_OF_BYTES) {}
        }
        return null;
    }

    public static <T extends Message> T read(byte[] message, MessageSerializer<T> serializer) throws IOException {
        ByteArrayInputStream baos = new ByteArrayInputStream(message);
        BinaryInputStream bos = new BinaryInputStream(baos);
        BinaryMessageReader bvw = new BinaryMessageReader(bos);
        return serializer.read(bvw);
    }
}
