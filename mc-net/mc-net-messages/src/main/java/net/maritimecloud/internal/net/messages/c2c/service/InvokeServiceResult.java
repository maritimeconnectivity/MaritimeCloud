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
package net.maritimecloud.internal.net.messages.c2c.service;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.internal.net.messages.c2c.ClientRelayedMessage;

/**
 * 
 * @author Kasper Nielsen
 */
public class InvokeServiceResult extends ClientRelayedMessage {

    final String message;

    final String replyType;

    final String uuid;

    public InvokeServiceResult(TextMessageReader pr) throws IOException {
        super(MessageType.SERVICE_INVOKE_RESULT, pr);
        this.uuid = requireNonNull(pr.takeString());
        this.message = requireNonNull(pr.takeString());
        this.replyType = requireNonNull(pr.takeString());
    }

    /**
     * @param messageType
     */
    public InvokeServiceResult(String uuid, String message, String replyType) {
        super(MessageType.SERVICE_INVOKE_RESULT);
        this.uuid = uuid;
        this.message = message;
        this.replyType = replyType;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the replyType
     */
    public String getReplyType() {
        return replyType;
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /** {@inheritDoc} */
    @Override
    protected void write1(TextMessageWriter w) {
        w.writeString(uuid);
        w.writeString(message);
        w.writeString(replyType);
    }

    /** {@inheritDoc} */
    @Override
    public ClientRelayedMessage cloneIt() {
        InvokeServiceResult is = new InvokeServiceResult(uuid, escape(message), replyType);
        is.setDestination(super.getDestination());
        is.setSource(super.getSource());
        return is;
    }
}
