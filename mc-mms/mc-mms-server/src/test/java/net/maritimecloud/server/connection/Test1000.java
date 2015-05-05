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
package net.maritimecloud.server.connection;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.mms.stubs.BroadcastTestMessage;
import net.maritimecloud.server.AbstractServerConnectionTest;
import net.maritimecloud.server.TesstEndpoint;
import net.maritimecloud.server.broadcast.BroadcastTest;
import net.maritimecloud.util.geometry.PositionTime;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class Test1000 extends AbstractServerConnectionTest {

    @Ignore
    @Test
    public void testIt() throws Exception {
        AtomicLong count = new AtomicLong();
        Map<MaritimeId, TesstEndpoint> s = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            MaritimeId id = MaritimeId.create("mmsi:" + i);
            TesstEndpoint t = newClient(id);
            s.put(id, t);
        }
        for (Entry<MaritimeId, TesstEndpoint> e : s.entrySet()) {
            BroadcastTestMessage hw = new BroadcastTestMessage().setMsg("foo1");
            e.getValue().send(
                    BroadcastTest.createBroadcast(e.getKey(), PositionTime.create(1, 1, 1), hw, null, 100, null));
        }
        for (int i = 0; i < s.size() - 1; i++) {
            for (Entry<MaritimeId, TesstEndpoint> e : s.entrySet()) {
                System.err.println(count.incrementAndGet());
            }
        }
        for (Entry<MaritimeId, TesstEndpoint> e : s.entrySet()) {
            e.getValue().close();
        }
        for (Entry<MaritimeId, TesstEndpoint> e : s.entrySet()) {
            e.getValue().take();
        }
        System.out.println(count.get());
    }
}
