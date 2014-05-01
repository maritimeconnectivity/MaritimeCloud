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
package net.maritimecloud.internal.net.client.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.AbstractClientConnectionTest;
import net.maritimecloud.messages.FindService;
import net.maritimecloud.messages.FindServiceAck;
import net.maritimecloud.messages.RegisterService;
import net.maritimecloud.messages.RegisterServiceAck;
import net.maritimecloud.messages.ServiceInvoke;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.NetworkFuture;
import net.maritimecloud.net.service.ServiceEndpoint;
import net.maritimecloud.net.service.ServiceInvocationFuture;
import net.maritimecloud.net.service.registration.ServiceRegistration;

import org.junit.Ignore;
import org.junit.Test;

import test.stubs.HelloService;
import test.stubs.HelloService.GetName;
import test.stubs.HelloService.Reply;

/**
 *
 * @author Kasper Nielsen
 */
@Ignore
public class ServiceTest extends AbstractClientConnectionTest {

    @Test
    public void register() throws Exception {
        MaritimeCloudClient c = createAndConnect();
        ServiceRegistration sr = c.serviceRegister(HelloService.GET_NAME, HelloService.create("ok"));

        RegisterService rs = t.take(RegisterService.class);
        assertEquals(HelloService.GET_NAME.getName(), rs.getServiceName());
        t.send(new RegisterServiceAck().setMessageAck(rs.getReplyTo()).setMessageAck(1L).setLatestReceivedId(0L));
        // t.send(new RegisterServiceAck().setMessageAck(rs.getReplyTo()).setMessageId(1L).setLatestReceivedId(1L));

        sr.awaitRegistered(10, TimeUnit.SECONDS);

        // c.serviceRegister(null, null)
        //
        // MaritimeCloudClient s = registerService(newClient(), "foo123");
        // MaritimeCloudClient c = newClient();
        // ServiceEndpoint<GetName, Reply> end = c.serviceLocate(HelloService.GET_NAME).nearest().get(6,
        // TimeUnit.SECONDS);
        // assertEquals(s.getClientId(), end.getId());
    }

    @Test
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
        ServiceInvoke is = t.take(ServiceInvoke.class);
        // InvokeServiceResult isr = is.createReply(new Reply("okfoo"));
        // isr.setLatestReceivedId(2L).setMessageId(0L);
        // t.send(isr);
        // invoke.receivedByCloud().get(1, TimeUnit.SECONDS);
        // assertTrue(invoke.receivedByCloud().isDone());
        // assertEquals("okfoo", invoke.get().getName());
    }
}
