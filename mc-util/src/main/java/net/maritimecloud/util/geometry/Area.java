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

import static java.util.Objects.requireNonNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.maritimecloud.util.function.Predicate;

/**
 * A shape has an area
 **/
public abstract class Area implements Element {
    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    final CoordinateSystem cs;

    public Area(CoordinateSystem cs) {
        this.cs = requireNonNull(cs);
    }

    static double nextDouble(Random r, double least, double bound) {
        return r.nextDouble() * (bound - least) + least;
    }

    /**
     * Returns a random position within the area.
     * 
     * @return a random position within the area
     */
    public final Position getRandomPosition() {
        return getRandomPosition(ThreadLocalRandom.current());
    }

    public abstract Position getRandomPosition(Random random);


    public final Predicate<Element> contains() {
        return new Predicate<Element>() {
            public boolean test(Element element) {
                return contains(element);
            }
        };
    }

    /**
     * Returns a bounding box of the area.
     * 
     * @return a bounding box of the area
     */
    public abstract BoundingBox getBoundingBox();

    /**
     * Returns <tt>true</tt> if the specified element is fully contained in the shape, otherwise <tt>false</tt>.
     * 
     * @param element
     *            the element to test
     * @return true if the specified element is fully contained in the shape, otherwise false
     */
    public boolean contains(Element element) {
        throw new UnsupportedOperationException();
    }

    public abstract boolean intersects(Area other);

    @Override
    public final double distanceTo(Element other, CoordinateSystem system) {
        return requireNonNull(system) == CoordinateSystem.CARTESIAN ? rhumbLineDistanceTo(other)
                : geodesicDistanceTo(other);
    }

    @Override
    public double geodesicDistanceTo(Element other) {
        throw new UnsupportedOperationException();
    }

    public final CoordinateSystem getCoordinateSystem() {
        return cs;
    }

    @Override
    public double rhumbLineDistanceTo(Element other) {
        throw new UnsupportedOperationException();
    }

    public Area unionWith(Area other) {
        throw new UnsupportedOperationException();
    }
}
