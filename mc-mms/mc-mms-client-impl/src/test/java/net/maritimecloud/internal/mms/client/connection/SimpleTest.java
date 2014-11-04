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
package net.maritimecloud.internal.mms.client.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.ClientInfo;
import net.maritimecloud.internal.mms.client.connection.ClientConnection;
import net.maritimecloud.internal.mms.messages.Close;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.net.messages.BroadcastAck;
import net.maritimecloud.util.Binary;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class SimpleTest extends AbstractConnectionTest {

    @Test
    public void connect() throws Exception {
        ClientConnection cc = new ClientConnection(ctm, new ClientInfo(conf), conf);
        assertFalse(cc.isEnabled());
        assertFalse(cc.isConnected());

        cc.setEnabled(true);
        assertTrue(cc.isEnabled());

        t.take(Hello.class);
        t.send(new Connected().setSessionId(Binary.random(32)));
        assertTrue(cc.await(true, 2, TimeUnit.SECONDS));
        assertTrue(cc.isConnected());
        closeNormally(cc);
    }

    @Test
    public void close() throws Exception {
        ClientConnection cc = connectNormally();
        assertTrue(cc.await(true, 2, TimeUnit.SECONDS));
        cc.setEnabled(false);

        t.take(Close.class);
        t.closeNormally();

        assertTrue(cc.await(false, 2, TimeUnit.SECONDS));
    }

    @Test
    public void receiveMsg() throws Exception {
        ClientConnection cc = connectNormally();

        CountDownLatch ok = new CountDownLatch(1);

        cc.subscribe(Broadcast.class, (m, b) -> {
            assertEquals(2, m.getMessageId());
            assertEquals(0, m.getLatestReceivedId());
            assertEquals("adssd", b.getSenderId());
            ok.countDown();
        });

        t.send(new BroadcastAck(), 1, 0);
        t.send(new Broadcast().setSenderId("adssd"), 2, 0);

        assertTrue(ok.await(2, TimeUnit.SECONDS));

        closeNormally(cc);
    }


    @Test(expected = IllegalStateException.class)
    public void sendFailDisabled() throws Exception {
        ClientConnection cc = new ClientConnection(ctm, new ClientInfo(conf), conf);
        cc.sendMessage(new Broadcast());
    }


    @Test
    public void sendMsg() throws Exception {
        ClientConnection cc = connectNormally();
        CompletableFuture<Void> f = cc.sendMessage(new Broadcast().setSenderId("foo"));

        assertEquals("foo", t.take(Broadcast.class).getSenderId());
        t.send(new Broadcast(), 1, 1); // notify that we have received the msg

        f.get(1, TimeUnit.SECONDS); // acked

        closeNormally(cc);
    }
}
