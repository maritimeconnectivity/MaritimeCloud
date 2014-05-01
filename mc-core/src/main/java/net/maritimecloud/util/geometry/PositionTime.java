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

import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageWriter;

/**
 *
 * @author Kasper Nielsen
 */
// TODO make it serializable
public class PositionTime extends Position {

    public static final MessageParser<PositionTime> PARSER = new MessageParser<PositionTime>() {

        /** {@inheritDoc} */
        @Override
        public PositionTime parse(MessageReader reader) throws IOException {
            return readFrom(reader);
        }
    };

    public static PositionTime readFrom(MessageReader r) throws IOException {
        // if (r.isCompact()) {
        // int lat = r.readInt32(1, "latitude");
        // int lon = r.readInt32(2, "longitude");
        // return Position.create(lat / 10_000_000d, lon / 10_000_000d);
        // } else {
        double lat = r.readRequiredDouble(1, "latitude");
        double lon = r.readRequiredDouble(2, "longitude");
        long time = r.readInt64(3, "time", 0L);

        return PositionTime.create(lat, lon, time);
        // }
    }

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private final long time;

    /**
     * @param latitude
     * @param longitude
     * @param time
     */
    public PositionTime(double latitude, double longitude, long time) {
        super(latitude, longitude);
        this.time = time;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        return other instanceof PositionTime && equals((PositionTime) other);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Position other) {
        return other instanceof PositionTime && equals((PositionTime) other);
    }

    public boolean positionEquals(Position other) {
        return super.equals(other);
    }

    // We probably want another function that also takes a precision.
    public boolean equals(PositionTime other) {
        return super.equals(other) && time == other.time;
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
        return (int) (time ^ latLong ^ latLong >>> 32) ^ (int) (time ^ lonLong ^ lonLong >>> 32);
    }

    public long getTime() {
        return time;
    }

    public static PositionTime create(Position position, long time) {
        return create(position.latitude, position.longitude, time);
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
    public static PositionTime create(double latitude, double longitude, long time) {
        return new PositionTime(latitude, longitude, time);
    }

    @Override
    public String toString() {
        return "(" + getLatitude() + ", " + getLongitude() + ", time= " + time + ")";
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
        w.writeInt64(3, "time", time);
        // }
    }
}
