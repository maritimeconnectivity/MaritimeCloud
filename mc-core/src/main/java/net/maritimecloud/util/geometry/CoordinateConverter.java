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

import static java.lang.StrictMath.asin;
import static java.lang.StrictMath.atan;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;
import static java.lang.StrictMath.toDegrees;
import static java.lang.StrictMath.toRadians;

/**
 * Helper methods to transform point coordinates between geodetic representation (in degrees latitude, longitude) to
 * cartesian representation in (x,y).
 *
 * Some of the functions require the class to be instantiated and configured with a plane touching the Earth in a
 * point(lon0,lat0) somewhere. Earth is here a perfect sphere It is then possible to use 2d geometry within some km of
 * the central point.
 *
 * Other functions are static and can be used without instantiating and configuring the class for use near a specific
 * point.
 *
 * In the geodetic representations latitudes and longitudes are in decimal degrees.
 *
 * In the cartesian representations the unit of x and y is 1 meter.
 */
final class CoordinateConverter {
    /** Earth radius in metres */
    private static final double RADIUS = 6356752.3;

    /** Latitude of the central point in degrees */
    private final double lat0;

    /** Latitude of the central point in radians */
    private final double lat0Rad;

    /** Longitude of the central point in degrees */
    private final double lon0;

    /** Longitude of the central point in radians */
    private final double lon0Rad;

    /**
     * Create and configure a new CoordinateConverter with the tangential plane touching in the provided geodetic
     * coordinates.
     *
     * @param centralLongitude
     *            aaaa
     * @param centralLatitude
     *            aaa
     */
    public CoordinateConverter(double centralLongitude, double centralLatitude) {
        this.lon0 = centralLongitude;
        this.lat0 = centralLatitude;
        this.lon0Rad = toRadians(centralLongitude);
        this.lat0Rad = toRadians(centralLatitude);
    }

    /**
     * Calculate y-coordinate of the cartesian point corresponding to geodetic point (lon, lat).
     *
     * @param lon
     *            asd
     * @param lat
     *            asd
     * @return asd
     */
    public double lat2y(double lon, double lat) {
        double lonRad = toRadians(lon);
        double latRad = toRadians(lat);

        double y = 0.0;
        double denom = 1.0 + sin(lat0Rad) * sin(latRad) + cos(lat0Rad) * cos(latRad) * cos(lonRad - lon0Rad);

        if (denom != 0.0) {
            y = 2.0 * RADIUS / denom
                    * (cos(lat0Rad) * sin(latRad) - sin(lat0Rad) * cos(latRad) * cos(lonRad - lon0Rad));
        }

        return y;
    }

    /**
     * Calculate x-coordinate of the cartesian point corresponding to geodetic point (lon, lat).
     *
     * @param lon
     *            sd
     * @param lat
     *            sd
     * @return sd
     */
    public double lon2x(double lon, double lat) {
        double lonRad = toRadians(lon);
        double latRad = toRadians(lat);

        double x = 0.0;
        double denom = 1.0 + sin(lat0Rad) * sin(latRad) + cos(lat0Rad) * cos(latRad) * cos(lonRad - lon0Rad);

        if (denom != 0.0) {
            x = 2.0 * RADIUS / denom * cos(latRad) * sin(lonRad - lon0Rad);
        }

        return x;
    }

    /**
     * Calculate longitude of the geodetic point corresponding to cartesian point (x, y).
     *
     * @param x
     *            sd
     * @param y
     *            sd
     * @return asd
     */
    public double x2Lon(double x, double y) {
        double ro = sqrt(x * x + y * y);
        double c = 2.0 * atan(ro / (2 * RADIUS));
        double denom = ro * cos(lat0Rad) * cos(c) - y * sin(lat0Rad) * sin(c);

        double lon = lon0;
        if (denom != 0.0) {
            lon = lon0Rad + atan(x * sin(c) / denom);
            lon = toDegrees(lon);
        }

        return lon;
    }

    /**
     * Calculate latitude of the geodetic point corresponding to cartesian point (x, y).
     *
     * @param x
     *            asd
     * @param y
     *            sd
     * @return sd
     */
    public double y2Lat(double x, double y) {
        double lat = lat0;
        double ro = sqrt(x * x + y * y);
        if (ro != 0.0) {
            double c = 2.0 * atan(ro / (2.0 * RADIUS));
            lat = asin(cos(c) * sin(lat0Rad) + y * sin(c) * cos(lat0Rad) / ro);
            lat = toDegrees(lat);
        }
        return lat;
    }

    /**
     * The opposite transformation of compass2cartesian.
     *
     * @param a
     *            sd
     * @return sd
     */
    public static double cartesian2compass(double a) {
        return compass2cartesian(a);
    }

    /**
     * Converts a compass heading (0-360 degrees, 0 north) to a cartesian angle (0-360, 0 along x-axis).
     *
     * @param a
     *            sds
     * @return sd
     */
    public static double compass2cartesian(double a) {
        double cartesianAngle;

        if (a >= 0.0 && a <= 90.0) {
            cartesianAngle = 90.0 - a;
        } else {
            cartesianAngle = 450.0 - a;
        }
        return cartesianAngle;
    }
}
