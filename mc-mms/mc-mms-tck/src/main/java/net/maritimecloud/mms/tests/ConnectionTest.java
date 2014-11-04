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
package net.maritimecloud.mms.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.net.mms.MmsClient;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class ConnectionTest extends AbstractNetworkTest {

    @Test
    public void singleClient() throws Exception {
        MmsClient c = newClient(ID1);
        assertTrue(c.connection().awaitConnected(10, TimeUnit.SECONDS));
        assertEquals(1, clients(si).size());
        // c.close();
        // Thread.sleep(1000);
        // assertEquals(1, si.info().getConnectionCount());
    }

    @Test
    public void manyClients() throws Exception {
        for (MmsClient c : newClients(20)) {
            c.connection().awaitConnected(10, TimeUnit.SECONDS);
        }
        assertEquals(20, clients(si).size());
    }

    @Test
    public void singleClientClose() throws Exception {
        MmsClient pc1;
        MmsClient pc = newClient(ID1);
        pc1 = pc;
        assertTrue(pc1.connection().awaitConnected(10, TimeUnit.SECONDS));
        assertEquals(1, clients(si).size());
        pc.connection().awaitConnected(1, TimeUnit.SECONDS);
        assertEquals(1, clients(si).size());

        assertTrue(pc1.isClosed());
        pc1.awaitTermination(1, TimeUnit.SECONDS);
        for (int i = 0; i < 100; i++) {
            if (clients(si).size() == 0) {
                return;
            }
            Thread.sleep(1);
        }
        // fail();
    }
}
