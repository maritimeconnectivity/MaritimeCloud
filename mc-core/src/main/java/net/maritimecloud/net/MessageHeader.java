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

import java.util.Collections;
import java.util.Map;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

/**
 * The header of a received message.
 *
 * @author Kasper Nielsen
 */
public interface MessageHeader {

    /**
     * Implementations <b>might</b> choose to provide additional properties to consumers of messages.
     *
     * @return a map containing additional properties about the message.
     */
    default Map<String, Object> context() {
        return Collections.emptyMap();
    }

    /**
     * Returns a unique id of the transmitted message.
     * <p>
     * This hash is calculated as a combination of the name of the remote actor who sent the message. The position and
     * time of the actor who sent the message. The name of the broadcast type. And the actual binary contents of the
     * broadcast message.
     *
     * @return a unique hash for the specified message
     */
    Binary getMessageId();

    /**
     * Returns the identity of the party that send the message.
     *
     * @return the identity of the party that send the message
     */
    MaritimeId getSender();

    /**
     * If the sending party has a position, returns said position. Otherwise returns <code>null</code>.
     *
     * @return if the sending party has a position, returns said position
     */
    Position getSenderPosition();

    /**
     * Returns the timestamp of the message.
     *
     * @return the timestamp of the message
     */
    Timestamp getSenderTime();
}
