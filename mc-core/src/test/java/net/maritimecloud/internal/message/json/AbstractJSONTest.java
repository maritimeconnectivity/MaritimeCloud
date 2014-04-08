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

import net.maritimecloud.core.message.MessageSerializable;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractJSONTest {

    static void assertJSON(IOConsumer<MessageWriter> c, String... lines) throws IOException {
        String s = MessageSerializers.writeToJSON(create(c));
        System.out.println(s);
        BufferedReader lr = new BufferedReader(new StringReader(s));
        assertEquals("{", lr.readLine());

        for (int i = 0; i < lines.length; i++) {
            assertEquals("  " + lines[i], lr.readLine());
        }
        assertEquals("}", lr.readLine());
    }

    static MessageSerializable create(IOConsumer<MessageWriter> c) {
        return new MessageSerializable() {
            public void writeTo(MessageWriter w) throws IOException {
                c.accept(w);
            }
        };
    }


    interface IOConsumer<T> {
        void accept(T t) throws IOException;
    }
}
