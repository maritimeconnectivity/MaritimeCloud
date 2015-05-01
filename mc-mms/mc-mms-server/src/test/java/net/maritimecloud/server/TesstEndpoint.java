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
package net.maritimecloud.server;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.mms.transport.MmsWireProtocol;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;

/**
 *
 * @author Kasper Nielsen
 */
@ClientEndpoint
public class TesstEndpoint {
    int connectIdCount;

    public BlockingQueue<MmsMessage> m = new SynchronousQueue<>();

    boolean queueEnabled = true;

    Session session;

    final CountDownLatch closed = new CountDownLatch(1);

    volatile CloseReason closeReason;

    public void close() throws IOException {
        session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "suckit"));
    }
    public CloseReason awaitClosed() {
        try {
            closed.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
        return closeReason;
    }
    @OnClose
    public final void onClose(CloseReason reason) {
        closeReason = reason;
        closed.countDown();
    }

    @OnMessage
    public final void onTextMessageReceived(String msg) throws InterruptedException {
        System.out.println("TEST: Received text: " + msg);
        MmsMessage tm = MmsMessage.parseTextMessage(msg);
        // System.out.println("GOT " + tm);
        m.put(tm);
    }

    @OnMessage
    public final void onBinaryMessageReceived(byte[] msg) throws InterruptedException, IOException {
        MmsMessage tm = MmsMessage.parseBinaryMessage(msg);
        System.out.println("TEST: Received binary: " + tm.toText());
        // System.out.println("GOT " + tm);
        m.put(tm);
    }

    @OnOpen
    public final void onWebsocketOpen(Session session) {
        this.session = session;
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
        if (!mms.isConnectionMessage()) {
            String msg = m.toJSON();
            MessageSerializer<?> p = MessageHelper.getSerializer(m);
            // MessageSerializer<?> p = (MessageSerializer<?>) m.getClass().getField("PARSER").get(null);

            MessageSerializer.readFromJSON(p, msg);
            // assertEquals(m, des);
        }

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


    public void sendBroadcast() {

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

    public <T extends Message> T take(Class<T> c) throws InterruptedException {
        return requireNonNull(c.cast(m.poll(5, TimeUnit.SECONDS).getM()));
    }
}
