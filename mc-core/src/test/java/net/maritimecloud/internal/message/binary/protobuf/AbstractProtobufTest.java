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

import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.PositionTime;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for test cases that centers on the binary protobuf message serialization format
 */
public class AbstractProtobufTest {



    static class Msg1 extends MessageSerializer<Msg1> implements Message {

        public static MessageSerializer<Msg1> SERIALIZER = new Msg1();

        String s;
        Integer i;
        Long l;
        Double d;
        BigDecimal bd;
        Timestamp t;
        byte[] bytes;
        PositionTime pt;
        List<Msg1> list = new ArrayList<>();
        Map<String, Msg1> map = new HashMap<>();

        /** {@inheritDoc} */
        @Override
        public void write(Msg1 message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }

        /** {@inheritDoc} */
        @Override
        public Msg1 read(MessageReader reader) throws IOException {
            Msg1 m = new Msg1();
            int index = 1;
            m.s = reader.readText(index++, "s", null);
            m.i = reader.readInt(index++, "i", null);
            m.l = reader.readInt64(index++, "l", null);
            m.d = reader.readDouble(index++, "d", null);
            m.bd = reader.readDecimal(index++, "bd", null);
            m.t = reader.readTimestamp(index++, "t", null);
            Binary binary = reader.readBinary(index++, "binary", null);
            m.bytes = (binary == null) ? null : binary.toByteArray();
            m.pt = reader.readPositionTime(index++, "pt", null);
            m.list = reader.readList(index++, "list", SERIALIZER);
            m.map = reader.readMap(index, "map", ValueSerializer.TEXT, SERIALIZER);
            return m;
        }

        public void writeTo(MessageWriter w) throws IOException {
            int index = 1;
            w.writeText(index++, "s", s);
            w.writeInt(index++, "i", i);
            w.writeInt64(index++, "l", l);
            w.writeDouble(index++, "d", d);
            w.writeDecimal(index++, "bd", bd);
            w.writeTimestamp(index++, "t", t);
            if (bytes != null) {
                w.writeBinary(index, "bytes", bytes);
            }
            index++;
            w.writePositionTime(index++, "pt", pt);
            w.writeList(index++, "list", list, SERIALIZER);
            w.writeMap(index, "map", map, ValueSerializer.TEXT, SERIALIZER);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Msg1 msg1 = (Msg1) o;

            if (s != null ? !s.equals(msg1.s) : msg1.s != null) return false;
            if (i != null ? !i.equals(msg1.i) : msg1.i != null) return false;
            if (l != null ? !l.equals(msg1.l) : msg1.l != null) return false;
            if (d != null ? !d.equals(msg1.d) : msg1.d != null) return false;
            if (bd != null ? !bd.equals(msg1.bd) : msg1.bd != null) return false;
            if (t != null ? !t.equals(msg1.t) : msg1.t != null) return false;
            if (!Arrays.equals(bytes, msg1.bytes)) return false;
            if (pt != null ? !pt.equals(msg1.pt) : msg1.pt != null) return false;
            if (list != null ? !list.equals(msg1.list) : msg1.list != null) return false;
            return !(map != null ? !map.equals(msg1.map) : msg1.map != null);

        }

        @Override
        public int hashCode() {
            int result = s != null ? s.hashCode() : 0;
            result = 31 * result + (i != null ? i.hashCode() : 0);
            result = 31 * result + (l != null ? l.hashCode() : 0);
            result = 31 * result + (d != null ? d.hashCode() : 0);
            result = 31 * result + (bd != null ? bd.hashCode() : 0);
            result = 31 * result + (t != null ? t.hashCode() : 0);
            result = 31 * result + (bytes != null ? Arrays.hashCode(bytes) : 0);
            result = 31 * result + (pt != null ? pt.hashCode() : 0);
            result = 31 * result + (list != null ? list.hashCode() : 0);
            result = 31 * result + (map != null ? map.hashCode() : 0);
            return result;
        }
    }

}
