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
package net.maritimecloud.internal.net.client.endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.net.messages.FindService;
import net.maritimecloud.internal.net.messages.FindServiceAck;
import net.maritimecloud.internal.net.messages.RegisterEndpoint;
import net.maritimecloud.internal.net.messages.RegisterEndpointAck;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.NetworkFuture;
import net.maritimecloud.net.endpoint.EndpointRegistration;
import net.maritimecloud.net.service.ServiceEndpoint;
import net.maritimecloud.net.service.ServiceInvocationFuture;

import org.junit.Ignore;
import org.junit.Test;

import test.stubs.HelloService;
import test.stubs.HelloService.GetName;
import test.stubs.HelloService.Reply;

/**
 *
 * @author Kasper Nielsen
 */
public class EndpointTest extends AbstractClientConnectionTest {

    @Test
    public void register() throws Exception {
        MaritimeCloudClient c = createAndConnect();

        EndpointRegistration sr = c.endpointRegister(new HelloWorldEndpointImpl());

        RegisterEndpoint rs = t.take(RegisterEndpoint.class);
        assertEquals(HelloWorldEndpoint.NAME, rs.getEndpointName());
        t.send(new RegisterEndpointAck().setMessageAck(rs.getReplyTo()).setMessageAck(1L).setLatestReceivedId(0L)
                .setMessageId(0L));

        assertTrue(sr.awaitRegistered(3, TimeUnit.SECONDS));
    }

    @Test
    @Ignore
    public void locate() throws Exception {
        MaritimeCloudClient c = createAndConnect();
        NetworkFuture<ServiceEndpoint<GetName, Reply>> locator = c.serviceLocate(HelloService.GET_NAME).nearest();

        FindService rs = t.take(FindService.class);
        assertEquals(HelloService.GET_NAME.getName(), rs.getServiceName());
        t.send(new FindServiceAck().setMessageAck(rs.getReplyTo()).addRemoteIDS("mmsi://4321").setMessageId(0L));

        ServiceEndpoint<GetName, Reply> se = locator.get(1, TimeUnit.SECONDS);
        assertEquals(MaritimeId.create("mmsi://4321"), se.getId());


        ServiceInvocationFuture<Reply> invoke = se.invoke(new GetName());
        assertFalse(invoke.receivedByCloud().isDone());
        // ServiceInvoke is = t.take(ServiceInvoke.class);
        // InvokeServiceResult isr = is.createReply(new Reply("okfoo"));
        // isr.setLatestReceivedId(2L).setMessageId(0L);
        // t.send(isr);
        // invoke.receivedByCloud().get(1, TimeUnit.SECONDS);
        // assertTrue(invoke.receivedByCloud().isDone());
        // assertEquals("okfoo", invoke.get().getName());
    }
}
