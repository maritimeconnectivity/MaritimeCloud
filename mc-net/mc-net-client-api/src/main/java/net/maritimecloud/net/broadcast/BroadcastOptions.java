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

import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.util.geometry.Area;

/**
 * Various options that can be used when broadcasting messages via
 * {@link MaritimeCloudClient#broadcast(BroadcastMessage, BroadcastOptions)}.
 * 
 * @author Kasper Nielsen
 */
public class BroadcastOptions {

    private Area area;

    /** The distance in meters that the broadcast should be delivered. */
    private int distance = 10000;

    // boolean fastAck;

    // int keepAliveSeconds;

    // ack of id, time, position

    // Targets
    // Actors within a defined area, for example, Denmark.
    // Actors within a defined group? For example, ships under danish flag
    // Specific type, for example, ships, VTS center

    // Broadcast policy.
    // VTS centers can only broadcast in the country they are registered
    //

    boolean receiverAck;

    public BroadcastOptions() {}

    BroadcastOptions(BroadcastOptions options) {
        this.distance = options.distance;
        this.receiverAck = options.receiverAck;
        this.area = options.area;
    }

    /**
     * @return the area
     */
    public Area getBroadcastArea() {
        return area;
    }

    public final int getBroadcastRadius() {
        return distance;
    }

    /**
     * Returns a new immutable copy of these options. Trying to invoke any of the setter methods on the returned object
     * will result in {@link UnsupportedOperationException}.
     * 
     * @return a new immutable copy of these options
     */
    public BroadcastOptions immutable() {
        return new Immutable(this);
    }

    /**
     * @return the receiverAck
     */
    public final boolean isReceiverAckEnabled() {
        return receiverAck;
    }

    /**
     * Sets the area for which the broadcast will be visible to other actors. The area is not relative to the current
     * position of the client. Any area set by this method will override any radius set by
     * {@link #setBroadcastRadius(int)}.
     * 
     * @param area
     *            the area to broadcast to
     * @return this option object
     */
    public BroadcastOptions setBroadcastArea(Area area) {
        this.area = area;
        return this;
    }

    /**
     * Sets the radius (in meters) for which the broadcast will be visible to other actors. The radius is relative to
     * the current position of the client. If an area is set via {@link #setBroadcastArea(Area)} this will override any
     * radius set by this method.
     * 
     * @param radius
     *            the radius in meters
     * @return this option object
     */
    public BroadcastOptions setBroadcastRadius(int radius) {
        this.distance = radius;
        return this;
    }


    /**
     * Sets whether or not each actor that receives the broadcast will send an ack back to the broadcaster.
     * 
     * @param receiverAck
     *            the receiverAck to set
     */
    public BroadcastOptions setReceiverAckEnabled(boolean receiverAck) {
        this.receiverAck = receiverAck;
        return this;
    }

    /** An immutable version of BroadcastOptions. */
    static class Immutable extends BroadcastOptions {
        /**
         * Creates a new immutable copy of the specified broadcast options.
         * 
         * @param options
         *            the options to create an immutable copy of
         */
        Immutable(BroadcastOptions options) {
            super(options);
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastOptions immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastOptions setBroadcastRadius(int radius) {
            throw new UnsupportedOperationException("options are immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastOptions setBroadcastArea(Area area) {
            throw new UnsupportedOperationException("options are immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastOptions setReceiverAckEnabled(boolean receiverAck) {
            throw new UnsupportedOperationException("options are immutable");
        }
    }
}
