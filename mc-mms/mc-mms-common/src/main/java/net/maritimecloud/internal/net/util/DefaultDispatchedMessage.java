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
import net.maritimecloud.internal.util.StoredMessage;
import net.maritimecloud.net.Acknowledgement;
import net.maritimecloud.net.DispatchedMessage;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

/**
 * The default implementation of DispatchedMessage.
 *
 * @author Kasper Nielsen
 */
public class DefaultDispatchedMessage implements DispatchedMessage, StoredMessage {

    protected final DefaultAcknowledgement relayed;

    protected final Binary hash;

    protected final Position position;

    protected final Timestamp timestamp;

    protected final long now = System.nanoTime();

    public DefaultDispatchedMessage(Binary hash, Position position, Timestamp timestamp,
            DefaultAcknowledgement acknowledgement) {
        this.hash = requireNonNull(hash);
        this.position = requireNonNull(position);
        this.timestamp = requireNonNull(timestamp);
        this.relayed = requireNonNull(acknowledgement);
    }

    /** {@inheritDoc} */
    @Override
    public Binary getMessageId() {
        return hash;
    }

    /** {@inheritDoc} */
    @Override
    public Position getPosition() {
        return position;
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp getTime() {
        return timestamp;
    }

    /** {@inheritDoc} */
    @Override
    public Acknowledgement relayed() {
        return relayed;
    }

    /** {@inheritDoc} */
    @Override
    public long getTimestamp() {
        return now;
    }
}
