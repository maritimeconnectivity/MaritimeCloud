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
package net.maritimecloud.internal.net.server.broadcast;

import static org.junit.Assert.assertEquals;
import net.maritimecloud.internal.net.client.broadcast.stubs.HelloWorld;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastDeliver;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastSend;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastSendAck;
import net.maritimecloud.internal.net.server.AbstractServerConnectionTest;
import net.maritimecloud.internal.net.server.TesstEndpoint;
import net.maritimecloud.net.broadcast.BroadcastOptions;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.Test;

/**
 * 
 * @author Kasper Nielsen
 */
public class BroadcastTest extends AbstractServerConnectionTest {

    @Test
    public void oneClient() throws Exception {
        TesstEndpoint c1 = newClient(ID1);
        TesstEndpoint c2 = newClient(ID6);

        HelloWorld hw = new HelloWorld("foo1");
        c1.send(BroadcastSend.create(ID1, PositionTime.create(1, 1, 1), hw, new BroadcastOptions()).setReplyTo(1234));

        BroadcastDeliver bd = c2.take(BroadcastDeliver.class);
        HelloWorld tryRead = (HelloWorld) bd.tryRead();
        assertEquals("foo1", tryRead.getMessage());

        assertEquals(1234, c1.take(BroadcastSendAck.class).getMessageAck());
    }

    @Test
    public void twoClients() throws Exception {
        TesstEndpoint c1 = newClient(ID1);
        TesstEndpoint c3 = newClient(ID3);
        TesstEndpoint c4 = newClient(ID4);

        HelloWorld hw = new HelloWorld("foo1");
        c1.send(BroadcastSend.create(ID1, PositionTime.create(1, 1, 1), hw, new BroadcastOptions()).setReplyTo(4321));

        assertEquals("foo1", ((HelloWorld) c3.take(BroadcastDeliver.class).tryRead()).getMessage());
        assertEquals("foo1", ((HelloWorld) c4.take(BroadcastDeliver.class).tryRead()).getMessage());

        assertEquals(4321, c1.take(BroadcastSendAck.class).getMessageAck());
    }

    @Test
    public void areaBroadcast() throws Exception {
        TesstEndpoint c1 = newClient(ID1, 1, 1);
        TesstEndpoint c3 = newClient(ID3, 1.63, 1.63);
        TesstEndpoint c4 = newClient(ID4, 1.64, 1.64);

        c1.send(BroadcastSend.create(ID1, PositionTime.create(1, 1, 1), new HelloWorld("foo1"),
                new BroadcastOptions().setBroadcastRadius(100000)).setReplyTo(5));

        assertEquals("foo1", ((HelloWorld) c3.take(BroadcastDeliver.class).tryRead()).getMessage());

        // send message with c4 with 110 kilometers
        c1.send(BroadcastSend.create(ID1, PositionTime.create(1, 1, 1), new HelloWorld("foo2"),
                new BroadcastOptions().setBroadcastRadius(110000)).setReplyTo(6));

        assertEquals("foo2", ((HelloWorld) c3.take(BroadcastDeliver.class).tryRead()).getMessage());
        assertEquals("foo2", ((HelloWorld) c4.take(BroadcastDeliver.class).tryRead()).getMessage());


        assertEquals(5, c1.take(BroadcastSendAck.class).getMessageAck());
        assertEquals(6, c1.take(BroadcastSendAck.class).getMessageAck());
    }

}
