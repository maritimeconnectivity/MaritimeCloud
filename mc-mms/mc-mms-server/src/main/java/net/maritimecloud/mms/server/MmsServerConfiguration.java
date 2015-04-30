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

import net.maritimecloud.core.id.ServerId;

import com.beust.jcommander.Parameter;

/**
 *
 * @author Kasper Nielsen
 */
public class MmsServerConfiguration {

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

    @Parameter(names = "-accessLogs", description = "The directory to write access logs into")
    String logRequestDirectory;

    @Parameter(names = "-port", description = "The port to listen for MMS connections on")
    int port = DEFAULT_PORT;

    @Parameter(names = "-requireTLS", description = "if true clients will not be able to connect without TLS")
    boolean requireTLS = false;

    @Parameter(names = "-secureport", description = "The secure port to listen for MMS connections on")
    int secureport = DEFAULT_SECURE_PORT;

    @Parameter(names = "-rest", description = "The webserver port for the administrative interface")
    int webserverport = -1;

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

    /**
     * @return the logRequestDirectory
     */
    public String getLogRequestDirectory() {
        return logRequestDirectory;
    }

    /**
     * @return the secureport
     */
    public int getSecureport() {
        return secureport;
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
        return webserverport;
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
     * @param logRequestDirectory
     *            the logRequestDirectory to set
     */
    public void setLogRequestDirectory(String logRequestDirectory) {
        this.logRequestDirectory = logRequestDirectory;
    }

    /**
     * @param requireTLS
     *            the requireTLS to set
     */
    public void setRequireTLS(boolean requireTLS) {
        this.requireTLS = requireTLS;
    }

    /**
     * @param secureport
     *            the secureport to set
     */
    public void setSecureport(int secureport) {
        this.secureport = secureport;
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
        this.webserverport = webserverport;
        return this;
    }
}
