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

public class PositionTimeTest {

    @Test
    public void testLinearInterpolation() {
        assertEquals(1, PositionTime.linearInterpolation(1, 1, 3, 3, 1), 1e-16);
        assertEquals(3, PositionTime.linearInterpolation(1, 1, 3, 3, 3), 1e-16);

        assertEquals(2, PositionTime.linearInterpolation(1, 1, 3, 3, 2), 1e-16);
        assertEquals(10, PositionTime.linearInterpolation(1, 1, 3, 3, 10), 1e-16);

        assertEquals(2.5, PositionTime.linearInterpolation(0, 0, 5, 10, 5), 1e-16);
        assertEquals(4.5, PositionTime.linearInterpolation(0, 0, 5, 10, 9), 1e-16);
    }

    @Test
    public void testCreateExtrapolated() {
        long t0 = 1000L;
        PositionTime pt1 = PositionTime.create(56.0, 12.0, t0);

        PositionTime extrapolated = pt1.extrapolatePosition(90, 1, t0 + 60000L);
        assertEquals(61000L, extrapolated.getTime());
        assertEquals(56.000000, extrapolated.getLatitude(), 1e-6);
        assertEquals(12.000497, extrapolated.getLongitude(), 1e-6);

        extrapolated = pt1.extrapolatePosition(90, 1, t0 + 2 * 60000L);
        assertEquals(121000L, extrapolated.getTime());
        assertEquals(56.000000, extrapolated.getLatitude(), 1e-6);
        assertEquals(12.000994, extrapolated.getLongitude(), 1e-6);

        extrapolated = pt1.extrapolatePosition(180, 1, t0 + 60000L);
        assertEquals(61000L, extrapolated.getTime());
        assertEquals(55.999721, extrapolated.getLatitude(), 1e-6);
        assertEquals(12.000000, extrapolated.getLongitude(), 1e-6);

        extrapolated = pt1.extrapolatePosition(180, 10, t0 + 60000L);
        assertEquals(61000L, extrapolated.getTime());
        assertEquals(55.997218, extrapolated.getLatitude(), 1e-6);
        assertEquals(12.000000, extrapolated.getLongitude(), 1e-6);
    }

    @Test
    public void testCreateInterpolated() {
        PositionTime pt1 = PositionTime.create(56.0, 12.0, 1000L);
        PositionTime pt2 = PositionTime.create(57.0, 13.0, 2000L);

        PositionTime interpolated = pt1.interpolatedPosition(pt2, 1500L);
        assertEquals(1500L, interpolated.getTime());
        assertEquals(56.5, interpolated.getLatitude(), 1e-16);
        assertEquals(12.5, interpolated.getLongitude(), 1e-16);

        interpolated = pt1.interpolatedPosition(pt2, 1000L);
        assertEquals(1000L, interpolated.getTime());
        assertEquals(56.0, interpolated.getLatitude(), 1e-16);
        assertEquals(12.0, interpolated.getLongitude(), 1e-16);

        interpolated = pt1.interpolatedPosition(pt2, 2000L);
        assertEquals(2000L, interpolated.getTime());
        assertEquals(57.0, interpolated.getLatitude(), 1e-16);
        assertEquals(13.0, interpolated.getLongitude(), 1e-16);
    }

}
