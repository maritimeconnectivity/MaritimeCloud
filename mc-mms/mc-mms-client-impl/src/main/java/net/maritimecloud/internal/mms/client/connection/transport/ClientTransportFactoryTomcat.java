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
import org.apache.tomcat.websocket.WsWebSocketContainer;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

/**
 * A transport manager that uses Tomcat to create new web socket connections.
 */
public class ClientTransportFactoryTomcat extends ClientTransportFactory {

    /** The single instance of a WebSocketContainer. */
    private final WebSocketContainer container;

    /**
     * Constructor
     * @param conf the MMS client configuration
     */
    public ClientTransportFactoryTomcat(MmsClientConfiguration conf) {
        super(conf);

        // Check if we need to define an SSL context for the connection
        SSLContext context = null;
        try {
            TrustManager[] trustStore = null;
            KeyManager[] keyStore = null;
            if (conf.getKeystore() != null && conf.getKeystorePassword() != null) {
                keyStore = TransportSecurityUtils.loadKeyStore(conf.getKeystore(), conf.getKeystorePassword().toCharArray());
            }
            if (conf.getTruststore() != null && conf.getTruststorePassword() != null) {
                trustStore = TransportSecurityUtils.loadTrustStore(conf.getTruststore(), conf.getTruststorePassword().toCharArray());
            }
            if (keyStore != null || trustStore != null) {
                context = SSLContext.getInstance("TLS");
                context.init(keyStore, trustStore, null);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed setting up Tomcat WebSocket SSL context ", e);
        }

        container = new TomcatWebsocketContainer(context);
        // Default settings
        container.setDefaultMaxTextMessageBufferSize(5 * 1024 * 1024);
        container.setDefaultMaxBinaryMessageBufferSize(5 * 1024 * 1024);
    }

    /** {@inheritDoc} */
    @Override
    public ClientTransport create(ClientTransportListener transportListener,
            MmsConnection.Listener connectionListener) {
        return new ClientTransportJsr356(conf, transportListener, connectionListener, container);
    }

    /**
     * Sub-class the Tomcat websocket container class to add SSL support.
     */
    public static class TomcatWebsocketContainer extends WsWebSocketContainer {

        private final SSLContext context;

        /**
         * Constructor
         */
        public TomcatWebsocketContainer(SSLContext context) {
            super();
            this.context = context;
        }

        /** {@inheritDoc} */
        @Override
        public Session connectToServer(Endpoint endpoint, ClientEndpointConfig clientEndpointConfiguration, URI path)
                throws DeploymentException {

            // Set the SSL context, if defined
            if (context != null) {
                clientEndpointConfiguration.getUserProperties().put("org.apache.tomcat.websocket.SSL_CONTEXT", context);
            }

            return super.connectToServer(endpoint, clientEndpointConfiguration, path);
        }
    }

}
