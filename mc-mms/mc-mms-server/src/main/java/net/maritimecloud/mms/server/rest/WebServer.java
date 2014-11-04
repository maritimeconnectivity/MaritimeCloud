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
package net.maritimecloud.mms.server.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.maritimecloud.mms.server.MmsServerConfiguration;

import org.cakeframework.container.RunOnStart;
import org.cakeframework.container.RunOnStop;
import org.cakeframework.container.ServiceManager;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kasper Nielsen
 */
public class WebServer {

    /** The logger */
    static final Logger LOG = LoggerFactory.getLogger(WebServer.class);

    final Server server;

    final int port;

    final ResourceConfig config = new ResourceConfig();

    public WebServer(MmsServerConfiguration conf) {
        port = conf.getWebserverPort();
        server = new Server(conf.getWebserverPort());
    }

    @RunOnStart
    public void start(ServiceManager sm) {
        ((ServerConnector) server.getConnectors()[0]).setReuseAddress(true);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        config.register(sm.inject(JSONMessageBodyWriter.class));
        config.register(sm.inject(EndpointInvoke.class));
        config.register(sm.inject(ClientResource.class));
        config.register(sm.inject(DmaExceptionMapper.class));


        ServletHolder sho = new ServletHolder(new ServletContainer(config));
        // sho.setClassName("org.glassfish.jersey.servlet.ServletContainer");
        // This flag is set to disable internal buffering in jersey.
        // this is mainly done to avoid delays from when people request something. To the first output is delivered
        sho.setInitParameter(CommonProperties.OUTBOUND_CONTENT_LENGTH_BUFFER, "-1");

        context.addServlet(sho, "/*");
        HandlerWrapper hw = new HandlerWrapper() {

            /** {@inheritDoc} */
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                long start = System.nanoTime();
                String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
                LOG.info("Received connection from " + request.getRemoteHost() + " (" + request.getRemoteAddr() + ":"
                        + request.getRemotePort() + ") request = " + request.getRequestURI() + queryString);
                super.handle(target, baseRequest, request, response);
                LOG.info("Connection closed from " + request.getRemoteHost() + " (" + request.getRemoteAddr() + ":"
                        + request.getRemotePort() + ") request = " + request.getRequestURI() + queryString
                        + ", Duration = " + (System.nanoTime() - start) / 1000000 + " ms");
            }
        };
        hw.setHandler(context);
        server.setHandler(hw);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RunOnStop
    public void stop() throws Exception {
        server.stop();
    }
}
