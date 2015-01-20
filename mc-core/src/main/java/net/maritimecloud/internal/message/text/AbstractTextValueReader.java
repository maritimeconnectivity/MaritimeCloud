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
package net.maritimecloud.internal.message.text;

import java.io.IOException;

import net.maritimecloud.message.MessageEnum;
import net.maritimecloud.message.MessageEnumSerializer;
import net.maritimecloud.message.MessageFormatType;
import net.maritimecloud.message.SerializationException;
import net.maritimecloud.message.ValueReader;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractTextValueReader implements ValueReader {

    /** {@inheritDoc} */
    @Override
    public final MessageFormatType getFormatType() {
        return MessageFormatType.HUMAN_READABLE;
    }

    /** {@inheritDoc} */
    @Override
    public Binary readBinary() throws IOException {
        String str = readString();
        try {
            return Binary.copyFromBase64(str);
        } catch (IllegalArgumentException e) {
            throw new SerializationException("The serialized string does not contain a valid based 64 encoded message");
        }
    }

    /** {@inheritDoc} */
    @Override
    public <T extends /* Enum<T> & */MessageEnum> T readEnum(MessageEnumSerializer<T> factory) throws IOException {
        String str = readString();
        T t = factory.from(str);
        if (t == null) {
            throw new SerializationException("The string '" + str + "' does not correspond to a valid enum");
        }
        return t;
    }

    /** {@inheritDoc} */
    @Override
    public Position readPosition() throws IOException {
        return readMessage(Position.SERIALIZER);
    }

    /** {@inheritDoc} */
    @Override
    public PositionTime readPositionTime() throws IOException {
        return readMessage(PositionTime.SERIALIZER);
    }

    protected abstract String readString() throws IOException;

    /** {@inheritDoc} */
    @Override
    public String readText() throws IOException {
        String str = readString();
        return unescape(str);
    }

    protected String unescape(String value) {
        return value;
    }
}
