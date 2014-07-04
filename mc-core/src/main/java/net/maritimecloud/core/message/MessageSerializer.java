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
package net.maritimecloud.core.message;

import java.io.IOException;

/**
 * A message serializer takes care of persisting and retrieving {@link Message messages}.
 *
 * @author Kasper Nielsen
 */
public abstract class MessageSerializer<T extends MessageSerializable> extends ValueSerializer<T> {

    /**
     * Reads a message from the specified reader
     *
     * @param reader
     *            the reader to create the message from
     * @return the message that was constructed from the reader
     * @throws IOException
     *             if the message could not be read
     */
    public abstract T read(MessageReader reader) throws IOException;

    /** {@inheritDoc} */
    @Override
    public final T read(ValueReader reader) throws IOException {
        return reader.readMessage(this);
    }

    /**
     * Write the specified message to the specified writer.
     *
     * @param message
     *            the message to write
     * @param writer
     *            the writer to write the message to
     * @throws IOException
     *             if the message could not be written
     */
    public void write(T message, MessageWriter writer) throws IOException {
        message.writeTo(writer);
    }

    /** {@inheritDoc} */
    @Override
    public final void write(T value, ValueWriter writer) throws IOException {
        writer.writeMessage(value, this);
    }
}
