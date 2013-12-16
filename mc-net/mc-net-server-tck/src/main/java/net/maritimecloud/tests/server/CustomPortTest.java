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
package net.maritimecloud.tests.server;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.net.client.broadcast.stubs.HelloWorld;
import net.maritimecloud.internal.net.server.InternalServer;
import net.maritimecloud.internal.net.server.ServerConfiguration;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.MaritimeCloudClientConfiguration;

import org.junit.Test;

/**
 * Tests that we can run both the server and the client on a custom port.
 * 
 * @author Kasper Nielsen
 */
public class CustomPortTest {

    @Test
    public void testNonDefaultPort() throws Exception {
        ServerConfiguration sc = new ServerConfiguration();
        sc.setServerPort(12445);
        InternalServer server = new InternalServer(sc);
        server.start();
        MaritimeCloudClientConfiguration b = MaritimeCloudClientConfiguration.create("mmsi://1234");
        b.setHost("localhost:12445");
        System.out.println("a");
        try (MaritimeCloudClient c = b.build()) {
            System.out.println("b");
            c.broadcast(new HelloWorld());
        }
        System.out.println("c");
        server.shutdown();
        server.awaitTerminated(1, TimeUnit.SECONDS);
    }
}
