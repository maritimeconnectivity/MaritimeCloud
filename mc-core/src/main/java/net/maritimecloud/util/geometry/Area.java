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

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;

/**
 * A shape has an area
 **/
public abstract class Area implements Message, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    static final String UNION = "union";

    static final int UNION_TAG = 1;

    static final String CIRCLE = "circle";

    static final int CIRCLE_TAG = 2;

    static final String RECTANGLE = "rectangle";

    static final int RECTANGLE_TAG = 3;

    static final String POLYGON = "polygon";

    static final int POLYGON_TAG = 4;

    static final String ELIPSE = "elipse";

    static final int ELIPSE_TAG = 5;


    /** A parser of areas. */
    public static final MessageSerializer<Area> SERIALIZER = new MessageSerializer<Area>() {

        /** {@inheritDoc} */
        @Override
        public Area read(MessageReader r) throws IOException {
            if (r.isNext(UNION_TAG, UNION)) {
                return new AreaUnion(r.readList(UNION_TAG, UNION, AreaUnion.SERIALIZER));
            } else if (r.isNext(CIRCLE_TAG, CIRCLE)) {
                return r.readMessage(CIRCLE_TAG, CIRCLE, Circle.SERIALIZER);
            } else if (r.isNext(RECTANGLE_TAG, RECTANGLE)) {
                return r.readMessage(RECTANGLE_TAG, RECTANGLE, Rectangle.SERIALIZER);
            } else if (r.isNext(POLYGON_TAG, POLYGON)) {
                return r.readMessage(POLYGON_TAG, POLYGON, Polygon.SERIALIZER);
            } else if (r.isNext(ELIPSE_TAG, ELIPSE)) {
                return r.readMessage(POLYGON_TAG, POLYGON, Polygon.SERIALIZER);
            } else {
                throw new UnsupportedOperationException();
            }
        }

        /** {@inheritDoc} */
        @Override
        public void write(Area a, MessageWriter w) throws IOException {
            if (a != null) {
                if (a instanceof AreaUnion) {
                    w.writeMessage(UNION_TAG, UNION, (AreaUnion) a, AreaUnion.SERIALIZER);
                } else if (a instanceof Circle) {
                    w.writeMessage(CIRCLE_TAG, CIRCLE, (Circle) a, Circle.SERIALIZER);
                } else if (a instanceof Rectangle) {
                    w.writeMessage(RECTANGLE_TAG, RECTANGLE, (Rectangle) a, Rectangle.SERIALIZER);
                } else if (a instanceof Polygon) {
                    w.writeMessage(POLYGON_TAG, POLYGON, (Polygon) a, Polygon.SERIALIZER);
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }
    };


    /**
     * Returns <tt>true</tt> if the specified element is fully contained in the shape, otherwise <tt>false</tt>.
     *
     * @param area
     *            the area to test
     * @return true if the specified element is fully contained in the shape, otherwise false
     */
    public boolean contains(Area area) {
        throw new UnsupportedOperationException();
    }

    public abstract boolean contains(Position position);

    /**
     * Returns a bounding box of the area.
     *
     * @return a bounding box of the area
     */
    public abstract Rectangle getBoundingBox();

    /**
     * Returns a random position within the area.
     *
     * @return a random position within the area
     */
    public final Position getRandomPosition() {
        return getRandomPosition(ThreadLocalRandom.current());
    }

    /**
     * Returns a random position within the area using a specified random source.
     *
     * @param random
     *            the random source
     * @return a random position within the area
     */
    public abstract Position getRandomPosition(Random random);

    public abstract boolean intersects(Area other);

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Returns a union of this area with the specified area.
     *
     * @param other
     *            the area to join with this area.
     * @return the joined area
     */
    public final Area unionWith(Area other) {
        return new AreaUnion(this, other);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not
     * match a valid area.
     *
     * @param string
     *            the JSON string to parse
     * @return the parsed area
     */
    public static Area fromJSON(CharSequence string) {
        return MessageSerializer.readFromJSON(SERIALIZER, string);
    }

    static double nextDouble(Random r, double least, double bound) {
        return r.nextDouble() * (bound - least) + least;
    }

    public static Area unionOf(Area... areas) {
        Area[] a = areas.clone();
        return new AreaUnion(a);
    }
}

// @Override
// public double rhumbLineDistanceTo(Position other) {
// throw new UnsupportedOperationException();
// }
// /** {@inheritDoc} */
// @Override
// public final double distanceTo(Element other, CoordinateSystem system) {
// return requireNonNull(system) == CoordinateSystem.CARTESIAN ? rhumbLineDistanceTo(other)
// : geodesicDistanceTo(other);
// }

// /** {@inheritDoc} */
// @Override
// public double geodesicDistanceTo(Element other) {
// throw new UnsupportedOperationException();
// }
