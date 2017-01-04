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

import java.net.URI;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Ignore;
import org.junit.Test;

import net.maritimecloud.mms.stubs.AbstractHelloWorldEndpoint;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.mms.stubs.HelloWorldEndpoint;
import net.maritimecloud.net.DispatchedMessage;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsConnection;

/**
 *
 * @author Kasper Nielsen
 */
public class ReconnectTest extends AbstractNetworkTest {

    public ReconnectTest() {
        super(true);
    }

    @Test
    // @Ignore
    public void randomKilling() throws Exception {
        final AtomicInteger ai = new AtomicInteger();
        MmsClient c1 = newClient(ID1);
        MmsClient c6 = newClient(ID6);

        final BitSet result = new BitSet();

        c1.endpointRegister(new AbstractHelloWorldEndpoint() {
            protected String hello(MessageHeader context) {
                return ai.getAndIncrement() + "";
            }
        });
        c6.endpointRegister(new AbstractHelloWorldEndpoint() {
            protected String hello(MessageHeader context) {
                return ai.getAndIncrement() + "";
            }
        });
        c1.connection().awaitConnected(1, TimeUnit.SECONDS);
        c6.connection().awaitConnected(1, TimeUnit.SECONDS);

        HelloWorldEndpoint ec6 = c1.endpointLocate(HelloWorldEndpoint.class).findNearest().get();
        HelloWorldEndpoint ec1 = c6.endpointLocate(HelloWorldEndpoint.class).findNearest().get();

        //
        // System.out.println();
        pt.killRandom(500, TimeUnit.MILLISECONDS);

        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            HashSet<EndpointInvocationFuture<String>> s = new HashSet<>();
            for (int j = 0; j < 100; j++) {
                // System.out.print(j);

                long now = System.nanoTime();
                EndpointInvocationFuture<String> f6 = ec6.hello();
                s.add(f6);
                // System.out.println(DurationFormatter.DEFAULT.formatNanos(System.nanoTime() - now));
            }
            for (EndpointInvocationFuture<String> f : s) {
                Integer ii = Integer.parseInt(f.get(5, TimeUnit.SECONDS));
                result.set(ii);
            }
        }
        System.out.println(result.cardinality());
        // Thread.sleep(10000);
        //
        // Map<TestInit, MmsFuture<TesstService.TestReply>> set = new LinkedHashMap<>();
        //

