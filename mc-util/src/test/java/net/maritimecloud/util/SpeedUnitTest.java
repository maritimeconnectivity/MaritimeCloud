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
package net.maritimecloud.util;

import static org.junit.Assert.assertEquals;
import net.maritimecloud.util.units.SpeedUnit;

import org.junit.Test;

/**
 * Tests {@link SpeedUnit}.
 * 
 * @author Kasper Nielsen
 */
public class SpeedUnitTest {

    @Test
    public void kphTo() {
        assertEquals(5.399568034557235, SpeedUnit.KILOMETERS_PER_HOUR.toKnots(10), 0.000000001);
        assertEquals(2.7777777777777777, SpeedUnit.KILOMETERS_PER_HOUR.toMetersPerSecond(10), 0.000000001);
        assertEquals(10, SpeedUnit.KILOMETERS_PER_HOUR.toKilometersPerHour(10), 0.000000001);
        assertEquals(6.2137119223733395, SpeedUnit.KILOMETERS_PER_HOUR.toMilesPerHour(10), 0.000000001);
    }

    @Test
    public void mpsTo() {
        assertEquals(19.438444924406046, SpeedUnit.METERS_PER_SECOND.toKnots(10), 0.000000001);
        assertEquals(10, SpeedUnit.METERS_PER_SECOND.toMetersPerSecond(10), 0.000000001);
        assertEquals(36, SpeedUnit.METERS_PER_SECOND.toKilometersPerHour(10), 0.000000001);
        assertEquals(22.36936292054402, SpeedUnit.METERS_PER_SECOND.toMilesPerHour(10), 0.000000001);
    }

    @Test
    public void mphTo() {
        assertEquals(8.68976241900648, SpeedUnit.MILES_PER_HOUR.toKnots(10), 0.000000001);
        assertEquals(4.4704, SpeedUnit.MILES_PER_HOUR.toMetersPerSecond(10), 0.000000001);
        assertEquals(16.09344, SpeedUnit.MILES_PER_HOUR.toKilometersPerHour(10), 0.000000001);
        assertEquals(10, SpeedUnit.MILES_PER_HOUR.toMilesPerHour(10), 0.000000001);
    }

    @Test
    public void knotsTo() {
        assertEquals(10, SpeedUnit.KNOTS.toKnots(10), 0.000000001);
        assertEquals(5.1444444444444444444, SpeedUnit.KNOTS.toMetersPerSecond(10), 0.000000001);
        assertEquals(18.52, SpeedUnit.KNOTS.toKilometersPerHour(10), 0.000000001);
        assertEquals(11.507794480235425, SpeedUnit.KNOTS.toMilesPerHour(10), 0.000000001);
    }
}
