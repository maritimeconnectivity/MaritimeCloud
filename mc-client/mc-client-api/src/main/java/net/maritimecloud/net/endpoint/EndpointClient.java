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
package net.maritimecloud.net.endpoint;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.net.ConnectionClosedException;
import net.maritimecloud.net.MaritimeCloudClient;

/**
 *
 * @author Kasper Nielsen
 */
public interface EndpointClient {

    /**
     * Creates a ServiceLocator for a service of the specified type.
     *
     * @param sip
     *            the service initiation point
     * @return a service locator object
     *
     * @throws ConnectionClosedException
     *             if the connection has been permanently closed
     */
    <T extends EndpointLocal> T endpointFind(Class<? extends T> endpointType, MaritimeId id);

    <T extends EndpointLocal> EndpointLocator<T> endpointFind(Class<? extends T> endpointType);

    /**
     * Registers the specified endpoint with the maritime cloud. If a client is closed via
     * {@link MaritimeCloudClient#close()} the server will automatically deregister all endpoints.
     *
     * @param implementation
     *            the endpoint implementation
     * @return an endpoint registration object
     *
     * @throws ConnectionClosedException
     *             if the connection has been permanently closed
     */
    EndpointRegistration endpointRegister(EndpointImplementation implementation);
}
