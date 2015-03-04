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
package net.maritimecloud.internal.mms.client.connection;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.ClientInfo;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.net.messages.BroadcastAck;
import net.maritimecloud.internal.util.concurrent.CompletableFuture;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class DisableEnableTest extends AbstractConnectionTest {

    @Test
    public void enableDisable() throws Exception {
        ClientConnection cc = new ClientConnection(ctm, new ClientInfo(conf), conf);
        int rounds = 10 + ThreadLocalRandom.current().nextInt(10);
        for (int i = 0; i < rounds; i++) {
            connectNormally(cc);
            assertTrue(cc.await(true, 2, TimeUnit.SECONDS));
            long msgClient = 0;
            int msgServer = 0;
            List<CompletableFuture<Void>> m = new ArrayList<>();


            for (int j = 0; j < rounds; j++) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    msgClient++;
                    m.add(cc.sendMessage(new Broadcast().setSenderId("foo" + j)));
                }

                if (ThreadLocalRandom.current().nextBoolean()) {
                    t.send(new BroadcastAck(), ++msgServer, msgClient);
                }
            }
            for (int j = 0; j < m.size(); j++) {
                t.take(Broadcast.class);
            }
            closeNormally(cc);
            assertTrue(cc.await(false, 2, TimeUnit.SECONDS));
            for (CompletableFuture<Void> cf : m) {
                cf.get(2, TimeUnit.SECONDS);
            }
        }
    }
}
