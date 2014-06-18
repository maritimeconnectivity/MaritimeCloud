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
package net.maritimecloud.internal.message.json;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonReader;
import javax.json.spi.JsonProvider;

import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializable;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractJSONTest {
    static final Binary B1 = Binary.copyFrom(new byte[] { -1, 127, 4 });

    static final Binary B2 = Binary.copyFrom(new byte[] { 0, 27, -1 });

    static final Binary B3 = Binary.copyFrom(new byte[] { -4, -3, -2, -1 });

    static void assertJSONWrite(IOConsumer<MessageWriter> c, String... lines) throws IOException {
        String s = MessageSerializers.writeToJSON(create(c));
        // System.out.println(s);
        BufferedReader lr = new BufferedReader(new StringReader(s));
        assertEquals("{", lr.readLine());

        for (int i = 0; i < lines.length; i++) {
            assertEquals("  " + lines[i], lr.readLine());
        }
        assertEquals("}", lr.readLine());
    }

    static JSONMessageReader readerOf(String... lines) {
        JsonReader reader = JsonProvider.provider().createReader(new StringReader(jsonWrite(lines)));
        return new JSONMessageReader(reader);
    }

    static String jsonWrite(String... lines) {
        StringWriter sw = new StringWriter();
        sw.append("{").append("\n");
        for (String l : lines) {
            sw.write("  " + l + "\n");
        }
        sw.append("}").append("\n");
        return sw.toString();
    }

    static MessageSerializable create(IOConsumer<MessageWriter> c) {
        return new MessageSerializable() {
            public void writeTo(MessageWriter w) throws IOException {
                c.accept(w);
            }
        };
    }

    static <T extends MessageSerializable> MessageParser<T> create1(IOFunction<MessageReader, T> c) {
        return new MessageParser<T>() {
            @Override
            public T parse(MessageReader reader) throws IOException {
                return c.apply(reader);
            }
        };
    }

    interface IOConsumer<T> {
        void accept(T t) throws IOException;
    }

    interface IOFunction<F, T> {
        T apply(F t) throws IOException;
    }

    static class Msg1 extends MessageParser<Msg1> implements MessageSerializable {

        int i1;

        int i2;

        long l1;

        List<Msg1> list = new ArrayList<>();

        /** {@inheritDoc} */
        @Override
        public Msg1 parse(MessageReader reader) throws IOException {
            Msg1 ddd = new Msg1();
            i1 = reader.readInt32(1, "i1", null);
            i2 = reader.readInt32(2, "i2", null);
            l1 = reader.readInt64(3, "l1", null);
            return ddd;
        }

        /** {@inheritDoc} */
        @Override
        public void writeTo(MessageWriter w) throws IOException {
            w.writeInt32(1, "i1", i1);
            w.writeInt32(2, "i2", i2);
            w.writeInt64(3, "l1", l1);
            w.writeList(4, "list", list);
        }
    }
}
