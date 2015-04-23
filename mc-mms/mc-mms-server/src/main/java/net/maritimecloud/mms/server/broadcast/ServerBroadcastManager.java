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
package net.maritimecloud.mms.server.broadcast;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import net.maritimecloud.internal.mms.messages.PositionReport;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.net.messages.BroadcastAck;
import net.maritimecloud.mms.server.connection.client.Client;
import net.maritimecloud.mms.server.connection.client.ClientManager;
import net.maritimecloud.mms.server.connectionold.MmsServerConnectionBus;
import net.maritimecloud.mms.server.connectionold.ServerConnection;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.PositionTime;

/**
 * The server side broadcast manager.
 *
 * @author Kasper Nielsen
 */
public class ServerBroadcastManager {

    final ConcurrentHashMap<String, BroadcastSubscriptionSet> listeners = new ConcurrentHashMap<>();

    private final ClientManager tm;

    public ServerBroadcastManager(ClientManager tm, MmsServerConnectionBus bus) {
        this.tm = requireNonNull(tm);
        bus.setBroadcastManager(this);
    }

    public PositionReport broadcast(ServerConnection sourceConnection, Broadcast broadcast) {
        final Client target = sourceConnection.getClient();
        // final PositionTime sourcePositionTime = send.getPositionTime();

        tm.forEachTarget(t -> {
            if (t != target && t.isConnected()) { // do not broadcast to self
                broadcast(sourceConnection, broadcast, t);
            }
        });
        return new PositionReport();
    }

    void broadcast(ServerConnection source, Broadcast broadcast, Client t) {
        PositionTime latest = t.getLatestPosition();
        if (latest != null) {

            boolean doSend = false;
            Area area = broadcast.getArea();
            // if (area instanceof RelativeCircularArea) {
            // double distance = sourcePositionTime.geodesicDistanceTo(latest);
            // RelativeCircularArea c = (RelativeCircularArea) area;
            // doSend = distance < c.getRadius();
            // } else {
            doSend = area.contains(latest);
            // }
            if (doSend) {
                broadcastSend(source, broadcast, t);
            }
        }
    }

    void broadcastSend(ServerConnection source, Broadcast broadcast, Client t) {
        Broadcast bd = new Broadcast();
        bd.setMessageId(broadcast.getMessageId());
        bd.setBroadcastType(broadcast.getBroadcastType());
        bd.setSenderId(broadcast.getSenderId());
        bd.setSenderTimestamp(broadcast.getSenderTimestamp());
        bd.setSenderPosition(broadcast.getSenderPosition());

        bd.setPayload(broadcast.getPayload());
        bd.setSignature(broadcast.getSignature());

        ServerConnection destination = t.getActiveConnection();
        CompletableFuture<Void> f = destination.messageSend(bd).protocolAcked();

        if (broadcast.hasAckBroadcast()) {
            f.thenAccept(e -> {
                BroadcastAck ba = new BroadcastAck();
                ba.setAckForMessageId(bd.getMessageId());
                // Ignore original sender id
                Client org = destination.getClient();
                ba.setReceiverId(org.getId().toString());

                PositionTime pt = org.getLatestPosition();
                ba.setReceiverTimestamp(pt.timestamp());
                ba.setReceiverPosition(pt);

                source.messageSend(ba);
            });
        }
    }

}
