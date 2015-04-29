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
package net.maritimecloud.mms.server.connection.client;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.connectionold.ServerConnection;
import net.maritimecloud.mms.server.endpoints.ServerClientEndpointManager;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public class Client {

    private volatile ServerConnection activeConnection;

    volatile InnerClient client;

    final ClientManager clientManager;

    final ServerClientEndpointManager endpointManager;

    /** The unique if of the client. */
    private final MaritimeId id;

    /** The latest reported time and position. */
    private volatile PositionTime latestPosition;

    private volatile ClientProperties properties = new ClientProperties(null, null, null);

    final ReentrantLock retrieveLock = new ReentrantLock();

    final ReentrantLock sendLock = new ReentrantLock();

    public Client(ClientManager clientManager, MaritimeId id) {
        this.id = requireNonNull(id);
        this.clientManager = requireNonNull(clientManager);
        endpointManager = new ServerClientEndpointManager(this);
    }

    public void fullyLock() {
        retrieveLock.lock();
        sendLock.lock();
    }

    public void fullyUnlock() {
        sendLock.unlock();
        retrieveLock.unlock();
    }

    /**
     * @return the connection
     */
    public ServerConnection getActiveConnection() {
        return activeConnection;
    }

    /**
     * @return the endpointManager
     */
    public ServerClientEndpointManager getEndpointManager() {
        return endpointManager;
    }

    /**
     * Returns the unique id of the client.
     *
     * @return the unique id of the client
     */
    public MaritimeId getId() {
        return id;
    }

    /**
     * @return the latestPosition
     */
    public PositionTime getLatestPosition() {
        return latestPosition;
    }

    /**
     * @return the properties
     */
    public ClientProperties getProperties() {
        return properties;
    }

    /**
     * @return whether or not we are connected
     */
    public boolean isConnected() {
        return activeConnection != null;
    }

    public void sendIfConnected(Message cm) {
        ServerConnection c = getActiveConnection();
        if (c != null) {
            c.messageSend(cm);
        }
    }

    /**
     * @param connegction
     *            the connegction to set
     */
    public void setConnection(ServerConnection connegction) {
        this.activeConnection = connegction;
    }

    /**
     * @param latestPosition
     *            the latestPosition to set
     */
    public void setLatestPosition(PositionTime latestPosition) {
        this.latestPosition = latestPosition;
        // previously we reported the updated position immediately.
        // now we sample every second, can be removed at some point
        // clientManager.reportPosition(this, latestPosition);
    }

    /**
     * @param properties
     *            the properties to set
     */
    public void setProperties(ClientProperties properties) {
        this.properties = requireNonNull(properties);
    }
}
