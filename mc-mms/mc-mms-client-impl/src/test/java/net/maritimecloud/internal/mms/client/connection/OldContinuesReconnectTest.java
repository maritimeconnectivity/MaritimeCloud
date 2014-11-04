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

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import net.maritimecloud.internal.mms.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.net.mms.MmsClient;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class OldContinuesReconnectTest extends AbstractClientConnectionTest {

    static final int NUMBER_OF_MESSAGES = 100;

    final SortedSet<Integer> received = Collections.synchronizedSortedSet(new TreeSet<>());

    @Test
    public void test() throws Exception {
        MmsClient c = createAndConnect();

        new Thread(() -> {
            while (received.size() < NUMBER_OF_MESSAGES) {
                rndSleep(300, TimeUnit.MILLISECONDS);
                t.closeIt();
            }
        }).start();

        final AtomicInteger ack = new AtomicInteger();

        Runnable server = new Runnable() {
            public void run() {
                int latestId = 0;
                Integer ackIt = null;
                while (received.size() < NUMBER_OF_MESSAGES) {
                    Message tm = t.take(Message.class);

                    if (tm instanceof Broadcast) {
                        Broadcast bs = (Broadcast) tm;
                        try {
                            BroadcastTestMessage hw = (BroadcastTestMessage) MmsMessage.tryRead(bs);
                            int id = hw.getId();
                            assertEquals(latestId++, id);
                            received.add(id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ackIt = null;
                    } else {
                        if (ackIt == null) {
                            ackIt = ThreadLocalRandom.current().nextInt(ack.get(), latestId + 1);
                        }
                        ack.set(ackIt);
                        latestId = ackIt;
                        t.send(new Connected().setSessionId(BIN1).setLastReceivedMessageId((long) ackIt));
                    }
                }
            }
        };
        Thread t = new Thread(server);
        t.setUncaughtExceptionHandler((a, b) -> b.printStackTrace());
        t.start();


        for (int i = 0; i < NUMBER_OF_MESSAGES; i++) {
            Thread.sleep(ThreadLocalRandom.current().nextLong(10));
            c.broadcast(new BroadcastTestMessage().setId(i));
        }

        while (received.size() < NUMBER_OF_MESSAGES) {
            Thread.sleep(10);
        }
    }
}
