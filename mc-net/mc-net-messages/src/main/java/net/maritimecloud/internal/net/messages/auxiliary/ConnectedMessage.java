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
package net.maritimecloud.internal.net.messages.auxiliary;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.internal.net.messages.TransportMessage;

/**
 *
 * @author Kasper Nielsen
 */
public class ConnectedMessage extends TransportMessage {

    private final String connectionId;

    public final long lastReceivedMessageId;

    public ConnectedMessage(String connectionId, long lastReceivedMessageId) {
        super(MessageType.CONNECTED);
        this.connectionId = requireNonNull(connectionId);
        this.lastReceivedMessageId = lastReceivedMessageId;
    }

    public ConnectedMessage(TextMessageReader pr) throws IOException {
        this(pr.takeString(), pr.takeLong());
    }

    public String getConnectionId() {
        return connectionId;
    }

    /**
     * @return the lastReceivedMessageId
     */
    public long getLastReceivedMessageId() {
        return lastReceivedMessageId;
    }

    /** {@inheritDoc} */
    @Override
    public void write(TextMessageWriter w) {
        w.writeString(connectionId);
        w.writeLong(lastReceivedMessageId);
    }
}
