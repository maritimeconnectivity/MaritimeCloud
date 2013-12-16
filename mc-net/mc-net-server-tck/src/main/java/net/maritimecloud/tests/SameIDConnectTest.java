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
package net.maritimecloud.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.net.MaritimeCloudClient;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests that if multiple clients connect with the same id connects. Only one is connected at the same time
 * 
 * @author Kasper Nielsen
 */
public class SameIDConnectTest extends AbstractNetworkTest {

    /**
     * Tests that an existing client will disconnect
     * 
     * @throws Exception
     */
    @Test
    public void twoConnect() throws Exception {
        MaritimeCloudClient pc1 = newClient(ID1);
        pc1.connection().awaitConnected(1, TimeUnit.SECONDS);

        MaritimeCloudClient pc2 = newClient(ID1);
        pc2.connection().awaitConnected(1, TimeUnit.SECONDS);

        // pc1 should be disconnected now
        assertEquals(1, si.info().getConnectionCount());
        pc1.connection().awaitDisconnected(1, TimeUnit.SECONDS);
        assertTrue(pc2.connection().isConnected());
        assertEquals(1, si.info().getConnectionCount());
    }

    @Test
    @Ignore
    public void manyConnect() throws Exception {
        ExecutorService e = Executors.newFixedThreadPool(10);
        final Set<Future<MaritimeCloudClient>> s = Collections
                .newSetFromMap(new ConcurrentHashMap<Future<MaritimeCloudClient>, Boolean>());
        for (int i = 0; i < 100; i++) {
            s.add(e.submit(new Callable<MaritimeCloudClient>() {
                public MaritimeCloudClient call() throws Exception {
                    return newClient(ID1);
                }
            }));
        }

        Set<MaritimeCloudClient> con = new HashSet<>();
        for (Future<MaritimeCloudClient> f : s) {
            try {
                con.add(f.get());
            } catch (ExecutionException ignore) {}
        }
        for (int i = 0; i < 100; i++) {
            if (si.info().getConnectionCount() == 1) {
                int connectCount = 0;
                for (MaritimeCloudClient pc : con) {
                    if (pc.connection().isConnected()) {
                        connectCount++;
                    }
                }
                assertEquals(1, connectCount);
                return;
            }
            Thread.sleep(15);
        }
        fail("Number of connections = " + si.info().getConnectionCount());

        // TODO when we have asynchronous connect, there should be exactly 100 connections
    }
}
