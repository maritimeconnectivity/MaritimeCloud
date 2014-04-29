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

import static java.util.Objects.requireNonNull;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractTransportMessage implements TransportMessage {

    /** The type of message. */
    private final MessageType messageType;


    /**
     * Creates a new AbstractMessage.
     *
     * @param messageType
     *            the type of message
     * @throws NullPointerException
     *             if the specified message type is null
     */
    protected AbstractTransportMessage(MessageType messageType) {
        this.messageType = requireNonNull(messageType);
    }

    public String toText() {
        TextMessageWriter w = new TextMessageWriter();
        // w.writeInt(getMessageType().type);
        write(w);
        String s = w.sb.append("]").toString();
        s = messageType.type + ":" + s;
        return s;
    }

    protected abstract void write(TextMessageWriter w);

}
