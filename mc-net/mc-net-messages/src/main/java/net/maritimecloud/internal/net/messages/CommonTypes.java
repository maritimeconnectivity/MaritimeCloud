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
package net.maritimecloud.internal.net.messages;

import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.BoundingBox;
import net.maritimecloud.util.geometry.Circle;

/**
 * 
 * @author Kasper Nielsen
 */
public class CommonTypes {

    public static void writeArea(Area area, TextMessageWriter w) {
        // always geodesic
        if (area instanceof Circle) {
            Circle c = (Circle) area;
            w.writeInt(0);
            w.writePosition(c.getCenter());
            w.writeDouble(c.getRadius());
        } else if (area instanceof BoundingBox) {
            BoundingBox bb = (BoundingBox) area;
            w.writeInt(1);
            w.writePosition(bb.getUpperLeft());
            w.writePosition(bb.getLowerRight());
        } else {
            throw new UnsupportedOperationException("Only circles and bounding boxes supported, was " + area.getClass());
        }
    }
}
