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
package net.maritimecloud.mms.server;

import net.maritimecloud.mms.server.connection.client.DefaultTransportListener;
import net.maritimecloud.mms.server.connection.transport.ServerTransportJsr356Endpoint;
import net.maritimecloud.mms.server.rest.*;
import net.maritimecloud.mms.server.security.MmsSecurityManager;
import org.cakeframework.container.ServiceManager;
import org.cakeframework.container.lifecycle.RunOnStart;
import org.cakeframework.container.lifecycle.RunOnStop;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Builder;
import java.lang.management.ManagementFactory;

import static java.util.Objects.requireNonNull;


/**
 * Configures a web server with a WebSocket interface used for the MMS transport layer
 * and a REST interface used for management of the MMS server.
 *
 * @author Kasper Nielsen
 */
public class WebServer {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(WebServer.class);

    /** The actual WebSocket server */
    private final Server server;

    final MmsServer is;

    final DefaultTransportListener defaultTransport;

    final ServerEventListener eventListener;

    /**
     * Constructor
     *
     * @param listener the server event listener
     * @param defaultTransport the transport listener
     * @param configuration the MMS configuration
     * @param securityManager the security manager
     * @param is the MMS server
     */
    public WebServer(ServerEventListener listener, DefaultTransportListener defaultTransport,
                     MmsServerConfiguration configuration, MmsSecurityManager securityManager, MmsServer is) {
        this.eventListener = requireNonNull(listener);
        this.defaultTransport = requireNonNull(defaultTransport);
        this.is = requireNonNull(is);
        this.server = new Server();

        // Configure HTTP and HTTPS of the server based on the configuration
        configureServer(this.server, configuration, securityManager);
    }

    /**
     * Called when the MMS server starts up
     * @param sm the service manager
     */
    @RunOnStart
    public void start(ServiceManager sm) throws Exception {
        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);

        // New handler
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        server.setHandler(context);

        // Configure the WebSocket interface
        configureWebSockets(context);

        // Configure the REST interface
        configureRest(sm, context);

        server.start();
        LOG.info("System is ready accept client connections");
    }

    /**
     * Called when the MMS server terminates
     */
    @RunOnStop
    public void stop() throws Exception {
        server.stop();
    }

    /**
     * Configures the HTTP (ws) and HTTPS (wss) transport of the server
     *
     * @param server the server to configure
     * @param configuration the configuration to use
     * @param securityManager the security manager
     */
    private void configureServer(Server server, MmsServerConfiguration configuration, MmsSecurityManager securityManager) {

        // Configure HTTP
        if (configuration.getServerPort() != null) {
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(configuration.getServerPort());
            connector.setReuseAddress(true);
            server.addConnector(connector);
        }

        // Configure HTTPS
        if (configuration.getSecurePort() != null) {

            HttpConfiguration http_config = new HttpConfiguration();
            http_config.setSecureScheme("https");
            http_config.setSecurePort(configuration.getSecurePort());
            http_config.setOutputBufferSize(32768);

            // SSL Context Factory for HTTPS
            SslContextFactory sslContextFactory = securityManager.getSslContextFactory();

            // HTTPS Configuration
            HttpConfiguration https_config = new HttpConfiguration(http_config);
            https_config.addCustomizer(new SecureRequestCustomizer());

            // HTTPS connector
            ServerConnector https = new ServerConnector(server, new SslConnectionFactory(sslContextFactory,
                    HttpVersion.HTTP_1_1.asString()), new HttpConnectionFactory(https_config));
            https.setPort(configuration.getSecurePort());
            https.setIdleTimeout(500000);
            server.addConnector(https);
        }
    }

    /**
     * Configures the WebSocket interface
     *
     * @param context the servlet context
     */
    private void configureWebSockets(ServletContextHandler context) throws DeploymentException, ServletException {
        // Enable javax.websocket configuration for the context
        ServerContainer wsContainer = WebSocketServerContainerInitializer.configureContext(context);

        wsContainer.setDefaultMaxTextMessageBufferSize(10 * 1024 * 1024);
        // Add our default endpoint.

        Builder b = ServerEndpointConfig.Builder.create(ServerTransportJsr356Endpoint.class, "/");
        b.configurator(new ServerEndpointConfig.Configurator() {
            @SuppressWarnings("unchecked")
            public <S> S getEndpointInstance(Class<S> endpointClass) throws InstantiationException {
                return (S) is.getService(ServiceManager.class).inject(ServerTransportJsr356Endpoint.class);
            }
        });

        wsContainer.addEndpoint(b.build());

    }

    /**
     * Configures the REST interface
     *
     * @param sm the service manager
     * @param context the servlet context
     */
    private void configureRest(ServiceManager sm, ServletContextHandler context) {
        final ResourceConfig config = new ResourceConfig();

        // Register the REST endpoints
        config.register(sm.inject(JSONMessageBodyWriter.class));
        config.register(sm.inject(EndpointInvoke.class));
        config.register(sm.inject(ClientResource.class));
        config.register(sm.inject(MetricsResource.class));
        config.register(sm.inject(JSONMetricRegistryBodyWriter.class));
        config.register(sm.inject(DmaExceptionMapper.class));


        ServletHolder sho = new ServletHolder(new ServletContainer(config));
        // sho.setClassName("org.glassfish.jersey.servlet.ServletContainer");
        // This flag is set to disable internal buffering in jersey.
        // this is mainly done to avoid delays from when people request something. To the first output is delivered
        sho.setInitParameter(CommonProperties.OUTBOUND_CONTENT_LENGTH_BUFFER, "-1");

        context.addServlet(sho, "/*");
    }
}
