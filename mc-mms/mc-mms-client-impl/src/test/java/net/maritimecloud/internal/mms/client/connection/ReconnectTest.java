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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class ReconnectTest extends AbstractConnectionTest {


    @Test
    @Ignore
    public void reconnectSameSession() throws Exception {
        reconnect(true);
    }

    private void reconnect(boolean keepSession) throws Exception {
        ClientConnection cc = connectNormally();
        int rounds = 10 + ThreadLocalRandom.current().nextInt(10);

        List<CompletableFuture<Void>> m = new ArrayList<>();
        for (int i = 0; i < rounds; i++) {
            m.add(cc.sendMessage(new Broadcast().setSenderId("foo" + i)));
        }
        t.disconnect();
        t.m.clear();
        long lastReceivedId = 0;
        long cliId = 1;

        while (!m.isEmpty()) {
            long mId = lastReceivedId;
            MmsMessage mm = t.t();
            while (mm.getMessage() instanceof Broadcast) {
                mm = t.t();
            }

            Hello h = mm.cast(Hello.class);
            for (;;) {
                t.send(new Connected().setSessionId(h.getSessionId()).setLastReceivedMessageId(lastReceivedId));

                if (ThreadLocalRandom.current().nextBoolean()) {
                    assertEquals("foo" + mId++, t.take(Broadcast.class).getSenderId());
                }

                if (ThreadLocalRandom.current().nextBoolean()) {
                    lastReceivedId++;
                    t.send(new Broadcast(), cliId++, lastReceivedId);
                }

                if (ThreadLocalRandom.current().nextBoolean()) {
                    t.disconnect();
                    t.m.clear();
                    break;
                }
            }
        }
    }
}
