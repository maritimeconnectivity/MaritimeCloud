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
package net.maritimecloud.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.net.ClosingCode;
import net.maritimecloud.net.MaritimeCloudConnection;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Kasper Nielsen
 */
public class ConnectionStateListenerTest extends AbstractNetworkTest {

    @Test
    public void connected() throws Exception {
        final CountDownLatch cdl = new CountDownLatch(1);
        newClient(newBuilder(ID1).addListener(new AbstractConnectionTestListener() {
            public void connected() {
                assertEquals(1, cdl.getCount());
                cdl.countDown();
            }
        }));
        assertTrue(cdl.await(1, TimeUnit.SECONDS));
    }

    @Test
    public void connectedTwoListeners() throws Exception {
        final CountDownLatch cdl = new CountDownLatch(2);
        newClient(newBuilder(ID1).addListener(new AbstractConnectionTestListener() {
            public void connected() {
                cdl.countDown();
            }
        }).addListener(new AbstractConnectionTestListener() {
            public void connected() {
                cdl.countDown();
            }
        }));
        assertTrue(cdl.await(1, TimeUnit.SECONDS));
    }

    @Test
    @Ignore
    public void closed() throws Exception {
        final CountDownLatch cdl = new CountDownLatch(1);
        newClient(newBuilder(ID1).addListener(new AbstractConnectionTestListener() {
            public void disconnected(ClosingCode reason) {
                assertEquals(1000, reason.getId());
                cdl.countDown();
            }
        }));
        assertTrue(cdl.await(1, TimeUnit.SECONDS));
    }

    static class AbstractConnectionTestListener extends MaritimeCloudConnection.Listener {

    }
}
