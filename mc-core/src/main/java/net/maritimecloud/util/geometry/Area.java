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
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializable;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;

/**
 * A shape has an area
 **/
public abstract class Area implements Message, Serializable {

    /** A parser of areas. */
    public static final MessageParser<Area> PARSER = new MessageParser<Area>() {
        /** {@inheritDoc} */
        @Override
        public Area parse(MessageReader reader) throws IOException {
            return readFrom(reader);
        }
    };

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    final MessageSerializable areaWriter() {
        return new Writer();
    }

    // public final Predicate<Element> contains() {
    // return new Predicate<Element>() {
    // public boolean test(Element element) {
    // return contains(element);
    // }
    // };
    // }

    /**
     * Returns <tt>true</tt> if the specified element is fully contained in the shape, otherwise <tt>false</tt>.
     *
     * @param element
     *            the element to test
     * @return true if the specified element is fully contained in the shape, otherwise false
     */
    public boolean contains(Area area) {
        throw new UnsupportedOperationException();
    }

    public abstract boolean contains(Position position);

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

    /**
     * Returns a bounding box of the area.
     *
     * @return a bounding box of the area
     */
    public abstract BoundingBox getBoundingBox();

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

    /** {@inheritDoc} */
    public Area immutable() {
        return this;
    }

    public abstract boolean intersects(Area other);

    // @Override
    // public double rhumbLineDistanceTo(Position other) {
    // throw new UnsupportedOperationException();
    // }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
    }

    public Area unionWith(Area other) {
        return new AreaUnion(this, other);
    }

    public static Area createUnion(Area... areas) {
        Area[] a = areas.clone();
        return new AreaUnion(a);
    }

    static double nextDouble(Random r, double least, double bound) {
        return r.nextDouble() * (bound - least) + least;
    }

    public static Area readFrom(MessageReader r) throws IOException {
        // Circle = 1;
        // Box = 2;
        // Polygon = 3;
        // Union = 4;
        if (r.isNext(1, "areas")) {
            List<AreaUnion> readList = r.readList(1, "areas", AreaUnion.PARSER);
            return new AreaUnion(readList.toArray(new AreaUnion[0]));
        } else if (r.isNext(2, "circle")) {
            return r.readMessage(1, "circle", Circle.PARSER);
        } else if (r.isNext(3, "boundingbox")) {
            return r.readMessage(2, "boundingbox", BoundingBox.PARSER);
        } else if (r.isNext(4, "polygon")) {
            return r.readMessage(3, "polygon", Polygon.PARSER);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    class Writer implements MessageSerializable {

        /** {@inheritDoc} */
        @Override
        public void writeTo(MessageWriter w) throws IOException {
            Area a = Area.this;
            if (a instanceof AreaUnion) {
                w.writeMessage(1, "areas", a);
            } else if (a instanceof Circle) {
                w.writeMessage(2, "circle", a);
            } else if (a instanceof BoundingBox) {
                w.writeMessage(3, "boundingbox", a);
            } else if (a instanceof Polygon) {
                w.writeMessage(4, "polygon", a);
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }
}
