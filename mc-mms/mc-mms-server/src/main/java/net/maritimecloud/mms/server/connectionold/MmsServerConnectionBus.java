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

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.messages.PositionReport;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.net.messages.MethodInvoke;
import net.maritimecloud.internal.net.messages.MethodInvokeResult;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.broadcast.ServerBroadcastManager;
import net.maritimecloud.mms.server.connection.client.Client;
import net.maritimecloud.mms.server.connection.client.ClientManager;
import net.maritimecloud.mms.server.endpoints.ServerEndpointManager;

/**
 *
 * @author Kasper Nielsen
 */
public class MmsServerConnectionBus {

    ServerBroadcastManager sbm;

    final ClientManager tm;

    final ServerEndpointManager sem;

    // Metrics
    final Meter broadcastsMeter;
    final Meter methodInvokesMeter;
    final Meter positionReportsMeter;


    public MmsServerConnectionBus(ServerEndpointManager sem, ClientManager tm, MetricRegistry metrics) {
        this.tm = requireNonNull(tm);
        this.sem = sem;

        // Metrics
        broadcastsMeter = metrics.meter("broadcasts");
        methodInvokesMeter = metrics.meter("methodInvokes");
        positionReportsMeter = metrics.meter("positionReports");
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
            positionReportsMeter.mark();
        } else if (message instanceof Broadcast) {
            onBroadcast(connection, (Broadcast) message);
            broadcastsMeter.mark();
        } else if (message instanceof MethodInvoke) {
            MethodInvoke mi = (MethodInvoke) message;
            onMethodInvoke(connection, message, mi.getReceiverId(), mi.getSenderId());
            methodInvokesMeter.mark();
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
        Client t = tm.get(MaritimeId.create(receiverId));
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
