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
package net.maritimecloud.internal.mms.client;

import net.maritimecloud.internal.mms.messages.Welcome;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.mms.transport.MmsWireProtocol;
import net.maritimecloud.message.Message;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.eclipse.jetty.websocket.common.io.AbstractWebSocketConnection;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 *
 * @author Kasper Nielsen
 */
@ServerEndpoint(value = "/")
public class TestClientEndpoint {

    public final CountDownLatch closed = new CountDownLatch(1);

    public BlockingQueue<MmsMessage> m = new ArrayBlockingQueue<>(10000);

    public CloseReason reason;

    Session session;

    public void disconnect() {
        WebSocketSession ss = (WebSocketSession) session;
        ss.getConnection().disconnect();
        AbstractWebSocketConnection cc = (AbstractWebSocketConnection) ss.getConnection();
        cc.getEndPoint().getTransport();// socket
        session = null;
    }

    public void closeNormally() throws IOException {
        session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "normal closure"));
    }

    public void close(int code, String reason) throws IOException {
        session.close(new CloseReason(() -> code, reason));
    }


    public void close() throws IOException {
        session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "TestClientEndpoint.close()"));
    }

    public void closeIt() {
        try {
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isOpen() {
        Session session = this.session;
        return session != null && session.isOpen();
    }

    @OnMessage
    public final void messageReceived(String msg, Session userSession) throws InterruptedException {
        if (session != userSession) {
            return;// ignore
        }
        MmsMessage tm = MmsMessage.parseTextMessage(msg);
        m.put(tm);
    }

    @OnMessage
    public final void messageReceived(byte[] msg, Session userSession) throws InterruptedException, IOException {
        if (session != userSession) {
            return;// ignore
        }
        MmsMessage tm = MmsMessage.parseBinaryMessage(msg);
        m.put(tm);
    }

    @OnClose
    public final void onClose(CloseReason reason) {
        this.reason = reason;
        closed.countDown();
    }

    @OnOpen
    public final void onWebsocketOpen(Session session) {
        this.session = session;
        m.clear();
        send(new Welcome().addProtocolVersion(1).setServerId("123").putProperties("implementation", "enavServer/1.0"));

    }

    protected <T extends Message> T poll(Class<T> c) {
        return c.cast(m.poll());
    }

    public void send(Message m) {
        send(m, 0, 0);
    }

    public void send(Message m, long msgId, long latestReceivedId) {
        MmsMessage mms = new MmsMessage();
        mms.setM(m);
        if (mms.isConnectionMessage()) {
            mms.setMessageId(msgId);
            mms.setLatestReceivedId(latestReceivedId);
        }
        Basic r = session.getBasicRemote();
        try {
            if (MmsWireProtocol.USE_BINARY) {
                r.sendBinary(ByteBuffer.wrap(mms.toBinary()));
            } else {
                r.sendText(mms.toText());
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public <T extends BlockingQueue<MmsMessage>> T setQueue(T q) {
        this.m = requireNonNull(q);
        return q;
    }

    public MmsMessage t() {
        try {
            return requireNonNull(m.poll(5, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Message> T take(Class<T> c) {
        try {
            return requireNonNull(c.cast(m.poll(5, TimeUnit.SECONDS).getM()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
