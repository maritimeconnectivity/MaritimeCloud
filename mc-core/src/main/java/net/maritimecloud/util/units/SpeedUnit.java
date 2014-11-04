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
