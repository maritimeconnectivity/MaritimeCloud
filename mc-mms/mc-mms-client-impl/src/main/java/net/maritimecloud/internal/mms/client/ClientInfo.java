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
package net.maritimecloud.internal.mms.client;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionTime;

/**
 * Static information about the client.
 *
 * @author Kasper Nielsen
 */
public class ClientInfo {

    final Map<String, String> clientConnectString;

    /** The id of this client */
    final MaritimeId clientId;

    /** Responsible for creating a current position and time. */
    final PositionReader positionReader;

    /** The URI to connect to. Is constant. */
    final URI serverUri;

    public ClientInfo(MmsClientConfiguration configuration) {
        this.clientId = requireNonNull(configuration.getId());
        this.positionReader = configuration.getPositionReader();

        clientConnectString = new HashMap<>();
        clientConnectString.put("version", "0.3");
        if (configuration.properties().getName() != null) {
            clientConnectString.put("name", configuration.properties().getName());
        }
        if (configuration.properties().getDescription() != null) {
            clientConnectString.put("description", configuration.properties().getDescription());
        }
        if (configuration.properties().getOrganization() != null) {
            clientConnectString.put("organization", configuration.properties().getOrganization());
        }


        try {
            String remote = configuration.getHost();

            // Add default port, if no specific port has been specified
            if (!remote.contains(":")) {
                remote += ":43234";
            }

            // use standard http (ws instead of wss)
            remote = "ws://" + remote;

            // Tomcat does not automatically append a '/' to the host address
            if (!remote.endsWith("/")) {
                remote += "/";
            }
            this.serverUri = new URI(remote);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @return the clientConnectString
     */
    public Map<String, String> getClientConnectString() {
        return clientConnectString;
    }

    public Timestamp currentTime() {
        return Timestamp.now();
    }

    public MaritimeId getClientId() {
        return clientId;
    }

    public URI getServerURI() {
        return serverUri;
    }

    public boolean hasPosition() {
        return positionReader != null;
    }

    public Optional<PositionTime> getCurrentPosition() {
        if (positionReader == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(positionReader.getCurrentPosition());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
