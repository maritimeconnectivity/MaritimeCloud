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
package net.maritimecloud.internal.mms.client.endpoint;

import static org.junit.Assert.assertEquals;
import net.maritimecloud.internal.mms.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.net.messages.MethodInvoke;
import net.maritimecloud.internal.net.messages.MethodInvokeResult;
import net.maritimecloud.mms.stubs.HelloWorldEndpoint;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.util.Binary;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class EndpointTest extends AbstractClientConnectionTest {

    // @Test
    // public void register() throws Exception {
    // MmsClient c = createAndConnect();
    //
    // // Request
    // EndpointRegistration sr = c.endpointRegister(new HelloWorldEndpointImpl());
    //
    // RegisterEndpoint rs = t.take(RegisterEndpoint.class);
    // assertEquals(HelloWorldEndpoint.NAME, rs.getEndpointName());
    //
    // // Response
    // RegisterEndpointAck ack = new RegisterEndpointAck();
    // ack.setMessageAck(rs.getReplyTo());
    // ack.setMessageAck(1L);
    // ack.setLatestReceivedId(0L);
    // ack.setOldMessageId(0L);
    // t.send(ack);
    //
    // assertTrue(sr.awaitRegistered(3, TimeUnit.SECONDS));
    // }


    // @Test
    // @Ignore
    // public void findAllEndpointsOld() throws Exception {
    // MmsClient c = createAndConnect();
    //
    // // Request
    // EndpointInvocationFuture<List<HelloWorldEndpoint>> findAll = c.endpointFind(HelloWorldEndpoint.class).findAll();
    //
    // FindEndpoint fe = t.take(FindEndpoint.class);
    // assertEquals(HelloWorldEndpoint.NAME, fe.getEndpointName());
    // assertEquals(Integer.MAX_VALUE, fe.getMax().intValue());
    // assertEquals(Integer.MAX_VALUE, fe.getMeters().intValue());
    //
    // // Response
    // FindEndpointAck ack = new FindEndpointAck();
    // ack.setMessageAck(fe.getReplyTo());
    // ack.addRemoteIDS(ID3.toString());
    // ack.addRemoteIDS(ID4.toString());
    // ack.addRemoteIDS(ID5.toString());
    // t.send(ack);
    // List<HelloWorldEndpoint> list = findAll.get();
    // assertEquals(3, list.size());
    // assertEquals(ID3, list.get(0).getRemote());
    // assertEquals(ID4, list.get(1).getRemote());
    // assertEquals(ID5, list.get(2).getRemote());
    // }

    // @Test
    // public void findAllEndpoints() throws Exception {
    // MmsClient c = createAndConnect();
    //
    // // Request
    // EndpointInvocationFuture<List<HelloWorldEndpoint>> findAll = c.endpointFind(HelloWorldEndpoint.class).findAll();
    //
    // EndpointInvoke fe = t.take(EndpointInvoke.class);
    // assertEquals(HelloWorldEndpoint.NAME, fe.getEndpointName());
    // assertEquals(Integer.MAX_VALUE, fe.getMax().intValue());
    // assertEquals(Integer.MAX_VALUE, fe.getMeters().intValue());
    //
    // // Response
    // FindEndpointAck ack = new FindEndpointAck();
    // ack.setMessageAck(fe.getReplyTo());
    // ack.addRemoteIDS(ID3.toString());
    // ack.addRemoteIDS(ID4.toString());
    // ack.addRemoteIDS(ID5.toString());
    // t.send(ack);
    // List<HelloWorldEndpoint> list = findAll.get();
    // assertEquals(3, list.size());
    // assertEquals(ID3, list.get(0).getRemote());
    // assertEquals(ID4, list.get(1).getRemote());
    // assertEquals(ID5, list.get(2).getRemote());
    // }


    @Test
    public void invoke() throws Exception {
        MmsClient c = createAndConnect();

        // Request
        HelloWorldEndpoint hw = c.endpointCreate(ID4, HelloWorldEndpoint.class);
        EndpointInvocationFuture<String> f = hw.hello();

        MethodInvoke fe = t.take(MethodInvoke.class);

        assertEquals(ID4.toString(), fe.getReceiverId());
        assertEquals(c.getClientId().toString(), fe.getSenderId());
        assertEquals(HelloWorldEndpoint.NAME + ".hello", fe.getEndpointMethod());

        // Response
        MethodInvokeResult ack = new MethodInvokeResult();
        ack.setResultForMessageId(fe.getMessageId());
        ack.setResult(Binary.copyFromUtf8("\"ABC\""));
        t.send(ack);

        assertEquals("ABC", f.join());
        // assertTrue(f.receivedByCloud().isDone());
    }


    // @Test
    // public void invocation() throws Exception {
    // MmsClient c = createAndConnect();
    // AtomicReference<MessageHeader> ref = new AtomicReference<>();
    // c.endpointRegister(new AbstractHelloWorldEndpoint() {
    // @Override
    // protected String hello(MessageHeader context) {
    // ref.set(context);
    // return "FooBar";
    // }
    // });
    //
    // RegisterEndpoint rs = t.take(RegisterEndpoint.class);
    // RegisterEndpointAck ack = new RegisterEndpointAck();
    // ack.setMessageAck(rs.getReplyTo());
    // ack.setMessageAck(1L);
    // ack.setLatestReceivedId(0L);
    // ack.setOldMessageId(0L);
    // t.send(ack);
    //
    //
    // EndpointInvoke ei = new EndpointInvoke();
    // ei.setReceiverId(c.getClientId().toString());
    // ei.setSenderId(ID4.toString());
    // ei.setEndpointMethod(HelloWorldEndpoint.NAME + ".hello");
    // ei.setInvocationId(BIN1);
    // t.send(ei);
    //
    //
    // EndpointInvokeAck eia = t.take(EndpointInvokeAck.class);
    // assertEquals(ID4.toString(), eia.getReceiverId());
    // assertEquals(c.getClientId().toString(), eia.getSenderId());
    // assertEquals("\"FooBar\"", eia.getMsg());
    // assertEquals(BIN1, eia.getInvocationId());
    //
    // }
}
