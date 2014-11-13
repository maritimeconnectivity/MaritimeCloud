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
package net.maritimecloud.net.mms;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.util.geometry.Area;

/**
 *
 * @author Kasper Nielsen
 */
public class MmsBroadcastOptions {

    private Area area;

    private Consumer<? super MessageHeader> consumer;

    private int radius = 50000;

    public MmsBroadcastOptions() {}

    MmsBroadcastOptions(MmsBroadcastOptions options) {
        this.area = options.area;
        this.consumer = options.consumer;
        this.radius = options.radius;
    }

    /**
     * @return the area
     */
    public Area getArea() {
        return area;
    }

    /**
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @return the consumer
     */
    public Consumer<? super MessageHeader> getRemoteReceive() {
        return consumer;
    }

    /**
     * The specified consumer will be invoked every time a remote party has received the broadcast message.
     * <p>
     * This is done on a best effort basis. For example, a remote actor might loose Internet connectivity after having
     * received the broadcast but before an acknowledge message can be send.
     *
     * @param consumer
     *            the consumer of each acknowledgment
     * @throws UnsupportedOperationException
     *             if the messages has not been sent with receiver acknowledgment enabled
     */
    public MmsBroadcastOptions onRemoteReceive(Consumer<? super MessageHeader> consumer) {
        this.consumer = requireNonNull(consumer);
        return this;
    }

    /**
     * Sets the area for which the broadcast will be visible to other actors. The area is not relative to the current
     * position of the client. Any area set by this method will override any radius set by {@link #toArea(int)}.
     *
     * @param area
     *            the area to broadcast to
     * @return this option object
     */
    public MmsBroadcastOptions toArea(Area area) {
        this.area = area;
        return this;
    }

    /**
     * Sets the radius (in meters) for which the broadcast will be visible to other actors. The radius is relative to
     * the current position of the client. If an area is set via {@link #toArea(Area)} this will override any radius set
     * by this method.
     * <p>
     * Invoking this method is equivalent to invoking {@link #toArea(Area)} with a circle with the specified radius.
     *
     * @param radius
     *            the radius in meters
     * @return this option object
     */
    public MmsBroadcastOptions toArea(int radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("radius must be non-negative, was " + radius);
        }
        area = null;
        this.radius = radius;
        return this;
    }

    public MmsBroadcastOptions immutable() {
        return new Immutable(this);
    }

    static class Immutable extends MmsBroadcastOptions {
        Immutable(MmsBroadcastOptions o) {
            super(o);
        }

        public MmsBroadcastOptions immutable() {
            return this;
        }
    }
}
