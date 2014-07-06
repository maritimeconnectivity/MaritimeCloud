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

import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializer;
import net.maritimecloud.core.message.MessageWriter;

/**
 * A union of multiple areas
 *
 * @author Kasper Nielsen
 */
class AreaUnion extends Area {

    public static final MessageSerializer<AreaUnion> SERIALIZER = new MessageSerializer<AreaUnion>() {

        /** {@inheritDoc} */
        @Override
        public AreaUnion read(MessageReader reader) throws IOException {
            return readFrom(reader);
        }

        public void write(AreaUnion message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    };

    /** {@inheritDoc} */
    public AreaUnion immutable() {
        return this;
    }

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    final Area[] areas;

    AreaUnion(List<? extends Area> areas) {
        this(areas.toArray(new Area[areas.size()]));
    }

    /**
     * @param cs
     */
    AreaUnion(Area... a) {
        ArrayList<Area> areas = new ArrayList<>();
        for (Area ar : a) {
            requireNonNull(ar);
            if (ar instanceof AreaUnion) {
                areas.addAll(Arrays.asList(((AreaUnion) ar).areas));
            } else {
                areas.add(ar);
            }
        }
        this.areas = areas.toArray(new Area[areas.size()]);
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
            System.out.println(others);
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

    public boolean equals(Object other) {
        return other == this || other instanceof AreaUnion && equals((AreaUnion) other);
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

    /** {@inheritDoc} */
    @Override
    public Rectangle getBoundingBox() {
        // make boundry boxes for everyone
        // calculate vertical distances between each boundary box.
        // Combine boxes that are closes, recalculate


        // for each boundry box
        // Mht til dato linjen. Maa det vaere den mindste box der kan laves
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Position getRandomPosition(Random random) {
        // bounding box og saa blev ved indtil vi finder en position
        // Den er svaerd fordi det skal vaere i forhold til arealet.
        // Vi skal have en getAreaSize//
        // AliasedMethodTable http://www.keithschwarz.com/darts-dice-coins/
        // bliver noedt til at have et volatile temp object her der gemmer da shit
        throw new UnsupportedOperationException("getRandomPosition is not current supported");
    }

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
    @Override
    public boolean intersects(Area other) {
        for (Area a : areas) {
            if (a.intersects(other)) {
                return true;
            }
        }
        return false;
    }


    // /** {@inheritDoc} */
    // @Override
    // public double rhumbLineDistanceTo(Element other) {
    // double distance = Double.MAX_VALUE;
    // for (Area a : areas) {
    // distance = Math.min(distance, a.rhumbLineDistanceTo(other));
    // }
    // return distance;
    // }

    /** {@inheritDoc} */
    @Override
    public void writeTo(MessageWriter w) throws IOException {
        // ArrayList<Area> l = new ArrayList<>(areas.length);
        // for (Area a : areas) {
        // l.add((Area) a.areaWriter());
        // }
        w.writeList(1, "areas", Arrays.asList(areas), Area.SERIALIZER);
    }

    /** {@inheritDoc} */
    public static AreaUnion readFrom(MessageReader r) throws IOException {
        List<Area> list = r.readList(1, "areas", Area.SERIALIZER);
        return new AreaUnion(list.toArray(new Area[list.size()]));
    }
}
