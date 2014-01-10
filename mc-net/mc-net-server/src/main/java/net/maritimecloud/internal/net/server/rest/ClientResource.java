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
package net.maritimecloud.internal.net.server.rest;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import net.maritimecloud.internal.net.server.targets.Target;
import net.maritimecloud.internal.net.server.targets.TargetManager;
import net.maritimecloud.internal.net.server.targets.TargetProperties;
import net.maritimecloud.util.geometry.PositionTime;


/**
 * 
 * @author Kasper Nielsen
 */

@Path("/clients")
public class ClientResource {
    final TargetManager tm;

    public ClientResource(TargetManager tm) {
        this.tm = tm;
    }

    /**
     * Returns a list of source IDs in the source. This one is hard coded for now
     * 
     * @return a list of source IDs in the source
     */
    @GET
    @Path("/list")
    public JSONObject getSourceIDs() {
        ArrayList<Object> clients = new ArrayList<>();
        for (Target t : tm) {
            TargetProperties p = t.getProperties();
            JSONObject j = new JSONObject();
            j.addElement("id", t.getId().toString());
            j.addElement("name", p.getName());
            j.addElement("description", p.getDescription());
            j.addElement("organization", p.getOrganization());
            clients.add(j);

            PositionTime pt = t.getLatestPosition();
            if (pt != null) {
                j.addElement(
                        "position",
                        JSONObject.single("latitude", pt.getLatitude(), "longitude", pt.getLongitude(), "time",
                                pt.getTime()));
            }
        }
        JSONObject root = new JSONObject();
        root.addList("clients", clients.toArray());
        return root;
    }
}
