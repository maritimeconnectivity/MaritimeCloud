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
package net.maritimecloud.internal.net.client;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.maritimecloud.core.id.ServerId;
import net.maritimecloud.internal.net.messages.TransportMessage;
import net.maritimecloud.internal.net.messages.TMHelpers;
import net.maritimecloud.internal.net.messages.auxiliary.WelcomeMessage;

/**
 *
 * @author Kasper Nielsen
 */
@ServerEndpoint(value = "/")
public class TestClientEndpoint {

    public BlockingQueue<TransportMessage> m = new ArrayBlockingQueue<>(10000);

    Session session;

    public void close() throws IOException {
        session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "TestClientEndpoint.close()"));
    }

    @OnMessage
    public final void messageReceived(String msg, Session userSession) throws InterruptedException, IOException {
        if (session != userSession) {
            throw new Error();
        }
        TransportMessage tm = TMHelpers.parseMessage(msg);
        m.put(tm);
    }

    @OnOpen
    public final void onWebsocketOpen(Session session) {
        this.session = session;
        m.clear();
        send(new WelcomeMessage(1, new ServerId(123), "enavServer/1.0"));

    }

    protected <T extends TransportMessage> T poll(Class<T> c) {
        return c.cast(m.poll());
    }

    public void send(TransportMessage m) {
        Basic r = session.getBasicRemote();
        try {
            r.sendText(m.toText());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public <T extends BlockingQueue<TransportMessage>> T setQueue(T q) {
        this.m = requireNonNull(q);
        return q;
    }

    public <T extends TransportMessage> T take(Class<T> c) throws InterruptedException {
        return requireNonNull(c.cast(m.poll(5, TimeUnit.SECONDS)));
    }
}
