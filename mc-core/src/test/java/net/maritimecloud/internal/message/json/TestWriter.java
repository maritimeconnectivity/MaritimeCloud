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

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;

import net.maritimecloud.internal.message.TestEnum;
import net.maritimecloud.util.Binary;

import org.junit.Test;


/**
 *
 * @author Kasper Nielsen
 */
public class TestWriter extends AbstractJSONTest {
    static final Binary B1 = Binary.copyFrom(new byte[] { -1, 127, 4 });

    static final Binary B2 = Binary.copyFrom(new byte[] { 0, 27, -1 });

    static final Binary B3 = Binary.copyFrom(new byte[] { -4, -3, -2, -1 });

    @Test
    public void testPrimitives() throws IOException {
        assertJSON(w -> w.writeInt32(1, "i1", 100), "\"i1\": 100");
        assertJSON(w -> w.writeInt64(1, "1f1", -100), "\"1f1\": -100");

        assertJSON(w -> w.writeFloat(1, "i1", 1.2345f), "\"i1\": 1.2345");
        assertJSON(w -> w.writeDouble(1, "1f1", -10.12123d), "\"1f1\": -10.12123");

        assertJSON(w -> w.writeBool(1, "i1", true), "\"i1\": true");
        assertJSON(w -> w.writeBool(1, "i1", false), "\"i1\": false");
        assertJSON(w -> w.writeString(1, "1f1", "hello"), "\"1f1\": \"hello\"");
    }

    @Test
    public void testBinary() throws IOException {
        assertJSON(w -> w.writeBinary(1, "i1", B1), "\"i1\": \"/38E\"");
    }

    @Test
    public void testEnum() throws IOException {
        assertJSON(w -> w.writeEnum(1, "i1", TestEnum.T2), "\"i1\": \"T2\"");
    }

    @Test
    public void testListSet() throws IOException {
        assertJSON(w -> w.writeList(1, "i1", Arrays.asList(1, 3, 2, -4, 1)), "\"i1\": [", "  1,", "  3,", "  2,",
                "  -4,", "  1", "]");
        assertJSON(w -> w.writeSet(1, "i1", new LinkedHashSet<>(Arrays.asList(1, 3, 2, -4, 1))), "\"i1\": [", "  1,",
                "  3,", "  2,", "  -4", "]");
        assertJSON(w -> w.writeList(1, "i1", Arrays.asList(TestEnum.T2, TestEnum.T3, TestEnum.T1)), "\"i1\": [",
                "  \"T2\",", "  \"T3\",", "  \"T1\"", "]");
        assertJSON(w -> w.writeList(1, "i1", Arrays.asList(B1, B2, B3)), "\"i1\": [", "  \"/38E\",", "  \"ABv/\",",
                "  \"/P3+/w==\"", "]");
        assertJSON(w -> w.writeList(1, "i1", Arrays.asList(Arrays.asList(1, 3, 2), Arrays.asList(-4, 1))), "\"i1\": [",
                "  [", "    1,", "    3,", "    2", "  ],", "  [", "    -4,", "    1", "  ]", "]");
    }
}
