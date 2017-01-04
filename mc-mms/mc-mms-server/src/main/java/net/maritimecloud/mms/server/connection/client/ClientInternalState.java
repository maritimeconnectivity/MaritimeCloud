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

import static java.util.Objects.requireNonNull;

import net.maritimecloud.mms.server.connection.client.Client.State;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;

/**
 *
 * @author Kasper Nielsen
 */
class ClientInternalState {

    static final ClientInternalState TERMINATED = new ClientInternalState(State.TERMINATED, null, null);

    final ServerTransport transport;

    final Session session;

    final Client.State state;

    /**
     * @param transport
     */
    ClientInternalState(Client.State state, ServerTransport transport, Session session) {
        this.state = requireNonNull(state);
        this.transport = transport;
        this.session = session;
    }
}
