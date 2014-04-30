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

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import net.maritimecloud.internal.messages.TransportMessage;
import net.maritimecloud.internal.net.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.net.client.broadcast.stubs.HelloWorld;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastSend;
import net.maritimecloud.messages.Connected;
import net.maritimecloud.net.MaritimeCloudClient;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class ContinuesReconnectTest extends AbstractClientConnectionTest {

    static final int NUMBER_OF_MESSAGES = 100;

    @Test
    public void test() throws Exception {
        final AtomicInteger ack = new AtomicInteger();
        MaritimeCloudClient c = createAndConnect();

        Runnable closer = new Runnable() {
            @SuppressWarnings("synthetic-access")
            public void run() {
                while (ack.get() < NUMBER_OF_MESSAGES) {
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextLong(1000));
                        t.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(closer).start();

        Runnable server = new Runnable() {
            @SuppressWarnings("synthetic-access")
            public void run() {
                int latestId = 0;
                Integer ackIt = null;
                while (ack.get() < NUMBER_OF_MESSAGES) {
                    TransportMessage tm = null;
                    try {
                        tm = t.take(TransportMessage.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (tm instanceof BroadcastSend) {
                        BroadcastSend bs = (BroadcastSend) tm;
                        try {
                            HelloWorld hw = (HelloWorld) bs.tryRead();
                            int id = Integer.parseInt(hw.getMessage());
                            assertEquals(++latestId, id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ackIt = null;
                    } else {
                        if (ackIt == null) {
                            ackIt = ThreadLocalRandom.current().nextInt(ack.get(), latestId + 1);
                            // System.out.println(ack.get() + " " + (latestId + 1) + "" + ackIt);
                        }
                        ack.set(ackIt);
                        latestId = ackIt;
                        System.out.println("CONNECTED  " + ackIt);
                        t.send(new Connected().setConnectionId("ABC").setLastReceivedMessageId((long) ackIt));
                    }
                }
            }
        };
        new Thread(server).start();


        for (int i = 0; i < NUMBER_OF_MESSAGES; i++) {
            Thread.sleep(ThreadLocalRandom.current().nextLong(100));
            c.broadcast(new HelloWorld("" + (i + 1)));
        }

    }
}
