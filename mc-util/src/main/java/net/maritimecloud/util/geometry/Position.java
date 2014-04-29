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
import java.util.Locale;

import net.maritimecloud.core.message.Message;
import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.util.geometry.CoordinateSystem.VincentyCalculationType;

/**
 * Representation of a WGS84 position and methods for calculating range and bearing between positions.
 */
public class Position implements Element, Message {

    public static final MessageParser<Position> PARSER = new MessageParser<Position>() {

        /** {@inheritDoc} */
        @Override
        public Position parse(MessageReader reader) throws IOException {
            return readFrom(reader);
        }
    };

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

    public double distanceTo(Element other, CoordinateSystem system) {
        return requireNonNull(system) == CoordinateSystem.CARTESIAN ? rhumbLineDistanceTo(other)
                : geodesicDistanceTo(other);
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
     * Get great circle distance to location
     *
     * @param location
     * @return distance in meters
     */
    public double geodesicDistanceTo(Element other) {
        if (other instanceof Position) {
            return CoordinateSystem.GEODETIC.distanceBetween(this, (Position) other);
        }
        return other.geodesicDistanceTo(this);
    }

    /**
     * Calculate final bearing for great circle route to location using Thaddeus Vincenty's</a> inverse formula.
     *
     * @param the
     *            second location
     * @return bearing in degrees
     */
    public double geodesicFinalBearingTo(Position location) {
        return CoordinateSystem.vincentyFormula(getLatitude(), getLongitude(), location.getLatitude(),
                location.getLongitude(), VincentyCalculationType.FINAL_BEARING);
    }

    /**
     * Calculate initial bearing for great circle route to location using Thaddeus Vincenty's</a> inverse formula.
     *
     * @param the
     *            second location
     * @return bearing in degrees
     */
    public double geodesicInitialBearingTo(Position location) {
        return CoordinateSystem.vincentyFormula(getLatitude(), getLongitude(), location.getLatitude(),
                location.getLongitude(), VincentyCalculationType.INITIAL_BEARING);
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
        String ns = "N";
        if (lat < 0) {
            ns = "S";
            lat *= -1;
        }
        int hours = (int) lat;
        lat -= hours;
        lat *= 60;
        String latStr = String.format(Locale.US, "%3.3f", lat);
        while (latStr.indexOf('.') < 2) {
            latStr = "0" + latStr;
        }
        return String.format(Locale.US, "%02d %s%s", hours, latStr, ns);
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
        String ns = "E";
        if (lon < 0) {
            ns = "W";
            lon *= -1;
        }
        int hours = (int) lon;
        lon -= hours;
        lon *= 60;
        String lonStr = String.format(Locale.US, "%3.3f", lon);
        while (lonStr.indexOf('.') < 2) {
            lonStr = "0" + lonStr;
        }
        return String.format(Locale.US, "%03d %s%s", hours, lonStr, ns);
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

    public double rhumbLineDistanceTo(Element other) {
        if (other instanceof Position) {
            return CoordinateSystem.CARTESIAN.distanceBetween(this, (Position) other);
        }
        return other.rhumbLineDistanceTo(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializers.writeToJSON(this);
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

    // we lose some pression

    public Position withLatitude(double latitude) {
        return new Position(latitude, longitude);
    }

    public Position withLongitude(double longitude) {
        return new Position(latitude, longitude);
    }

    public PositionTime withTime(long time) {
        return PositionTime.create(this, time);
    }

    /**
     * Writes this position to the specified MSDL output stream.
     *
     * @param os
     *            the output stream
     * @throws IOException
     *             the position failed to be written
     */
    public void writeTo(MessageWriter w) throws IOException {
        // if (w.isCompact()) {
        // writeToPacked(w, 1, "latitude", 2, "longitude");
        // } else {
        w.writeDouble(1, "latitude", latitude);
        w.writeDouble(2, "longitude", longitude);
        // }
    }

    void writeToPacked(MessageWriter w, int latId, String latName, int lonId, String lonName) throws IOException {
        w.writeInt32(latId, latName, (int) (latitude * 10_000_000d));
        w.writeInt32(lonId, lonName, (int) (longitude * 10_000_000d));
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

    public static Position createFromPacked(int latitude, int longitude) {
        return new Position(latitude / 10_000_000, longitude / 10_000_000);
    }

    public static Position fromPackedLong(long l) {
        return new Position(Float.intBitsToFloat((int) (l >> 32)), Float.intBitsToFloat((int) l));
    }

    public static boolean isValid(double latitude, double longitude) {
        return latitude <= 90 && latitude >= -90 && longitude <= 180 && longitude >= -180;
    }

    public static Position readFrom(MessageReader r) throws IOException {
        // if (r.isCompact()) {
        // int lat = r.readInt32(1, "latitude");
        // int lon = r.readInt32(2, "longitude");
        // return Position.create(lat / 10_000_000d, lon / 10_000_000d);
        // } else {
        double lat = r.readRequiredDouble(1, "latitude");
        double lon = r.readRequiredDouble(2, "longitude");
        return Position.create(lat, lon);
        // }
    }

    static Position readFromPacked(MessageReader r, int latId, String latName, int lonId, String lonName)
            throws IOException {
        int lat = r.readInt32(latId, latName);
        int lon = r.readInt32(lonId, lonName);
        return Position.create(lat / 10_000_000d, lon / 10_000_000d);
    }

    /**
     * Verify that latitude is within the interval [-90:90].
     *
     * @param latitude
     * @throws IllegalArgumentException
     *             When latitude is invalid
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
     * @throws IllegalArgumentException
     *             When longitude is invalid
     */
    public static double verifyLongitude(double longitude) {
        if (longitude > 180 || longitude < -180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180, was " + longitude);
        }
        return longitude == -0.0 ? 0.0 : longitude;
    }
}
