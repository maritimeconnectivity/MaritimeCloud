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
package net.maritimecloud.server.broadcast;

import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.server.AbstractServerConnectionTest;
import net.maritimecloud.server.TesstEndpoint;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.Test;

/**
 * Test acknowledgments from receivers of broadcasts.
 *
 * @author Kasper Nielsen
 */
public class BroadcastReceiverAck extends AbstractServerConnectionTest {

    @Test
    public void receiverAck() throws Exception {
        TesstEndpoint c1 = newClient(ID1);
        TesstEndpoint c6 = newClient(ID6);

        BroadcastTestMessage hw = new BroadcastTestMessage().setMsg("foo1");
        Broadcast bp = BroadcastTest.createBroadcast(ID1, PositionTime.create(1, 1, 1), hw, null, 10, e -> {});
        c1.send(bp);

        Broadcast bd = c6.take(Broadcast.class);
        // BroadcastTestMessage tryRead = (BroadcastTestMessage) MessageHelpers.tryRead(bd);
        // assertEquals("foo1", tryRead.getMsg());
        //
        // long time = System.currentTimeMillis();
        // System.out.println(time);
        // c6.send(new PositionReport().setPositionTime(PositionTime.create(4, 4, time))
        // .setLatestReceivedId(bd.getOldMessageId()).setOldMessageId(1L));
        // c1.take(BroadcastPublishAck.class);
        // Thread.sleep(100);
        //
        // BroadcastAck ba = c1.take(BroadcastAck.class);
        // assertEquals(ba.getAckToBroadcastId(), bp.getBroadcastId());
        // assertEquals(Position.create(4, 4), ba.getReceiverPosition());
        // assertEquals(time, ba.getReceiverTimestamp().getTime());
        // assertEquals(ID6.toString(), ba.getReceiverId());
    }
}
