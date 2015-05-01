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
package net.maritimecloud.mms.server.connection.client;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import net.maritimecloud.internal.mms.messages.services.AbstractClients;
import net.maritimecloud.internal.mms.messages.services.ClientInfo;
import net.maritimecloud.internal.mms.messages.services.ClientList;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.util.geometry.PositionTime;

/**
 * This class is just temporary until the server has been properly refactored
 * 
 * @author Kasper Nielsen
 */
public class ClientManagerStatistics extends AbstractClients {
    private final OldClientManager cm;

    public ClientManagerStatistics(OldClientManager cm) {
        this.cm = cm;
    }

    /** {@inheritDoc} */
    @Override
    protected List<ClientInfo> getAllClient(MessageHeader header) {
        return getAllClients().getClients();
    }

    public ClientList getAllClients() {
        ClientList cl = new ClientList();
        for (OldClient t : cm) {
            ClientInfo ci = new ClientInfo();
            ci.setId(t.getId().toString());
            PositionTime pt = t.getLatestPosition();
            if (pt != null) {
                ci.setLastSeen(pt.timestamp());
                ci.setLatestPosition(pt);
            }

            ClientProperties p = t.getProperties();
            ci.setName(p.getName());
            ci.setDescription(p.getDescription());
            ci.setOrganization(p.getOrganization());

            cl.addClients(ci);
        }
        return cl;
    }

    /** {@inheritDoc} */
    @Override
    protected Integer getConnectionCount(MessageHeader header) {
        final LongAdder i = new LongAdder();
        cm.forEachTarget(t -> {
            if (t.isConnected()) {
                i.increment();
            }
        });
        return i.intValue();
    }
}
