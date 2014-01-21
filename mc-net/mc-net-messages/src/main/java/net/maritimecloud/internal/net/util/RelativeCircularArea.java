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
package net.maritimecloud.internal.net.util;

import java.util.Random;

import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.BoundingBox;
import net.maritimecloud.util.geometry.CoordinateSystem;
import net.maritimecloud.util.geometry.Position;

/**
 * A "fake" relative area.
 * 
 * @author Kasper Nielsen
 */
public class RelativeCircularArea extends Area {
    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The radius. */
    private final double radius;

    /**
     * @param cs
     */
    public RelativeCircularArea(double radius) {
        super(CoordinateSystem.CARTESIAN);
        this.radius = radius;

    }

    /** {@inheritDoc} */
    @Override
    public BoundingBox getBoundingBox() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /** {@inheritDoc} */
    @Override
    public Position getRandomPosition(Random r) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(Area other) {
        throw new UnsupportedOperationException();
    }

}
