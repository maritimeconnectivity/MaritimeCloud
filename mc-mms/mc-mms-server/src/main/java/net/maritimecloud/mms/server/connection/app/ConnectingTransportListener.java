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
package net.maritimecloud.mms.server.connection.app;

import static java.util.Objects.requireNonNull;
import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.Welcome;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;
import net.maritimecloud.mms.server.connection.transport.ServerTransportListener;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

/**
 *
 * @author Kasper Nielsen
 */
public class ConnectingTransportListener implements ServerTransportListener {

    final AppLayerManager alm;

    final DelegatingTransportListener dtl;

    ServerTransport t;

    ConnectingTransportListener(AppLayerManager alm, DelegatingTransportListener dtl) {
        this.alm = requireNonNull(alm);
        this.dtl = dtl;
    }

    /** {@inheritDoc} */
    @Override
    public void onClose(ServerTransport t, MmsConnectionClosingCode closingCode) {
        // Well not really my problem. Client must reconnect
    }

    /**
     * @param m
     */
    private void onHello(Hello m) {
        String id = m.getClientId();
        AppLayer a = alm.getOrCreate(MaritimeId.create(id));
        a.onHello(this, m);
    }

    /** {@inheritDoc} */
    @Override
    public void onMessage(ServerTransport t, MmsMessage message) {
        Message m = message.getM();
        if (m instanceof Hello) {
            onHello((Hello) m);
        } else {
            String err = "Expected a welcome message, but was: " + m.getClass().getSimpleName();
            t.close(MmsConnectionClosingCode.WRONG_MESSAGE.withMessage(err));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onOpen(ServerTransport t) {
        this.t = requireNonNull(t);
        Welcome w = new Welcome().addProtocolVersion(1).setServerId(alm.server.getServerId().toString())
                .putProperties("implementation", "mmsServer/0.3");
        t.sendMessage(new MmsMessage(w));
    }

}
