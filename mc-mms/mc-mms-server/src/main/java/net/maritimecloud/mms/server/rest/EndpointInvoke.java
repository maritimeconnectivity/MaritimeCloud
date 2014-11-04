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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.endpoint.EndpointMirror;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.targets.Target;
import net.maritimecloud.mms.server.targets.TargetManager;

/**
 *
 * @author Kasper Nielsen
 */
@Path("/endpoint")
public class EndpointInvoke {
    final TargetManager tm;

    public EndpointInvoke(TargetManager tm) {
        this.tm = tm;
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
    public Message invoke(@PathParam("mmsi") String mmsi, @PathParam("endpoint") String endpoint) {
        MaritimeId id = MaritimeId.create("mmsi:" + mmsi);
        String ep = EndpointMirror.stripEndpointMethod(endpoint);
        Target t = tm.find(id);
        if (t != null) {
            if (t.getEndpointManager().hasService(ep)) {
                // Vi skal lave en "Fake" sources
            }
        }
        return null;
    }
}
