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

import net.maritimecloud.internal.message.BinaryUtil;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.CoordinateSystem.VincentyCalculationType;

/**
 * Representation of a WGS84 position and methods for calculating range and bearing between positions.
 */
public class Position implements Message, Serializable {

    static final double POS_INT_SCALE = 10_000_000d;

    public static final MessageSerializer<Position> SERIALIZER = new MessageSerializer<Position>() {

        /** {@inheritDoc} */
        @Override
        public Position read(MessageReader reader) throws IOException {
            double lat = reader.readDouble(1, "latitude");
            double lon = reader.readDouble(2, "longitude");
            return Position.create(lat, lon);
        }

        public void write(Position message, MessageWriter writer) throws IOException {
            writer.writeDouble(1, "latitude", message.latitude);
            writer.writeDouble(2, "longitude", message.longitude);
        }
    };

    //
    // static Position readFrom(MessageReader r) throws IOException {
    // // if (r.isCompact()) {
    // // int lat = r.readInt32(1, "latitude");
    // // int lon = r.readInt32(2, "longitude");
    // // return Position.create(lat / 10_000_000d, lon / 10_000_000d);
    // // } else {
    // double lat = r.readDouble(1, "latitude");
    // double lon = r.readDouble(2, "longitude");
    // return Position.create(lat, lon);
    // // }
    // }
    //
    // static Position readFromPacked(MessageReader r, int latId, String latName, int lonId, String lonName)
    // throws IOException {
    // int lat = r.readInt(latId, latName);
    // int lon = r.readInt(lonId, lonName);
    // return Position.create(lat / 10_000_000d, lon / 10_000_000d);
    // }

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The latitude part of the position. */
    final double latitude;

    /** The longitude part of the position. */
    final double longitude;

    /**
     * Constructor given position and timezone
     *
     * @param latitude
     *            Negative south of equator
     * @param longitude
     *            Negative east of Prime Meridian
     */

    Position(double latitude, double longitude) {
        this.latitude = verifyLatitude(latitude);
        this.longitude = verifyLongitude(longitude);
    }

    /**
     * Equals method
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Position && equals((Position) other);
    }

    // We probably want another function that also takes a precision.
    public boolean equals(Position other) {
        // id longitude 180 == - 180???
        return other == this || other != null && latitude == other.latitude && longitude == other.longitude;
    }

    /**
     * Returns the great circle distance to the specified position.
     *
     * @param other
     *            the position to calculate the distance from
     * @return distance in meters
     */
    public double geodesicDistanceTo(Position other) {
        return CoordinateSystem.GEODETIC.distanceBetween(this, other);
    }

    /**
     * Calculate final bearing for great circle route to location using Thaddeus Vincenty's inverse formula.
     *
     * @param other
     *            the position to calculate the bearing from
     * @return bearing in degrees
     */
    public double geodesicFinalBearingTo(Position other) {
        return CoordinateSystem.vincentyFormula(getLatitude(), getLongitude(), other.getLatitude(),
                other.getLongitude(), VincentyCalculationType.FINAL_BEARING);
    }

    /**
     * Calculate initial bearing for great circle route to location using Thaddeus Vincenty's inverse formula.
     *
     * @param other
     *            the position to calculate the bearing from
     * @return bearing in degrees
     */
    public double geodesicInitialBearingTo(Position other) {
        return CoordinateSystem.vincentyFormula(getLatitude(), getLongitude(), other.getLatitude(),
                other.getLongitude(), VincentyCalculationType.INITIAL_BEARING);
    }

    public long getCell(double degress) {
        if (degress < 0.0001) {
            throw new IllegalArgumentException("degress = " + degress);
        } else if (degress > 100) {
            throw new IllegalArgumentException("degress = " + degress);
        }
        return (long) (Math.floor(getLatitude() / degress) * (360.0 / degress))
                + (long) ((360.0 + getLongitude()) / degress) - (long) (360L / degress);
    }

    public int getCellInt(double degress) {
        // bigger cellsize than 0.01 cannot be supported. unless we change the cellsize to long
        if (degress < 0.01) {
            throw new IllegalArgumentException("degress = " + degress);
        }
        return (int) getCell(degress);
    }

    /**
     * Returns the latitude part of this position.
     *
     * @return the latitude part of this position
     */
    public double getLatitude() {
        return latitude;
    }

