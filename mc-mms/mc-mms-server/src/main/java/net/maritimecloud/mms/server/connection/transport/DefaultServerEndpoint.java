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
package net.maritimecloud.mms.server.connection.transport;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * The endpoint taking care of creating server transports.
 *
 * @author Kasper Nielsen
 */
@ServerEndpoint(value = "/")
public class DefaultServerEndpoint {

    /** The connection is attached to, or null, if it is not attached to one. */
    private volatile ServerTransport transport;

    /** A factory for creating server transports. */
    private final Supplier<ServerTransport> transportFactory;

    public DefaultServerEndpoint(Supplier<ServerTransport> transportFactory) {
        this.transportFactory = requireNonNull(transportFactory);
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
        transport.onClose(closeReason);
    }

    @OnOpen
    public void onOpen(Session session) {
        session.setMaxBinaryMessageBufferSize(10 * 1024 * 1024);
        transport = transportFactory.get();
        transport.onOpen(session);
    }

    @OnMessage
    public void onTextMessage(String textMessage) {
        transport.onTextMessage(textMessage);
    }

    @OnMessage
    public void onBinaryMessage(byte[] binaryMessage) {
        transport.onBinaryMessage(binaryMessage);
    }
}
