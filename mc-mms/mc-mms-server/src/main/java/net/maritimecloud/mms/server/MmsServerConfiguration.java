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

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.codahale.metrics.MetricRegistry;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.maritimecloud.core.id.ServerId;
import net.maritimecloud.internal.mms.transport.AccessLogManager;
import net.maritimecloud.mms.server.broadcast.ServerBroadcastManager;
import net.maritimecloud.mms.server.connection.client.ClientManager;
import net.maritimecloud.mms.server.connection.client.ClientReaper;
import net.maritimecloud.mms.server.connection.client.DefaultTransportListener;
import net.maritimecloud.mms.server.endpoints.ServerEndpointManager;
import net.maritimecloud.mms.server.endpoints.ServerServices;
import net.maritimecloud.mms.server.security.MmsSecurityManager;
import net.maritimecloud.mms.server.tracker.PositionTracker;
import org.cakeframework.container.spi.AbstractContainerConfiguration;
import org.cakeframework.container.spi.ContainerComposer;
import org.cakeframework.container.spi.ContainerFactory;
import org.cakeframework.util.properties.Property;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static net.maritimecloud.internal.mms.transport.AccessLogManager.AccessLogConfiguration;
import static net.maritimecloud.internal.mms.transport.AccessLogManager.AccessLogFormat;

/**
 * Defines the MMS server configuration.
 * <p/>
 * The MMS configuration parameters can either be set directly using the command line or by specifying
 * a configuration file with the "-conf" parameter.
 * <p/>
 * The command line parameters are:
 * <ul>
 *     <li>-conf: The configuration file</li>
 *     <li>-port: The port to listen for REST and MMS connections on</li>
 *     <li>-securePort: The secure port to listen for REST MMS connections on</li>
 *     <li>-accessLog: The file to write access logs to. Use 'stdout' for standard out</li>
 *     <li>-accessLogFormat: The access log message format. One of 'text', 'binary' or 'compact'</li>
 * </ul>
 *
 * The format of the MMS configuration file can be seen from the default {@code src/main/resources/mms.conf}
 * configuration file.
 *
 * @author Kasper Nielsen
 */
public class MmsServerConfiguration implements AccessLogConfiguration {

    /** The default port this server is running on. */
    public static final int DEFAULT_PORT = 43234;

    /** The id of the server, hard coded for now */
    ServerId id = new ServerId(1);

    @Parameter(names = "-conf", description = "Path to configuration file", converter = FileConverter.class)
    File confFile;

    @Parameter(names = "-port", description = "The port to listen for REST and MMS connections on")
    Integer port;

    @Parameter(names = "-securePort", description = "The secure port to listen for REST MMS connections on")
    Integer securePort;

    @Parameter(names = "-accessLog", description = "The file to write access logs to. Use 'stdout' for standard out")
    String accessLog;

    @Parameter(names = "-accessLogFormat", description = "The access log message format. One of 'text', 'binary' or 'compact'",
            converter = AccessLogFormatConverter.class)
    AccessLogFormat accessLogFormat;

    /**
     * @return the id
     */
    public ServerId getId() {
        return id;
    }

    /**
     * @return the security configuration file
     */
    public File getConfFile() {
        return confFile;
    }

    /** {@inheritDoc} */
    @Override
    public String getAccessLog() {
        return accessLog;
    }

    /** {@inheritDoc} */
    @Override
    public AccessLogFormat getAccessLogFormat() {
        return accessLogFormat;
    }

    /**
     * @return the securePort
     */
    public Integer getSecurePort() {
        return securePort;
    }

    /**
     * @return the serverPort
     */
    public Integer getServerPort() {
        return port;
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
     * @param accessLog
     *            the accessLog to set
     */
    public void setAccessLog(String accessLog) {
        this.accessLog = accessLog;
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
     * Reads any file configuration specified by a "-conf" parameter
     * @return the file configuration
     */
    private Config readFileConfiguration() {
        // Load the template mms.conf configuration file
        Config fileConf = ConfigFactory.load("mms").resolve();

        // If a "-conf" parameter has been specified, load and resolve the file
        fileConf = confFile != null && confFile.exists()
                ? ConfigFactory.parseFile(confFile).withFallback(fileConf).resolve()
                : fileConf;

        // Command line parameters takes precedence over configuration file parameters
        if (port == null && fileConf.hasPath("port")) {
            port = fileConf.getInt("port");
        }
        if (securePort == null && fileConf.hasPath("secure-port")) {
            securePort = fileConf.getInt("secure-port");
        }
        if (accessLog == null && fileConf.hasPath("access-log")) {
            accessLog = fileConf.getString("access-log");
        }
        if (accessLogFormat == null && fileConf.hasPath("access-log-format")) {
            accessLogFormat = new AccessLogFormatConverter().convert(fileConf.getString("access-log-format"));
        }

        return fileConf;
    }

    /**
     * Creates a new instance of this class.
     */
    public MmsServer build() {

        // Check that either port or securePort is defined
        if (port == null && securePort == null) {
            port = DEFAULT_PORT;
        }

        // Read any specified configuration file
        Config fileConfig = readFileConfiguration();
        Config securityConfig = fileConfig.hasPath("security-conf")
                ? fileConfig.getConfig("security-conf")
                : ConfigFactory.empty();

        MyConfiguration conf = new MyConfiguration();

        conf.withThreads().addPool(Executors.newFixedThreadPool(5));
        conf.addService(this);
        conf.addService(requireNonNull(getId()));

        conf.addService(ClientManager.class);
        conf.addService(ClientReaper.class);
        conf.addService(DefaultTransportListener.class);

        conf.addService(new ServerEventListener() {});

        conf.addService(PositionTracker.class);
        conf.addService(WebServer.class);

        conf.addService(ServerServices.class);
        conf.addService(MmsServerConnectionBus.class);
        conf.addService(ServerBroadcastManager.class);
        conf.addService(ServerEndpointManager.class);
        conf.addService(AccessLogManager.class);
        conf.addService(MetricRegistry.class);
        conf.addService(new MmsSecurityManager(securityConfig));
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

    /** Parses the accessLogFormat parameter into an AccessLogFormat enum value */
    public static class AccessLogFormatConverter implements IStringConverter<AccessLogFormat> {

        /** {@inheritDoc} */
        @Override
        public AccessLogFormat convert(String value) {
            try {
                return AccessLogFormat.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ParameterException("'" + value + "' is not a valid access log format value. Valid options: "
                    + Arrays.stream(AccessLogFormat.values())
                        .map(v -> v.toString().toLowerCase())
                        .collect(Collectors.joining(", ")));
            }
        }
    }

    /** Converts JCommander argument to a file */
    public static class FileConverter implements IStringConverter<File> {
        @Override
        public File convert(String value) {
            return value == null ? null : new File(value);
        }
    }
}
