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

import java.io.IOException;
import java.net.URI;

import net.maritimecloud.net.ClosingCode;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 *
 * @author Kasper Nielsen
 */
public class ConnectionTransportManagerAndroid extends ConnectionTransportManager {

    /** {@inheritDoc} */
    @Override
    public ConnectionTransport create(ClientConnection cc, ClientConnectFuture future) {
        return new JavaxWebsocketTransport(future, cc);
    }

    final class JavaxWebsocketTransport extends ConnectionTransport {
        private final WebSocketConnection mConnection = new WebSocketConnection();

        JavaxWebsocketTransport(ClientConnectFuture connectFuture, ClientConnection connection) {
            super(connectFuture, connection);
        }

        void doClose(ClosingCode reason) {
            this.mConnection.disconnect();
        }

        public void onClose(int code, String reasons) {
            ClosingCode reason = ClosingCode.create(code, reasons);
            this.connection.transportDisconnected(this, reason);
        }

        public void onTextMessage(String textMessage) {
            super.onTextMessage(textMessage);
        }

        public void sendText(String text) {
            if (this.mConnection.isConnected()) {
                if (text.length() < 1000) {
                    System.out.println("Sending : " + text);
                }

                this.mConnection.sendTextMessage(text);
            }
        }

        void connect(URI uri) throws IOException {
            try {
                this.mConnection.connect(uri.toString(), new WebSocketHandler() {
                    public void onClose(int code, String reason) {
                        JavaxWebsocketTransport.this.onClose(code, reason);
                    }

                    public void onTextMessage(String payload) {
                        JavaxWebsocketTransport.this.onTextMessage(payload);
                    }
                });
            } catch (WebSocketException e) {
                throw new IOException(e);
            }
        }
    }

}
