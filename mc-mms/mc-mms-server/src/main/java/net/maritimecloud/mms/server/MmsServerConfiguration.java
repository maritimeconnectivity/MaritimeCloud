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

import static java.util.Objects.requireNonNull;

import java.util.concurrent.Executors;

import net.maritimecloud.core.id.ServerId;
import net.maritimecloud.internal.mms.transport.AccessLogManager;
import net.maritimecloud.internal.mms.transport.AccessLogManager.AccessLogConfiguration;
import net.maritimecloud.mms.server.broadcast.ServerBroadcastManager;
import net.maritimecloud.mms.server.connection.client.ClientManager;
import net.maritimecloud.mms.server.connection.client.ClientReaper;
import net.maritimecloud.mms.server.connection.client.DefaultTransportListener;
import net.maritimecloud.mms.server.endpoints.ServerEndpointManager;
import net.maritimecloud.mms.server.endpoints.ServerServices;
import net.maritimecloud.mms.server.rest.WebServer;
import net.maritimecloud.mms.server.tracker.PositionTracker;

import org.cakeframework.container.spi.AbstractContainerConfiguration;
import org.cakeframework.container.spi.ContainerComposer;
import org.cakeframework.container.spi.ContainerFactory;
import org.cakeframework.util.properties.Property;

import com.beust.jcommander.Parameter;
import com.codahale.metrics.MetricRegistry;

/**
 *
 * @author Kasper Nielsen
 */
public class MmsServerConfiguration implements AccessLogConfiguration {

    /** The default port this server is running on. */
    public static final int DEFAULT_PORT = 43234;

    /** The default port this server is running on. */
    public static final int DEFAULT_SECURE_PORT = -1;

    /** The default port the web server is running on. */
    public static final int DEFAULT_WEBSERVER_PORT = 9090;

    /** The id of the server, hard coded for now */
    ServerId id = new ServerId(1);

    @Parameter(names = "-keystore", description = "The path to the keystore")
    String keystore = null;

    @Parameter(names = "-keystorePassword", description = "The password of the keystore")
    String keystorePassword = null;

    @Parameter(names = "-accessLog", description = "The file to write access logs to. Use 'stdout' for standard out")
    String accessLog;

    @Parameter(names = "-port", description = "The port to listen for MMS connections on")
    int port = DEFAULT_PORT;

    @Parameter(names = "-requireTLS", description = "if true clients will not be able to connect without TLS")
    boolean requireTLS = false;

    @Parameter(names = "-securePort", description = "The secure port to listen for MMS connections on")
    int securePort = DEFAULT_SECURE_PORT;

    @Parameter(names = "-rest", description = "The webserver port for the administrative interface")
    int webserverPort = -1;

    /**
     * @return the id
     */
    public ServerId getId() {
        return id;
    }

    /**
     * @return the keystore
     */
    public String getKeystore() {
        return keystore;
    }

    /**
     * @return the keystorePassword
     */
    public String getKeystorePassword() {
        return keystorePassword;
    }

    /** {@inheritDoc} */
    @Override
    public String getAccessLog() {
        return accessLog;
    }

    /**
     * @return the securePort
     */
    public int getSecurePort() {
        return securePort;
    }

    /**
     * @return the serverPort
     */
    public int getServerPort() {
        return port;
    }

    /**
     * @return the webserverPort
     */
    public int getWebserverPort() {
        return webserverPort;
    }

    /**
     * @return the requireTLS
     */
    public boolean isRequireTLS() {
        return requireTLS;
    }

    /**
     * @param id
     *            the id to set
     * @return this configuration
     */
    public MmsServerConfiguration setId(ServerId id) {
        this.id = id;
        return this;
    }

    /**
     * @param keystore
     *            the keystore to set
     */
    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    /**
     * @param keystorePassword
     *            the keystorePassword to set
     */
    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    /**
     * @param accessLog
     *            the accessLog to set
     */
    public void setAccessLog(String accessLog) {
        this.accessLog = accessLog;
    }

    /**
     * @param requireTLS
     *            the requireTLS to set
     */
    public void setRequireTLS(boolean requireTLS) {
        this.requireTLS = requireTLS;
    }

    /**
     * @param securePort
     *            the securePort to set
     */
    public void setSecurePort(int securePort) {
        this.securePort = securePort;
    }

    /**
     * @param port
     *            the serverPort to set
     * @return this configuration
     */
    public MmsServerConfiguration setServerPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * @param webserverport
     *            the webserverPort to set
     * @return this configuration
     */
    public MmsServerConfiguration setWebserverPort(int webserverport) {
        this.webserverPort = webserverport;
        return this;
    }

    /**
     * Creates a new instance of this class.
     */
    public MmsServer build() {
        MyConfiguration conf = new MyConfiguration();

        conf.withThreads().addPool(Executors.newFixedThreadPool(5));
        conf.addService(this);
        conf.addService(requireNonNull(getId()));

        conf.addService(ClientManager.class);
        conf.addService(ClientReaper.class);
        conf.addService(DefaultTransportListener.class);

        conf.addService(new ServerEventListener() {});

        conf.addService(PositionTracker.class);
        conf.addService(WebSocketServer.class);

        conf.addService(ServerServices.class);
        conf.addService(MmsServerConnectionBus.class);
        conf.addService(ServerBroadcastManager.class);
        conf.addService(ServerEndpointManager.class);
        if (getWebserverPort() > 0) {
            conf.addService(WebServer.class);
        }
        conf.addService(AccessLogManager.class);
        conf.addService(MetricRegistry.class);
        return conf.create();
    }


    public static class MyConfiguration extends AbstractContainerConfiguration<MmsServer> {
        static final Property<?> FACTORY = Property.create("cake.container.factory",
                MmsServerConfiguration.class.getCanonicalName() + "$Factory", Class.class, "Container");

        MyConfiguration() {
            super(FACTORY);
        }
    }

    public static class Factory extends ContainerFactory<MmsServer, MyConfiguration> {

        /** {@inheritDoc} */
        @Override
        public MmsServer create(MyConfiguration configuration, ContainerComposer composer) {
            return new MmsServer(configuration, composer);
        }
    }
}