    public String getLatitudeAsString() {
        double lat = latitude;
        if (lat < 0) {
            lat *= -1;
        }
        int hours = (int) lat;
        lat -= hours;
        lat *= 60;

        StringBuilder latitudeAsString = new StringBuilder(16);
        latitudeAsString.append(format00(hours));
        latitudeAsString.append(" ");
        latitudeAsString.append(format00((int) lat));
        latitudeAsString.append(".");
        latitudeAsString.append(format000((int) Math.round(1000 * (lat - (int) lat))));
        latitudeAsString.append(latitude < 0 ? "S" : "N");
        return latitudeAsString.toString();
    }

    /**
     * Returns the longitude part of this position.
     *
     * @return the longitude part of this position
     */
    public double getLongitude() {
        return longitude;
    }

    public String getLongitudeAsString() {
        double lon = longitude;
        if (lon < 0) {
            lon *= -1;
        }
        int hours = (int) lon;
        lon -= hours;
        lon *= 60;

        StringBuilder longitudeAsString = new StringBuilder(16);
        longitudeAsString.append(format000(hours));
        longitudeAsString.append(" ");
        longitudeAsString.append(format00((int) lon));
        longitudeAsString.append(".");
        longitudeAsString.append(format000((int) Math.round(1000 * (lon - (int) lon))));
        longitudeAsString.append(longitude < 0 ? "W" : "E");
        return longitudeAsString.toString();
    }

    /**
     * Hash code for the location
     */
    @Override
    public int hashCode() {
        // If we need to use this as a key somewhere we can use the same hash
        // code technique as java.lang.String
        long latLong = Double.doubleToLongBits(latitude);
        long lonLong = Double.doubleToLongBits(longitude);
        return (int) (latLong ^ latLong >>> 32) ^ (int) (lonLong ^ lonLong >>> 32);
    }

    /** {@inheritDoc} */
    @Override
    public Position immutable() {
        return this;
    }

    /**
     * Calculates the rhumb line bearing to the specified position
     *
     * @param position
     *            the position
     * @return the rhumb line bearing in degrees
     */
    public double rhumbLineBearingTo(Position position) {
        double lat1 = Math.toRadians(latitude);
        double lat2 = Math.toRadians(position.latitude);
        double dPhi = Math.log(Math.tan(lat2 / 2 + Math.PI / 4) / Math.tan(lat1 / 2 + Math.PI / 4));

        double dLon = Math.toRadians(position.longitude - longitude);
        if (Math.abs(dLon) > Math.PI) {
            dLon = dLon > 0 ? -(2 * Math.PI - dLon) : 2 * Math.PI + dLon;
        }
        double brng = Math.atan2(dLon, dPhi);
        return (Math.toDegrees(brng) + 360) % 360;
    }

