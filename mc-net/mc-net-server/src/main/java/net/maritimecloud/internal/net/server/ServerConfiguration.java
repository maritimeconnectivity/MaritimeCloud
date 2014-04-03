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
package net.maritimecloud.internal.net.server;

import net.maritimecloud.core.id.ServerId;

import com.beust.jcommander.Parameter;

/**
 *
 * @author Kasper Nielsen
 */
public class ServerConfiguration {

    /** The default port this server is running on. */
    public static final int DEFAULT_PORT = 43234;

    /** The default port the web server is running on. */
    public static final int DEFAULT_WEBSERVER_PORT = 9090;

    /** The id of the server, hard coded for now */
    ServerId id = new ServerId(1);

    @Parameter(names = "-port", description = "The port to listen on")
    int port = ServerConfiguration.DEFAULT_PORT;

    @Parameter(names = "-rest", description = "The webserver port for the administrative interface")
    int webserverport = -1;

    /**
     * @return the id
     */
    public ServerId getId() {
        return id;
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
     */
    public ServerConfiguration setId(ServerId id) {
        this.id = id;
        return this;
    }

    public ServerConfiguration setServerPort(int port) {
        this.port = port;
        return this;
    }

    public ServerConfiguration setWebserverPort(int webserverport) {
        this.webserverport = webserverport;
        return this;
    }

    public static ServerConfiguration from(int port) {
        ServerConfiguration conf = new ServerConfiguration();
        conf.setServerPort(port);
        return conf;
    }
}
