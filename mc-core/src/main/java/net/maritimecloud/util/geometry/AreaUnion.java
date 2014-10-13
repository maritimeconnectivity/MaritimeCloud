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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;

/**
 * A union of multiple areas
 *
 * @author Kasper Nielsen
 */
class AreaUnion extends Area {

    /** A serializer for area unions. */
    public static final MessageSerializer<AreaUnion> SERIALIZER = new MessageSerializer<AreaUnion>() {

        /** {@inheritDoc} */
        @Override
        public AreaUnion read(MessageReader reader) throws IOException {
            List<Area> list = reader.readList(1, "areas", Area.SERIALIZER);
            return new AreaUnion(list.toArray(new Area[list.size()]));
        }

        public void write(AreaUnion message, MessageWriter writer) throws IOException {
            writer.writeList(1, "areas", Arrays.asList(message.areas), Area.SERIALIZER);
        }
    };

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The combined areas. */
    final Area[] areas;

    /**
     * Creates a new AreaUnion.
     *
     * @param area
     *            the areas to combine
     */
    AreaUnion(Area... area) {
        ArrayList<Area> areas = new ArrayList<>();
        for (Area ar : area) {
            requireNonNull(ar);
            if (ar instanceof AreaUnion) {
                areas.addAll(Arrays.asList(((AreaUnion) ar).areas));
            } else {
                areas.add(ar);
            }
        }
        this.areas = areas.toArray(new Area[areas.size()]);
    }

    AreaUnion(List<? extends Area> areas) {
        this(areas.toArray(new Area[areas.size()]));
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Position position) {
        for (Area a : areas) {
            if (a.contains(position)) {
                return true;
            }
        }
        return false;
    }

    private boolean equals(AreaUnion other) {
        if (areas.length == other.areas.length) {
            List<Area> others = Arrays.asList(other.areas.clone());
            for (Area area : areas) {
                boolean found = false;
                for (int i = 0; i < others.size(); i++) {
                    Area otherArea = others.get(i);
                    if (area == otherArea || area.equals(otherArea)) {
                        found = true;
                        others.remove(i);
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof AreaUnion && equals((AreaUnion) other);
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getBoundingBox() {
        // make boundry boxes for everyone
        // calculate vertical distances between each boundary box.
        // Combine boxes that are closes, recalculate


        // for each boundry box
        // Mht til dato linjen. Maa det vaere den mindste box der kan laves
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public Position getRandomPosition(Random random) {
        // Implementations
        // 1)
        // bounding box og saa blev ved indtil vi finder en position
        // Den er svaerd fordi det skal vaere i forhold til arealet.

        // 2)
        // Vi skal have en getAreaSize og saa bruge en
        // AliasedMethodTable http://www.keithschwarz.com/darts-dice-coins/
        // We lazy calculater bliver noedt til at have et volatile temp object her der gemmer da shit
        throw new UnsupportedOperationException("getRandomPosition is not currently supported");
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        if (areas.length == 0) {
            return 0;
        }
        // hashCode is commutative
        int hashCode = areas[0].hashCode();
        for (int i = 1; i < areas.length; i++) {
            hashCode = hashCode ^ areas[i].hashCode();
        }
        return hashCode;
    }

    /** {@inheritDoc} */
    public AreaUnion immutable() {
        return this;
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


// /** {@inheritDoc} */
// @Override
// public double geodesicDistanceTo(Element other) {
// double distance = Double.MAX_VALUE;
// for (Area a : areas) {
// distance = Math.min(distance, a.geodesicDistanceTo(other));
// }
// return distance;
// }
// /** {@inheritDoc} */
// @Override
// public double rhumbLineDistanceTo(Element other) {
// double distance = Double.MAX_VALUE;
// for (Area a : areas) {
// distance = Math.min(distance, a.rhumbLineDistanceTo(other));
// }
// return distance;
// }
