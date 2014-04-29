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

import static org.junit.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.messages.Connected;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.MaritimeCloudClientConfiguration;

import org.junit.After;
import org.junit.Before;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 *
 * @author Kasper Nielsen
 */
public class AbstractClientConnectionTest {
    public static final MaritimeId ID1 = MaritimeId.create("mmsi://1");

    public static final MaritimeId ID2 = MaritimeId.create("mmsi://2");

    public static final MaritimeId ID3 = MaritimeId.create("mmsi://3");

    public static final MaritimeId ID4 = MaritimeId.create("mmsi://4");

    public static final MaritimeId ID5 = MaritimeId.create("mmsi://5");

    public static final MaritimeId ID6 = MaritimeId.create("mmsi://6");


    protected TestClientEndpoint t;

    int clientPort;

    MaritimeCloudClientConfiguration conf;

    TestWebSocketServer ws;

    MaritimeCloudClient client;

    @Before
    public void before() {
        clientPort = ThreadLocalRandom.current().nextInt(40000, 50000);
        ws = new TestWebSocketServer(clientPort);
        ws.start();
        t = ws.addEndpoint(new TestClientEndpoint());
        conf = MaritimeCloudClientConfiguration.create(ID1);
        conf.setHost("localhost:" + clientPort);
        conf.setKeepAlive(1, TimeUnit.HOURS);
    }

    protected MaritimeCloudClient create() {
        MaritimeCloudClient c = conf.build();
        return client = c;
    }

    protected MaritimeCloudClient createAndConnect() throws InterruptedException {
        MaritimeCloudClient c = conf.build();
        t.m.take();
        t.send(new Connected().setConnectionId("ABC").setLastReceivedMessageId(0L));
        assertTrue(c.connection().awaitConnected(1, TimeUnit.SECONDS));
        assertTrue(c.connection().isConnected());
        return client = c;
    }


    @After
    public void after() throws Exception {
        if (client != null) {
            client.close();
            assertTrue(client.awaitTermination(5, TimeUnit.SECONDS));
        }
        ws.stop();
    }


    protected static String persist(Object o) {
        ObjectMapper om = new ObjectMapper();
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            return om.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not be persisted", e);
        }
    }

    protected static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    protected static String persistAndEscape(Object o) {
        return escape(persist(o));
    }
}
