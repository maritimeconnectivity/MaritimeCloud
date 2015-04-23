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
package net.maritimecloud.mms.tests;

import static org.junit.Assert.assertTrue;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.messages.services.ClientInfo;
import net.maritimecloud.mms.server.MmsServer;
import net.maritimecloud.mms.server.MmsServerConfiguration;
import net.maritimecloud.mms.server.connection.client.ClientManager;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.After;
import org.junit.Before;

import test.util.ProxyTester;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractNetworkTest {

    public static final MaritimeId ID1 = MaritimeId.create("mmsi:1");

    public static final MaritimeId ID2 = MaritimeId.create("mmsi:2");

    public static final MaritimeId ID3 = MaritimeId.create("mmsi:3");

    public static final MaritimeId ID4 = MaritimeId.create("mmsi:4");

    public static final MaritimeId ID5 = MaritimeId.create("mmsi:5");

    public static final MaritimeId ID6 = MaritimeId.create("mmsi:6");

    int clientPort;

    protected final ConcurrentHashMap<MaritimeId, MmsClient> clients = new ConcurrentHashMap<>();

    ExecutorService es = Executors.newCachedThreadPool();

    protected final ConcurrentHashMap<MaritimeId, LocationSup> locs = new ConcurrentHashMap<>();

    ProxyTester pt;

    MmsServer si;

    final boolean useProxy;

    public AbstractNetworkTest() {
        this(false);
    }

    public AbstractNetworkTest(boolean useProxy) {
        this.useProxy = useProxy;
    }

    protected MmsClientConfiguration newBuilder(MaritimeId id) {
        MmsClientConfiguration b = MmsClientConfiguration.create(id);
        b.setHost("localhost:" + clientPort);
        return b;
    }

    protected MmsClient newClient() throws Exception {
        for (;;) {
            MaritimeId id = MaritimeId.create("mmsi:" + ThreadLocalRandom.current().nextInt(1000));
            if (!clients.containsKey(id)) {
                return newClient(id);
            }
        }
    }

    protected MmsClient newClient(double lat, double lon) throws Exception {
        for (;;) {
            MaritimeId id = MaritimeId.create("mmsi:" + ThreadLocalRandom.current().nextInt(1000));
            if (!clients.containsKey(id)) {
                return newClient(id, lat, lon);
            }
        }
    }

    protected MmsClient newClient(MaritimeId id) throws Exception {
        MmsClientConfiguration b = newBuilder(id);
        locs.put(id, new LocationSup());
        MmsClient c = b.build();
        clients.put(id, c);
        return c;
    }

    protected MmsClient newClient(MaritimeId id, double lat, double lon) throws Exception {
        MmsClientConfiguration b = newBuilder(id);
        LocationSup ls = new LocationSup();
        b.setPositionReader(ls);
        locs.put(id, ls);
        setPosition(id, lat, lon);
        MmsClient c = b.build();
        clients.put(id, c);
        return c;
    }

    protected MmsClient newClient(MmsClientConfiguration b) throws Exception {
        locs.put(b.getId(), new LocationSup());
        MmsClient c = b.build();
        clients.put(b.getId(), c);
        return c;
    }

    protected Future<MmsClient> newClientAsync(final MaritimeId id) throws Exception {
        final MmsClientConfiguration b = newBuilder(id);
        locs.put(id, new LocationSup());
        return es.submit(new Callable<MmsClient>() {

            @Override
            public MmsClient call() throws Exception {
                MmsClient c = b.build();
                clients.put(id, c);
                return c;
            }
        });
    }

    protected Set<MmsClient> newClients(int count) throws Exception {
        HashSet<Future<MmsClient>> futures = new HashSet<>();
        for (int j = 0; j < count; j++) {
            futures.add(newClientAsync(MaritimeId.create("mmsi:1234" + j)));
        }
        HashSet<MmsClient> result = new HashSet<>();
        for (Future<MmsClient> f : futures) {
            result.add(f.get(3, TimeUnit.SECONDS));
        }
        return result;
    }

    protected MaritimeId setPosition(MaritimeId id, double lat, double lon) {
        locs.get(id).lat = lat;
        locs.get(id).lon = lon;
        return id;
    }

    protected MmsClient setPosition(MmsClient pnc, double lat, double lon) {
        locs.get(pnc.getClientId()).lat = lat;
        locs.get(pnc.getClientId()).lon = lon;
        return pnc;
    }

    @Before
    public void setup() throws Exception {
        clientPort = ThreadLocalRandom.current().nextInt(40000, 50000);
        MmsServerConfiguration sc = new MmsServerConfiguration();
        if (useProxy) {
            sc.setServerPort(12222);
            si = new MmsServer(sc);
            pt = new ProxyTester(new InetSocketAddress(clientPort), new InetSocketAddress(12222));
            pt.start();
        } else {
            sc.setServerPort(clientPort);
            si = new MmsServer(sc);
        }
        si.startBlocking();
    }

    @After
    public void teardown() throws InterruptedException {
        for (final MmsClient c : clients.values()) {
            es.execute(new Runnable() {
                public void run() {
                    c.shutdown();
                }
            });
        }
        for (MmsClient c : clients.values()) {
            if (!c.awaitTermination(2, TimeUnit.SECONDS)) {
                System.out.println("Failed to shutdown " + c.getClientId());
                System.err.println(crunchifyGenerateThreadDump());
                throw new AssertionError("Failed to shutdown " + c.getClientId());
            }
        }

        clients.clear();
        si.shutdown();
        es.shutdown();
        if (pt != null) {
            pt.shutdown();
        }
        assertTrue(es.awaitTermination(10, TimeUnit.SECONDS));

        assertTrue(si.awaitTerminated(10, TimeUnit.SECONDS));
    }

    public List<ClientInfo> clients(MmsServer server) {
        ClientManager ac = server.getService(ClientManager.class);
        return ac.statistics().getAllClients().getClients();
    }


    public static String crunchifyGenerateThreadDump() {
        final StringBuilder dump = new StringBuilder();
        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        final ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 100);
        for (ThreadInfo threadInfo : threadInfos) {
            dump.append('"');
            dump.append(threadInfo.getThreadName());
            dump.append("\" ");
            final Thread.State state = threadInfo.getThreadState();
            dump.append("\n   java.lang.Thread.State: ");
            dump.append(state);
            final StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
            for (final StackTraceElement stackTraceElement : stackTraceElements) {
                dump.append("\n        at ");
                dump.append(stackTraceElement);
            }
            dump.append("\n\n");
        }
        return dump.toString();
    }

    static class LocationSup extends PositionReader {
        double lat;

        double lon;

        /** {@inheritDoc} */
        @Override
        public PositionTime getCurrentPosition() {
            return PositionTime.create(lat, lon, System.currentTimeMillis());
        }

    }
}
