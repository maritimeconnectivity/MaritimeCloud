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

import net.maritimecloud.internal.mms.messages.Close;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

/**
 *
 * @author Kasper Nielsen
 */
final class SessionStateDisconnecting extends SessionState {

    /**
     * @param session
     */
    private SessionStateDisconnecting(Session session) {
        super(session);
    }

    /**
     * @param reason
     */
    static void disconnectWhileFullyLocked(SessionStateConnected connected, MmsConnectionClosingCode reason) {
        Session s = connected.session;
        Close close = new Close().setCloseCode(1)/* .setMaintainSession(false) */
        .setLastReceivedMessageId(s.latestReceivedId);
        MmsMessage mm = new MmsMessage(close);
        connected.transport.sendMessage(mm);
        s.state = new SessionStateDisconnecting(s);
    }
}
