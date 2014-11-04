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
package net.maritimecloud.server.endpoint;

import net.maritimecloud.server.AbstractServerConnectionTest;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class EndpointTest extends AbstractServerConnectionTest {

    @Test
    public void ignore() {}
    // @Test
    // public void foo() {
    // HelloWorld hw = new HelloWorld().setMsg("foo1");
    // Broadcast bp = MessageHelpers.create(ID1, PositionTime.create(1, 1, 1), hw, new BroadcastSendOptions())
    // .setReplyTo(1234L);
    // System.out.println(bp.toJSON());
    //
    // Broadcast bp2 = MessageSerializer.readFromJSON(BroadcastPublish.PARSER, bp.toJSON());
    // System.out.println(bp2.toJSON());
    //
    // System.out.println(bp.equals(bp2));
    // System.out.println(bp.getArea().equals(bp2.getArea()));
    //
    // }

    //
    // @Test
    // public void registerEndpoint() throws Exception {
    // TesstEndpoint c1 = newClient(ID1);
    // // TesstEndpoint c2 = newClient(ID6);
    //
    // RegisterEndpoint rs = new RegisterEndpoint().setEndpointName("FooBar").setReplyTo(123L).setLatestReceivedId(1L)
    // .setOldMessageId(1L);
    // c1.send(rs);
    //
    // RegisterEndpointAck ack = c1.take(RegisterEndpointAck.class);
    // assertEquals(123L, ack.getMessageAck().longValue());
    // // HelloWorld tryRead = (HelloWorld) MessageHelpers.tryRead(bd);
    // // assertEquals("foo1", tryRead.getMsg());
    //
    // // assertEquals(1234, c1.take(BroadcastPublishAck.class).getMessageAck().longValue());
    // }


    //
    // @Test
    // public void twoClients() throws Exception {
    // TesstEndpoint c1 = newClient(ID1);
    // TesstEndpoint c3 = newClient(ID3);
    // TesstEndpoint c4 = newClient(ID4);
    //
    // HelloWorld hw = new HelloWorld().setMsg("foo1");
    // c1.send(MessageHelpers.create(ID1, PositionTime.create(1, 1, 1), hw, new BroadcastSendOptions()).setReplyTo(
    // 4321L));
    //
    // assertEquals("foo1", ((HelloWorld) MessageHelpers.tryRead(c3.take(BroadcastRelay.class))).getMsg());
    // assertEquals("foo1", ((HelloWorld) MessageHelpers.tryRead(c4.take(BroadcastRelay.class))).getMsg());
    //
    // assertEquals(4321, c1.take(BroadcastPublishAck.class).getMessageAck().longValue());
    // }
    //
    // @Test
    // public void areaBroadcast() throws Exception {
    // TesstEndpoint c1 = newClient(ID1, 1, 1);
    // TesstEndpoint c3 = newClient(ID3, 1.63, 1.63);
    // TesstEndpoint c4 = newClient(ID4, 1.64, 1.64);
    //
    // c1.send(MessageHelpers.create(ID1, PositionTime.create(1, 1, 1), new HelloWorld().setMsg("foo1"),
    // new BroadcastSendOptions().setBroadcastRadius(100000)).setReplyTo(5L));
    //
    // assertEquals("foo1", ((HelloWorld) MessageHelpers.tryRead(c3.take(BroadcastRelay.class))).getMsg());
    //
    // // send message with c4 with 110 kilometers
    // c1.send(MessageHelpers.create(ID1, PositionTime.create(1, 1, 1), new HelloWorld().setMsg("foo2"),
    // new BroadcastSendOptions().setBroadcastRadius(110000)).setReplyTo(6L));
    //
    // assertEquals("foo2", ((HelloWorld) MessageHelpers.tryRead(c3.take(BroadcastRelay.class))).getMsg());
    // assertEquals("foo2", ((HelloWorld) MessageHelpers.tryRead(c4.take(BroadcastRelay.class))).getMsg());
    //
    //
    // assertEquals(5, c1.take(BroadcastPublishAck.class).getMessageAck().longValue());
    // assertEquals(6, c1.take(BroadcastPublishAck.class).getMessageAck().longValue());
    // }

}
