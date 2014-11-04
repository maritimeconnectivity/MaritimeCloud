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
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.mms.stubs.HelloWorldEndpoint;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientClosedException;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests
 *
 * @author Kasper Nielsen
 */
@Ignore
public class NetworkFuturesOnCloseTest extends AbstractNetworkTest {

    @Test
    public void serviceFind() throws Exception {
        MmsClient pc1 = newClient(ID1);

        EndpointInvocationFuture<HelloWorldEndpoint> f = pc1.endpointFind(HelloWorldEndpoint.class).findNearest();
        EndpointInvocationFuture<HelloWorldEndpoint> f2 = f.timeout(4, TimeUnit.SECONDS);

        pc1.close();
        try {
            System.out.println(f.get());
            fail();
        } catch (ExecutionException e) {
            assertTrue(e.getCause() instanceof MmsClientClosedException);
        }

        pc1.close();
        try {
            System.out.println(f2.get());
            fail();
        } catch (ExecutionException e) {
            assertTrue(e.getCause() instanceof MmsClientClosedException);
        }
    }

    @Test
    public void serviceInvoke() throws Exception {
        MmsClient pc1 = newClient(ID1);
        newClient(ID2);

        EndpointInvocationFuture<HelloWorldEndpoint> f = pc1.endpointFind(HelloWorldEndpoint.class).findNearest();
        pc1.close();
        try {
            f.get();
            fail();
        } catch (ExecutionException e) {
            assertTrue(e.getCause() instanceof MmsClientClosedException);
        }
    }
}
