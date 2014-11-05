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
package net.maritimecloud.net.mms;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.net.BroadcastConsumer;
import net.maritimecloud.net.BroadcastMessage;
import net.maritimecloud.net.BroadcastSubscription;
import net.maritimecloud.net.DispatchedMessage;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.EndpointRegistration;
import net.maritimecloud.net.LocalEndpoint;
import net.maritimecloud.util.geometry.Area;

/**
 * A client that can be used to access the e-navigation network.
 *
 * @author Kasper Nielsen
 */
public interface MmsClient {

    /**
     * Blocks until all tasks have completed execution after a close request, or the timeout occurs, or the current
     * thread is interrupted, whichever happens first.
     *
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return {@code true} if this client terminated and {@code false} if the timeout elapsed before termination
     * @throws InterruptedException
     *             if interrupted while waiting
     */
    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Asynchronously broadcasts the specified message. No guarantees are made to the delivery of the specified message.
     *
     * The future returned can be used to determine when the messages has been received by the MMS server.
     *
     * @return a future that can be used to determine when the broadcast messages is received by the MMS server
     *
     * @param message
     *            the message to broadcast
     * @throws NullPointerException
     *             if the specified message is null
     * @throws MmsClientClosedException
     *             if the connection has been permanently closed
     * @see #withBroadcast(BroadcastMessage)
     */
    DispatchedMessage broadcast(BroadcastMessage message);

    /**
     * Subscribes to the the specified type of broadcast messages.
     *
     * @param messageType
     *            the type of broadcast message to listen for
     * @param consumer
     *            the consumer that will be invoked for each broadcast message that is received
     * @param <T>
     *            the type of broadcast message
     * @return a subscription that can be used to stop listening for broadcasts of the specified type
     * @throws NullPointerException
     *             if the message type or consumer is null
     * @throws MmsClientClosedException
     *             if the connection has been permanently closed
     */
    <T extends BroadcastMessage> BroadcastSubscription broadcastSubscribe(Class<T> messageType,
            BroadcastConsumer<T> consumer);

    /**
     * Subscribes to the the specified type of broadcast messages in the specified area.
     *
     * @param messageType
     *            the type of message to listen for
     * @param consumer
     *            the consumer of messages
     * @param area
     *            the area for which the broadcast will be visible to other actors. The area is not relative to the
     *            current position of the client.
     * @param <T>
     *            the type of broadcast
     * @return a subscription that can be used to stop listening for broadcasts of the specified type
     * @throws NullPointerException
     *             if the message type, consumer or area is null
     * @throws MmsClientClosedException
     *             if the connection has been permanently closed
     */
    <T extends BroadcastMessage> BroadcastSubscription broadcastSubscribe(Class<T> messageType,
            BroadcastConsumer<T> consumer, Area area);

    /**
     * Asynchronously shutdowns this client. use {@link #awaitTermination(long, TimeUnit)} to await complete termination
     * (acknowledgment by the remote side).
     */
    void close();

    /**
     * Returns details about the actual connection to a server.
     *
     * @return connection details
     */
    MmsConnection connection();

    <T extends LocalEndpoint> MmsEndpointLocator<T> endpointFind(Class<T> endpointType);

    /**
     * Creates a ServiceLocator for a service of the specified type.
     *
     * @param id
     *            the id of the remote party
     * @param endpointType
     *            the endpoint type
     * @param <T>
     *            the type of endpoint
     * @return a service locator object
     *
     * @throws MmsClientClosedException
     *             if the connection has been permanently closed
     */
    <T extends LocalEndpoint> T endpointFind(MaritimeId id, Class<T> endpointType);

    /**
     * Registers the specified endpoint with the maritime cloud. If a client is closed via {@link MmsClient#close()} the
     * server will automatically deregister all endpoints.
     *
     * @param implementation
     *            the endpoint implementation
     * @return an endpoint registration object
     *
     * @throws MmsClientClosedException
     *             if the connection has been permanently closed
     */
    EndpointRegistration endpointRegister(EndpointImplementation implementation);

    /**
     * Returns the id of this client.
     *
     * @return the id of this client
     */
    MaritimeId getClientId();

    /**
     * Returns {@code true} if this executor has been shut down.
     *
     * @return {@code true} if this executor has been shut down
     */
    boolean isClosed();

    /**
     * Returns {@code true} if all tasks have completed following shut down. Note that {@code isTerminated} is never
     * {@code true} unless either {@code shutdown} or {@code shutdownNow} was called first.
     *
     * @return {@code true} if all tasks have completed following shut down
     */
    boolean isTerminated();

    WithBroadcast withBroadcast(BroadcastMessage message);
}
