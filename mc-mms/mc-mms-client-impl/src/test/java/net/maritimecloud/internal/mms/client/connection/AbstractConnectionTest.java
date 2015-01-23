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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.mms.client.ClientInfo;
import net.maritimecloud.internal.mms.client.connection.transport.ClientTransportFactoryJetty;
import net.maritimecloud.internal.mms.messages.Close;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.util.Binary;

import org.junit.After;
import org.junit.Before;

/**
 *
 * @author Kasper Nielsen
 */
public class AbstractConnectionTest extends AbstractClientConnectionTest {

    ClientTransportFactoryJetty ctm = new ClientTransportFactoryJetty();

    @Before
    public void setup() throws Exception {
        ctm.start();
    }

    @After
    public void teardown() throws Exception {
        ctm.stop();
    }

    ClientConnection connectNormally() throws InterruptedException {
        ClientConnection cc = new ClientConnection(ctm, new ClientInfo(conf), conf);
        assertFalse(cc.isEnabled());
        assertFalse(cc.isConnected());
        connectNormally(cc);
        return cc;
    }


    Binary connectNormally(ClientConnection cc) throws InterruptedException {
        cc.setEnabled(true);
        assertTrue(cc.isEnabled());
        t.take(Hello.class);
        Binary b = Binary.random(32);
        t.send(new Connected().setSessionId(b));
        assertTrue(cc.await(true, 2, TimeUnit.SECONDS));
        return b;
    }

    void closeNormally(ClientConnection cc) throws Exception {
        cc.setEnabled(false);
        t.take(Close.class);
        t.closeNormally();

        assertTrue(cc.await(false, 2, TimeUnit.SECONDS));

    }

}
