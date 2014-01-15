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

/**
 * A circle
 * 
 */
public class Circle extends Area {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The center of the circle. */
    private final Position center;

    /** The radius of the circle. */
    private final double radius;

    public Circle(double latitude, double longitude, double radius, CoordinateSystem cs) {
        this(Position.create(latitude, longitude), radius, cs);
    }

    public Circle(Position center, double radius, CoordinateSystem cs) {
        super(cs);
        this.center = requireNonNull(center, "center is null");

        // circles with no radius??
        // check radious nan, we could use the radius of the earth
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive, was " + radius);
        }
        this.radius = radius;
    }

    @Override
    public boolean contains(Element element) {
        if (element instanceof Position) {
            return center.distanceTo(element, cs) <= radius;
        } else if (element instanceof Circle) {
            Circle c = (Circle) element;
            if (c.cs != cs) {
                throw new IllegalArgumentException("Cannot compare circles in different coordinate systems");
            }
            return center.distanceTo(c.center, cs) <= radius + c.radius;
        }
        return super.contains(element);
    }

    public boolean equals(Circle other) {
        return other == this || other != null && center.equals(other.center) && radius == other.radius;
    }

    /**
     * Equals method
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Circle && equals((Circle) other);
    }

    @Override
    public double geodesicDistanceTo(Element other) {
        if (other instanceof Position) {
            return Math.max(0, center.geodesicDistanceTo(other) - radius);
        }
        return super.geodesicDistanceTo(other);
    }

    /**
     * Return the center of the circle.
     * 
     * @return the center of the circle
     */
    public Position getCenter() {
        return center;
    }

    /**
     * Returns the radius of the circle.
     * 
     * @return the radius of the circle
     */
    public double getRadius() {
        return radius;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return center.hashCode() ^ new Double(radius).hashCode();
    }

    @Override
    public double rhumbLineDistanceTo(Element other) {
        if (other instanceof Position) {
            return Math.max(0, center.rhumbLineDistanceTo(other) - radius);
        }
        return super.rhumbLineDistanceTo(other);
    }

    /**
     * Returns a new circle with the same radius as this circle but with the new position as the center
     * 
     * @param center
     *            the new center of the circle
     * @return a new circle
     */
    public Circle withCenter(Position center) {
        return new Circle(center, radius, cs);
    }

    /**
     * Returns a new circle with the same center as this circle but with the new radius.
     * 
     * @param radius
     *            the new radius of the circle
     * @return a new circle
     */
    public Circle withRadius(double radius) {
        return new Circle(center, radius, cs);
    }

    /** {@inheritDoc} */
    @Override
    public Position getRandomPosition(Random r) {
        BoundingBox bb = getBoundingBox();
        for (int i = 0; i < 10000; i++) {
            Position p = bb.getRandomPosition(r);
            if (contains(p)) {
                return p;
            }
        }
        throw new RuntimeException("Inifinite loop");
    }

    /** {@inheritDoc} */
    @Override
    public BoundingBox getBoundingBox() {
        double right = cs.pointOnBearing(center, radius, 0).latitude;
        double left = cs.pointOnBearing(center, radius, 180).latitude;
        double top = cs.pointOnBearing(center, radius, 90).longitude;
        double buttom = cs.pointOnBearing(center, radius, 270).longitude;
        Position topLeft = Position.create(left, top);
        Position buttomRight = Position.create(right, buttom);
        return BoundingBox.create(topLeft, buttomRight, cs);
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(Area other) {
        if (other instanceof Circle) {
            return intersects((Circle) other);
        } else if (other instanceof BoundingBox) {
            return intersects((BoundingBox) other);
        } else {
            throw new UnsupportedOperationException("Only circles and BoundingBoxes supported");
        }
    }

    public boolean intersects(BoundingBox other) {
        return other.intersects(this);
    }

    public boolean intersects(Line line) {
        return intersects(line.getStart(), line.getEnd());
    }

    boolean intersects(Position p1, Position p2) {
        double baX = p2.getLongitude() - p1.getLongitude();
        double baY = p2.getLatitude() - p1.getLatitude();
        double caX = center.getLongitude() - p2.getLongitude();
        double caY = center.getLatitude() - p2.getLatitude();

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        return disc >= 0;
    }

    public boolean intersects(Circle other) {
        double centerDistance = getCoordinateSystem().distanceBetween(center, other.center);
        return radius + other.radius >= centerDistance;
    }
}
