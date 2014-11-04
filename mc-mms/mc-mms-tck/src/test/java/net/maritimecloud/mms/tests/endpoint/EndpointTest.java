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
package net.maritimecloud.mms.tests.endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.endpoint.HelloWorldEndpointImpl;
import net.maritimecloud.mms.stubs.AbstractTestEndpoint;
import net.maritimecloud.mms.stubs.HelloWorldEndpoint;
import net.maritimecloud.mms.stubs.TestEndpoint;
import net.maritimecloud.mms.stubs.TestMessage;
import net.maritimecloud.mms.tests.AbstractNetworkTest;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.EndpointRegistration;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.net.mms.MmsClient;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class EndpointTest extends AbstractNetworkTest {

    @Test
    public void invokeService() throws Exception {
        MmsClient c1 = newClient(ID1);

        EndpointRegistration sr = c1.endpointRegister(new HelloWorldEndpointImpl());

        assertTrue(sr.awaitRegistered(10, TimeUnit.SECONDS));

        MmsClient c2 = newClient(ID2);

        EndpointInvocationFuture<List<HelloWorldEndpoint>> all = c2.endpointFind(HelloWorldEndpoint.class).findAll();

        List<HelloWorldEndpoint> list = all.get();
        assertEquals(1, list.size());
        HelloWorldEndpoint ee = list.get(0);
        assertEquals("HelloWorld", ee.hello().join());
    }

    @Test
    public void findServices() throws Exception {
        MmsClient c1 = newClient(ID1);
        MmsClient c2 = newClient(ID2);
        EndpointRegistration sr1 = c1.endpointRegister(new AbstractTestEndpoint() {
            @Override
            protected List<Long> invokeIt(MessageHeader context, List<TestMessage> li) {
                ArrayList<Long> result = new ArrayList<>();
                for (TestMessage m : li) {
                    result.add(m.getF1().stream().mapToLong(e -> e).sum());
                }
                return result;
            }
        });
        assertTrue(sr1.awaitRegistered(10, TimeUnit.SECONDS));


        EndpointInvocationFuture<List<TestEndpoint>> l = c2.endpointFind(TestEndpoint.class).findAll();
        assertEquals(1, l.get().size());
        assertEquals(ID1, l.get().get(0).getRemoteId());
    }

    @Test
    public void invokeServiceWithParameters() throws Exception {
        MmsClient c1 = newClient(ID1);
        EndpointRegistration sr = c1.endpointRegister(new AbstractTestEndpoint() {
            @Override
            protected List<Long> invokeIt(MessageHeader context, List<TestMessage> li) {
                ArrayList<Long> result = new ArrayList<>();
                for (TestMessage m : li) {
                    result.add(m.getF1().stream().mapToLong(e -> e).sum());
                }
                return result;
            }
        });
        assertTrue(sr.awaitRegistered(10, TimeUnit.SECONDS));

        MmsClient c2 = newClient(ID2);
        TestEndpoint e = c2.endpointFind(ID1, TestEndpoint.class);

        List<TestMessage> l = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TestMessage tm = new TestMessage();
            for (int j = 0; j < i; j++) {
                tm.addF1((long) j);
            }
            l.add(tm);
        }
        EndpointInvocationFuture<List<Long>> invokeIt = e.invokeIt(l);

        List<Long> join = invokeIt.join();
        for (int i = 0; i < join.size(); i++) {
            assertEquals((i - 1) * i / 2, join.get(i).longValue());
        }
    }
}
