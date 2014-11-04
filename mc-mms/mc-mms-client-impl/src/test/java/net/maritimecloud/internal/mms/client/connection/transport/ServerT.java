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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class ServerT extends WebSocketAdapter {
    public static final boolean IS_ASYNC = true; // change this flag

    static CopyOnWriteArraySet<Session> s = new CopyOnWriteArraySet<>();

    /** {@inheritDoc} */
    @Override
    public void onWebSocketConnect(Session sess) {
        s.add(sess);
        try {
            sess.getRemote().sendString("ASDSD");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onWebSocketClose(int statusCode, String reason) {}

    /** {@inheritDoc} */
    @Override
    public void onWebSocketText(final String message) {
        for (final Session ses : s) {
            if (IS_ASYNC) {
                new Thread() {
                    public void run() {
                        submit(ses, message);
                    }
                }.start();
            } else {
                submit(ses, message);
            }
        }
    }

    static void submit(Session ses, String message) {
        try {
            ses.getRemote().sendString(message);
        } catch (Exception ignore) {}
    }

    public static void main(String[] args) throws Exception {
        WebSocketHandler wsHandler = new WebSocketHandler() {
            public void configure(WebSocketServletFactory factory) {
                factory.setCreator(new WebSocketCreator() {
                    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
                        return new ServerT();
                    }
                });
            }
        };
        Server server = new Server(new InetSocketAddress(61616));
        server.setHandler(wsHandler);
        server.start();
        Thread.sleep(10000000);
    }
}
