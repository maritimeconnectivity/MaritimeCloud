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

    @Parameter(names = "-port", description = "The (unsecure) port to listen for MMS connections on")
    int port = DEFAULT_PORT;

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
     * @param id
     *            the id to set
     * @return this configuration
     */
    public MmsServerConfiguration setId(ServerId id) {
        this.id = id;
        return this;
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
