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
import java.util.Random;

import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializer;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;

public final class Rectangle extends Area {

    /** A bounding box encompassing all coordinates. */
    public static final Rectangle ALL = create(-90, 90, -180, 180);


    public static final MessageSerializer<Rectangle> SERIALIZER = new MessageSerializer<Rectangle>() {

        /** {@inheritDoc} */
        @Override
        public Rectangle read(MessageReader reader) throws IOException {
            return readFrom(reader);
        }
    };

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private final double maxLatitude;

    private final double maxLongitude;

    private final double minLatitude;

    private final double minLongitude;

    private Rectangle(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
        this.minLatitude = Position.verifyLatitude(minLatitude);
        this.maxLatitude = Position.verifyLatitude(maxLatitude);
        this.minLongitude = Position.verifyLongitude(minLongitude);
        this.maxLongitude = Position.verifyLongitude(maxLongitude);
    }

    /** {@inheritDoc} */
    public Rectangle immutable() {
        return this;
    }

    // @Override
    // public boolean contains(Element element) {
    // if (element instanceof Position) {
    // return contains((Position) element);
    // } else {
    // return super.contains(element);
    // }
    // }
    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not
     * match
     */
    public static Rectangle fromJSON(CharSequence c) {
        return MessageSerializers.readFromJSON(SERIALIZER, c);
    }

    public boolean contains(Position point) {
        return point.getLatitude() >= minLatitude && point.getLongitude() >= minLongitude
                && point.getLatitude() <= maxLatitude && point.getLongitude() <= maxLongitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Rectangle) {
            Rectangle that = (Rectangle) obj;
            return minLatitude == that.minLatitude && minLongitude == that.minLongitude
                    && maxLatitude == that.maxLatitude && maxLongitude == that.maxLongitude;
        }
        return false;
    }

    public double getArea(/* DistanceUnit unit */) {
        // CoordinateSystem.CARTESIAN.distanceBetween(minLatitude, minLongitude, maxLatitude, maxLongitude)
        final Position a = new Position(maxLatitude, minLongitude);
        final Position b = new Position(maxLatitude, maxLongitude);
        final Position c = new Position(minLatitude, minLongitude);
        final double ab = a.rhumbLineDistanceTo(b); // meters
        final double ac = a.rhumbLineDistanceTo(c); // meters
        return ab * ac;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getBoundingBox() {
        return this;
    }

    public Position getCenterPoint() {
        return Position.create((minLatitude + maxLatitude) / 2, (minLongitude + maxLongitude) / 2);
    }

    public double getLatitudeSize() {
        return maxLatitude - minLatitude;
    }

    public double getLongitudeSize() {
        return maxLongitude - minLongitude;
    }

    public Position getLowerRight() {
        return Position.create(minLatitude, maxLongitude);
    }

    public double getMaxLat() {
        return maxLatitude;
    }

    public double getMaxLon() {
        return maxLongitude;
    }

    public double getMinLat() {
        return minLatitude;
    }

    public double getMinLon() {
        return minLongitude;
    }

    /**
     * Returns a random position within the box.
     *
     * @return a random position within the box
     */
    @Deprecated
    public Position getRandom() {
        return getRandomPosition();
    }

    /** {@inheritDoc} */
    @Override
    public Position getRandomPosition(Random r) {
        return Position.create(nextDouble(r, minLatitude, maxLatitude), nextDouble(r, minLongitude, maxLongitude));
    }

    public Position getUpperLeft() {
        return Position.create(maxLatitude, minLongitude);
    }


    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + hashCode(minLatitude);
        result = 37 * result + hashCode(maxLatitude);
        result = 37 * result + hashCode(minLongitude);
        result = 37 * result + hashCode(maxLongitude);
        return result;
    }


    /**
     * Returns a new bounding box that includes the specified bounding box.
     *
     * @param other
     *            the bounding box to include
     * @return a bounding box
     */
    public Rectangle include(Rectangle other) {
        double minLon = this.minLongitude;
        double maxLon = this.maxLongitude;
        double minLat = this.minLatitude;
        double maxLat = this.maxLatitude;
        boolean changed = false;
        if (other.minLongitude < minLon) {
            minLon = other.minLongitude;
            changed = true;
        }
        if (other.maxLongitude > maxLon) {
            maxLon = other.maxLongitude;
            changed = true;
        }
        if (other.minLatitude < minLat) {
            minLat = other.minLatitude;
            changed = true;
        }
        if (other.maxLatitude > maxLat) {
            maxLat = other.maxLatitude;
            changed = true;
        }
        return changed ? new Rectangle(minLat, maxLat, minLon, maxLon) : this;
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

    public boolean intersects(Rectangle other) {
        // If the bounding box of this and other is shorter than the sum of the heights AND skinnier than the sum of the
        // widths, they must intersect
        Rectangle common = include(other);
        return common.getLatitudeSize() < getLatitudeSize() + other.getLatitudeSize()
                && common.getLongitudeSize() < getLongitudeSize() + other.getLongitudeSize();
    }

    public boolean intersects(Circle other) {
        // Either the circle's centre lies inside the rectangle, or
        // One of the edges of the rectangle intersects the circle.
        // See more here http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
        if (contains(other.getCenter())) {
            return true;
        }
        Position topLeft = Position.create(minLatitude, minLongitude);
        Position topRight = Position.create(minLatitude, maxLongitude);
        Position buttomLeft = Position.create(maxLatitude, minLongitude);
        Position buttomRight = Position.create(maxLatitude, maxLongitude);
        return other.intersects(topLeft, topRight) || other.intersects(topRight, buttomLeft)
                || other.intersects(topLeft, buttomLeft) || other.intersects(buttomLeft, buttomRight);
    }

    @Override
    public String toString() {
        return getUpperLeft() + " -> " + getLowerRight();
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeDouble(1, "topLeftLatitude", getMinLat());
        w.writeDouble(2, "topLeftLongitude", getMinLon());
        w.writeDouble(3, "buttomRightLatitude", getMaxLat());
        w.writeDouble(4, "buttomRightLongiture", getMaxLon());
    }

    static Rectangle create(double y1, double y2, double x1, double x2) {
        return new Rectangle(Math.min(y1, y2), Math.max(y1, y2), Math.min(x1, x2), Math.max(x1, x2));
    }

    public static Rectangle create(Position topLeft, Position buttomRight) {
        return create(topLeft.getLatitude(), buttomRight.getLatitude(), topLeft.getLongitude(),
                buttomRight.getLongitude());
    }

    private static int hashCode(double x) {
        long f = Double.doubleToLongBits(x);
        return (int) (f ^ f >>> 32);
    }

    public static Rectangle readFrom(MessageReader r) throws IOException {
        double tlLat = r.readDouble(1, "topLeftLatitude");
        double tlLon = r.readDouble(2, "topLeftLongitude");
        double brLat = r.readDouble(3, "buttomRightLatitude");
        double brLon = r.readDouble(4, "buttomRightLongiture");
        return Rectangle.create(tlLat, brLat, tlLon, brLon);
    }
}
