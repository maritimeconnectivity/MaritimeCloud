/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.maritimecloud.util.geometry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
