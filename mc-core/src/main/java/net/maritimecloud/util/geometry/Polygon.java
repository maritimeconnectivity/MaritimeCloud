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
import java.util.List;
import java.util.Random;

import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializer;
import net.maritimecloud.core.message.MessageWriter;

public class Polygon extends Area {
    public static final MessageSerializer<Polygon> SERIALIZER = new MessageSerializer<Polygon>() {

        /** {@inheritDoc} */
        @Override
        public Polygon read(MessageReader reader) throws IOException {
            return readPolygonFrom(reader);
        }
    };

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private final Position[] positions;

    public Polygon(Position... positions) {
        this.positions = positions;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Position position) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public Polygon immutable() {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getBoundingBox() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public Position getRandomPosition(Random random) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(Area other) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeList(1, "points", Arrays.asList(positions), Position.SERIALIZER);
    }

    public static Polygon create(Position... positions) {
        return new Polygon(positions);
    }

    static Polygon readPolygonFrom(MessageReader r) throws IOException {
        List<Position> positions = r.readList(1, "points", Position.SERIALIZER);
        return Polygon.create(positions.toArray(new Position[positions.size()]));
    }
}
