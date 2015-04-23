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

import net.maritimecloud.net.mms.MmsConnection;

/**
 * A ConnectionTransportManager takes care of creating new ConnectionTransport. This class mainly exists because we are
 * using different web socket implementations. For example, we do not use Jetty when deployed in a Java EE container.
 *
 * @author Kasper Nielsen
 */
@SuppressWarnings("unchecked")
public abstract class ClientTransportFactory {

    static final Class<? extends ClientTransportFactory> FACTORY;

    static final String PREFIX = ClientTransportFactory.class.getCanonicalName();

    static {
        final String name;
        if (classExists(PREFIX + "AndroidNotImplementedYet")) {
            name = PREFIX + "AndroidNotImplementedYet";
        } else if (classExists("org.eclipse.jetty.websocket.jsr356.ClientContainer")) {
            name = PREFIX + "Jetty";
        } else {
            name = PREFIX + "Jsr356";
        }
        try {
            FACTORY = (Class<? extends ClientTransportFactory>) Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
    }

    /**
     * Creates a new transport
     *
     * @param transportListener the transport listener
     * @param connectionListener the connection listener
     * @return the transport
     */
    public abstract ClientTransport create(ClientTransportListener transportListener,
            MmsConnection.Listener connectionListener);

    static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Creates a new connection transport factory.
     *
     * @return a new connection transport factory
     */
    public static ClientTransportFactory create() {
        try {
            return FACTORY.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }
    }
}
