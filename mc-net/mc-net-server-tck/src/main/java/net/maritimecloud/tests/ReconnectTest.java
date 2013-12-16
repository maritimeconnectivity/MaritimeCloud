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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import net.maritimecloud.internal.net.client.broadcast.stubs.BroadcastTestMessage;
import net.maritimecloud.net.ConnectionFuture;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.broadcast.BroadcastListener;
import net.maritimecloud.net.broadcast.BroadcastMessageHeader;
import net.maritimecloud.net.service.invocation.InvocationCallback;

import org.junit.Ignore;
import org.junit.Test;

import test.util.TesstService;
import test.util.TesstService.TestInit;
import test.util.TesstService.TestReply;
import test.util.TesstService2;

/**
 * 
 * @author Kasper Nielsen
 */
@Ignore
public class ReconnectTest extends AbstractNetworkTest {

    public ReconnectTest() {
        super(true);
    }

    @Test
    @Ignore
    public void randomKilling() throws Exception {
        final AtomicInteger ai = new AtomicInteger();
        MaritimeCloudClient c1 = newClient(ID1);
        c1.serviceRegister(TesstService.TEST_INIT,
                new InvocationCallback<TesstService.TestInit, TesstService.TestReply>() {
                    public void process(TesstService.TestInit l, Context<TesstService.TestReply> context) {
                        context.complete(l.reply());
                        ai.incrementAndGet();
                        System.out.println("Receive " + l);
                    }
                }).awaitRegistered(1, TimeUnit.SECONDS);

        MaritimeCloudClient c6 = newClient(ID6);

        pt.killRandom(1000, TimeUnit.MILLISECONDS);
        Map<TestInit, ConnectionFuture<TesstService.TestReply>> set = new LinkedHashMap<>();
        assertEquals(2, si.info().getConnectionCount());
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                TesstService.TestInit init = new TesstService.TestInit(i * 100 + j, ID6, ID1);
                set.put(init, c6.serviceInvoke(ID1, init));
                System.out.println("SEND " + init);
            }
            for (Map.Entry<TestInit, ConnectionFuture<TesstService.TestReply>> f : set.entrySet()) {
                try {
                    TestReply reply = f.getValue().get(5, TimeUnit.SECONDS);
                    System.out.println("End " + reply.getInit());
                } catch (TimeoutException e) {
                    System.err.println(f.getKey());
                    throw e;
                }
            }
            set.clear();
        }

        assertEquals(100 * 100, ai.get());
        System.out.println(ai);
    }

    @Test
    @Ignore
    public void randomKilling2() throws Exception {
        final AtomicInteger ai = new AtomicInteger();
        MaritimeCloudClient c1 = newClient(ID1);
        c1.serviceRegister(TesstService.TEST_INIT,
                new InvocationCallback<TesstService.TestInit, TesstService.TestReply>() {
                    public void process(TesstService.TestInit l, Context<TesstService.TestReply> context) {
                        context.complete(l.reply());
                        ai.incrementAndGet();
                        System.out.println("Receive " + l);
                    }
                }).awaitRegistered(1, TimeUnit.SECONDS);

        MaritimeCloudClient c6 = newClient(ID6);

        pt.killRandom(500, TimeUnit.MILLISECONDS);
        Map<TestInit, ConnectionFuture<TesstService.TestReply>> set = new LinkedHashMap<>();
        assertEquals(2, si.info().getConnectionCount());
        for (int j = 0; j < 10; j++) {
            TesstService.TestInit init = new TesstService.TestInit(j, ID6, ID1);
            set.put(init, c6.serviceInvoke(ID1, init));
            System.out.println("SEND " + init);
        }
        for (Map.Entry<TestInit, ConnectionFuture<TesstService.TestReply>> f : set.entrySet()) {
            try {
                TestReply reply = f.getValue().get(5, TimeUnit.SECONDS);
                System.out.println("End " + reply.getInit());
            } catch (TimeoutException e) {
                System.err.println(f.getKey());
                throw e;
            }
        }
        set.clear();

        // assertEquals(100 * 100, ai.get());
        System.out.println(ai);
    }

    @Test
    public void singleClient() throws Exception {
        final AtomicInteger ai = new AtomicInteger();
        MaritimeCloudClient c1 = newClient(ID1);

        c1.serviceRegister(TesstService2.TEST_INIT,
                new InvocationCallback<TesstService2.TestInit, TesstService2.TestReply>() {
                    public void process(TesstService2.TestInit l, Context<TesstService2.TestReply> context) {
                        context.complete(l.reply());
                        ai.incrementAndGet();
                    }
                }).awaitRegistered(1, TimeUnit.SECONDS);

        MaritimeCloudClient c6 = newClient(ID6);
        while (si.info().getConnectionCount() < 2) {
            Thread.sleep(10);
        }

        for (int i = 0; i < 100; i++) {
            pt.killAll();
            TesstService2.TestInit ti = new TesstService2.TestInit(i, ID1.toString(), ID6.toString());
            assertEquals(ti.getId(), c6.serviceInvoke(ID1, ti).get(5, TimeUnit.SECONDS).getTestInit().getId());
        }
        System.out.println(ai);
    }

    @Test
    public void singleClient2() throws Exception {
        final int count = 10;
        final Set<Integer> received = new HashSet<>();
        final CountDownLatch cdl = new CountDownLatch(count);
        MaritimeCloudClient c1 = newClient(ID1);
        MaritimeCloudClient c2 = newClient(ID2);

        c2.broadcastListen(BroadcastTestMessage.class, new BroadcastListener<BroadcastTestMessage>() {
            public void onMessage(BroadcastMessageHeader header, BroadcastTestMessage broadcast) {
                received.add(Integer.parseInt(broadcast.getName()));
                cdl.countDown();
            }
        });

        for (int i = 1; i <= count; i++) {
            c1.broadcast(new BroadcastTestMessage("" + i));
            pt.killAll();
            Thread.sleep(100);
        }

        cdl.await(2, TimeUnit.SECONDS);
        System.out.println("Received " + received);
    }
}
