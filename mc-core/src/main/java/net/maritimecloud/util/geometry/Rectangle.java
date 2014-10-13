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

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;

public final class Rectangle extends Area {

    /** A bounding box encompassing all coordinates. */
    public static final Rectangle ALL = new Rectangle(90, -180, -90, 180);

    public static final MessageSerializer<Rectangle> SERIALIZER = new MessageSerializer<Rectangle>() {

        /** {@inheritDoc} */
        @Override
        public Rectangle read(MessageReader reader) throws IOException {
            double topLeftLatitude = reader.readDouble(1, "topLeftLatitude");
            double topLeftLongitude = reader.readDouble(2, "topLeftLongitude");
            double bottomRightLatitude = reader.readDouble(3, "bottomRightLatitude");
            double bottomRightLongitude = reader.readDouble(4, "bottomRightLongitude");
            return Rectangle.create(topLeftLatitude, topLeftLongitude, bottomRightLatitude, bottomRightLongitude);
        }

        public void write(Rectangle message, MessageWriter w) throws IOException {
            w.writeDouble(1, "topLeftLatitude", message.getTopLeftLatitude());
            w.writeDouble(2, "topLeftLongitude", message.getTopLeftLongitude());
            w.writeDouble(3, "bottomRightLatitude", message.getBottomRightLatitude());
            w.writeDouble(4, "bottomRightLongitude", message.getBottomRightLongitude());
        }
    };

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    final double bottomRightLatitude;

    final double bottomRightLongitude;

    final double topLeftLatitude;

    final double topLeftLongitude;

    Rectangle(double topLeftLatitude, double topLeftLongitude, double bottomRightLatitude, double bottomRightLongitude) {
        this.topLeftLatitude = Position.verifyLatitude(topLeftLatitude);
        this.topLeftLongitude = Position.verifyLongitude(topLeftLongitude);

        this.bottomRightLatitude = Position.verifyLatitude(bottomRightLatitude);
        this.bottomRightLongitude = Position.verifyLongitude(bottomRightLongitude);
    }

    public boolean contains(double latitude, double longitude) {
        return containsLatitude(latitude) && containsLongitude(longitude);
    }

    public boolean contains(Position position) {
        return contains(position.getLatitude(), position.getLongitude());
    }

