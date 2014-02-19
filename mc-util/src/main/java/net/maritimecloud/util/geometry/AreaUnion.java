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

import java.util.Random;

/**
 * A union of multiple areas
 * 
 * @author Kasper Nielsen
 */
class AreaUnion extends Area {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    final Area[] areas;

    /**
     * @param cs
     */
    AreaUnion(Area... a) {
        super(a[0].cs);
        areas = a;
    }

    /** {@inheritDoc} */
    @Override
    public double geodesicDistanceTo(Element other) {
        double distance = Double.MAX_VALUE;
        for (Area a : areas) {
            distance = Math.min(distance, a.geodesicDistanceTo(other));
        }
        return distance;
    }

    /** {@inheritDoc} */
    @Override
    public double rhumbLineDistanceTo(Element other) {
        double distance = Double.MAX_VALUE;
        for (Area a : areas) {
            distance = Math.min(distance, a.rhumbLineDistanceTo(other));
        }
        return distance;
    }

    /** {@inheritDoc} */
    @Override
    public BoundingBox getBoundingBox() {
        // Mht til dato linjen. Maa det vaere den mindste box der kan laves
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Position getRandomPosition(Random random) {
        // Den er svaerd fordi det skal vaere i forhold til arealet.
        // Vi skal have en getAreaSize//
        // AliasedMethodTable http://www.keithschwarz.com/darts-dice-coins/
        // bliver noedt til at have et volatile temp object her der gemmer da shit
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(Area other) {
        for (Area a : areas) {
            if (a.intersects(other)) {
                return true;
            }
        }
        return false;
    }
}
