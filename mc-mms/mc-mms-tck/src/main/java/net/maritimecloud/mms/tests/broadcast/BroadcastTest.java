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
package net.maritimecloud.mms.tests.broadcast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.mms.tests.AbstractNetworkTest;
import net.maritimecloud.net.mms.MmsClient;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class BroadcastTest extends AbstractNetworkTest {

    @Test
    public void oneBroadcast() throws Exception {
        MmsClient c1 = newClient(ID1, 1, 1);
        MmsClient c2 = newClient(ID6, 1, 1);
        final CountDownLatch cdl = new CountDownLatch(1);
        assertTrue(c2.connection().awaitConnected(1, TimeUnit.SECONDS));
        c2.broadcastSubscribe(BroadcastTestMessage.class, (header, m) -> {
            assertEquals("fooo", m.getMsg());
            cdl.countDown();
        });


        c1.broadcast(new BroadcastTestMessage().setMsg("fooo"));
        assertTrue(cdl.await(4, TimeUnit.SECONDS));
    }

    @Test
    public void multipleReceivers() throws Exception {
        MmsClient c1 = newClient(ID1, 1, 1);
        final CountDownLatch cdl = new CountDownLatch(10);

        List<MmsClient> clients = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MmsClient mc = newClient(MaritimeId.create("mmsi:1234" + i), 1, 1);
            clients.add(mc);
        }
        for (MmsClient mnc : clients) {
            assertTrue(mnc.connection().awaitConnected(1, TimeUnit.SECONDS));
            mnc.broadcastSubscribe(BroadcastTestMessage.class, (header, m) -> {
                assertEquals("fooo", m.getMsg());
                cdl.countDown();
            });
        }

        c1.broadcast(new BroadcastTestMessage().setMsg("fooo"));
        assertTrue(cdl.await(4, TimeUnit.SECONDS));
    }

    @Test
    public void receiveNotSelf() throws Exception {
        MmsClient c1 = newClient(ID1, 1, 1);
        MmsClient c2 = newClient(ID6, 1, 1);
        final CountDownLatch cdl1 = new CountDownLatch(1);
        final CountDownLatch cdl2 = new CountDownLatch(1);
        c1.broadcastSubscribe(BroadcastTestMessage.class, (header, m) -> {
            assertEquals("fooo", m.getMsg());
            cdl1.countDown();
        });
        c2.broadcastSubscribe(BroadcastTestMessage.class, (header, m) -> {
            assertEquals("fooo", m.getMsg());
            cdl2.countDown();
        });

        c1.broadcast(new BroadcastTestMessage().setMsg("fooo"));
        assertTrue(cdl2.await(4, TimeUnit.SECONDS));
        assertFalse(cdl1.await(20, TimeUnit.MILLISECONDS));
    }
}
