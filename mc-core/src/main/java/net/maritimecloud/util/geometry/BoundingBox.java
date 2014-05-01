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

import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageWriter;

public final class BoundingBox extends Polygon {

    /** A bounding box encompassing all coordinates. */
    public static final BoundingBox ALL = create(-90, 90, -180, 180, CoordinateSystem.GEODETIC);


    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private final double maxLatitude;

    private final double maxLongitude;

    private final double minLatitude;

    private final double minLongitude;

    public static final MessageParser<BoundingBox> PARSER = new MessageParser<BoundingBox>() {

        /** {@inheritDoc} */
        @Override
        public BoundingBox parse(MessageReader reader) throws IOException {
            return readFrom(reader);
        }
    };

    private BoundingBox(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude,
            CoordinateSystem cs) {
        super(cs);
        this.minLatitude = Position.verifyLatitude(minLatitude);
        this.maxLatitude = Position.verifyLatitude(maxLatitude);
        this.minLongitude = Position.verifyLongitude(minLongitude);
        this.maxLongitude = Position.verifyLongitude(maxLongitude);
    }

    @Override
    public boolean contains(Element element) {
        if (element instanceof Position) {
            return contains((Position) element);
        } else {
            return super.contains(element);
        }
    }

    public boolean contains(Position point) {
        return point.getLatitude() >= minLatitude && point.getLongitude() >= minLongitude
                && point.getLatitude() <= maxLatitude && point.getLongitude() <= maxLongitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof BoundingBox) {
            BoundingBox that = (BoundingBox) obj;
            return minLatitude == that.minLatitude && minLongitude == that.minLongitude
                    && maxLatitude == that.maxLatitude && maxLongitude == that.maxLongitude;
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public BoundingBox getBoundingBox() {
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
    public BoundingBox include(BoundingBox other) {
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
        return changed ? new BoundingBox(minLat, maxLat, minLon, maxLon, cs) : this;
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
        // If the bounding box of this and other is shorter than the sum of the heights AND skinnier than the sum of the
        // widths, they must intersect
        BoundingBox common = include(other);
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

    static BoundingBox create(double y1, double y2, double x1, double x2, CoordinateSystem cs) {
        return new BoundingBox(Math.min(y1, y2), Math.max(y1, y2), Math.min(x1, x2), Math.max(x1, x2), cs);
    }

    public static BoundingBox create(Position location, Position other, CoordinateSystem cs) {
        return create(location.getLatitude(), other.getLatitude(), location.getLongitude(), other.getLongitude(), cs);
    }

    private static int hashCode(double x) {
        long f = Double.doubleToLongBits(x);
        return (int) (f ^ f >>> 32);
    }

    public static void main(String[] args) {
        Circle c = Circle.create(Position.create(0, 0), 1);

        BoundingBox c2 = BoundingBox.create(Position.create(1, 1), Position.create(2, 2), CoordinateSystem.CARTESIAN);

        System.out.println(c.intersects(c2));

    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        w.writeInt32(1, "topLeftLatitude", (int) (getUpperLeft().getLatitude() * 10_000_000d));
        w.writeInt32(2, "topLeftLongitude", (int) (getUpperLeft().getLongitude() * 10_000_000d));
        w.writeInt32(3, "topLeftLatitude", (int) (getUpperLeft().getLatitude() * 10_000_000d));
        w.writeInt32(4, "topLeftLatitude", (int) (getUpperLeft().getLatitude() * 10_000_000d));
    }

    public static BoundingBox readFrom(MessageReader r) {
        return null;
    }
}
