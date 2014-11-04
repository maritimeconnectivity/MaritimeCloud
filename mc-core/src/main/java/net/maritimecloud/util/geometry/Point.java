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

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.toRadians;

/**
 * This class represent a point expressed in Cartesian coordinates (x,y).
 */
final class Point {

    public static final Point ORIGIN = new Point(0.0, 0.0);

    private final double x;

    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Rotate this point around point p0 with thetaDeg degrees and return the rotated point.
     *
     * @param p0
     *            the point to rotate around.
     * @param thetaDeg
     *            the no. of degrees counter-clockwise to rotate.
     * @return the rotated point.
     */
    public Point rotate(Point p0, double thetaDeg) {
        final double theta = toRadians(thetaDeg);

        double xr = p0.x + (cos(theta) * (this.x - p0.x) - sin(theta) * (this.y - p0.y));
        double yr = p0.y + (sin(theta) * (this.x - p0.x) + cos(theta) * (this.y - p0.y));

        return new Point(xr, yr);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Point{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Translate this point by vector (dx, dy) and return the translated point.
     *
     * @param dx
     *            the no. of units to transate along the x-axis.
     * @param dy
     *            the no. of units to transate along the y-axis.
     * @return the translated point.
     */
    public Point translate(double dx, double dy) {
        return new Point(x + dx, y + dy);
    }
}
