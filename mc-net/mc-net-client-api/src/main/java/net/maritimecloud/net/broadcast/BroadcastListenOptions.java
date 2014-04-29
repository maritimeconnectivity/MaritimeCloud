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
package net.maritimecloud.net.broadcast;

import net.maritimecloud.util.geometry.Area;

/**
 * 
 * @author Kasper Nielsen
 */
public class BroadcastListenOptions {

    Area area;

    /**
     * Sets the area for which the broadcast will be visible to other actors. The area is not relative to the current
     * position of the client. Any area set by this method will override any radius set by
     * {@link #setBroadcastRadius(int)}.
     * 
     * @param area
     *            the area to listen for broadcasts within
     * @return this option object
     */
    public BroadcastListenOptions setArea(Area area) {
        this.area = area;
        return this;
    }
}
