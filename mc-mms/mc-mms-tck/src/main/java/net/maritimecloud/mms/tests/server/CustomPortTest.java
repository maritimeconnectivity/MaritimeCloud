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
package net.maritimecloud.mms.tests.server;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.mms.server.MmsServer;
import net.maritimecloud.mms.server.MmsServerConfiguration;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;

import org.junit.Test;

/**
 * Tests that we can run both the server and the client on a custom port.
 *
 * @author Kasper Nielsen
 */
public class CustomPortTest {

    @Test
    public void testNonDefaultPort() throws Exception {
        MmsServerConfiguration sc = new MmsServerConfiguration();
        sc.setServerPort(12445);
        MmsServer server = new MmsServer(sc);
        server.startBlocking();
        MmsClientConfiguration b = MmsClientConfiguration.create("mmsi:1234");
        b.setHost("localhost:12445");
        System.out.println("a");
        MmsClient c = b.build();
        System.out.println("b");
        c.broadcast(new BroadcastTestMessage());

        System.out.println("c");
        server.shutdown();
        server.awaitTerminated(1, TimeUnit.SECONDS);
    }
}
