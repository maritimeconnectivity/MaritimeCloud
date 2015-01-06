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
package net.maritimecloud.internal.message.text.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import net.maritimecloud.internal.core.javax.json.JsonReader;
import net.maritimecloud.internal.core.javax.json.spi.JsonProvider;
import net.maritimecloud.internal.message.TaggableMessageWriter;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractJSONTest {
    static final Binary B1 = Binary.copyFrom(new byte[] { -1, 127, 4 });

    static final Binary B2 = Binary.copyFrom(new byte[] { 0, 27, -1 });

    static final Binary B3 = Binary.copyFrom(new byte[] { -4, -3, -2, -1 });


    static final String BIG_INT = "1234567898765432112345678987654321";

    static final String BIG_DECIMAL = "1234567898765432112345678987654321.123456789";

    static void assertJSONWrite(IOConsumer<MessageWriter> c, String... lines) throws IOException {
        StringWriter sw = new StringWriter();
        c.accept(new TaggableMessageWriter(new JsonValueWriter(sw)));
        String s = sw.toString();// MessageSerializer.writeToJSON(create(c), create3(c));

        BufferedReader lr = new BufferedReader(new StringReader(s));
        if (lines.length > 0) {
            assertEquals("", lr.readLine());
            for (int i = 0; i < lines.length; i++) {
                assertEquals(lines[i], lr.readLine());
            }
        } else {
            assertNull(lr.readLine());
        }
    }

    static JsonMessageReader readerOf(String... lines) {
        JsonReader reader = JsonProvider.provider().createReader(new StringReader(jsonWrite(lines)));
        return new JsonMessageReader(reader);
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

    // static Message create(IOConsumer<MessageWriter> c) {
    // return new Message() {
    // public void writeTo(MessageWriter w) throws IOException {
    // c.accept(w);
    // }
    //
    // @Override
    // public Message immutable() {
    // throw new UnsupportedOperationException();
    // }
    //
    // @Override
    // public String toJSON() {
    // throw new UnsupportedOperationException();
    // }
    // };
    // }
    //
    // static MessageSerializer<Message> create3(IOConsumer<MessageWriter> c) {
    // return new MessageSerializer<Message>() {
    //
    // public void write(Message message, MessageWriter writer) throws IOException {
    // message.writeTo(writer);
    // }
    //
    // @Override
    // public Message read(MessageReader reader) throws IOException {
    // throw new UnsupportedOperationException();
    // }
    // };
    // }
    //
    // static <T extends Message> MessageSerializer<T> create1(IOFunction<MessageReader, T> c) {
    // return new MessageSerializer<T>() {
    //
    // public void write(T message, MessageWriter writer) throws IOException {
    // message.writeTo(writer);
    // }
    //
    // @Override
    // public T read(MessageReader reader) throws IOException {
    // return c.apply(reader);
    // }
    // };
    // }

    interface IOConsumer<T> {
        void accept(T t) throws IOException;
    }

    interface IOFunction<F, T> {
        T apply(F t) throws IOException;
    }

    static class Msg1 extends MessageSerializer<Msg1> implements Message {

        int i1;

        int i2;

        long l1;

        List<Msg1> list = new ArrayList<>();

        public void write(Msg1 message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }

        /** {@inheritDoc} */
        @Override
        public Msg1 read(MessageReader reader) throws IOException {
            Msg1 ddd = new Msg1();
            i1 = reader.readInt(1, "i1", null);
            i2 = reader.readInt(2, "i2", null);
            l1 = reader.readInt64(3, "l1", null);
            return ddd;
        }

        public void writeTo(MessageWriter w) throws IOException {
            w.writeInt(1, "i1", i1);
            w.writeInt(2, "i2", i2);
            w.writeInt64(3, "l1", l1);
            w.writeList(4, "list", list, this);
        }

        /** {@inheritDoc} */
        @Override
        public Message immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public String toJSON() {
            throw new UnsupportedOperationException();
        }
    }
}
