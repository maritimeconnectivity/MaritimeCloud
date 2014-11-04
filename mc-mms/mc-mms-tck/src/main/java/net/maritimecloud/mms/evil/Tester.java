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
package net.maritimecloud.mms.evil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;

import net.maritimecloud.mms.tck.stubs.AbstractTestEndpoint;
import net.maritimecloud.mms.tck.stubs.TestEndpoint;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public class Tester {

    private final MmsClient[] clients;

    final AtomicLong al = new AtomicLong();

    final ConcurrentHashMap<Long, LongSupplier> runners = new ConcurrentHashMap<>();

    Tester(int numberOfClients) {
        clients = new MmsClient[numberOfClients];
        for (int i = 0; i < clients.length; i++) {
            MmsClientConfiguration conf = MmsClientConfiguration.create("mmsi:" + i);
            conf.setPositionReader(PositionReader.fixedPosition(PositionTime.create(1, 1, 1)));
            clients[i] = conf.build();
            clients[i].endpointRegister(new AbstractTestEndpoint() {

                @Override
                protected Long hello(MessageHeader context, Long testId) {
                    LongSupplier r = runners.remove(testId);
                    if (r == null) {
                        System.err.println("Could not find anything with id " + testId);
                        return -1L;
                    } else {
                        return r.getAsLong();
                    }
                }
            });
        }
    }

    public void close() {
        for (int i = 0; i < clients.length; i++) {
            clients[i].close();
        }
        for (int i = 0; i < clients.length; i++) {
            try {
                clients[i].awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    void sendRandomMsg() {
        MmsClient form = rnd();
        MmsClient to = rnd();
        long fromId = al.incrementAndGet();
        long toId = al.incrementAndGet();
        runners.put(fromId, () -> toId);
        runners.put(toId, () -> toId);
        long start = System.nanoTime();
        EndpointInvocationFuture<Long> hello = form.endpointFind(to.getClientId(), TestEndpoint.class).hello(fromId);
        hello.thenRun(() -> {
            System.out.println("Latency " + (System.nanoTime() - start));
        });
    }

    MmsClient rnd() {
        return clients[ThreadLocalRandom.current().nextInt(clients.length)];
    }

}
