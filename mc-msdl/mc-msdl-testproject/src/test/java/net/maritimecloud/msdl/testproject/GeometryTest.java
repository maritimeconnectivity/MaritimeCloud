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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.Polygon;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.Rectangle;

import org.junit.Test;

import testproject.Geometry1;

/**
 *
 * @author Kasper Nielsen
 */
public class GeometryTest {

    @Test
    public void circleTest() {
        Geometry1 g = new Geometry1();
        Circle c = Circle.create(1, -1, 10);
        assertSame(g, g.setC1(c));
        assertSame(c, g.getC1());

        Geometry1 c2 = Geometry1.fromJSON(g.toJSON());

        assertNotSame(c, c2);
        assertEquals(g, c2);
        assertEquals(g.toJSON(), Geometry1.fromJSON(g.toJSON()).toJSON());

        assertEquals(1, c2.getC1().getCenter().getLatitude(), 0);
        assertEquals(-1, c2.getC1().getCenter().getLongitude(), 0);
        assertEquals(10, c2.getC1().getRadius(), 0);
    }

    @Test
    public void boundingBoxTest() {
        Geometry1 g = new Geometry1();
        Rectangle b = Rectangle.create(Position.create(1, 1), Position.create(-1, -1));
        assertSame(g, g.setB1(b));
        assertSame(b, g.getB1());

        Geometry1 g2 = Geometry1.fromJSON(g.toJSON());
        assertNotSame(g, g2);
        assertEquals(g.toJSON(), Geometry1.fromJSON(g.toJSON()).toJSON());
        //
        // assertEquals(1, c2.getC1().getCenter().getLatitude(), 0);
        // assertEquals(-1, c2.getC1().getCenter().getLongitude(), 0);
        // assertEquals(10, c2.getC1().getRadius(), 0);
    }

    @Test
    public void polygonTest() {
        Geometry1 g = new Geometry1();
        Polygon p = Polygon.create(Position.create(1, 1), Position.create(2, 2), Position.create(3, 3));
        assertSame(g, g.setP1(p));
        assertSame(p, g.getP1());

        Geometry1 g2 = Geometry1.fromJSON(g.toJSON());
        assertNotSame(g, g2);
        assertEquals(g.toJSON(), Geometry1.fromJSON(g.toJSON()).toJSON());
        //
        // assertEquals(1, c2.getC1().getCenter().getLatitude(), 0);
        // assertEquals(-1, c2.getC1().getCenter().getLongitude(), 0);
        // assertEquals(10, c2.getC1().getRadius(), 0);
    }

    @Test
    public void areaTest() {
        // Geometry1 g = new Geometry1();

    }
}
