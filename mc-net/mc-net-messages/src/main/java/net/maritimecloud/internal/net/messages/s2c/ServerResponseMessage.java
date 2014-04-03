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
package net.maritimecloud.internal.net.messages.s2c;

import java.io.IOException;

import net.maritimecloud.internal.net.messages.ConnectionMessage;
import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class ServerResponseMessage extends ConnectionMessage {

    final long messageAck;

    public ServerResponseMessage(MessageType type, long messageAck) {
        super(type);
        this.messageAck = messageAck;
    }

    public ServerResponseMessage(MessageType type, TextMessageReader pr) throws IOException {
        super(type, pr);
        this.messageAck = pr.takeLong();
    }

    public long getMessageAck() {
        return messageAck;
    }

    /** {@inheritDoc} */
    @Override
    protected final void write0(TextMessageWriter w) {
        w.writeLong(messageAck);
        write1(w);
    }

    protected void write1(TextMessageWriter w) {};
}
