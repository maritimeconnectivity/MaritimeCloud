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
package net.maritimecloud.internal.message.rnd;

import java.io.IOException;

import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;

/**
 *
 * @author Kasper Nielsen
 */
public class RndTest {

    private final int rounds;

    SerializerImpl si;

    public RndTest(int rounds) {
        this.rounds = rounds;

    }


    public void run() {
        for (int i = 0; i < rounds; i++) {

            // gen msg
            Ser ser = null;

            byte[] b = si.serializer(ser, ser);

            si.deserializer(ser, b);
        }
    }

    class Ser extends MessageSerializer<Ser> implements Message {

        /** {@inheritDoc} */
        @Override
        public Ser read(MessageReader reader) throws IOException {
            return null;
        }

        /** {@inheritDoc} */
        @Override
        public void write(Ser message, MessageWriter writer) throws IOException {}

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

    class Field {
        private int id;

        private String name;

        private Object value;

        private Class<?> type;


        void writeIt(MessageWriter w) throws IOException {
            if (type == Double.class) {
                w.writeDouble(id, name, (Double) value);
            }
        }
    }


}
