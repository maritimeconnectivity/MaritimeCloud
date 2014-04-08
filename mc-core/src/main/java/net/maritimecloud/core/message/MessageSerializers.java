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
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import net.maritimecloud.internal.message.json.JSONMessageReader;
import net.maritimecloud.internal.message.json.JSONMessageWriter;
import net.maritimecloud.internal.message.json.JSONTokener;

/**
 *
 * @author Kasper Nielsen
 */
public class MessageSerializers {

    // Almindelig vil skrive { som det foerste. og } naar den bliver closed.

    /**
     * Creates a message writer that will serialize messages as JSON. The serialized message can be read by a reader
     * created by {@link MessageSerializers#newJSONReader(CharSequence)}. The writer created by this method will not
     * include starting (<code>{</code>) and ending (<code>}</code>) tags. This tags are not added to allow for easier
     * embedding of fragments of JSON.
     *
     * @param ps
     *            the print stream to write to
     * @return a message writer that will write to the given print stream
     */
    public static MessageWriter newJSONFragmentWriter(PrintStream ps) {
        return newJSONFragmentWriter(new PrintWriter(ps));
    }

    /**
     * Creates a message writer that can serialize messages as JSON. The serialized message can be read by a reader
     * created by {@link MessageReader#createJSONReader(CharSequence)}.
     *
     * @param ps
     *            the writer to write to
     * @return a message writer that will write to the given print stream
     */
    public static MessageWriter newJSONFragmentWriter(Writer w) {
        PrintWriter pw = w instanceof PrintWriter ? (PrintWriter) w : new PrintWriter(w);
        return new JSONMessageWriter(pw);
    }

    public static String writeToJSON(MessageSerializable message) {
        StringWriter sw = new StringWriter();
        try {
            writeToJSON(message, sw);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write message as JSON", e);
        }
        return sw.toString();
    }

    public static void writeToJSON(MessageSerializable message, Writer w) throws IOException {
        PrintWriter pw = w instanceof PrintWriter ? (PrintWriter) w : new PrintWriter(w);
        try (JSONMessageWriter jw = new JSONMessageWriter(pw)) {
            jw.writeMessage(message);
        }
    }

    public static <T> T readFromJSON(CharSequence cs, MessageParser<T> parser) {
        return null;
    }

    public static MessageReader newJSONReader(CharSequence cs, boolean readStartStopTags) {
        if (!readStartStopTags) {
            cs = "{" + cs + "}";
        }
        return new JSONMessageReader(new JSONTokener(cs.toString()));
    }

    /**
     * Returns a JSON string from the specified message serializable.
     *
     * @param rootName
     * @param serializable
     * @return
     * @throws IOException
     */
    public static String toJSONString(String rootName, MessageSerializable serializable) throws IOException {
        StringWriter sw = new StringWriter();
        sw.append('{');
        newJSONFragmentWriter(sw).writeMessage(1, rootName, serializable);
        sw.append('}');
        return sw.toString();
    }
}
