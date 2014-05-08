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

import static java.util.Objects.requireNonNull;
import net.maritimecloud.net.ClosingCode;

/**
 * 
 * @author Kasper Nielsen
 */
class ClientDisconnectFuture implements Runnable {

    final ClientConnection connection;

    private final ConnectionTransport transport;

    ClientDisconnectFuture(ClientConnection connection, ConnectionTransport transport) {
        this.connection = requireNonNull(connection);
        this.transport = transport;
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        // TODO send poison pill
        transport.doClose(ClosingCode.NORMAL);
        connection.disconnected(this);
        // ClientTransport transport = new ClientTransport(connection, reconnectId);
        // ConnectionManager cm = connection.connectionManager;
        // cm.getWebsocketContainer().connectToServer(transport, cm.uri);
    }

    // void connect2() {
    //
    // ConnectionState state = this.state;
    // if (state == ConnectionState.NOT_CONNECTED) {
    // this.transport = new ClientTransport(this, -1);
    // this.state = ConnectionState.CONNECTING;
    // connectingFuture = connectionManager.threadManager.submit(new Callable<Void>() {
    // public Void call() throws Exception {
    // connect2();
    // return null;
    // }
    // });
    //
    // if (state != ConnectionState.CONNECTING) {
    // return;
    // }
    // LOG.info("Connecting to " + connectionManager.uri);
    // try {
    // connectionManager.getWebsocketContainer().connectToServer(transport, connectionManager.uri);
    // } catch (IOException e) {
    // e.printStackTrace();
    // } catch (DeploymentException e1) {
    // e1.printStackTrace();
    // }
    // }

}
