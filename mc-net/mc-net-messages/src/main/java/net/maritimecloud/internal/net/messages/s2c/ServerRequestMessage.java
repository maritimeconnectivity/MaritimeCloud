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

import net.maritimecloud.internal.net.messages.ConnectionOldMessage;
import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;

/**
 * 
 * @author Kasper Nielsen
 */
public abstract class ServerRequestMessage<T extends ServerResponseMessage> extends ConnectionOldMessage {

    long replyTo;

    public ServerRequestMessage(MessageType messageType, TextMessageReader pr) throws IOException {
        super(messageType, pr);
        this.replyTo = pr.takeLong();
    }

    /**
     * @param messageType
     */
    public ServerRequestMessage(MessageType messageType) {
        super(messageType);
    }

    public ServerRequestMessage<T> setReplyTo(long replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public long getReplyTo() {
        return replyTo;
    }

    /** {@inheritDoc} */
    @Override
    protected final void write0(TextMessageWriter w) {
        w.writeLong(replyTo);
        write1(w);
    }

    protected abstract void write1(TextMessageWriter w);
}
