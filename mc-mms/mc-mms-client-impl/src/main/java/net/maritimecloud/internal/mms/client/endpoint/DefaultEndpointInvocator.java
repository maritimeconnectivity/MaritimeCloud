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
package net.maritimecloud.internal.mms.client.endpoint;

import static java.util.Objects.requireNonNull;
import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.endpoint.EndpointMirror;
import net.maritimecloud.internal.net.messages.MethodInvoke;
import net.maritimecloud.internal.net.util.DefaultEndpointInvocationFuture;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.LocalEndpoint;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class DefaultEndpointInvocator implements LocalEndpoint.Invocator {

    /** The id of the remote client. */
    final MaritimeId receiver;

    final ClientEndpointManager cem;

    final EndpointMirror mirror;

    DefaultEndpointInvocator(ClientEndpointManager cem, MaritimeId receiver, EndpointMirror mirror) {
        this.cem = requireNonNull(cem);
        this.receiver = receiver;
        this.mirror = requireNonNull(mirror);

    }

    /** {@inheritDoc} */
    @Override
    public MaritimeId getRemote() {
        return receiver;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> EndpointInvocationFuture<T> invokeRemote(String endpoint, Message parameters,
            MessageSerializer<? extends Message> serializer, ValueSerializer<T> resultParser) {
        requireNonNull(endpoint, "endpoint is null");
        requireNonNull(parameters, "parameters is null");

        MethodInvoke ei = new MethodInvoke();
        ei.setMessageId(Binary.random(32));
        ei.setEndpointMethod(endpoint);
        // Vi skal have en serializer med i metoden
        ei.setParameters(MessageSerializer.writeToJSON(parameters, (MessageSerializer) serializer));
        if (receiver != null) {
            ei.setReceiverId(receiver.toString());
        }
        ei.setSenderId(cem.clientInfo.getClientId().toString());

        final DefaultEndpointInvocationFuture<T> result = cem.threadManager.create(ei.getMessageId());

        result.recivedByCloud = cem.connection.sendMessage(ei);
        cem.invokers.put(ei.getMessageId(), new RemoteInvocation(result, mirror, resultParser));

        return result;
    }
}
