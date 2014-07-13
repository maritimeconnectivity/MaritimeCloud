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

/**
 * A coordinate system.
 *
 * @author Kasper Nielsen
 */
enum CoordinateSystem {

    /** A Cartesian coordinate system. */
    CARTESIAN {

        /** {@inheritDoc} */
        @Override
        double distanceBetween(double latitude1, double longitude1, double latitude2, double longitude2) {
            double lat1 = Math.toRadians(latitude1);
            double lat2 = Math.toRadians(latitude2);
            double dLat = Math.toRadians(latitude2 - latitude1);
            double dLon = Math.toRadians(Math.abs(longitude2 - longitude1));
            double dPhi = Math.log(Math.tan(lat2 / 2 + Math.PI / 4) / Math.tan(lat1 / 2 + Math.PI / 4));
            double q = dPhi == 0 ? Math.cos(lat1) : dLat / dPhi;
            // if dLon over 180 take shorter rhumb across 180 meridian:
            if (dLon > Math.PI) {
                dLon = 2 * Math.PI - dLon;
            }
            return Math.sqrt(dLat * dLat + q * q * dLon * dLon) * EARTH_MEAN_RADIUS_KM * 1000;
        }

        /** {@inheritDoc} */
        @Override
        Position pointOnBearing0(double startLatDegrees, double startLonDegrees, double distanceMeters,
                double bearingDegrees) {
            // Convert to radians
            startLatDegrees = Math.toRadians(startLatDegrees);
            startLonDegrees = Math.toRadians(startLonDegrees);
            bearingDegrees = Math.toRadians(bearingDegrees);
            // the earth's radius in meters
            final double earthRadius = EARTH_MEAN_RADIUS_KM * 1000.0;

            double endLat = Math.asin(Math.sin(startLatDegrees) * Math.cos(distanceMeters / earthRadius)
                    + Math.cos(startLatDegrees) * Math.sin(distanceMeters / earthRadius) * Math.cos(bearingDegrees));
            double endLon = startLonDegrees
                    + Math.atan2(
                            Math.sin(bearingDegrees) * Math.sin(distanceMeters / earthRadius)
                                    * Math.cos(startLatDegrees),
                            Math.cos(distanceMeters / earthRadius) - Math.sin(startLatDegrees) * Math.sin(endLat));
            return Position.create(Math.toDegrees(endLat), Math.toDegrees(endLon));
        }
    },

    /** A Geodetic coordinate system. */
    GEODETIC {

        /** {@inheritDoc} */
        @Override
        double distanceBetween(double latitude1, double longitude1, double latitude2, double longitude2) {
            return vincentyFormula(latitude1, longitude1, latitude2, longitude2, VincentyCalculationType.DISTANCE);
        }

        /** {@inheritDoc} */
        @Override
        Position pointOnBearing0(double latitude, double longitude, double distance, double bearing) {
            throw new UnsupportedOperationException();
        }

    };

    /** The equatorial radius of the Earth in kilometers. */
    public static final double EARTH_EQUATORIAL_RADIUS_KM = 6378.1370;

    /** Earth's mean radius in KM according to The International Union of Geodesy and Gephysics. */
    public static final double EARTH_MEAN_RADIUS_KM = 6371.0087714;

    /**
     * Returns the distance between the two positions in the given coordinate system.
     *
     * @param latitude1
     *            the latitude of the first position
     * @param longitude1
     *            the longitude of the first position
     * @param latitude2
     *            the latitude of the second position
     * @param longitude2
     *            the longitude of the second position
     * @return the distance between the two positions in the given coordinate system
     */
    abstract double distanceBetween(double latitude1, double longitude1, double latitude2, double longitude2);

    /**
     * Returns the distance between the two positions in the given coordinate system.
     *
     * @param p1
     *            the first position
     * @param p2
     *            the second position
     * @return the distance between the two positions in the given coordinate system
     */
    public double distanceBetween(Position p1, Position p2) {
        return distanceBetween(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());
    }

    public Position pointOnBearing(Position position, double distance, double bearing) {
        if (distance < 0) {
            throw new IllegalArgumentException("distance must be positive, was " + distance);
        } else if (distance == 0) {
            return position;
        } else {
            return pointOnBearing0(position.getLatitude(), position.getLongitude(), distance, bearing);
        }
    }

    abstract Position pointOnBearing0(double latitude, double longitude, double distance, double bearing);

    static double vincentyFormula(double latitude1, double longitude1, double latitude2, double longitude2,
            VincentyCalculationType type) {
        double a = 6378137;
        double b = 6356752.3142;
        double f = 1 / 298.257223563; // WGS-84 ellipsiod
        double L = Math.toRadians(longitude2 - longitude1);
        double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(latitude1)));
        double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(latitude2)));
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double lambda = L;
        double lambdaP = 2 * Math.PI;
        double iterLimit = 20;
        double sinLambda = 0;
        double cosLambda = 0;
        double sinSigma = 0;
        double cosSigma = 0;
        double sigma = 0;
        double sinAlpha = 0;
        double cosSqAlpha = 0;
        double cos2SigmaM = 0;
        double C;
        while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0) {
            sinLambda = Math.sin(lambda);
            cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt(cosU2 * sinLambda * (cosU2 * sinLambda) + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
                    * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
            if (sinSigma == 0) {
                return 0; // co-incident points
            }
            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
            if (Double.isNaN(cos2SigmaM)) {
                cos2SigmaM = 0; // equatorial line
            }
            C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1 - C) * f * sinAlpha
                    * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        }
        if (iterLimit == 0) {
            return Double.NaN; // formula failed to converge
        }

        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B
                * sinSigma
                * (cos2SigmaM + B
                        / 4
                        * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
                                * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
        double distance = b * A * (sigma - deltaSigma);
        if (type == VincentyCalculationType.DISTANCE) {
            return distance;
        }
        // initial bearing
        double fwdAz = Math.toDegrees(Math.atan2(cosU2 * sinLambda, cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
        if (type == VincentyCalculationType.INITIAL_BEARING) {
            return fwdAz;
        }
        // final bearing
        return Math.toDegrees(Math.atan2(cosU1 * sinLambda, -sinU1 * cosU2 + cosU1 * sinU2 * cosLambda));
    }

    /** What kind of calculation type we are performing. */
    static enum VincentyCalculationType {
        DISTANCE, FINAL_BEARING, INITIAL_BEARING;
    }
}
