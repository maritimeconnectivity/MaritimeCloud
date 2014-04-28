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
package net.maritimecloud.internal.net.client.connection;

import org.picocontainer.Startable;

/**
 *
 * @author Kasper Nielsen
 */
@SuppressWarnings("unchecked")
public abstract class ConnectionTransportManager implements Startable {

    static final String TRANSPORT_MANAGER_JSR356 = ConnectionTransportManager.class.getPackage().getName()
            + ".ConnectionTransportManagerJsr356";

    static final String TRANSPORT_MANAGER_JETTY = ConnectionTransportManager.class.getPackage().getName()
            + ".ConnectionTransportManagerJetty";

    static final String TRANSPORT_MANAGER_ANDROID = ConnectionTransportManager.class.getPackage().getName()
            + ".ConnectionTransportManagerAndroidxx";

    static final Class<? extends ConnectionTransportManager> TRANSPORT_MANAGER;

    public abstract ConnectionTransport create(ClientConnection cc, ClientConnectFuture future);

    /** {@inheritDoc} */
    @Override
    public void start() {}

    /** {@inheritDoc} */
    @Override
    public void stop() {}

    static {
        final String name;
        if (exists(TRANSPORT_MANAGER_ANDROID)) {
            name = TRANSPORT_MANAGER_ANDROID;
        } else if (exists("org.eclipse.jetty.websocket.jsr356.ClientContainer")) {
            name = TRANSPORT_MANAGER_JETTY;
        } else {
            name = TRANSPORT_MANAGER_JSR356;
        }
        try {
            TRANSPORT_MANAGER = (Class<? extends ConnectionTransportManager>) Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
    }

    static boolean exists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    public static ConnectionTransportManager create() {
        try {
            return TRANSPORT_MANAGER.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
