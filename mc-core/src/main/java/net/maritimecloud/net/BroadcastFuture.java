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

import net.maritimecloud.util.Binary;
import net.maritimecloud.util.geometry.PositionTime;

/**
 * A future returned when sending a broadcast. This future can be used if supported by the underlying transport
 * mechanism.
 *
 * @author Kasper Nielsen
 */
public interface BroadcastFuture {

    /**
     * A future that can be used to determine when the message have been received by the server. This is done on a best
     * effort basis. The broadcast message might be delivered to remote clients before the returned future completes.
     *
     * @return a completion stage that can be used to execute actions when the broadcast has been delivered to a central
     *         server
     *
     * @throws UnsupportedOperationException
     *             if the underlying communication mechanism does not support acknowledgement of the broadcast message.
     */
    Acknowledgement acknowledgement();

    /**
     * Returns a unique hash of the broadcast message that was send.
     *
     * @return a unique hash of the broadcast message that was send.
     */
    Binary getMessageHash();

    /**
     * Returns the position time that was attached to the broadcast being sent.
     *
     * @return the position time that was attached to the broadcast being sent
     */
    // Separate Time + Postion me thinkgs
    PositionTime getPositionTime();
}
