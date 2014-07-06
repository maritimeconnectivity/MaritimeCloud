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

package net.maritimecloud.util.geometry;


import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;
import static java.lang.StrictMath.toRadians;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.maritimecloud.core.message.MessageWriter;

/**
 * This class holds the defining parameters for en ellipse.
 *
 * The location of the ellipse can optionally be offset from the geodetic reference point by dx, dy meters.
 */
public final class Ellipse extends Area {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Length of half axis in direction theta (in meters) */
    private final double alpha;

    /** Length of half axis in direction orthogonal to theta (in meters) */
    private final double beta;

    private final CoordinateConverter coordinateConverter;

    /** Location offset of X coordinate from geodetic reference (in meters). */
    private final double dx;

    /** {@inheritDoc} */
    public Ellipse immutable() {
        return this;
    }

    /** Location offset of Y coordinate from geodetic reference (in meters). */
    private final double dy;

    /** The geodetic point on Earth corresponding to (dx,dy) = (0,0) */
    private final Position geodeticReference;

    /**
     * Direction of half axis alpha measured in Cartesian degrees; 0 degrees is parallel with the increasing direction
     * of the X axis.
     */
    private final double thetaDeg;

    /**
     * Create an ellipse with center in the geodetic reference point.
     *
     * @param geodeticReference
     *            The center point of the ellipse
     * @param alpha
     *            Length of half axis in direction theta (in meters)
     * @param beta
     *            Length of half axis in direction orthogonal to theta (in meters)
     * @param thetaDeg
     *            Direction of half axis alpha measured in degrees; 0 degrees is parallel with the increasing direction
     *            of the cartesian X axis.
     * @param cs
     */
    public Ellipse(Position geodeticReference, double alpha, double beta, double thetaDeg) {
        this.geodeticReference = geodeticReference;
        this.coordinateConverter = geodeticReference == null ? null : new CoordinateConverter(
                geodeticReference.longitude, geodeticReference.latitude);
        this.dx = 0.0;
        this.dy = 0.0;
        this.alpha = alpha;
        this.beta = beta;
        this.thetaDeg = thetaDeg;
    }

    /**
     * Create an ellipse offset dx, dy meters from the geodetic reference point.
     *
     * @param geodeticReference
     *            The position, from which the center of the ellipse is offset by (dx, dy) meters.
     * @param dx
     * @param dy
     * @param alpha
     *            Length of half axis in direction theta (in meters)
     * @param beta
     *            Length of half axis in direction orthogonal to theta (in meters)
     * @param thetaDeg
     *            Direction of half axis alpha measured in degrees; 0 degrees is parallel with the increasing direction
     *            of the cartesian X axis.
     * @param cs
     */
    public Ellipse(Position geodeticReference, double dx, double dy, double alpha, double beta, double thetaDeg) {
        this.geodeticReference = geodeticReference;
        this.coordinateConverter = geodeticReference == null ? null : new CoordinateConverter(
                geodeticReference.longitude, geodeticReference.latitude);
        this.dx = dx;
        this.dy = dy;
        this.alpha = alpha;
        this.beta = beta;
        this.thetaDeg = thetaDeg;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Position position) {
        throw new UnsupportedOperationException();
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getBoundingBox() {
        throw new UnsupportedOperationException();
    }

    public Position getGeodeticReference() {
        return geodeticReference;
    }

    public double getMajorAxisGeodeticHeading() {
        return CoordinateConverter.cartesian2compass(thetaDeg);
    }

    /** {@inheritDoc} */
    @Override
    public Position getRandomPosition(Random random) {
        throw new UnsupportedOperationException();
    }

    public double getThetaDeg() {
        return thetaDeg;
    }

    public double getX() {
        return dx;
    }

    public double getY() {
        return dy;
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(Area other) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns true if two safety zones intersect.
     *
     * @param otherEllipse
     *            the other safety zone.
     * @return
     */
    public boolean intersects(Ellipse otherEllipse) {
        // TODO must have equal geodeticReference to compare

        final double h1x = cos(toRadians(thetaDeg));
        final double h1y = sin(toRadians(thetaDeg));

        final double h2x = cos(toRadians(otherEllipse.thetaDeg));
        final double h2y = sin(toRadians(otherEllipse.thetaDeg));

        final double vx = otherEllipse.dx - dx;
        final double vy = otherEllipse.dy - dy;

        final double d = sqrt(vx * vx + vy * vy);

        boolean intersects = true;

        final double SMALL_NUM = 0.1;

        if (d > SMALL_NUM) {
            final double cosb1 = (h1x * vx + h1y * vy) / (sqrt(h1x * h1x + h1y * h1y) * d);
            final double sinb1 = (h1x * vy - h1y * vx) / (sqrt(h1x * h1x + h1y * h1y) * d);
            final double d1 = sqrt(alpha * alpha * beta * beta
                    / (alpha * alpha * sinb1 * sinb1 + beta * beta * cosb1 * cosb1));
            final double cosb2 = (h2x * vx + h2y * vy) / (sqrt(h2x * h2x + h2y * h2y) * d);
            final double sinb2 = (h2x * vy - h2y * vx) / (sqrt(h2x * h2x + h2y * h2y) * d);
            final double d2 = sqrt(otherEllipse.alpha
                    * otherEllipse.alpha
                    * otherEllipse.beta
                    * otherEllipse.beta
                    / (otherEllipse.alpha * otherEllipse.alpha * sinb2 * sinb2 + otherEllipse.beta * otherEllipse.beta
                            * cosb2 * cosb2));
            if (d - d1 - d2 < 0.0) {
                intersects = true;
            } else {
                intersects = false;
            }
        }

        return intersects;
    }

    /**
     * Sample the perimeter along the ellipse in 'n' points, and return a list of positions all located and evenly
     * distributed on the perimeter. This is useful e.g. to draw the perimeter on a chart using geodetic coordinates.
     *
     * @param n
     *            the number of perimeter samples to return.
     * @return a list of positions on the perimeter.
     */
    public List<Position> samplePerimeter(int n) {
        // Sample ellipse scaled to meters
        List<Point> unitPerimeter = new ArrayList<>(n);
        double pi2 = 2 * Math.PI;
        double dtheta = pi2 / n;
        double theta = 0.0;
        do {
            unitPerimeter.add(new Point(alpha * cos(theta), beta * sin(theta)));
            theta += dtheta;
        } while (theta < pi2);

        // Rotate ellipse to thetaDeg
        List<Point> rotatedPerimeter = new ArrayList<>(n);
        for (Point point : unitPerimeter) {
            Point pr = point.rotate(Point.ORIGIN, thetaDeg).translate(dx, dy);
            rotatedPerimeter.add(pr);
        }

        // Convert to geodetic
        List<Position> perimeter = new ArrayList<>(n);
        for (Point point : rotatedPerimeter) {
            double lon = coordinateConverter.x2Lon(point.getX(), point.getY());
            double lat = coordinateConverter.y2Lat(point.getX(), point.getY());
            perimeter.add(Position.create(lat, lon));
        }

        return perimeter;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Ellipse{");
        sb.append("geodeticReference=").append(geodeticReference);
        sb.append(", dx=").append(dx);
        sb.append(", dy=").append(dy);
        sb.append(", alpha=").append(alpha);
        sb.append(", beta=").append(beta);
        sb.append(", thetaDeg=").append(thetaDeg);
        sb.append('}');
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {}
}