    private boolean containsLatitude(double latitude) {
        if (topLeftLatitude >= bottomRightLatitude) {
            return bottomRightLatitude <= latitude && latitude <= topLeftLatitude;
        } else { // crosses date line
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }

    private boolean containsLongitude(double longitude) {
        if (topLeftLongitude <= bottomRightLongitude) {
            return topLeftLongitude <= longitude && longitude <= bottomRightLongitude;
        } else { // crosses date line
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Rectangle && equals((Rectangle) obj);
    }

    private boolean equals(Rectangle that) {
        return topLeftLatitude == that.topLeftLatitude && topLeftLongitude == that.topLeftLongitude
                && bottomRightLatitude == that.bottomRightLatitude && bottomRightLongitude == that.bottomRightLongitude;
    }

    public Position getBottomRight() {
        return Position.create(getBottomRightLatitude(), getBottomRightLongitude());
    }

    public Position getBottomLeft() {
        return Position.create(getBottomRightLatitude(), getTopLeftLongitude());
    }

    /**
     * @return the bottomRightLatitude
     */
    public double getBottomRightLatitude() {
        return bottomRightLatitude;
    }

    /**
     * @return the bottomRightLongitude
     */
    public double getBottomRightLongitude() {
        return bottomRightLongitude;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getBoundingBox() {
        return this;
    }

    //
    // public Position getCenterPoint() {
    // return Position.create((bottomLatitude + topLatitude) / 2, (leftLongitude + rightLongitude) / 2);
    // }


    /** {@inheritDoc} */
    @Override
    public Position getRandomPosition(Random r) {
        double latitude;
        if (topLeftLatitude >= bottomRightLatitude) {
            latitude = Area.nextDouble(r, bottomRightLatitude, topLeftLatitude);
        } else { // crosses date line
            throw new UnsupportedOperationException("Not implemented yet");
        }
        double longitude;
        if (topLeftLongitude <= bottomRightLongitude) {
            longitude = Area.nextDouble(r, topLeftLongitude, bottomRightLongitude);
        } else { // crosses date line
            throw new UnsupportedOperationException("Not implemented yet");
        }
        return Position.create(latitude, longitude);
    }

    public Position getTopLeft() {
        return Position.create(getTopLeftLatitude(), getTopLeftLongitude());
    }

    public Position getTopRight() {
        return Position.create(getTopLeftLatitude(), getBottomRightLongitude());
    }

    /**
     * @return the topLeftLatitude
     */
    public double getTopLeftLatitude() {
        return topLeftLatitude;
    }

    /**
     * @return the topLeftLongitude
     */
    public double getTopLeftLongitude() {
        return topLeftLongitude;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + Double.hashCode(topLeftLatitude);
        result = 37 * result + Double.hashCode(topLeftLongitude);
        result = 37 * result + Double.hashCode(bottomRightLatitude);
        result = 37 * result + Double.hashCode(bottomRightLongitude);
        return result;
    }

    /** {@inheritDoc} */
    public Rectangle immutable() {
        return this;
    }

    // /**
    // * Returns a rectangle bounding box that includes the specified bounding box.
    // *
    // * @param other
    // * the bounding box to include
    // * @return a bounding box
    // */
    // public Rectangle union(Rectangle other) {
    //
    // // double minLon = this.leftLongitude;
    // // double maxLon = this.rightLongitude;
    // // double minLat = this.bottomLatitude;
    // // double maxLat = this.topLatitude;
    // // boolean changed = false;
    // // if (other.leftLongitude < minLon) {
    // // minLon = other.leftLongitude;
    // // changed = true;
    // // }
    // // if (other.rightLongitude > maxLon) {
    // // maxLon = other.rightLongitude;
    // // changed = true;
    // // }
    // // if (other.bottomLatitude < minLat) {
    // // minLat = other.bottomLatitude;
    // // changed = true;
    // // }
    // // if (other.topLatitude > maxLat) {
    // // maxLat = other.topLatitude;
    // // changed = true;
    // // }
    // // return changed ? new Rectangle(minLat, maxLat, minLon, maxLon) : this;
    // }


    /** {@inheritDoc} */
    @Override
    public boolean intersects(Area other) {
        if (other instanceof Circle) {
            return intersects((Circle) other);
        } else if (other instanceof Rectangle) {
            return intersects((Rectangle) other);
        } else {
            throw new UnsupportedOperationException("Only circles and Rectangle supported");
        }
    }


    public boolean intersects(Circle other) {
        // Either the circle's centre lies inside the rectangle, or
        // One of the edges of the rectangle intersects the circle.
        // See more here http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
        if (contains(other.getCenter())) {
            return true;
        }
        Position topLeft = getTopLeft();
        Position topRight = getTopRight();
        Position bottomLeft = getBottomLeft();
        Position bottomRight = getBottomRight();
        return other.intersects(topLeft, topRight) || other.intersects(topRight, bottomLeft)
                || other.intersects(topLeft, bottomLeft) || other.intersects(bottomLeft, bottomRight);
    }

    public boolean intersects(Rectangle other) {
        // If the bounding box of this and other is shorter than the sum of the heights AND skinnier than the sum of the
        // widths, they must intersect
        // Rectangle common = union(other);
        // return common.getLatitudeSize() < getLatitudeSize() + other.getLatitudeSize()
        // && common.getLongitudeSize() < getLongitudeSize() + other.getLongitudeSize();
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "(" + topLeftLatitude + "," + topLeftLongitude + "), (" + bottomRightLatitude + ","
                + bottomRightLongitude + ")";
    }

    boolean wrapsDateLine() {
        return topLeftLongitude > bottomRightLongitude;
    }

    static Rectangle create(double y1, double y2, double x1, double x2) {
        return new Rectangle(Math.min(y1, y2), Math.max(y1, y2), Math.min(x1, x2), Math.max(x1, x2));
    }

    public static Rectangle create(Position topLeft, Position bottomRight) {
        return new Rectangle(topLeft.getLatitude(), topLeft.getLongitude(), bottomRight.getLatitude(),
                bottomRight.getLongitude());
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
     * match.
     *
     * @param string
     *            the JSON string to parse
     * @return the parsed rectangle
     */
    public static Rectangle fromJSON(CharSequence string) {
        return MessageSerializer.readFromJSON(SERIALIZER, string);
    }
}
//
// public double getArea(/* DistanceUnit unit */) {
// // CoordinateSystem.CARTESIAN.distanceBetween(minLatitude, minLongitude, maxLatitude, maxLongitude)
// final Position a = new Position(topLatitude, leftLongitude);
// final Position b = new Position(topLatitude, rightLongitude);
// final Position c = new Position(bottomLatitude, leftLongitude);
// final double ab = a.rhumbLineDistanceTo(b); // meters
// final double ac = a.rhumbLineDistanceTo(c); // meters
// return ab * ac;
// }
