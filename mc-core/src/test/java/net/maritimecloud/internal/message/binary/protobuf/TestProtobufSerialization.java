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

import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Random;

/**
 * Test the binary protobuf message reader and writer
 */
public class TestProtobufSerialization extends AbstractProtobufTest {

    private static final Random RANDOM = new Random();

    /**
     * Test the Protobuf message reader and writer by generating a load of random
     * messages, writing these to a Protobuf buffer, reading them from the buffer
     * and comparing the resulting message wiht the original
     */
    @Test
    public void testMessageSerialization() throws IOException {

        long t0 = System.currentTimeMillis();
        int cnt = 1000;

        for (int x = 0; x < cnt; x++) {
            Msg1 m = randomMsg(true);
            byte[] data = ProtobufMessageWriter.write(m, Msg1.SERIALIZER);
            Msg1 mm = ProtobufMessageReader.read(data, Msg1.SERIALIZER);

            Assert.assertEquals(m, mm);
        }

        System.out.println("Tested (de-)serialization of " + cnt + " random messages in " +
                (System.currentTimeMillis() - t0) + " ms");
    }

    /**
     * Generate a test message with random field values
     * @param nested whether to add nested messages in list and map fields
     * @return the random message
     */
    private static Msg1 randomMsg(boolean nested) {
        Msg1 m = new Msg1();
        m.s = rndValue(50, rndString(100));
        m.i = rndValue(50, (int)(Integer.MAX_VALUE * Math.random()) + (int)(Integer.MIN_VALUE * Math.random()));
        m.l = rndValue(50, (long)(Long.MAX_VALUE * Math.random()) + (long)(Long.MIN_VALUE * Math.random()));
        m.d = rndValue(50, (Double.MAX_VALUE * Math.random()) + (Double.MIN_VALUE * Math.random()));
        m.t = rndValue(50, Timestamp.now());
        m.bytes = rndValue(50, rndString(10).getBytes());
        m.bd = rndValue(50, BigDecimal.valueOf((Double.MAX_VALUE * Math.random()) + (Double.MIN_VALUE * Math.random())));

        // NB: Positions are loosing precision when they are serialized (bad idea, methinks)
        // Round them to integers in this test.
        Position pt = Position.random();
        m.pt = rndValue(50, PositionTime.create((int)pt.getLatitude(), (int)pt.getLongitude(), System.currentTimeMillis()));

        if (nested && Math.random() < 0.5) {
            int nestedMsgCnt = (int) (Math.random() * 10);
            for (int x = 0; x < nestedMsgCnt; x++) {
                Msg1 nm = randomMsg(false);
                m.list.add(nm);
                m.map.put("Key" + x, nm);
            }
        }

        return m;
    }

    /**
     * Returns the value with a chance of pct percent. Otherwise null is returned
     * @param pct the change of returning the value
     * @param val the value to return
     * @return the value or null
     */
    private static <T> T rndValue(int pct, T val) {
        return  (Math.random() * 100.0 < pct) ? val : null;
    }

    /**
     * Generates a random UTF-8 string of the given length
     * @param length the length of the string
     * @return the string
     */
    private static String rndString(int length) {
        StringBuilder sb = new StringBuilder(length);
        while (sb.length() < length) {
            sb.append((char)RANDOM.nextInt(Character.MAX_VALUE));
        }
        // Make sure it is
        try {
            return new String(sb.toString().getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "Hello mum!";
        }
    }
}
