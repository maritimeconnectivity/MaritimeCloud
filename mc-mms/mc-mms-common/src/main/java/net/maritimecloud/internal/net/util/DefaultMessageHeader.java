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
package net.maritimecloud.internal.net.util;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Map;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

/**
 * Properties conveyed to receivers of broadcast messages.
 *
 * @author Kasper Nielsen
 */
public class DefaultMessageHeader implements MessageHeader {

    final Map<String, Object> context;

    /** The id of the ship sending the broadcast. */
    private final MaritimeId id;

    /** The id of the message. */
    private final Binary messageId;

    /** The position when the message was created (optional). */
    private final Position position;

    /** The time when the message was created (optional). */
    private final Timestamp timestamp;

    public DefaultMessageHeader(MaritimeId id, Binary messageId, Timestamp timestamp, Position position) {
        this(id, messageId, timestamp, position, Collections.emptyMap());
    }

    public DefaultMessageHeader(MaritimeId id, Binary messageId, Timestamp timestamp, Position position,
            Map<String, Object> context) {
        this.id = requireNonNull(id);
        this.messageId = requireNonNull(messageId);
        this.timestamp = timestamp;
        this.position = position;
        this.context = context;
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> context() {
        return context;
    }

    /** {@inheritDoc} */
    @Override
    public Binary getMessageId() {
        return messageId;
    }

    /** {@inheritDoc} */
    @Override
    public MaritimeId getSender() {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public Position getSenderPosition() {
        return position;
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp getSenderTime() {
        return timestamp;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "id=" + id + ", hash=" + messageId + ", timestamp=" + timestamp + ", position=" + position;
    }
}
