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

import java.io.Serializable;

//This is an interface to 
public interface Element extends Serializable {
    // We should only need 1 or 2 of these

    double distanceTo(Element position, CoordinateSystem system);

    /**
     * Returns the geodetic (great circle) distance from this element to the specified element in meters.
     * 
     * @param other
     *            the other element
     * @return the distance in meters to the other element
     */
    double geodesicDistanceTo(Element other);

    double rhumbLineDistanceTo(Element other);
}


/**
 * Describe the relationship between the two shapes. For example
 * <ul>
 * <li>this is WITHIN other</li>
 * <li>this CONTAINS other</li>
 * <li>this is DISJOINT other</li>
 * <li>this INTERSECTS other</li>
 * </ul>
 * Note that a Shape implementation may choose to return INTERSECTS when the true answer is WITHIN or CONTAINS for
 * performance reasons. If a shape does this then it <i>must</i> document when it does. Ideally the shape will not do
 * this approximation in all circumstances, just sometimes.
 * <p />
 * If the shapes are equal then the result is CONTAINS (preferred) or WITHIN.
 */
// SpatialRelation relationTo(Element otherShape);
