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

import java.io.IOException;

import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.internal.net.messages.c2c.ClientRelayedMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Kasper Nielsen
 */
public class InvokeService extends ClientRelayedMessage {

    final String conversationId;

    final String message;

    final String messageType;

    final String serviceType;

    final int status;

    public InvokeService(int status, String conversationId, String serviceType, String messageType, Object o) {
        this(status, conversationId, serviceType, messageType, persistAndEscape(o));
    }

    public InvokeService(int status, String conversationId, String serviceType, String messageType, String message) {
        super(MessageType.SERVICE_INVOKE);
        this.status = status;
        this.conversationId = conversationId;
        this.serviceType = serviceType;
        this.messageType = messageType;
        this.message = message;
    }

    public InvokeService(TextMessageReader pr) throws IOException {
        super(MessageType.SERVICE_INVOKE, pr);
        status = pr.takeInt();
        conversationId = pr.takeString();
        serviceType = pr.takeString();
        messageType = pr.takeString();
        message = pr.takeString();
    }

    /** {@inheritDoc} */
    @Override
    public ClientRelayedMessage cloneIt() {
        InvokeService is = new InvokeService(status, conversationId, serviceType, messageType, escape(message));
        is.setDestination(super.getDestination());
        is.setSource(super.getSource());
        return is;
    }

    /**
     * @param result
     */
    public InvokeServiceResult createReply(Object result) {
        InvokeServiceResult isa = new InvokeServiceResult(conversationId, persistAndEscape(result), result.getClass()
                .getName());
        isa.setDestination(getSource());
        isa.setSource(getDestination());
        return isa;
    }

    public Object parseMessage() throws Exception {
        Class<?> mt = Class.forName(getServiceType());
        ObjectMapper om = new ObjectMapper();
        return om.readValue(getMessage(), mt);
    }

    /**
     * @return the conversationId
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the messageType
     */
    public String getServiceMessageType() {
        return messageType;
    }

    /**
     * @return the serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "InvokeService [conversationId=" + conversationId + ", message=" + message + ", messageType="
                + messageType + ", serviceType=" + serviceType + ", status=" + status + ", destination="
                + getDestination() + ", source=" + getSource() + "]";
    }

    /** {@inheritDoc} */
    @Override
    protected void write1(TextMessageWriter w) {
        w.writeInt(status);
        w.writeString(conversationId);
        w.writeString(serviceType);
        w.writeString(messageType);
        w.writeString(message);
    }
}
