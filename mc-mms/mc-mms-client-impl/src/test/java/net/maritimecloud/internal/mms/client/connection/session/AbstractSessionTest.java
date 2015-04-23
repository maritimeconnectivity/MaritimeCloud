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
package net.maritimecloud.internal.mms.client.connection.session;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import net.maritimecloud.internal.mms.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.mms.client.ClientInfo;
import net.maritimecloud.internal.mms.client.connection.transport.ClientTransportFactoryJetty;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.Binary;

import org.junit.After;
import org.junit.Before;

/**
 *
 * @author Kasper Nielsen
 */
public class AbstractSessionTest extends AbstractClientConnectionTest {

    volatile Consumer<MmsConnectionClosingCode> connectedNormallySessionCloseConsumer;

    ClientTransportFactoryJetty ctm = new ClientTransportFactoryJetty();

    Session connectNormally(Consumer<MmsMessage> c) throws InterruptedException {
        return connectNormally(c, new MmsConnection.Listener() {});
    }

    Session connectNormally(Consumer<MmsMessage> c, MmsConnection.Listener listener) throws InterruptedException {
        ClientInfo ci = new ClientInfo(conf);
        CountDownLatch connected = new CountDownLatch(1);
        Session s = Session.createNewSessionAndConnect(ctm, ci, new SessionListener() {
            @Override
            public void onMessage(MmsMessage message) {
                c.accept(message);
            }

            /** {@inheritDoc} */
            @Override
            public void onSessionClose(MmsConnectionClosingCode closingCode) {
                Consumer<MmsConnectionClosingCode> c = connectedNormallySessionCloseConsumer;
                if (c != null) {
                    c.accept(closingCode);
                }
            }
        }, new DelegateConnectionListener(listener) {

            /** {@inheritDoc} */
            @Override
            public void connected(URI host) {
                connected.countDown();
                super.connected(host);
            }

        });

        t.take(Hello.class);
        Connected co = new Connected();
        co.setSessionId(Binary.random(32));
        t.send(co);

        assertTrue(connected.await(2, TimeUnit.SECONDS));
        return s;
    }

    Session connect(SessionListener sessionListener, MmsConnection.Listener listener) throws Exception {
        ClientInfo ci = new ClientInfo(conf);
        CountDownLatch connected = new CountDownLatch(1);
        Session s = Session.createNewSessionAndConnect(ctm, ci, sessionListener, new DelegateConnectionListener(
                listener) {

            /** {@inheritDoc} */
            @Override
            public void connected(URI host) {
                connected.countDown();
                super.connected(host);
            }

        });

        t.take(Hello.class);
        Connected co = new Connected();
        co.setSessionId(Binary.random(32));
        t.send(co);
        assertTrue(connected.await(2, TimeUnit.SECONDS));
        return s;
    }


    @Before
    public void setup() throws Exception {
        ctm.start();
    }

    @After
    public void teardown() throws Exception {
        ctm.stop();
    }
}
