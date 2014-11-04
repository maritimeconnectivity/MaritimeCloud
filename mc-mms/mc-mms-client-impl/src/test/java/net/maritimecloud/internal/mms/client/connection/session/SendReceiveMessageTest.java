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
package net.maritimecloud.internal.mms.client.connection.session;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class SendReceiveMessageTest extends AbstractSessionTest {

    @Test
    public void sendMessages() throws Exception {
        Session s = connectNormally(msg -> {});

        // First msg
        s.sendMessage(new Broadcast().setSenderId("abc"), new CompletableFuture<>());

        MmsMessage mm = t.t();
        assertEquals(1, mm.getMessageId());
        assertEquals(0, mm.getLatestReceivedId());
        assertEquals("abc", mm.cast(Broadcast.class).getSenderId());


        // next messages
        for (int i = 1; i < 100; i++) {
            s.sendMessage(new Broadcast().setSenderId("abcd" + i), new CompletableFuture<>());

            mm = t.t();
            assertEquals(i + 1, mm.getMessageId());
            assertEquals(0, mm.getLatestReceivedId());
            assertEquals("abcd" + i, mm.cast(Broadcast.class).getSenderId());
        }
        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }

    @Test
    public void receiveMessages() throws Exception {
        BlockingQueue<MmsMessage> q = new LinkedBlockingQueue<>();

        Session s = connectNormally(msg -> q.add(msg));

        // First msg
        t.send(new Broadcast().setSenderId("abc"), 1, 0);

        MmsMessage mm = q.poll(2, TimeUnit.SECONDS);
        assertEquals(1, mm.getMessageId());
        assertEquals(0, mm.getLatestReceivedId());
        assertEquals("abc", mm.cast(Broadcast.class).getSenderId());

        for (int i = 2; i < 100; i++) {
            t.send(new Broadcast().setSenderId("abcd" + i), i, 0);

            mm = q.poll(2, TimeUnit.SECONDS);
            assertEquals(i, mm.getMessageId());
            assertEquals(0, mm.getLatestReceivedId());
            assertEquals("abcd" + i, mm.cast(Broadcast.class).getSenderId());
        }
        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }

    @Test
    public void receiveMessagesAckCheck() throws Exception {
        BlockingQueue<MmsMessage> q = new LinkedBlockingQueue<>();

        Session s = connectNormally(msg -> q.add(msg));

        t.send(new Broadcast().setSenderId("abc"), 1, 0);

        MmsMessage mm = q.poll(2, TimeUnit.SECONDS);
        assertEquals(1, mm.getMessageId());
        assertEquals(0, mm.getLatestReceivedId());
        assertEquals("abc", mm.cast(Broadcast.class).getSenderId());

        // We need to acquire this lock to make sure we are out of the receiving loop (last ack adjusted
        s.receiveLock.lock();
        s.receiveLock.unlock();

        CompletableFuture<Void> cf = new CompletableFuture<>();
        s.sendMessage(new Broadcast().setSenderId("cba"), cf);

        mm = t.t();
        assertEquals(1, mm.getMessageId());
        assertEquals(1, mm.getLatestReceivedId());
        assertEquals("cba", mm.cast(Broadcast.class).getSenderId());


        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }

    @Test
    public void receiveMessagesAckCheckMany() throws Exception {
        BlockingQueue<MmsMessage> q = new LinkedBlockingQueue<>();

        Session s = connectNormally(msg -> q.add(msg));

        for (int i = 0; i < 100; i++) {
            t.send(new Broadcast().setSenderId("abc"), i + 1, i);

            MmsMessage mm = q.poll(2, TimeUnit.SECONDS);
            assertEquals(i + 1, mm.getMessageId());
            assertEquals(i, mm.getLatestReceivedId());
            assertEquals("abc", mm.cast(Broadcast.class).getSenderId());

            // We need to acquire this lock to make sure we are out of the receiving loop (last ack adjusted
            s.receiveLock.lock();
            s.receiveLock.unlock();

            CompletableFuture<Void> cf = new CompletableFuture<>();
            s.sendMessage(new Broadcast().setSenderId("cba"), cf);

            mm = t.t();
            assertEquals(i + 1, mm.getMessageId());
            assertEquals(i + 1, mm.getLatestReceivedId());
            assertEquals("cba", mm.cast(Broadcast.class).getSenderId());
        }

        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }

    @Test
    public void sendMessageCompletable() throws Exception {
        BlockingQueue<MmsMessage> q = new LinkedBlockingQueue<>();
        Session s = connectNormally(msg -> q.add(msg));

        CompletableFuture<Void> cf = new CompletableFuture<>();
        s.sendMessage(new Broadcast().setSenderId("abc"), cf);
        t.t();// take and ignore

        t.send(new Broadcast().setSenderId("abc"), 1, 1);

        cf.get(2, TimeUnit.SECONDS);

        for (int i = 2; i < 100; i++) {
            cf = new CompletableFuture<>();
            s.sendMessage(new Broadcast().setSenderId("abc" + i), cf);

            MmsMessage mm = t.t();
            assertEquals(i, mm.getMessageId());
            t.send(new Broadcast().setSenderId("abc"), i, mm.getMessageId());

            cf.get(2, TimeUnit.SECONDS);
        }
        // if this breaks it is a timeing issue, just keep trying until its 99
        assertEquals(99, s.latestReceivedId);

        s.closeSession(MmsConnectionClosingCode.NORMAL);
    }
}
