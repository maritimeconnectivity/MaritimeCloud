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
package net.maritimecloud.net;

import static java.util.Objects.requireNonNull;
import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class LocalEndpoint {

    /** Invocator doing the heavy lifting. */
    private final LocalEndpoint.Invocator invocator;

    /**
     * Creates a new {@link LocalEndpoint}.
     *
     * @param invocator
     *            an invocator supplied by the MMS implementation.
     */
    protected LocalEndpoint(LocalEndpoint.Invocator invocator) {
        this.invocator = requireNonNull(invocator, "invocator is null");
    }

    /**
     * Returns the id of the remote actor implementing the endpoint.
     *
     * @return the id of the remote actor implementing the endpoint
     */
    public final MaritimeId getRemoteId() {
        return invocator.getRemote();
    }

    /**
     * Invoke a remote method.
     *
     * @param endpoint
     *            the full name of the endpoint method
     * @param parameters
     *            a message containing each parameter
     * @param serializer
     *            the serializer for serializing each parameter
     * @param resultParser
     *            a parser that is used to the parse the result from the remote actor.
     * @param <T>
     *            the type of result of the remote method
     * @return an invocation future
     */
    protected final <T> EndpointInvocationFuture<T> invokeRemote(String endpoint, Message parameters,
            MessageSerializer<? extends Message> serializer, ValueSerializer<T> resultParser) {
        return invocator.invokeRemote(endpoint, parameters, serializer, resultParser);
    }

    /** Used internally for invoking remote methods. */
    public interface Invocator {

        /**
         * Returns the id of the remote party publishing the endpoint.
         *
         * @return the id of the remote party publishing the endpoint.
         */
        MaritimeId getRemote();

        /**
         * @param endpoint
         *            the name of the endpoint
         * @param parameters
         *            an optional list of parameters to the method
         * @param parameterSerializer
         *            serializer for parameters
         * @param resultSerializer
         *            the serializer used for the result
         * @param <T>
         *            the type of result of the remote method
         * @return a future representing pending completion of the invocation
         */
        <T> EndpointInvocationFuture<T> invokeRemote(String endpoint, Message parameters,
                MessageSerializer<? extends Message> parameterSerializer, ValueSerializer<T> resultSerializer);
    }
}
