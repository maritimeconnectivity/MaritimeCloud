/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * adouble with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.maritimecloud.util.units;


/**
 * A unit of speed.
 * 
 * @author Kasper Nielsen
 */
public enum SpeedUnit {
    /** Kilometers per hour. */
    KILOMETERS_PER_HOUR {
        @Override
        public double toKilometersPerHour(double speed) {
            return speed;
        }

        @Override
        public double toKnots(double speed) {
            return speed / DistanceUnit.METERS_TO_NAUTICAL_MILES * 1000d;
        }

        @Override
        public double toMetersPerSecond(double speed) {
            return speed / 3.6;
        }

        @Override
        public double toMilesPerHour(double speed) {
            return speed / DistanceUnit.METERS_TO_MILES * 1000d;
        }
    },
    /** Knots. */
    KNOTS {
        @Override
        public double toKilometersPerHour(double speed) {
            return speed * DistanceUnit.METERS_TO_NAUTICAL_MILES / 1000d;
        }

        @Override
        public double toKnots(double speed) {
            return speed;
        }

        @Override
        public double toMetersPerSecond(double speed) {
            return speed * DistanceUnit.METERS_TO_NAUTICAL_MILES / 3600d;
        }

        @Override
        public double toMilesPerHour(double speed) {
            return speed / DistanceUnit.METERS_TO_MILES * DistanceUnit.METERS_TO_NAUTICAL_MILES;
        }
    },
    /** Meters Per Second. */
    METERS_PER_SECOND {
        @Override
        public double toKilometersPerHour(double speed) {
            return speed * 3.6d;
        }

        @Override
        public double toKnots(double speed) {
            return speed / DistanceUnit.METERS_TO_NAUTICAL_MILES * 3600d;
        }

        @Override
        public double toMetersPerSecond(double speed) {
            return speed;
        }

        @Override
        public double toMilesPerHour(double speed) {
            return speed / DistanceUnit.METERS_TO_MILES * 3600d;
        }
    },
    /** Miles Per Hour. */
    MILES_PER_HOUR {
        @Override
        public double toKilometersPerHour(double speed) {
            return speed * DistanceUnit.METERS_TO_MILES / 1000d;
        }

        @Override
        public double toKnots(double speed) {
            return speed * DistanceUnit.METERS_TO_MILES / DistanceUnit.METERS_TO_NAUTICAL_MILES;
        }

        @Override
        public double toMetersPerSecond(double speed) {
            return speed * DistanceUnit.METERS_TO_MILES / 3600d;
        }

        @Override
        public double toMilesPerHour(double speed) {
            return speed;
        }
    };

    // new Dist(DistType.NAUTICAL_MILES, nm).in(DistType.METERS).doubleValue();
    // DistanceUnit.METERS.from(DistanceUnit.NauticalMiles, nm);
    // DistanceUnit.METERS.fromNauticalMiles(nm);

    // SpeedUnit.METERS_PER_SECOND.toKilometersPerHour(100)


    // SpeedUnit.KILOMETERS_PER_HOUR.fromMetersPerSecond(100)

    /**
     * Converts the specified speed from this speed unit to kilometers per hour. For example, to convert 100 meters per
     * second to kilometers per hour: <code>SpeedUnit.METERS_PER_SECOND.toKilometersPerHour(100)</code>.
     * 
     * @param speed
     *            the speed to convert
     * @return the converted speed
     */
    public abstract double toKilometersPerHour(double speed);

    /**
     * Converts the specified speed from this speed unit to knots. For example, to convert 100 meters per second to
     * knots: <code>SpeedUnit.METERS_PER_SECOND.toKnots(100)</code>.
     * 
     * @param speed
     *            the speed to convert
     * @return the converted speed
     */
    public abstract double toKnots(double speed);

    /**
     * Converts the specified speed from this speed unit to miles per hour. For example, to convert 100 knots to meters
     * per second: <code>SpeedUnit.KNOTS.toMetersPerSecond(100)</code>.
     * 
     * @param speed
     *            the speed to convert
     * @return the converted speed
     */
    public abstract double toMetersPerSecond(double speed);

    /**
     * Converts the specified speed from this speed unit to miles per hour. For example, to convert 100 meters per
     * second to miles per hour: <code>SpeedUnit.METERS_PER_SECOND.toMilesPerHour(100)</code>.
     * 
     * @param speed
     *            the speed to convert
     * @return the converted speed
     */
    public abstract double toMilesPerHour(double speed);
}
