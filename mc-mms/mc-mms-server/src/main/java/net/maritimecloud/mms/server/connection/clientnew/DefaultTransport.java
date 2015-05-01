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
package net.maritimecloud.mms.server.connection.clientnew;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.Welcome;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.MmsServer;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;
import net.maritimecloud.mms.server.connection.transport.ServerTransportListener;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

/**
 *
 * @author Kasper Nielsen
 */
public class DefaultTransport implements ServerTransportListener {

    /** The client manager responsible for creating a new client when a hello message is received. */
    private final ClientManager clientManager;

    /** The id of the server. */
    private final String serverId;

    /**
     * We keep track of clients that have not yet send a hello. This is done in order to be able to close those
     * connections at some point. Otherwise they will be lying around forever, unless the client closes the socket.
     */
    private final Set<ServerTransport> missingHellos = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public DefaultTransport(ClientManager clientManager, MmsServer server) {
        this.clientManager = requireNonNull(clientManager);
        this.serverId = server.getServerId().toString();
    }

    /** {@inheritDoc} */
    @Override
    public void onClose(ServerTransport t, MmsConnectionClosingCode closingCode) {
        // if _succesfully_ connected the transport have already been removed. If connection failed. It is most likely
        // still there so we are just going to remove it to be sure.
        missingHellos.remove(t);

        Object o = t.getAttachment();
        if (o != null) {
            ((InternalClient) o).onClose(t, closingCode);
            t.setAttachment(null); // clear attachment
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onMessage(ServerTransport t, MmsMessage message) {
        Message m = message.getM();
        if (m instanceof Welcome) {
            t.close(MmsConnectionClosingCode.WRONG_MESSAGE.withMessage("A client must not send a Welcome message"));
        } else if (m instanceof Connected) {
            t.close(MmsConnectionClosingCode.WRONG_MESSAGE.withMessage("A client must not send a Connected message"));
        } else {
            Object client = t.getAttachment();
            if (client == null) {
                if (m instanceof Hello) {
                    Hello h = (Hello) m;
                    missingHellos.remove(t);
                    MaritimeId mid = MaritimeId.create(h.getClientId());
                    String clientId = mid.toString();
                    InternalClient newClient = clientManager.onHello(clientId, h, t);
                    if (newClient != null) {
                        t.setAttachment(newClient);
                    }
                } else {
                    t.close(MmsConnectionClosingCode.WRONG_MESSAGE
                            .withMessage("A Hello message must be sent to succesfully connect, but received a : "
                                    + m.getClass().getSimpleName() + " message"));
                }
            } else if (m instanceof Hello) {
                t.close(MmsConnectionClosingCode.WRONG_MESSAGE
                        .withMessage("A client must not send a Hello message more than once"));
            } else {
                ((InternalClient) client).onMessage(t, message);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onOpen(ServerTransport t) {
        // Will only be invoked once
        ServerTransportListener.super.onOpen(t);
        // send a Welcome message to the client as the first thing
        MmsMessage hello = new MmsMessage(new Welcome().addProtocolVersion(1).setServerId(serverId)
                .putProperties("implementation", "mmsServer/0.2"));

        // Send hello
        t.sendMessage(hello);

        missingHellos.add(t);
    }
}
