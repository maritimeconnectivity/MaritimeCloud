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
 * A distance of unit.
 *
 * @author Kasper Nielsen
 */
public enum DistanceUnit {

    /** Kilometers as defined by the International Bureau of Weights and Measures. */
    KILOMETERS {
        public double toKilometers(double distance) {
            return distance;
        }

        public double toMeters(double distance) {
            return distance * 1000;
        }
    },
    /** Meters as defined by the International Bureau of Weights and Measures. */
    METERS {
        public double toMeters(double distance) {
            return distance;
        }
    },
    /** A mile defined as 1609.344 meters. */
    MILES {
        public double toMeters(double distance) {
            return distance * METERS_TO_MILES;
        }

        public double toMiles(double distance) {
            return distance;
        }
    },
    /** A nautical mile defined as 1852 meters. */
    NAUTICAL_MILES {
        public double toMeters(double distance) {
            return distance * METERS_TO_NAUTICAL_MILES;
        }

        public double toNauticalMiles(double distance) {
            return distance;
        }
    };

    public static final double METERS_TO_MILES = 1609.344;

    public static final double METERS_TO_NAUTICAL_MILES = 1852;

    /**
     * Converts the specified distance from this distance unit to kilometers. For example, to convert 100 meters to
     * kilometers: <code>DistanceUnit.METERS.toKilometers(100)</code>.
     *
     * @param distance
     *            the distance to convert
     * @return the converted distance
     */
    public double toKilometers(double distance) {
        return toMeters(distance) / 1000;
    }

    /**
     * Converts the specified distance from this distance unit to miles. For example, to convert 100 miles to meters:
     * <code>DistanceUnit.MILES.toMeters(100)</code>.
     *
     * @param distance
     *            the distance to convert
     * @return the converted distance
     */
    public abstract double toMeters(double distance);

    /**
     * Converts the specified distance from this distance unit to miles. For example, to convert 100 meters to miles:
     * <code>DistanceUnit.METERS.toMiles(100)</code>.
     *
     * @param distance
     *            the distance to convert
     * @return the converted distance
     */
    public double toMiles(double distance) {
        return toMeters(distance) * METERS_TO_MILES;
    }

    /**
     * Converts the specified distance from this distance unit to nautical miles. For example, to convert 100 meters to
     * nautical miles: <code>DistanceUnit.METERS.toNauticalMiles(100)</code>.
     *
     * @param distance
     *            the distance to convert
     * @return the converted distance
     */
    public double toNauticalMiles(double distance) {
        return toMeters(distance) / METERS_TO_NAUTICAL_MILES;
    }
}
