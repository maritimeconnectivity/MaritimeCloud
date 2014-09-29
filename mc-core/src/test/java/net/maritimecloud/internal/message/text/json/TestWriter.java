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

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashSet;

import net.maritimecloud.message.TestEnum;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.Ignore;
import org.junit.Test;


/**
 *
 * @author Kasper Nielsen
 */
public class TestWriter extends AbstractJSONTest {

    @Test
    public void testPrimitives() throws IOException {
        assertJSONWrite(w -> w.writeInt(1, "i1", 100), "\"i1\": 100");
        assertJSONWrite(w -> w.writeInt64(1, "1f1", -100L), "\"1f1\": -100");
        assertJSONWrite(w -> w.writeVarInt(1, "f", new BigInteger(BIG_INT)), "\"f\": " + BIG_INT);

        assertJSONWrite(w -> w.writeFloat(1, "i1", 1.2345f), "\"i1\": 1.2345");
        assertJSONWrite(w -> w.writeDouble(1, "1f1", -10.12123d), "\"1f1\": -10.12123");
        assertJSONWrite(w -> w.writeVarInt(1, "f", new BigInteger(BIG_INT)), "\"f\": " + BIG_INT);

        assertJSONWrite(w -> w.writeBoolean(1, "i1", true), "\"i1\": true");
        assertJSONWrite(w -> w.writeBoolean(1, "i1", false), "\"i1\": false");
        assertJSONWrite(w -> w.writeBinary(1, "i1", B1), "\"i1\": \"/38E\"");
        assertJSONWrite(w -> w.writeText(1, "1f1", "hello"), "\"1f1\": \"hello\"");
        assertJSONWrite(w -> w.writeTimestamp(1, "1f1", Timestamp.create(32112)), "\"1f1\": 32112");

        assertJSONWrite(w -> w.writePosition(1, "f", Position.create(10, -10)), "\"f\": {", "  \"latitude\": 10.0,",
                "  \"longitude\": -10.0", "}");

        assertJSONWrite(w -> w.writePositionTime(1, "f", PositionTime.create(10, -10, 123456)), "\"f\": {",
                "  \"latitude\": 10.0,", "  \"longitude\": -10.0,", "  \"time\": 123456", "}");
    }

    @Test
    public void testEnum() throws IOException {
        assertJSONWrite(w -> w.writeEnum(1, "i1", TestEnum.T2), "\"i1\": \"T2\"");
    }

    @Test
    public void testLis2tAndSet() throws IOException {
        assertJSONWrite(w -> w.writeList(1, "i1", Arrays.asList(), ValueSerializer.INT));
    }

    @Test
    @Ignore
    public void testListAndSet() throws IOException {
        assertJSONWrite(w -> w.writeList(1, "i1", Arrays.asList(), ValueSerializer.INT));
        assertJSONWrite(w -> w.writeList(1, "i1", Arrays.asList(1, 3, 2, -4, 1), ValueSerializer.INT), "\"i1\": [",
                "  1,", "  3,", "  2,", "  -4,", "  1", "]");
        assertJSONWrite(
                w -> w.writeSet(1, "i1", new LinkedHashSet<>(Arrays.asList(1, 3, 2, -4, 1)), ValueSerializer.INT),
                "\"i1\": [", "  1,", "  3,", "  2,", "  -4", "]");
        assertJSONWrite(
                w -> w.writeList(1, "i1", Arrays.asList(TestEnum.T2, TestEnum.T3, TestEnum.T1), TestEnum.PARSER),
                "\"i1\": [", "  \"T2\",", "  \"T3\",", "  \"T1\"", "]");
        assertJSONWrite(w -> w.writeList(1, "i1", Arrays.asList(B1, B2, B3), ValueSerializer.BINARY), "\"i1\": [",
                "  \"/38E\",", "  \"ABv/\",", "  \"/P3+/w==\"", "]");
        assertJSONWrite(w -> w.writeList(1, "i1", Arrays.asList(Arrays.asList(1, 3, 2), Arrays.asList(-4, 1)),
                ValueSerializer.INT.listOf()), "\"i1\": [", "  [", "    1,", "    3,", "    2", "  ],", "  [",
                "    -4,", "    1", "  ]", "]");
    }

    @Test
    public void testMap() throws IOException {
        assertJSONWrite(w -> w.writeList(1, "i1", Arrays.asList(1, 3, 2, -4, 1), ValueSerializer.INT), "\"i1\": [",
                "  1,", "  3,", "  2,", "  -4,", "  1", "]");
        assertJSONWrite(
                w -> w.writeSet(1, "i1", new LinkedHashSet<>(Arrays.asList(1, 3, 2, -4, 1)), ValueSerializer.INT),
                "\"i1\": [", "  1,", "  3,", "  2,", "  -4", "]");
        assertJSONWrite(
                w -> w.writeList(1, "i1", Arrays.asList(TestEnum.T2, TestEnum.T3, TestEnum.T1), TestEnum.PARSER),
                "\"i1\": [", "  \"T2\",", "  \"T3\",", "  \"T1\"", "]");
        assertJSONWrite(w -> w.writeList(1, "i1", Arrays.asList(B1, B2, B3), ValueSerializer.BINARY), "\"i1\": [",
                "  \"/38E\",", "  \"ABv/\",", "  \"/P3+/w==\"", "]");
        assertJSONWrite(w -> w.writeList(1, "i1", Arrays.asList(Arrays.asList(1, 3, 2), Arrays.asList(-4, 1)),
                ValueSerializer.INT.listOf()), "\"i1\": [", "  [", "    1,", "    3,", "    2", "  ],", "  [",
                "    -4,", "    1", "  ]", "]");
    }

    @Test
    public void testMessage() throws IOException {
        Msg1 m = new Msg1();
        m.i1 = 123;
        m.i2 = 1234;
        m.l1 = -123;
        assertJSONWrite(w -> w.writeMessage(1, "m", m, new Msg1()), "\"m\": {", "  \"i1\": 123,", "  \"i2\": 1234,",
                "  \"l1\": -123", "}");

        Msg1 m2 = new Msg1();
        m2.i1 = 123;
        m2.i2 = 1234;
        m2.l1 = -123;
        m2.list.add(m);
        assertJSONWrite(w -> w.writeMessage(1, "m", m2, new Msg1()), "\"m\": {", "  \"i1\": 123,", "  \"i2\": 1234,",
                "  \"l1\": -123,", "  \"list\": [", "    {", "      \"i1\": 123,", "      \"i2\": 1234,",
                "      \"l1\": -123", "    }", "  ]", "}");
    }
}
