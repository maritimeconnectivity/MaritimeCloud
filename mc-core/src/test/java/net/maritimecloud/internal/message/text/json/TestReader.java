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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class TestReader extends AbstractJSONTest {

    @Test
    public void primitives() throws IOException {
        assertEquals(100, readerOf("\"f\": 100").readInt(1, "f", null).intValue());
        assertEquals(-100L, readerOf("\"f\": -100").readInt64(1, "f", null).longValue());
        assertEquals(new BigInteger(BIG_INT), readerOf("\"f\": " + BIG_INT).readVarInt(1, "f", null));

        assertEquals(1.2345f, readerOf("\"f\": 1.2345").readFloat(1, "f", null).floatValue(), 0);
        assertEquals(-10.12123d, readerOf("\"f\": -10.12123").readDouble(1, "f", null).doubleValue(), 0);
        assertEquals(new BigDecimal(BIG_DECIMAL), readerOf("\"f\": " + BIG_DECIMAL).readDecimal(1, "f", null));

        assertEquals(true, readerOf("\"i1\": true").readBoolean(1, "i1", null).booleanValue());
        assertEquals(false, readerOf("\"i1\": false").readBoolean(1, "i1", null).booleanValue());
        assertEquals(Binary.EMPTY, readerOf("\"f\": \"\"").readBinary(1, "f", null));
        assertEquals(Binary.copyFromUtf8("er"), readerOf("\"f\": \"ZXI=\"").readBinary(1, "f", null));
        assertEquals(Timestamp.create(32123), readerOf("\"f\": 32123").readTimestamp(1, "f", null));
        assertEquals("hello", readerOf("\"f\": \"hello\"").readText(1, "f", null));
        assertEquals("\\/\"", readerOf("\"f\": \"\\\\\\/\\\"\"").readText(1, "f", null));


        assertEquals(Position.create(10, -10), readerOf("\"f\": {\"latitude\": 10, \"longitude\": -10}  ")
                .readPosition(1, "f", null));

        assertEquals(
                PositionTime.create(10, -10, 12345),
                readerOf("\"f\": {\"latitude\": 10, \"longitude\": -10, \"time\": 12345}  ").readPositionTime(1, "f",
                        null));
    }

    @Test
    public void readListAndSet() throws IOException {
        assertEquals(Arrays.asList(), readerOf("\"i1\": []").readList(1, "i1", ValueSerializer.INT));
        assertEquals(Arrays.asList(1, 3, 2, -4, 1), readerOf("\"i1\": [", "  1,", "  3,", "  2,", "  -4,", "  1", "]")
                .readList(1, "i1", ValueSerializer.INT));
        assertEquals(
                Arrays.asList("1", "3", "2", "-4", "1"),
                readerOf("\"i1\": [", "  \"1\",", "  \"3\",", "  \"2\",", "  \"-4\",", "  \"1\"", "]").readList(1,
                        "i1", ValueSerializer.TEXT));
    }
}
