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
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

/**
 * A dispatched message is returned when sending a message asynchronously.
 *
 * @author Kasper Nielsen
 */
public interface DispatchedMessage {

    /**
     * Returns a unique id of the message that was send.
     *
     * @return a unique id of the message that was send
     */
    Binary getMessageId();

    /**
     * Returns any position that was attached to the message being sent. Clients that do not have a position reader
     * attached will not attach a position to the header of the message and returns <code>null</code>.
     *
     * @return any position that was attached to the message being sent
     */
    Position getPosition();

    /**
     * Returns the time stamp that was attached to the message being sent.
     *
     * @return the time stamp that was attached to the message being sent
     */
    Timestamp getTime();

    /**
     * If the underlying protocol used for sending the message uses a central relaying server, for example MMS. This
     * method can be used to determine when the message have been received by the MMS server. This is done on a best
     * effort basis. The message might, but rarely does, be delivered to remote clients before the returned future
     * completes.
     *
     * @return an acknowledgement object that can be used to execute actions when the message has been delivered to a
     *         relay server
     *
     * @throws UnsupportedOperationException
     *             if the underlying communication mechanism does not make of a central relaying server.
     */
    Acknowledgement relayed();
}
