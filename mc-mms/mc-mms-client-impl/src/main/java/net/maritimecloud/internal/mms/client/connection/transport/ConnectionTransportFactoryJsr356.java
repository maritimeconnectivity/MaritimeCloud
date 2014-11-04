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
package net.maritimecloud.internal.mms.client.connection.transport;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import net.maritimecloud.net.mms.MmsConnection;

/**
 *
 * @author Kasper Nielsen
 */
public class ConnectionTransportFactoryJsr356 extends ConnectionTransportFactory {

    /** The single instance of a WebSocketContainer. */
    static volatile WebSocketContainer CACHED_CONTAINER;

    /** A lock protecting the creation of the cached websocket container. */
    private final static Object LOCK = new Object();

    /** {@inheritDoc} */
    @Override
    public ConnectionTransport create(ConnectionTransportListener listener, MmsConnection.Listener connectionListener) {
        WebSocketContainer container = CACHED_CONTAINER;
        if (container == null) {
            synchronized (LOCK) {
                container = ContainerProvider.getWebSocketContainer();

                // Default settings, TODO check we also set it after having created as session in
                // ConnectionTransportJsr356
                container.setDefaultMaxTextMessageBufferSize(10 * 1024 * 1024);

                // Let others use this container as well. (most like none)
                CACHED_CONTAINER = container;
            }
        }
        return new ConnectionTransportJsr356(listener, connectionListener, container);
    }
}
