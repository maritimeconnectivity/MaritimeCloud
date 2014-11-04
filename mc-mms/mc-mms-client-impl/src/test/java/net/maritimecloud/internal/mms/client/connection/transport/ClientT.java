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

import java.net.URI;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class ClientT {

    public static void main(String[] args) throws Exception {
        int id = ThreadLocalRandom.current().nextInt(1000);
        WebSocketClient client = new WebSocketClient();
        client.start();
        final AtomicReference<Session> r = new AtomicReference<>();
        WebSocketAdapter a = new WebSocketAdapter() {
            public void onWebSocketConnect(Session sess) {
                r.set(sess);
            }
        };

        client.connect(a, new URI("ws://localhost:61616")).get();
        r.get().getRemote().sendString("Hi from " + id);

        Thread.sleep(1000);
        r.get().close(1001, "foo");
        Thread.sleep(1000);
    }
}
