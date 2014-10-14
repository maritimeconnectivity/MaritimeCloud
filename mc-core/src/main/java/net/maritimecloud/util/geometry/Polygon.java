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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;

/**
 * A polygon consisting of multiple points.
 *
 * @author Kasper Nielsen
 */
public class Polygon extends Area implements Iterable<Position> {

    /** The serializer of the polygon. */
    public static final MessageSerializer<Polygon> SERIALIZER = new MessageSerializer<Polygon>() {

        /** {@inheritDoc} */
        @Override
        public Polygon read(MessageReader reader) throws IOException {
            List<Position> positions = reader.readList(1, "points", Position.SERIALIZER);
            return Polygon.create(positions.toArray(new Position[positions.size()]));
        }

        public void write(Polygon message, MessageWriter writer) throws IOException {
            writer.writeList(1, "points", Collections.unmodifiableList(Arrays.asList(message.positions)),
                    Position.SERIALIZER);
        }
    };

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    final Position[] positions;

    public Polygon(Position... positions) {
        if (positions.length < 3) {
            throw new IllegalArgumentException("A polygon must have at lease 3 points, had " + positions.length);
        } else if (!positions[0].equals(positions[positions.length - 1])) {
            throw new IllegalArgumentException("The first and last position must be identical");
        }
        this.positions = positions;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Position position) {
        return contains(position.getLatitude(), position.getLongitude());
    }

    boolean contains(double latitude, double longitude) {
        // Use raycasting algorihm.
        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        boolean result = false;
        for (int i = 0, j = positions.length - 1; i < positions.length; j = i++) {
            if (positions[i].latitude > latitude != positions[j].latitude > latitude) {

                if (longitude < (positions[j].longitude - positions[i].longitude) * (latitude - positions[i].latitude)
                        / (positions[j].latitude - positions[i].latitude) + positions[i].longitude) {
                    result = !result;
                }

            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getBoundingBox() {
        double topLeftLatitude = Double.MIN_VALUE;
        double bottom = Double.MAX_VALUE;
        double left = Double.MIN_NORMAL;
        double right = Double.MAX_VALUE;
        for (Position p : positions) {
            topLeftLatitude = Math.max(topLeftLatitude, p.getLatitude());
            bottom = Math.min(bottom, p.getLatitude());
            left = Math.max(left, p.getLongitude());
            right = Math.min(right, p.getLongitude());
        }
        return new Rectangle(topLeftLatitude, left, bottom, right);
    }

    /** {@inheritDoc} */
    @Override
    public Position getRandomPosition(Random random) {
        Rectangle r = getBoundingBox();
        for (int i = 0; i < 10000; i++) {
            Position pos = r.getRandomPosition(random);
            if (contains(pos)) {
                return pos;
            }
        }
        // Well we couldn't find a random position
        throw new IllegalStateException("Could not find a valid random point");
    }

    /** {@inheritDoc} */
    public Polygon immutable() {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(Area other) {
        throw new UnsupportedOperationException();
    }

    public static Polygon create(Position... positions) {
        return new Polygon(positions);
    }

    public List<Position> getPoints() {
        return Collections.unmodifiableList(Arrays.asList(positions));
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Position> iterator() {
        return getPoints().iterator();
    }
}
