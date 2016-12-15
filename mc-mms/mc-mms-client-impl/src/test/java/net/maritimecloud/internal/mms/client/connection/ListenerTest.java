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
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Ignore;
import org.junit.Test;

import net.maritimecloud.internal.mms.client.ClientInfo;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class ListenerTest extends AbstractConnectionTest {

    @Test
    @Ignore
    public void connectDisconnect() throws Exception {
        AtomicReference<String> state = new AtomicReference<>();
        CountDownLatch connected = new CountDownLatch(1);
        conf.addListener(new MmsConnection.Listener() {

            @Override
            public void connecting(URI host) {
                state.compareAndSet(null, "connecting");
            }

            @Override
            public void connected(URI host, boolean isReconnect) {
                state.compareAndSet("connecting", "connected");
                connected.countDown();
            }

            @Override
            public void disconnected(MmsConnectionClosingCode closeReason) {
                state.compareAndSet("connected", "disconnected");
            }
        });
        ClientConnection cc = new ClientConnection(ctm, new ClientInfo(conf), conf);

        cc.setEnabled(true);
        assertTrue(cc.isEnabled());

        t.take(Hello.class);
        assertEquals("connecting", state.get());
        t.send(new Connected().setSessionId(Binary.random(32)));

        assertTrue(connected.await(2, TimeUnit.SECONDS));

        cc.setEnabled(false);

    }
}
