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
package net.maritimecloud.mms.server.rest;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.endpoint.EndpointMirror;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.connection.client.Client;
import net.maritimecloud.mms.server.connection.client.ClientManager;
import net.maritimecloud.mms.server.security.AuthenticationException;
import net.maritimecloud.mms.server.security.ClientVerificationException;
import net.maritimecloud.mms.server.security.MmsSecurityManager;
import net.maritimecloud.mms.server.security.Subject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author Kasper Nielsen
 */
@Path("/endpoint")
public class EndpointInvoke extends ProtectedResource {

    final ClientManager clientManager;

    /**
     * Constructor
     * @param clientManager the client manager
     * @param securityManager the security manager
     */
    public EndpointInvoke(ClientManager clientManager, MmsSecurityManager securityManager) {
        super(securityManager);
        this.clientManager = clientManager;
    }

    /**
     * Returns a list of source IDs in the source. This one is hard coded for now
     *
     * @param mmsi
     *            the mmsi number
     * @param endpoint
     *            the endpoint
     * @return a list of source IDs in the source
     */
    @GET
    @Path("/invoke/{mmsi}/{endpoint}")
    public Message invoke(@PathParam("mmsi") String mmsi, @PathParam("endpoint") String endpoint) throws AuthenticationException, ClientVerificationException {

        MaritimeId id = MaritimeId.create("mmsi:" + mmsi);

        // Instantiate a subject according to the current security configuration
        Subject subject = initializeSubject();
        subject.checkClient(id.toString());

        String ep = EndpointMirror.stripEndpointMethod(endpoint);
        Client client = clientManager.get(id);
        if (client != null) {
            if (client.getEndpointManager().hasService(ep)) {
                // Vi skal lave en "Fake" sources
            }
        }
        return null;
    }


}
