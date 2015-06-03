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

import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.net.mms.MmsConnection;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

/**
 *
 * @author Kasper Nielsen
 */
public class ClientTransportFactoryJsr356 extends ClientTransportFactory {

    public ClientTransportFactoryJsr356(MmsClientConfiguration conf) {
        super(conf);
    }

    /** {@inheritDoc} */
    @Override
    public ClientTransport create(ClientTransportListener transportListener,
            MmsConnection.Listener connectionListener) {
        return new ClientTransportJsr356(conf, transportListener, connectionListener, Singleton.INSTANCE);
    }

    /** Singleton. Uses the standard initialization-on-demand holder idiom. */
    static class Singleton {
        static final WebSocketContainer INSTANCE;

        static {
            INSTANCE = ContainerProvider.getWebSocketContainer();

            // Default settings
            INSTANCE.setDefaultMaxTextMessageBufferSize(5 * 1024 * 1024);
            INSTANCE.setDefaultMaxBinaryMessageBufferSize(5 * 1024 * 1024);

        }
    }
}
