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

import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.server.InternalServer;
import net.maritimecloud.internal.net.server.ServerConfiguration;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.MaritimeCloudClientConfiguration;
import net.maritimecloud.util.function.Supplier;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.After;
import org.junit.Before;

import test.util.ProxyTester;

/**
 * 
 * @author Kasper Nielsen
 */
public abstract class AbstractNetworkTest {

    public static final MaritimeId ID1 = MaritimeId.create("mmsi://1");

    public static final MaritimeId ID2 = MaritimeId.create("mmsi://2");

    public static final MaritimeId ID3 = MaritimeId.create("mmsi://3");

    public static final MaritimeId ID4 = MaritimeId.create("mmsi://4");

    public static final MaritimeId ID5 = MaritimeId.create("mmsi://5");

    public static final MaritimeId ID6 = MaritimeId.create("mmsi://6");

    int clientPort;

    protected final ConcurrentHashMap<MaritimeId, MaritimeCloudClient> clients = new ConcurrentHashMap<>();

    ExecutorService es = Executors.newCachedThreadPool();

    protected final ConcurrentHashMap<MaritimeId, LocationSup> locs = new ConcurrentHashMap<>();

    ProxyTester pt;

    InternalServer si;

    final boolean useProxy;

    public AbstractNetworkTest() {
        this(false);
    }

    public AbstractNetworkTest(boolean useProxy) {
        this.useProxy = useProxy;
    }

    protected MaritimeCloudClientConfiguration newBuilder(MaritimeId id) {
        MaritimeCloudClientConfiguration b = MaritimeCloudClientConfiguration.create(id);
        b.setHost("localhost:" + clientPort);
        return b;
    }

    protected MaritimeCloudClient newClient() throws Exception {
        for (;;) {
            MaritimeId id = MaritimeId.create("mmsi://" + ThreadLocalRandom.current().nextInt(1000));
            if (!clients.containsKey(id)) {
                return newClient(id);
            }
        }
    }

    protected MaritimeCloudClient newClient(double lat, double lon) throws Exception {
        for (;;) {
            MaritimeId id = MaritimeId.create("mmsi://" + ThreadLocalRandom.current().nextInt(1000));
            if (!clients.containsKey(id)) {
                return newClient(id, lat, lon);
            }
        }
    }

    protected MaritimeCloudClient newClient(MaritimeId id) throws Exception {
        MaritimeCloudClientConfiguration b = newBuilder(id);
        locs.put(id, new LocationSup());
        MaritimeCloudClient c = b.build();
        clients.put(id, c);
        return c;
    }

    protected MaritimeCloudClient newClient(MaritimeId id, double lat, double lon) throws Exception {
        MaritimeCloudClientConfiguration b = newBuilder(id);
        LocationSup ls = new LocationSup();
        b.setPositionSupplier(ls);
        locs.put(id, ls);
        setPosition(id, lat, lon);
        MaritimeCloudClient c = b.build();
        clients.put(id, c);
        return c;
    }

    protected MaritimeCloudClient newClient(MaritimeCloudClientConfiguration b) throws Exception {
        locs.put(b.getId(), new LocationSup());
        MaritimeCloudClient c = b.build();
        clients.put(b.getId(), c);
        return c;
    }

    protected Future<MaritimeCloudClient> newClientAsync(final MaritimeId id) throws Exception {
        final MaritimeCloudClientConfiguration b = newBuilder(id);
        locs.put(id, new LocationSup());
        return es.submit(new Callable<MaritimeCloudClient>() {

            @Override
            public MaritimeCloudClient call() throws Exception {
                MaritimeCloudClient c = b.build();
                clients.put(id, c);
                return c;
            }
        });
    }

    protected Set<MaritimeCloudClient> newClients(int count) throws Exception {
        HashSet<Future<MaritimeCloudClient>> futures = new HashSet<>();
        for (int j = 0; j < count; j++) {
            futures.add(newClientAsync(MaritimeId.create("mmsi://1234" + j)));
        }
        HashSet<MaritimeCloudClient> result = new HashSet<>();
        for (Future<MaritimeCloudClient> f : futures) {
            result.add(f.get(3, TimeUnit.SECONDS));
        }
        return result;
    }

    protected MaritimeId setPosition(MaritimeId id, double lat, double lon) {
        locs.get(id).lat = lat;
        locs.get(id).lon = lon;
        return id;
    }

    protected MaritimeCloudClient setPosition(MaritimeCloudClient pnc, double lat, double lon) {
        locs.get(pnc.getClientId()).lat = lat;
        locs.get(pnc.getClientId()).lon = lon;
        return pnc;
    }

    @Before
    public void setup() throws Exception {
        clientPort = ThreadLocalRandom.current().nextInt(40000, 50000);
        ServerConfiguration sc = new ServerConfiguration();
        if (useProxy) {
            sc.setServerPort(12222);
            si = new InternalServer(sc);
            pt = new ProxyTester(new InetSocketAddress(clientPort), new InetSocketAddress(12222));
            pt.start();
        } else {
            sc.setServerPort(clientPort);
            si = new InternalServer(sc);
        }
        si.start();
    }

    @After
    public void teardown() throws InterruptedException {
        for (final MaritimeCloudClient c : clients.values()) {
            es.execute(new Runnable() {
                public void run() {
                    c.close();
                }
            });
        }
        for (MaritimeCloudClient c : clients.values()) {
            assertTrue(c.awaitTermination(2, TimeUnit.SECONDS));
        }

        clients.clear();
        si.shutdown();
        es.shutdown();
        if (pt != null) {
            pt.shutdown();
        }
        assertTrue(es.awaitTermination(10, TimeUnit.SECONDS));

        assertTrue(si.awaitTerminated(10, TimeUnit.SECONDS));
        System.out.println("bye");
    }

    static class LocationSup extends Supplier<PositionTime> {
        double lat;

        double lon;

        /** {@inheritDoc} */
        @Override
        public PositionTime get() {
            return PositionTime.create(lat, lon, System.currentTimeMillis());
        }

    }
}
