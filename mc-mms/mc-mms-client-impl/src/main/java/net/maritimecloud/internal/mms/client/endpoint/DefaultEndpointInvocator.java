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
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.LocalEndpoint;

/**
 * The default implementation of a local endpoint invocator.
 *
 * @author Kasper Nielsen
 */
class DefaultEndpointInvocator implements LocalEndpoint.Invocator {

    /** The client endpoint manager. */
    private final ClientEndpointManager endpointManager;

    /** The id of the remote client. */
    private final MaritimeId receiver;

    /**
     * Creates a new default endpoint invocator.
     *
     * @param endpointManager
     *            the endpoint manager
     * @param receiver
     *            the remote endpoint
     */
    DefaultEndpointInvocator(ClientEndpointManager endpointManager, MaritimeId receiver) {
        this.endpointManager = requireNonNull(endpointManager);
        this.receiver = receiver;
    }

    /** {@inheritDoc} */
    @Override
    public MaritimeId getRemote() {
        return receiver;
    }

    /** {@inheritDoc} */
    @Override
    public <T> EndpointInvocationFuture<T> invokeRemote(String endpoint, Message parameters,
            MessageSerializer<? extends Message> serializer, ValueSerializer<T> resultParser) {
        return endpointManager.invokeRemote(receiver, endpoint, parameters, serializer, resultParser);
    }
}
