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

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.mms.server.connection.session.ServerSession;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

/**
 *
 * @author Kasper Nielsen
 */
public class AppLayer {

    /**
     * @param appLayerManager
     * @param id
     */
    public AppLayer(AppLayerManager appLayerManager, MaritimeId id) {}

    final ReentrantLock lock = new ReentrantLock();

    private ServerSession session;

    void onHello(ConnectingTransportListener ctl, Hello h) {
        if (session == null) {
            if (h.getSessionId() == null) {
                session = ServerSession.create();
            } else {
                ctl.t.close(MmsConnectionClosingCode.INVALID_SESSION);
                return;
            }
        } else {
            if (h.getSessionId() == null) {
                session.invalidate(MmsConnectionClosingCode.DUPLICATE_CONNECT);
                session = ServerSession.create();
            } else if (Objects.equals(session.getSessionId(), h.getSessionId())) {
                if (!session.reconnect(h.getLastReceivedMessageId())) {
                    session = null;
                    ctl.t.close(MmsConnectionClosingCode.INVALID_SESSION);
                    return;
                }
            } else { // connected with an invalid session id
                ctl.t.close(MmsConnectionClosingCode.INVALID_SESSION);
                return;
            }
        }
        // update position
    }
}
