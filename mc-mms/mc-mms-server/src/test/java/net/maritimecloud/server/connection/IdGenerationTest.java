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

import static org.junit.Assert.assertEquals;
import net.maritimecloud.internal.mms.messages.PositionReport;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
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
public class IdGenerationTest extends AbstractServerConnectionTest {


    @Ignore
    @Test
    public void oneClient() throws Exception {
        TesstEndpoint c1 = newClient(ID1);
        TesstEndpoint c6 = newClient(ID6);

        BroadcastTestMessage hw = new BroadcastTestMessage().setMsg("foo1");
        c1.send(BroadcastTest.createBroadcast(ID1, PositionTime.create(1, 1, 1), hw, null, 10, null));

        MmsMessage bd = c6.take();
        assertEquals(1, bd.getMessageId());
        assertEquals(0, bd.getLatestReceivedId());

        c6.send(BroadcastTest.createBroadcast(ID1, PositionTime.create(1, 1, 1), hw, null, 10, null));

        bd = c1.take();
        assertEquals(1, bd.getMessageId());
        assertEquals(1, bd.getLatestReceivedId());

        c1.take(PositionReport.class);
    }


}