        //
        //
        // for (int i = 0; i < 100; i++) {
        // for (int j = 0; j < 100; j++) {
        // HelloWorldEndpoint hwe;
        // TesstService.TestInit init = new TesstService.TestInit(i * 100 + j, ID6, ID1);
        // // set.put(init, c6.serviceInvoke(ID1, init));
        // System.out.println("SEND " + init);
        // }
        // for (Map.Entry<TestInit, MmsFuture<TesstService.TestReply>> f : set.entrySet()) {
        // try {
        // TestReply reply = f.getValue().get(5, TimeUnit.SECONDS);
        // System.out.println("End " + reply.getInit());
        // } catch (TimeoutException e) {
        // System.err.println(f.getKey());
        // throw e;
        // }
        // }
        // set.clear();
        // }
        //
        // assertEquals(100 * 100, ai.get());
        System.out.println(ai);
    }

    //
    // @Test
    // @Ignore
    // public void randomKilling2() throws Exception {
    // final AtomicInteger ai = new AtomicInteger();
    // MmsClient c1 = newClient(ID1);
    // System.out.println(c1);
    // // c1.serviceRegister(TesstService.TEST_INIT,
    // // new InvocationCallback<TesstService.TestInit, TesstService.TestReply>() {
    // // public void process(TesstService.TestInit l, Context<TesstService.TestReply> context) {
    // // context.complete(l.reply());
    // // ai.incrementAndGet();
    // // System.out.println("Receive " + l);
    // // }
    // // }).awaitRegistered(1, TimeUnit.SECONDS);
    //
    // // MaritimeCloudClient c6 = newClient(ID6);
    //
    // pt.killRandom(500, TimeUnit.MILLISECONDS);
    // Map<TestInit, MmsFuture<TesstService.TestReply>> set = new LinkedHashMap<>();
    // assertEquals(2, si.info().getConnectionCount());
    // for (int j = 0; j < 10; j++) {
    // TesstService.TestInit init = new TesstService.TestInit(j, ID6, ID1);
    // // set.put(init, c6.serviceInvoke(ID1, init));
    // System.out.println("SEND " + init);
    // }
    // for (Map.Entry<TestInit, MmsFuture<TesstService.TestReply>> f : set.entrySet()) {
    // try {
    // TestReply reply = f.getValue().get(5, TimeUnit.SECONDS);
    // System.out.println("End " + reply.getInit());
    // } catch (TimeoutException e) {
    // System.err.println(f.getKey());
    // throw e;
    // }
    // }
    // set.clear();
    //
    // // assertEquals(100 * 100, ai.get());
    // System.out.println(ai);
    // }

    @Test
    @Ignore
    public void singleClient() throws Exception {
        final AtomicInteger ai = new AtomicInteger();
        // MaritimeCloudClient c1 = newClient(ID1);

        // c1.serviceRegister(TesstService2.TEST_INIT,
        // new InvocationCallback<TesstService2.TestInit, TesstService2.TestReply>() {
        // public void process(TesstService2.TestInit l, Context<TesstService2.TestReply> context) {
        // context.complete(l.reply());
        // ai.incrementAndGet();
        // }
        // }).awaitRegistered(1, TimeUnit.SECONDS);

        // MaritimeCloudClient c6 = newClient(ID6);
        while (clients(si).size() < 2) {
            Thread.sleep(10);
        }

        for (int i = 0; i < 100; i++) {
            pt.killAll();
            // TesstService2.TestInit ti = new TesstService2.TestInit(i, ID1.toString(), ID6.toString());
            // assertEquals(ti.getId(), c6.serviceInvoke(ID1, ti).get(5, TimeUnit.SECONDS).getTestInit().getId());
        }
        System.out.println(ai);
    }

    @Test
    public void listener() throws Exception {
        MmsClient c2 = newClient(newBuilder(ID1).addListener(new MmsConnection.Listener() {
            public void connected(URI host, boolean isNewSession) {
                System.out.println(isNewSession);
            }
        }));

        Thread.sleep(200);
        pt.killRandom(100, TimeUnit.MILLISECONDS);
        Thread.sleep(200);
    }

    @Test
    public void simple() throws Exception {
        final int count = 1000;
        final Set<Integer> received = new HashSet<>();
        final CountDownLatch cdl = new CountDownLatch(count);
        MmsClient c1 = newClient(ID1, 1, 1);
        MmsClient c2 = newClient(ID2, 1, 1);

        c2.broadcastSubscribe(BroadcastTestMessage.class, (header, m) -> {
            received.add(Integer.parseInt(m.getMsg()));
            cdl.countDown();
        });

        Thread.sleep(200);
        pt.killRandom(100, TimeUnit.MILLISECONDS);
        Set<DispatchedMessage> set = new HashSet<>();
        for (int i = 1; i <= count; i++) {
            DispatchedMessage dm = c1.broadcast(new BroadcastTestMessage().setMsg("" + i));
            if (i % 3 == 0) {
                // pt.killRandom();
            }
            if (i % 4 == 0) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1, 10));
            }
            set.add(dm);
            if (i % 4 == 0) {
                // dm.relayed().join();
            }
        }

        for (DispatchedMessage m : set) {
            m.relayed().orTimeout(4, TimeUnit.SECONDS).join();
        }

        assertTrue(cdl.await(2, TimeUnit.SECONDS));
    }

    @Test
    @Ignore
    public void singleClient2() throws Exception {
        final int count = 10;
        final Set<Integer> received = new HashSet<>();
        final CountDownLatch cdl = new CountDownLatch(count);
        MmsClient c1 = newClient(ID1);
        MmsClient c2 = newClient(ID2);

        c2.broadcastSubscribe(BroadcastTestMessage.class, (header, m) -> {
            received.add(Integer.parseInt(m.getMsg()));
            cdl.countDown();
        });

        for (int i = 1; i <= count; i++) {
            c1.broadcast(new BroadcastTestMessage().setMsg("" + i));
            pt.killAll();
            Thread.sleep(100);
        }

        cdl.await(2, TimeUnit.SECONDS);
        System.out.println("Received " + received);
    }
}
