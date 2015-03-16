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

import static org.junit.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.util.Binary;

import org.junit.After;
import org.junit.Before;

/**
 *
 * @author Kasper Nielsen
 */
public class AbstractClientConnectionTest {
    public static final Binary BIN1 = Binary.random(32);

    public static final Binary BIN2 = Binary.random(32);

    public static final Binary BIN3 = Binary.random(32);

    public static final Binary BIN4 = Binary.random(32);

    public static final MaritimeId ID1 = MaritimeId.create("mmsi:1");

    public static final MaritimeId ID2 = MaritimeId.create("mmsi:2");

    public static final MaritimeId ID3 = MaritimeId.create("mmsi:3");

    public static final MaritimeId ID4 = MaritimeId.create("mmsi:4");

    public static final MaritimeId ID5 = MaritimeId.create("mmsi:5");

    public static final MaritimeId ID6 = MaritimeId.create("mmsi:6");

    public TestClientEndpoint t;

    int clientPort;

    protected MmsClientConfiguration conf;

    TestWebSocketServer ws;

    MmsClient client;

    @Before
    public void before() {
        ClientInfo.RECONNECT_TIME_DELAY = 0;
        clientPort = ThreadLocalRandom.current().nextInt(40000, 50000);
        ws = new TestWebSocketServer(clientPort);
        ws.start();
        t = ws.addEndpoint(new TestClientEndpoint());
        conf = MmsClientConfiguration.create(ID1);
        conf.setHost("localhost:" + clientPort);
        conf.setKeepAlive(1, TimeUnit.HOURS);
    }

    protected MmsClient create() {
        MmsClient c = conf.build();
        return client = c;
    }

    protected MmsClient createAndConnect() throws InterruptedException {
        MmsClient c = conf.build();
        t.m.take();
        t.send(new Connected().setSessionId(BIN1).setLastReceivedMessageId(0L));
        assertTrue(c.connection().awaitConnected(1, TimeUnit.SECONDS));
        assertTrue(c.connection().isConnected());
        return client = c;
    }

    public void rndSleep(long max, TimeUnit unit) {
        long millies = TimeUnit.NANOSECONDS.toMillis(ThreadLocalRandom.current().nextLong(unit.toNanos(max)));
        try {
            Thread.sleep(millies);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @After
    public void after() throws Exception {
        if (client != null) {
            client.shutdown();
            assertTrue(client.awaitTermination(5, TimeUnit.SECONDS));
        }
        ws.stop();
    }
}
