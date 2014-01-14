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
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.maritimecloud.util;

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

    static final double METERS_TO_MILES = 1609.344;

    static final double METERS_TO_NAUTICAL_MILES = 1852;

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
        return toMeters(distance) * METERS_TO_NAUTICAL_MILES;
    }
}
