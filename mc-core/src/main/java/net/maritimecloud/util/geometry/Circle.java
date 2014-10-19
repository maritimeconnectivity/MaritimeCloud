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

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;

/**
 * A circle
 *
 */
public class Circle extends Area {

    public static final MessageSerializer<Circle> SERIALIZER = new MessageSerializer<Circle>() {

        private static final String NAME_CENTER_LATITIUDE = "center-latitude";

        private static final String NAME_CENTER_LONGITUDE = "center-longitude";

        private static final String NAME_RADIUS = "radius";

        /** {@inheritDoc} */
        @Override
        public Circle read(MessageReader reader) throws IOException {
            double lat = reader.readDouble(1, NAME_CENTER_LATITIUDE);
            double lon = reader.readDouble(2, NAME_CENTER_LONGITUDE);
            double radius = reader.readDouble(3, NAME_RADIUS);
            return new Circle(Position.create(lat, lon), radius);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Circle message, MessageWriter w) throws IOException {
            w.writeDouble(1, NAME_CENTER_LATITIUDE, message.center.latitude);
            w.writeDouble(2, NAME_CENTER_LONGITUDE, message.center.longitude);
            w.writeDouble(3, NAME_RADIUS, message.radius);
        }
    };

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The center of the circle. */
    final Position center;

    /** The radius of the circle. */
    final double radius;

    Circle(Position center, double radius) {
        this.center = requireNonNull(center, "center is null");
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive, was " + radius);
        }
        this.radius = radius;
    }

    public boolean contains(Circle c) {
        return center.rhumbLineDistanceTo(c.center) <= radius + c.radius;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Position position) {
        return center.rhumbLineDistanceTo(position) <= radius;
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

    public double geodesicDistanceTo(Position other) {
        return Math.max(0, center.geodesicDistanceTo(other) - radius);
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getBoundingBox() {
        double top = CoordinateSystem.CARTESIAN.pointOnBearing(center, radius, 0).latitude;
        double right = CoordinateSystem.CARTESIAN.pointOnBearing(center, radius, 90).longitude;
        double bottom = CoordinateSystem.CARTESIAN.pointOnBearing(center, radius, 180).latitude;
        double left = CoordinateSystem.CARTESIAN.pointOnBearing(center, radius, 270).longitude;
        Position topLeft = Position.create(top, left);
        Position buttomRight = Position.create(bottom, right);
        return Rectangle.create(topLeft, buttomRight);
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
    public Position getRandomPosition(Random r) {
        Rectangle bb = getBoundingBox();
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
    public int hashCode() {
        return center.hashCode() ^ new Double(radius).hashCode();
    }

    /** {@inheritDoc} */
    public Circle immutable() {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(Area other) {
        if (other instanceof Circle) {
            return intersects((Circle) other);
        } else if (other instanceof Rectangle) {
            return intersects((Rectangle) other);
        } else {
            throw new UnsupportedOperationException("Only circles and BoundingBoxes supported");
        }
    }

    public boolean intersects(Circle other) {
        double centerDistance = CoordinateSystem.CARTESIAN.distanceBetween(center, other.center);
        return radius + other.radius >= centerDistance;
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

    public boolean intersects(Rectangle other) {
        return other.intersects(this);
    }

    public double rhumbLineDistanceTo(Position position) {
        return Math.max(0, center.rhumbLineDistanceTo(position) - radius);
    }

    public String toString() {
        return "Circle: center = " + center + ", radius = " + radius;
    }

    /**
     * Returns a new circle with the same radius as this circle but with the new position as the center
     *
     * @param center
     *            the new center of the circle
     * @return a new circle
     */
    public Circle withCenter(Position center) {
        return new Circle(center, radius);
    }

    /**
     * Returns a new circle with the same center as this circle but with the new radius.
     *
     * @param radius
     *            the new radius of the circle
     * @return a new circle
     */
    public Circle withRadius(double radius) {
        return new Circle(center, radius);
    }

    public static Circle create(double latitude, double longitude, double radius) {
        return new Circle(Position.create(latitude, longitude), radius);
    }

    /**
     * Creates a new circle with the specified center and radius.
     *
     * @param center
     *            the center of the circle
     * @param radius
     *            the radius in meters of the circle
     * @return the new circle
     * @throws NullPointerException
     *             if the specified center is null
     * @throws IllegalArgumentException
     *             if the specified is not a positive number
     */
    public static Circle create(Position center, double radius) {
        return new Circle(center, radius);
    }

    /**
     * Creates a message of this type from a JSON. Throwing a runtime exception if the format of the message does not
     * match.
     *
     * @param string
     *            the JSON string to parse
     * @return the parsed area
     */
    public static Circle fromJSON(CharSequence string) {
        return MessageSerializer.readFromJSON(SERIALIZER, string);
    }

    /**
     * Returns a random valid circle.
     *
     * @return the random circle
     */
    public static Circle random() {
        return random(ThreadLocalRandom.current());
    }

    /**
     * Returns a random valid circle.
     *
     * @param rnd
     *            the source of randomness
     * @return the random circle
     */
    public static Circle random(Random rnd) {
        return new Circle(Position.random(), (1 - rnd.nextDouble()) * 10000);
    }
}
