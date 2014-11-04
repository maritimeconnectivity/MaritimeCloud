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
package net.maritimecloud.mms.server.connectionold;

import static java.util.Objects.requireNonNull;
import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.connectionold.transport.ServerTransport;
import net.maritimecloud.mms.server.targets.Target;
import net.maritimecloud.mms.server.targets.TargetManager;
import net.maritimecloud.mms.server.targets.TargetProperties;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kasper Nielsen
 */
public class ServerConnectFuture {

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ServerConnectFuture.class);

    final ServerTransport serverTransport;

    /**
     * @param serverTransport
     */
    public ServerConnectFuture(ServerTransport serverTransport) {
        this.serverTransport = requireNonNull(serverTransport);
    }

    public void onMessage(Hello hm) {
        TargetManager tm = serverTransport.cm.targetManager;
        Target target = tm.getTarget(MaritimeId.create(hm.getClientId()));

        // make sure we only have one connection attempt for a target at a time
        target.fullyLock();
        try {
            ServerConnection connection = target.getActiveConnection();
            boolean isReconnect = connection != null;

            if (isReconnect) {
                ServerTransport st = connection.transport;
                if (st != null) {
                    connection.transport = null;
                    st.doClose(MmsConnectionClosingCode.DUPLICATE_CONNECT);
                }
                target.setLatestPosition(hm.getPositionTime());
            } else {
                connection = new ServerConnection(target, serverTransport.server);
                target.setLatestPosition(hm.getPositionTime());
                target.setConnection(connection);
            }
            target.setProperties(TargetProperties.createFrom(hm.getProperties()));


            connection.transport = serverTransport;

            long id = connection.worker.getLatestReceivedId();

            if (isReconnect) {} else {
                new Thread(connection.worker).start();
            }
            serverTransport.sendText(MmsMessage.toText(new Connected().setSessionId(connection.id)
                    .setLastReceivedMessageId(id)));
            serverTransport.connection = connection;
            serverTransport.connectFuture = null;
            connection.worker.onConnect(serverTransport,
                    hm.getLastReceivedMessageId() == null ? 0 : hm.getLastReceivedMessageId(), isReconnect);
        } finally {
            target.fullyUnlock();
        }
    }

    /**
     * @param msg
     */
    public void onMessage(Message m) {
        if (m instanceof Hello) {
            Hello hm = (Hello) m;
            onMessage(hm);
        } else {
            String err = "Expected a welcome message, but was: " + m.getClass().getSimpleName();
            LOG.error(err);
            // transport.doClose(ClosingCode.WRONG_MESSAGE.withMessage(err));
        }
    }
}
