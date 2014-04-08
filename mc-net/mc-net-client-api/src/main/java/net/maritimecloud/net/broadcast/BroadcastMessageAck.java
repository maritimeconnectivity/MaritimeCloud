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

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.util.geometry.PositionTime;

/**
 * An acknowledgment that can be send every time a broadcast is received by an actor.
 * 
 * @author Kasper Nielsen
 */
public interface BroadcastMessageAck {

    /**
     * Returns the id of the actor that received the broadcast.
     *
     * @return the id of the actor that received the broadcast
     */
    MaritimeId getId();

    /**
     * Returns the position and time when the actor received the message.
     *
     * @return the position and time when the actor received the message
     */
    PositionTime getPosition();
}
