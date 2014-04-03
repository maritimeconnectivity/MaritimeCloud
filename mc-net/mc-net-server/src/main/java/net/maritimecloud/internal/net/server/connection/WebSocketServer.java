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
package net.maritimecloud.internal.net.server.connection;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Builder;

import net.maritimecloud.internal.net.server.InternalServer;
import net.maritimecloud.internal.net.server.ServerConfiguration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.picocontainer.Startable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory used to create transports from connections by remote clients.
 *
 * @author Kasper Nielsen
 */
public class WebSocketServer implements Startable {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    /** The actual WebSocket server */
    private final Server server;

    final InetSocketAddress sa;

    final InternalServer is;

    public WebSocketServer(ServerConfiguration configuration, InternalServer is) {
        this.sa = new InetSocketAddress(configuration.getServerPort());
        this.is = requireNonNull(is);
        this.server = new Server(sa);

        // Sets the sockets reuse address to true
        ServerConnector connector = (ServerConnector) server.getConnectors()[0];
        connector.setReuseAddress(true);
    }

    public void start() {

        // New handler
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Jetty needs to have at least 1 servlet, so we add this dummy servlet
        context.addServlet(new ServletHolder(new DumpServlet()), "/*");

        // Enable javax.websocket configuration for the context
        ServerContainer wsContainer = WebSocketServerContainerInitializer.configureContext(context);

        // Add our default endpoint.

        Builder b = ServerEndpointConfig.Builder.create(ServerTransport.class, "/");
        b.configurator(new ServerEndpointConfig.Configurator() {
            @SuppressWarnings("unchecked")
            public <S> S getEndpointInstance(Class<S> endpointClass) throws InstantiationException {
                return (S) new ServerTransport(is);
            }
        });

        try {
            wsContainer.addEndpoint(b.build());
        } catch (DeploymentException e) {
            throw new RuntimeException("Could not start server", e);
        }
        try {
            server.start();
            LOG.info("System is ready accept client connections on " + sa);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
