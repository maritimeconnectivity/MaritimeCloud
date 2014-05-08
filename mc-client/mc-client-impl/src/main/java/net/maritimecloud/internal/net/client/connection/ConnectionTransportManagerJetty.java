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
package net.maritimecloud.internal.net.client.connection;

import net.maritimecloud.internal.net.client.connection.ConnectionTransportManagerJsr356.ConnectionTransportJsr356;

import org.eclipse.jetty.websocket.jsr356.ClientContainer;

/**
 * 
 * @author Kasper Nielsen
 */
class ConnectionTransportManagerJetty extends ConnectionTransportManager {

    /** The single instance of a WebSocketContainer. */
    final ClientContainer container = new ClientContainer();

    /** {@inheritDoc} */
    @Override
    public ConnectionTransport create(ClientConnection cc, ClientConnectFuture future) {
        return new ConnectionTransportJsr356(future, cc, container);
    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        try {
            container.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {
        try {
            container.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
