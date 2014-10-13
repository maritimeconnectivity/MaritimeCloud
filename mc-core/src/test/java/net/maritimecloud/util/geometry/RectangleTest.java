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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class RectangleTest extends AbstractAreaTest {

    static Rectangle R = Rectangle.create(P1, P9);

    static Rectangle R1 = Rectangle.create(P1, P5);

    static Rectangle R2 = Rectangle.create(P2, P6);

    static Rectangle R3 = Rectangle.create(P4, P8);

    static Rectangle R4 = Rectangle.create(P5, P9);

    static Collection<Rectangle> RALL = Arrays.asList(R, R1, R2, R3, R4);

    @Test
    public void equals() {
        assertNotEquals(R, "SDF");
        for (Rectangle r1 : RALL) {
            for (Rectangle r2 : RALL) {
                if (r1 == r2) {
                    assertEquals(r1, r2);
                    assertEquals(r1, Rectangle.create(r2.getTopLeft(), r2.getBottomRight()));
                } else {
                    assertNotEquals(r1, r2);
                }
            }
        }
    }

    @Test
    public void intercectCircle() {
        assertTrue(R.intersects(Circle.create(P5, 1)));
        assertTrue(R1.intersects(Circle.create(P5, 1)));
        // TODO more tests.
    }

    public void immutable() {
        assertSame(R, R.immutable());
    }

    @Test
    public void randomPosition() {
        for (int i = 0; i < 1000; i++) {
            assertTrue(R.contains(R.getRandomPosition()));
        }

        // int count = 0;
        // for (int i = 0; i < 10000; i++) {
        // if (R1.contains(R.getRandomPosition())) {
        // count++;
        // }
        // }
        // System.out.println(count); // should be around ~ 2500
    }

    @Test
    public void contains() {
        for (Position p : PALL) {
            assertTrue(R.contains(p));
        }
        assertFalse(R1.contains(P3));
        assertFalse(R1.contains(P6));
        assertFalse(R1.contains(P7));

        assertFalse(R2.contains(P1));
        assertFalse(R2.contains(P4));
        assertFalse(R2.contains(P9));

        assertFalse(R3.contains(P1));
        assertFalse(R3.contains(P2));
        assertFalse(R3.contains(P9));

        assertFalse(R4.contains(P2));
        assertFalse(R4.contains(P4));
        assertFalse(R4.contains(P7));
    }

    @Test
    public void getters() {
        assertEquals(P1, R.getTopLeft());
        assertEquals(P3, R.getTopRight());
        assertEquals(P7, R.getBottomLeft());
        assertEquals(P9, R.getBottomRight());
        assertEquals(P1.getLatitude(), R.getTopLeftLatitude(), 0);
        assertEquals(P1.getLongitude(), R.getTopLeftLongitude(), 0);
        assertEquals(P9.getLatitude(), R.getBottomRightLatitude(), 0);
        assertEquals(P9.getLongitude(), R.getBottomRightLongitude(), 0);
    }
}
