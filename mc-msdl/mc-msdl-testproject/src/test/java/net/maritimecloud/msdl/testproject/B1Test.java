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
package net.maritimecloud.msdl.testproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.maritimecloud.util.Binary;

import org.junit.Test;

import testproject.B1;

/**
 *
 * @author Kasper Nielsen
 */
public class B1Test {

    @Test
    public void testIt() {
        B1 b = new B1();

        b.setF1(123);
        assertEquals(123, b.getF1().intValue());

        b.setF2(1235L);
        assertEquals(1235L, b.getF2().longValue());

        b.setF3(new BigInteger("1234567898765432123456789"));
        assertEquals(new BigInteger("1234567898765432123456789"), b.getF3());

        b.setF4(-123.123f);
        assertEquals(-123.123f, b.getF4().floatValue(), 0);

        b.setF5(123.1233d);
        assertEquals(123.1233d, b.getF5().doubleValue(), 0);

        b.setF6(new BigDecimal("1234567898765432123456789.1234"));
        assertEquals(new BigDecimal("1234567898765432123456789.1234"), b.getF6());

        b.setF7(true);
        assertTrue(b.getF7());

        b.setF8(Binary.copyFromUtf8("abc"));
        assertEquals(Binary.copyFromUtf8("abc"), b.getF8());

        b.setF9("abc");
        assertEquals("abc", Binary.copyFromUtf8("abc"), b.getF8());


        B1 hw2 = B1.fromJSON(b.toJSON());

        assertEquals(b.getF1(), hw2.getF1());
        assertEquals(b.getF2(), hw2.getF2());
        assertEquals(b.getF3(), hw2.getF3());
        assertEquals(b.getF4(), hw2.getF4());
        assertEquals(b.getF5(), hw2.getF5());
        assertEquals(b.getF6(), hw2.getF6());
        assertEquals(b.getF7(), hw2.getF7());
    }
}