    public double rhumbLineDistanceTo(Position other) {
        return CoordinateSystem.CARTESIAN.distanceBetween(this, other);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Packs the position into a long (losing some precision). Can be read later by {@link #fromPackedLong(long)}
     *
     * @return the packet long
     */
    public long toPackedLong() {
        float lat = (float) getLatitude();
        float lon = (float) getLongitude();
        return ((long) Float.floatToRawIntBits(lat) << 32) + Float.floatToRawIntBits(lon);
    }

    @Override
    public String toString() {
        return "(" + getLatitude() + ", " + getLongitude() + ")";
    }

    public String toStringDegrees() {
        return "(" + getLatitudeAsString() + ", " + getLongitudeAsString() + ")";
    }

    /**
     * Returns a new position with the same longitude as this position but with the specified latitude.
     *
     * @param latitude
     *            the new latitude for the new position
     * @return the new position
     */
    public Position withLatitude(double latitude) {
        return new Position(latitude, longitude);
    }

    /**
     * Returns a new position with the same latitude as this position but with the specified longitude.
     *
     * @param longitude
     *            the new longitude for the new position
     * @return the new position
     */
    public Position withLongitude(double longitude) {
        return new Position(latitude, longitude);
    }

    /**
     * Returns a new position time with this position added with the current time.
     *
     * @param time
     *            the time of the new position time object
     * @return the new position time object
     */
    public PositionTime withTime(long time) {
        return PositionTime.create(this, time);
    }

    void writeToPacked(MessageWriter w, int latId, String latName, int lonId, String lonName) throws IOException {
        w.writeInt(latId, latName, getLatitudeAsInt());
        w.writeInt(lonId, lonName, getLongitudeAsInt());
    }

    int getLatitudeAsInt() {
        return (int) (latitude * POS_INT_SCALE);
    }

    int getLongitudeAsInt() {
        return (int) (longitude * POS_INT_SCALE);
    }

    /**
     * Creates a new position from the specified latitude and longitude.
     *
     * @param latitude
     *            the latitude
     * @param longitude
     *            the longitude
     * @return the new position
     * @throws IllegalArgumentException
     *             if the
     */
    public static Position create(double latitude, double longitude) {
        return new Position(latitude, longitude);
    }

    /**
     * Format the given integer value as a String of length 2 with leading zeros.
     *
     * @param value
     *            the value to format
     * @return the formatted string
     */
    private static String format00(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return Integer.toString(value);
    }

    /**
     * Returns a position from a 64 bit byte array encoded as decimal degress with 7 decimal places.
     *
     * @return the position from a binary array
     */
    public static Position fromBinary(Binary b) {
        byte[] bytes = b.toByteArray();
        Position pos = Position.create(
                BinaryUtil.readInt(bytes, 0) / POS_INT_SCALE,
                BinaryUtil.readInt(bytes, 4) / POS_INT_SCALE
        );
        return pos;
    }

    /**
     * Returns a 64 bit representation of this position. Encoded as decimal degress with 7 decimal places.
     *
     * @return the position as a binary
     */
    public Binary toBinary() {
        byte[] b = new byte[4 + 4];
        BinaryUtil.writeInt(getLatitudeAsInt(), b, 0);
        BinaryUtil.writeInt(getLongitudeAsInt(), b, 4);
        return Binary.copyFrom(b);
    }

    /**
     * Format the given integer value as a String of length 3 with leading zeros.
     *
     * @param value
     *            the value to format
     * @return the formatted string
     */
    private static String format000(int value) {
        if (value < 10) {
            return "00" + value;
        } else if (value < 100) {
            return "0" + value;
        }
        return Integer.toString(value);
    }

    public static Position fromPackedLong(long l) {
        return new Position(Float.intBitsToFloat((int) (l >> 32)), Float.intBitsToFloat((int) l));
    }

    /**
     * @param latitude
     *            the latitude
     * @param longitude
     *            the longitude
     * @return whether or not the specified latitude and longitude is valid
     */
    public static boolean isValid(double latitude, double longitude) {
        return latitude <= 90 && latitude >= -90 && longitude <= 180 && longitude >= -180;
    }

    /**
     * Returns a random valid position.
     *
     * @return the random position
     */
    public static Position random() {
        return random(ThreadLocalRandom.current());
    }

    /**
     * Returns a random valid position.
     *
     * @param rnd
     *            the source of randomness
     * @return the random position
     */
    public static Position random(Random rnd) {
        return Position.create(rnd.nextDouble() * 180 - 90, rnd.nextDouble() * 360 - 180);
    }


    /**
     * Verify that latitude is within the interval [-90:90].
     *
     * @param latitude
     *            the latitude to verify
     * @throws IllegalArgumentException
     *             When latitude is invalid
     * @return the specified latitude
     */
    public static double verifyLatitude(double latitude) {
        if (latitude > 90 || latitude < -90) {
            throw new IllegalArgumentException("Illegal latitude must be between -90 and 90, was " + latitude);
        }
        // We want simple equals and hashCode implementation. So we make sure
        // that positions are never constructed with -0.0 as latitude or longitude.
        return latitude == -0.0 ? 0.0 : latitude;
    }

    /**
     * Verify that longitude is within the interval [-180:180].
     *
     * @param longitude
     *            the longitude to verify
     * @throws IllegalArgumentException
     *             When longitude is invalid
     * @return the specified longitude
     */
    public static double verifyLongitude(double longitude) {
        if (longitude > 180 || longitude < -180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180, was " + longitude);
        }
        // We want simple equals and hashCode implementation. So we make sure
        // that positions are never constructed with -0.0 as latitude or longitude.
        return longitude == -0.0 ? 0.0 : longitude;
    }
}

//
// public static Position createFromPacked(int latitude, int longitude) {
// return new Position(latitude / 10_000_000, longitude / 10_000_000);
// }
