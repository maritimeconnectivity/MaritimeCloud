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

import java.io.IOException;
import java.util.Arrays;

import net.maritimecloud.core.message.ValueParser;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class TestReader extends AbstractJSONTest {

    @Test
    public void testEnum() throws IOException {
        assertEquals(100, readerOf("\"i1\": 100").readInt32(1, "i1", -1).intValue());
        assertEquals(-100L, readerOf("\"1f1\": -100").readInt64(1, "1f1", -1L).longValue());
        assertEquals(1.2345f, readerOf("\"i1\": 1.2345").readFloat(1, "i1", 0f).floatValue(), 0);
        assertEquals(-10.12123d, readerOf("\"1f1\": -10.12123").readDouble(1, "1f1", 0d).doubleValue(), 0);
        assertEquals(true, readerOf("\"i1\": true").readBool(1, "i1", null).booleanValue());
        assertEquals(false, readerOf("\"i1\": false").readBool(1, "i1", null).booleanValue());
        assertEquals("hello", readerOf("\"1f1\": \"hello\"").readString(1, "1f1", null));
    }

    @Test
    public void readListAndSet() throws IOException {
        assertEquals(Arrays.asList(), readerOf("\"i1\": []").readList(1, "i1", ValueParser.INT32));
        assertEquals(Arrays.asList(1, 3, 2, -4, 1), readerOf("\"i1\": [", "  1,", "  3,", "  2,", "  -4,", "  1", "]")
                .readList(1, "i1", ValueParser.INT32));
        assertEquals(
                Arrays.asList("1", "3", "2", "-4", "1"),
                readerOf("\"i1\": [", "  \"1\",", "  \"3\",", "  \"2\",", "  \"-4\",", "  \"1\"", "]").readList(1,
                        "i1", ValueParser.STRING));
    }
}
