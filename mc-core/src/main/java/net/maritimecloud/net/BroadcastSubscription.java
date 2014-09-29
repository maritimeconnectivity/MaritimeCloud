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

import net.maritimecloud.util.geometry.Area;

/**
 * A broadcast subscription is created every time a {@link BroadcastListener} is registered.
 *
 * @author Kasper Nielsen
 */
public interface BroadcastSubscription {

    /** Stops receiving any more broadcast messages for the registered listener. */
    void cancel();

    /**
     * Returns the area for which the client is listening for broadcast. If this subscription has been created using a
     * distance relative to a moving position. The returned area will change whenever the position is updated.
     * <p>
     * Returns <code>null</code> if the underlying implementation does not filter broadcast messages based on area.s
     *
     * @return the area for which the client is listening for broadcast.
     */
    Area getArea();

    /**
     * Returns the type of broadcast message we are listening for.
     *
     * @return the type of broadcast message we are listening for
     */
    Class<? extends BroadcastMessage> getBroadcastMessageType();

    /**
     * Returns the number of messages received by the registered listener.
     *
     * @return the number of messages received
     */
    long getNumberOfReceivedMessages();
}
