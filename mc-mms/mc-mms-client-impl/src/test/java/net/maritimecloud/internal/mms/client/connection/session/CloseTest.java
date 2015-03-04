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
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.MoreAsserts;
import net.maritimecloud.internal.mms.messages.Close;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.util.concurrent.CompletableFuture;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class CloseTest extends AbstractSessionTest {

    @Test
    public void closeNormally() throws Exception {
        CountDownLatch closed = new CountDownLatch(1);
        connectedNormallySessionCloseConsumer = e -> {
            assertEquals(MmsConnectionClosingCode.NORMAL.getId(), e.getId());
            closed.countDown();
        };

        Session s = connectNormally(e -> {});
        s.closeSession(MmsConnectionClosingCode.NORMAL);

        Close cl = t.take(Close.class);
        assertEquals(0L, cl.getLastReceivedMessageId().longValue());

        // Close websocket connection normally
        t.closeNormally();

        assertTrue(closed.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void noReceiveAfterClose() throws Exception {
        Session s = connectNormally(e -> {
            throw new AssertionError();
        });

        s.closeSession(MmsConnectionClosingCode.NORMAL);
        t.send(new Broadcast());
        t.take(Close.class);

        t.closeNormally();

    }

    @Test
    public void closeNormallyWithMessagesSent() throws Exception {
        CountDownLatch closed = new CountDownLatch(1);
        connectedNormallySessionCloseConsumer = e -> {
            assertEquals(MmsConnectionClosingCode.NORMAL.getId(), e.getId());
            closed.countDown();
        };

        Session s = connectNormally(e -> {});

        // Lets send some messages
        for (int i = 1; i < 100; i++) {
            CompletableFuture<Void> cf = new CompletableFuture<>();
            s.sendMessage(new Broadcast().setSenderId("abc" + i), cf);

            MmsMessage mm = t.t();
            assertEquals(i, mm.getMessageId());
        }
        t.send(new Broadcast().setSenderId("abc"), 1, 40);
        t.send(new Broadcast().setSenderId("abc"), 2, 60);


        MoreAsserts.assertTrueWithin(() -> s.latestReceivedId == 2, 2, TimeUnit.SECONDS);
        // if th

        s.closeSession(MmsConnectionClosingCode.NORMAL);

        Close cl = t.take(Close.class);
        assertEquals(2L, cl.getLastReceivedMessageId().longValue());

        // Close websocket connection normally
        t.closeNormally();

        assertTrue(closed.await(2, TimeUnit.SECONDS));
    }
}
