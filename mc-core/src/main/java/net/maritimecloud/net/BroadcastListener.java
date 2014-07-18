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
package net.maritimecloud.net;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.util.geometry.Position;

/**
 * A callback interface for receiving broadcast messages of a specific type.
 *
 * @author Kasper Nielsen
 */
@FunctionalInterface
public interface BroadcastListener<T extends BroadcastMessage> {

    /**
     * Invoked whenever a broadcast message was received.
     *
     * @param context
     *            information about the sender of the broadcast
     * @param broadcast
     *            the message that was received
     */
    void onMessage(BroadcastListener.Context context, T broadcast);

    /** Information about the broadcast that was received. */
    interface Context {

        /**
         * Returns the identity of the party that send the broadcast.
         *
         * @return the identity of the party that send the broadcast
         */
        MaritimeId getBroadcaster();

        /**
         * If the sending party has a position, returns said position. Otherwise returns <code>null</code>.
         *
         * @return if the sending party has a position, returns said position
         */
        Position getBroadcasterPosition();
    }
}
