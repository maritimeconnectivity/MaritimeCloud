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
package net.maritimecloud.util.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PositionTest {

    @Test
    public void testGetLatitudeAsString() {
        // Extremes
        assertEquals("00 00.000N", Position.create(0.0, 0.0).getLatitudeAsString());
        assertEquals("90 00.000N", Position.create(90.0, 0.0).getLatitudeAsString());
        assertEquals("90 00.000S", Position.create(-90.0, 0.0).getLatitudeAsString());

        // North / south
        assertEquals("01 00.000N", Position.create(1.0, 0.0).getLatitudeAsString());
        assertEquals("01 00.000S", Position.create(-1.0, 0.0).getLatitudeAsString());
        assertEquals("56 25.927N", Position.create(56.432111, 0.0).getLatitudeAsString());
        assertEquals("56 25.927S", Position.create(-56.432111, 0.0).getLatitudeAsString());

        // Trailing 0's
        assertEquals("55 00.000N", Position.create(55.0, 0.0).getLatitudeAsString());
        assertEquals("56 07.400N", Position.create(56.123325, 0.0).getLatitudeAsString());
        assertEquals("56 07.450N", Position.create(56.124171, 0.0).getLatitudeAsString());

        // Leading 0's on minutes
        assertEquals("01 00.000N", Position.create(1.0, 0.0).getLatitudeAsString());
        assertEquals("56 07.407N", Position.create(56.123456, 0.0).getLatitudeAsString());

        // Leading 0's on degrees
        assertEquals("01 00.000N", Position.create(1.0, 0.0).getLatitudeAsString());

        // Featured points selected for test
        assertEquals("55 34.046N", Position.create(55.567435, 0.0).getLatitudeAsString());
    }

    @Test
    public void testGetLongitudeAsString() {
        // Extremes
        assertEquals("000 00.000E", Position.create(0.0, 0.0).getLongitudeAsString());
        assertEquals("180 00.000E", Position.create(0.0, 180.0).getLongitudeAsString());
        assertEquals("180 00.000W", Position.create(0.0, -180.0).getLongitudeAsString());

        // East / west
        assertEquals("001 00.000E", Position.create(0.0, 1.0).getLongitudeAsString());
        assertEquals("001 00.000W", Position.create(0.0, -1.0).getLongitudeAsString());
        assertEquals("011 34.046E", Position.create(0.0, 11.56743).getLongitudeAsString());
        assertEquals("011 34.046W", Position.create(0.0, -11.56743).getLongitudeAsString());

        // Trailing 0's
        assertEquals("005 00.000E", Position.create(0.0, 5.0).getLongitudeAsString());
        assertEquals("005 33.600E", Position.create(0.0, 5.56).getLongitudeAsString());
        assertEquals("005 33.650E", Position.create(0.0, 5.56083).getLongitudeAsString());

        // Leading 0's on minutes
        assertEquals("001 00.000E", Position.create(0.0, 1.0).getLongitudeAsString());
        assertEquals("001 06.000E", Position.create(0.0, 1.1).getLongitudeAsString());

        // Leading 0's on degrees
        assertEquals("001 00.000E", Position.create(0.0, 1.0).getLongitudeAsString());
        assertEquals("010 00.000E", Position.create(0.0, 10.0).getLongitudeAsString());

        // Featured points selected for test
        assertEquals("010 56.843E", Position.create(0.0, 10.94738243).getLongitudeAsString());
    }

}
