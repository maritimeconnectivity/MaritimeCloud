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
package net.maritimecloud.internal.net.messages;

import java.io.IOException;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.messages.RelayMessage;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class ClientRelayedMessage extends ConnectionOldMessage implements RelayMessage {

    String destination;

    String source;

    /**
     * @param messageType
     */
    public ClientRelayedMessage(MessageType messageType) {
        super(messageType);
    }

    public ClientRelayedMessage(MessageType messageType, TextMessageReader pr) throws IOException {
        super(messageType, pr);
        this.source = pr.takeString();
        this.destination = pr.takeString();
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @return the src
     */
    public String getSource() {
        return source;
    }

    public MaritimeId getSourceId() {
        return MaritimeId.create(getSource());
    }

    public ClientRelayedMessage setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public ClientRelayedMessage setSource(String source) {
        this.source = source;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    protected final void write0(TextMessageWriter w) {
        w.writeString(source);
        w.writeString(destination);
        write1(w);
    }

    public abstract ClientRelayedMessage cloneIt();

    protected abstract void write1(TextMessageWriter w);
}
