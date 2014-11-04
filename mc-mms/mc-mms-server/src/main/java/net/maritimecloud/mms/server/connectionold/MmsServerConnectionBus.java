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
import net.maritimecloud.internal.mms.messages.PositionReport;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.net.messages.MethodInvoke;
import net.maritimecloud.internal.net.messages.MethodInvokeResult;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.broadcast.ServerBroadcastManager;
import net.maritimecloud.mms.server.endpoints.ServerEndpointManager;
import net.maritimecloud.mms.server.targets.Target;
import net.maritimecloud.mms.server.targets.TargetManager;

/**
 *
 * @author Kasper Nielsen
 */
public class MmsServerConnectionBus {

    ServerBroadcastManager sbm;

    final TargetManager tm;

    final ServerEndpointManager sem;

    public MmsServerConnectionBus(ServerEndpointManager sem, TargetManager tm) {
        this.tm = requireNonNull(tm);
        this.sem = sem;
    }

    public void onBroadcast(ServerConnection connection, Broadcast message) {
        try {
            PositionReport srm = sbm.broadcast(connection, message);
            connection.messageSend(srm);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMessage(ServerConnection connection, Message message) {
        if (message instanceof PositionReport) {
            connection.getTarget().setLatestPosition(((PositionReport) message).getPositionTime());
        } else if (message instanceof Broadcast) {
            onBroadcast(connection, (Broadcast) message);
        } else if (message instanceof MethodInvoke) {
            MethodInvoke mi = (MethodInvoke) message;
            onMethodInvoke(connection, message, mi.getReceiverId(), mi.getSenderId());
        } else if (message instanceof MethodInvokeResult) {
            MethodInvokeResult mi = (MethodInvokeResult) message;
            onMethodInvoke(connection, message, mi.getOriginalSenderId(), mi.getReceiverId());
        }
    }

    public void onMethodInvoke(ServerConnection connection, Message m, String receiverId, String senderId) {
        if (receiverId == null) {
            if (m instanceof MethodInvoke) {
                sem.invokeLocally(connection, (MethodInvoke) m);
            }
            return;
        }
        Target t = tm.find(MaritimeId.create(receiverId));
        if (t == null) {
            System.err.println("Unknown destination " + receiverId);
            return;
        }
        ServerConnection sc = t.getActiveConnection();
        if (sc == null) {
            System.err.println("Unknown destination " + receiverId);
            return;
        }
        sc.messageSend(m);
    }

    public void setBroadcastManager(ServerBroadcastManager sbm) {
        this.sbm = sbm;
    }
}
