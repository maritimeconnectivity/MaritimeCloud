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
package net.maritimecloud.internal.net.server;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.TestWebSocketServer;
import net.maritimecloud.internal.net.messages.auxiliary.ConnectedMessage;
import net.maritimecloud.internal.net.messages.auxiliary.HelloMessage;
import net.maritimecloud.internal.net.messages.auxiliary.WelcomeMessage;
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
public abstract class AbstractServerConnectionTest {

    public static final MaritimeId ID1 = MaritimeId.create("mmsi://1");

    public static final MaritimeId ID2 = MaritimeId.create("mmsi://2");

    public static final MaritimeId ID3 = MaritimeId.create("mmsi://3");

    public static final MaritimeId ID4 = MaritimeId.create("mmsi://4");

    public static final MaritimeId ID5 = MaritimeId.create("mmsi://5");

    public static final MaritimeId ID6 = MaritimeId.create("mmsi://6");

    private CopyOnWriteArraySet<TesstEndpoint> allClient = new CopyOnWriteArraySet<>();

    int clientPort;

    MaritimeCloudClientConfiguration conf;

    InternalServer server;

    TestWebSocketServer ws;

    @After
    public void after() throws Exception {
        server.shutdown();
        assertTrue(server.awaitTerminated(5, TimeUnit.SECONDS));
        for (TesstEndpoint te : allClient) {
            te.close();
        }
    }

    @Before
    public void before() throws Exception {
        clientPort = ThreadLocalRandom.current().nextInt(40000, 50000);
        ServerConfiguration sc = new ServerConfiguration();
        sc.setServerPort(clientPort);
        server = new InternalServer(sc);
        server.start();
    }

    protected TesstEndpoint newClient() throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        TesstEndpoint t = new TesstEndpoint();
        container.connectToServer(t, new URI("ws://localhost:" + clientPort));
        allClient.add(t);
        return t;
    }

    protected TesstEndpoint newClient(MaritimeId id) throws Exception {
        return newClient(id, 1, 1);
    }

    protected TesstEndpoint newClient(MaritimeId id, double latitude, double longitude) throws Exception {
        TesstEndpoint t = newClient();
        t.take(WelcomeMessage.class);
        t.send(new HelloMessage(id, "foo", "", 0, latitude, longitude));
        t.take(ConnectedMessage.class);
        return t;
    }


    protected static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
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

    protected static String persistAndEscape(Object o) {
        return escape(persist(o));
    }

}
