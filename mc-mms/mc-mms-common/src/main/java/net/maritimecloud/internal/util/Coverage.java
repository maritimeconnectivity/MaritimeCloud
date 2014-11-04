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
package net.maritimecloud.internal.util;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.Position;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class Coverage {

    public static Coverage ALL = new Coverage() {
        @Override
        public boolean isCovered(Position p) {
            return true;
        }
    };

    public abstract boolean isCovered(Position p);

    public static class RelativeCircleCoverage extends Coverage {
        final Supplier<Position> center;

        final int radiusInMeters;

        /**
         * @param radiusInMeters
         * @param center
         */
        public RelativeCircleCoverage(int radiusInMeters, Supplier<Position> center) {
            this.radiusInMeters = radiusInMeters;
            this.center = requireNonNull(center);
        }

        /** {@inheritDoc} */
        @Override
        public boolean isCovered(Position p) {
            return Circle.create(center.get(), radiusInMeters).contains(p);
        }

    }

    public static class StaticAreaCoverage extends Coverage {
        private final Area area;

        public StaticAreaCoverage(Area area) {
            this.area = requireNonNull(area);
        }

        /**
         * @return the area
         */
        public Area getArea() {
            return area;
        }

        /** {@inheritDoc} */
        @Override
        public boolean isCovered(Position p) {
            return area.contains(p);
        }
    }

}
