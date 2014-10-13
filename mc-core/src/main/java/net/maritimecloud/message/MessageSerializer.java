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
package net.maritimecloud.message;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import net.maritimecloud.internal.message.text.json.JsonMessageReader;
import net.maritimecloud.internal.message.text.json.JsonValueWriter;

/**
 * A message serializer takes care of persisting and retrieving {@link Message messages}.
 *
 * @param <T>
 *            the type of message that can be serialized
 * @author Kasper Nielsen
 */
public abstract class MessageSerializer<T extends Message> extends ValueSerializer<T> {

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
    public abstract void write(T message, MessageWriter writer) throws IOException;

    /** {@inheritDoc} */
    @Override
    public final void write(T value, ValueWriter writer) throws IOException {
        writer.writeMessage(value, this);
    }

    public static <T extends Message> String writeToJSON(T message, MessageSerializer<T> serializer) {
        StringWriter sw = new StringWriter();
        try {
            writeToJSON(message, serializer, sw);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write message as JSON", e);
        }
        return sw.toString();
    }

    /** {@inheritDoc} */
    @Override
    public void write(int tag, String name, T t, MessageWriter writer) throws IOException {
        writer.writeMessage(tag, name, t, this);
    }

    @SuppressWarnings("resource")
    public static <T extends Message> void writeToJSON(T message, MessageSerializer<T> serializer, Writer w)
            throws IOException {
        requireNonNull(serializer);
        new JsonValueWriter(w).writeMessage(message, serializer);
        w.flush();
    }

    public static <T extends Message> T readFromJSON(MessageSerializer<T> parser, CharSequence cs) {
        JsonMessageReader r = new JsonMessageReader(cs);
        try {
            return parser.read(r);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read message from JSON", e);
        }
    }
}


// Almindelig vil skrive { som det foerste. og } naar den bliver closed.

// /**
// * Creates a message writer that will serialize messages as JSON. The serialized message can be read by a reader
// * created by {@link MessageSerializer#newJSONReader(CharSequence)}. The writer created by this method will not
// * include starting (<code>{</code>) and ending (<code>}</code>) tags. This tags are not added to allow for easier
// * embedding of fragments of JSON.
// *
// * @param ps
// * the print stream to write to
// * @return a message writer that will write to the given print stream
// */
// public static MessageWriter newJSONFragmentWriter(PrintStream ps) {
// return newJSONFragmentWriter(new PrintWriter(ps));
// }

// /**
// * Creates a message writer that can serialize messages as JSON. The serialized message can be read by a reader
// * created by {@link MessageReader#createJSONReader(CharSequence)}.
// *
// * @param ps
// * the writer to write to
// * @return a message writer that will write to the given print stream
// */
// public static MessageWriter newJSONFragmentWriter(Writer w) {
// PrintWriter pw = w instanceof PrintWriter ? (PrintWriter) w : new PrintWriter(w);
// return new JSONMessageWriter(pw);
// }

// public static void main(String[] args) {
// }

// public static String writeToJSONOld(Message message) {
// StringWriter sw = new StringWriter();
// try {
// writeToJSON(message, sw);
// } catch (IOException e) {
// throw new RuntimeException("Failed to write message as JSON", e);
// }
// return sw.toString();
// }

// public static MessageReader newJSONReader(CharSequence cs, boolean readStartStopTags) {
// if (!readStartStopTags) {
// cs = "{" + cs + "}";
// }
// // return new JSONMessageReader(new JSONTokener(cs.toString()));
// return null;
// }

// /**
// * Returns a JSON string from the specified message serializable.
// *
// * @param rootName
// * @param serializable
// * @return
// * @throws IOException
// */
// public static String toJSONString(String rootName, Message serializable) throws IOException {
// StringWriter sw = new StringWriter();
// sw.append('{');
// newJSONFragmentWriter(sw).writeMessage(1, rootName, serializable);
// sw.append('}');
// return sw.toString();
// }

