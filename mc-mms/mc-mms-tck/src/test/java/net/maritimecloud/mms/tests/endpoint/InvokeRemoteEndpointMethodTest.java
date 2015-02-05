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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import net.maritimecloud.internal.mms.client.endpoint.HelloWorldEndpointImpl;
import net.maritimecloud.mms.stubs.HelloWorldEndpoint;
import net.maritimecloud.mms.tests.AbstractNetworkTest;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.EndpointRegistration;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.util.Binary;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class InvokeRemoteEndpointMethodTest extends AbstractNetworkTest {

    @Test
    @Ignore
    public void invokeService() throws Exception {
        MmsClient c1 = newClient(ID1);

        AtomicReference<Binary> msgId = new AtomicReference<>();
        EndpointRegistration sr = c1.endpointRegister(new HelloWorldEndpointImpl() {
            @Override
            protected String hello(MessageHeader context) {
                assertEquals(ID2, context.getSender());
                msgId.set(context.getMessageId());
                return "OK";
            }
        });

        assertTrue(sr.awaitRegistered(10, TimeUnit.SECONDS));

        MmsClient c2 = newClient(ID2);


        HelloWorldEndpoint ee = c2.endpointCreate(ID1, HelloWorldEndpoint.class);
        EndpointInvocationFuture<String> f = ee.hello();
        assertEquals("OK", f.join());
        assertEquals(f.getMessageId(), msgId.get());
        c2.shutdown();
        assertTrue(c2.awaitTermination(1, TimeUnit.SECONDS));
    }
}
