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
import org.cakeframework.container.ServiceManager;
import org.cakeframework.container.lifecycle.RunOnStart;
import org.cakeframework.container.lifecycle.RunOnStop;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Builder;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import static java.util.Objects.requireNonNull;


/**
 * A factory used to create transports from connections by remote clients.
 *
 * @author Kasper Nielsen
 */
public class WebSocketServer {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    /** The actual WebSocket server */
    private final Server server;

    final MmsServer is;

    final DefaultTransportListener defaultTransport;

    final ServerEventListener eventListener;

    public WebSocketServer(ServerEventListener listener, DefaultTransportListener defaultTransport,
            MmsServerConfiguration configuration, MmsServer is) {
        this.eventListener = requireNonNull(listener);
        this.defaultTransport = requireNonNull(defaultTransport);
        this.is = requireNonNull(is);
        this.server = new Server();
        if (!configuration.isRequireTLS()) {
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(configuration.getServerPort());
            connector.setReuseAddress(true);
            server.addConnector(connector);
        }
        if (configuration.getSecurePort() > 0) {

            // ADD HTPS
            // HTTP Configuration
            HttpConfiguration http_config = new HttpConfiguration();
            http_config.setSecureScheme("https");
            http_config.setSecurePort(configuration.getSecurePort());
            http_config.setOutputBufferSize(32768);

            // SSL Context Factory for HTTPS
            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setKeyStorePath(configuration.getKeystore());
            sslContextFactory.setKeyStorePassword(configuration.getKeystorePassword());

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

    @RunOnStart
    public void start() throws Exception {
        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);


        // New handler
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");


        server.setHandler(context);

        // Jetty needs to have at least 1 servlet, so we add this dummy servlet
        context.addServlet(new ServletHolder(new DumpServlet()), "/*");

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
        server.start();
        LOG.info("System is ready accept client connections");
    }

    @RunOnStop
    public void stop() throws Exception {
        server.stop();
    }

    @SuppressWarnings("serial")
    static class DumpServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>DumpServlet</h1><pre>");
            response.getWriter().println("requestURI=" + request.getRequestURI());
            response.getWriter().println("contextPath=" + request.getContextPath());
            response.getWriter().println("servletPath=" + request.getServletPath());
            response.getWriter().println("pathInfo=" + request.getPathInfo());
            response.getWriter().println("session=" + request.getSession(true).getId());

            String r = request.getParameter("resource");
            if (r != null) {
                response.getWriter().println("resource(" + r + ")=" + getServletContext().getResource(r));
            }

            response.getWriter().println("</pre>");
        }
    }
}
