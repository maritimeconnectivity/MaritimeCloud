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
package net.maritimecloud.mms.server.endpoints;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ConcurrentHashMap;

import net.maritimecloud.mms.server.connection.client.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages services for a single connected client.
 *
 * @author Kasper Nielsen
 */
public class ServerClientEndpointManager {

    /** A logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ServerClientEndpointManager.class);

    /** A map of all registered services at the client. */
    final ConcurrentHashMap<String, String> endpoints = new ConcurrentHashMap<>();

    /** The client */
    final Client client;

    public ServerClientEndpointManager(Client client) {
        this.client = requireNonNull(client);
    }

    public boolean hasService(String name) {
        for (String c : endpoints.keySet()) {
            if (c.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void registerEndpoint(String endpointName) {
        LOG.debug("Registered remote service " + endpointName + "@" + client.getId());
        endpoints.put(endpointName, endpointName);
    }
}
