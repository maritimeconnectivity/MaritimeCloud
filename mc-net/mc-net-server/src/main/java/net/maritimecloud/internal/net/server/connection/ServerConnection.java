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
package net.maritimecloud.internal.net.server.connection;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import net.maritimecloud.internal.net.messages.ConnectionMessage;
import net.maritimecloud.internal.net.server.InternalServer;
import net.maritimecloud.internal.net.server.requests.ServerMessageBus;
import net.maritimecloud.internal.net.server.targets.Target;
import net.maritimecloud.net.ClosingCode;

/**
 * 
 * @author Kasper Nielsen
 */
public class ServerConnection {

    final ServerMessageBus bus;

    final String id = UUID.randomUUID().toString();

    final InternalServer is;

    final Target target;

    volatile ServerTransport transport;

    final Worker worker = new Worker(this);

    ServerConnection(Target target, InternalServer is) {
        this.target = requireNonNull(target);
        this.bus = requireNonNull(is.getService(ServerMessageBus.class));
        this.is = is;
    }

    /**
     * @return the target
     */
    public Target getTarget() {
        return target;
    }

    /**
     * @param serverTransport
     * @param m
     */
    void messageReceive(ServerTransport serverTransport, ConnectionMessage m) {
        if (serverTransport == this.transport) {
            worker.messageReceived(m);
        }
    }

    public OutstandingMessage messageSend(ConnectionMessage message) {
        return worker.messageSend(message);
    }

    /**
     * @param serverTransport
     * @param reason
     */
    void transportDisconnected(ServerTransport serverTransport, ClosingCode reason) {}
}
