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
package net.maritimecloud.net.broadcast2;

import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.util.geometry.Area;

/**
 * A broadcast subscription is created every time a {@link BroadcastListener} is registered via
 * {@link MaritimeCloudClient#broadcastListen(Class, BroadcastListener)} or
 * {@link MaritimeCloudClient#broadcastListen(Class, BroadcastListener, net.maritimecloud.util.geometry.Area)}.
 * <p>
 * Sometimes registering a
 *
 * @author Kasper Nielsen
 */
public interface BroadcastSubscription {

    /** Stops receiving messages on the listener. */
    void cancel();

    /**
     * Returns the area for which the client is listening for broadcast. Or <code>null</code> if the current position is
     * used.
     *
     * @return the area for which the client is listening for broadcast. Or <code>null</code> if the current position is
     *         used
     * @see BroadcastListenOptions#setArea(Area)
     */
    Area getArea();

    /**
     * Returns the channel we are listening on.
     *
     * @return the channel we are listening on
     */
    String getChannel();

    /**
     * Returns the number of messages received by the listener.
     *
     * @return the number of messages received
     */
    long getNumberOfReceivedMessages();

}
// Tanken er at vi dynamisk registere nogle broadcasts
//
// /** The current state of the subscription. */
// enum State {
//
// /**
// * The initial state of this registration. When created at the client but before the server has registered the
// * service registration.
// */
// INITIATED,
//
// /** The service has been registered with server. And remote clients may now invoke the service */
// REGISTERED,
//
// /**
// * The client no longer offers the service. Remote clients attempting to invoke the service will fail with a
// * {@link ServiceUnavailableException}.
// */
// CANCELLED;
// }
