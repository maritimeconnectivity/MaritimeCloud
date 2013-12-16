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

/**
 * A broadcast subscription is created every time a {@link BroadcastListener} is registered via
 * {@link MaritimeCloudClient#broadcastListen(Class, BroadcastListener)}.
 * 
 * @author Kasper Nielsen
 */
public interface BroadcastSubscription {

    /** Stops receiving messages on the listener. */
    void cancel();

    /**
     * Returns the channel we are listening on.
     * 
     * @return the channel we are listening on
     */
    String getChannel();

    /**
     * Returns the number of messages received for the registered listener.
     * 
     * @return the number of messages received
     */
    long getNumberOfReceivedMessages();
}
