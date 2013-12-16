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
package net.maritimecloud.internal.net.server.broadcast;

import static java.util.Objects.requireNonNull;
import jsr166e.CompletableFuture;
import jsr166e.CompletableFuture.Action;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastAck;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastDeliver;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastSend;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastSendAck;
import net.maritimecloud.internal.net.server.connection.ServerConnection;
import net.maritimecloud.internal.net.server.requests.RequestException;
import net.maritimecloud.internal.net.server.requests.RequestProcessor;
import net.maritimecloud.internal.net.server.requests.ServerMessageBus;
import net.maritimecloud.internal.net.server.targets.Target;
import net.maritimecloud.internal.net.server.targets.TargetManager;
import net.maritimecloud.net.broadcast.BroadcastMessage;
import net.maritimecloud.util.function.Consumer;
import net.maritimecloud.util.geometry.PositionTime;

import org.picocontainer.Startable;

/**
 * 
 * @author Kasper Nielsen
 */
public class BroadcastManager implements Startable {
    private final TargetManager tm;

    private final ServerMessageBus bus;

    public BroadcastManager(TargetManager tm, ServerMessageBus bus) {
        this.tm = requireNonNull(tm);
        this.bus = requireNonNull(bus);
    }

    BroadcastSendAck broadcast(final ServerConnection source, final BroadcastSend send) throws RequestException {
        final BroadcastMessage bm;
        System.err.println("READ BROADCAST");

        final Target target = source.getTarget();
        final PositionTime sourcePositionTime = send.getPositionTime();

        tm.forEachTarget(new Consumer<Target>() {
            @Override
            public void accept(Target t) {
                if (t != target && t.isConnected()) { // do not broadcast to self
                    PositionTime latest = t.getLatestPosition();
                    if (latest != null) {
                        double distance = sourcePositionTime.geodesicDistanceTo(latest);
                        if (distance < send.getDistance()) {

                            BroadcastDeliver bd = BroadcastDeliver.create(send.getId(), send.getPositionTime(),
                                    send.getChannel(), send.getMessage());


                            final ServerConnection connection = t.getConnection();
                            CompletableFuture<Void> f = connection.messageSend(bd).protocolAcked();
                            if (send.isReceiverAck()) {
                                f.thenAccept(new Action<Void>() {
                                    public void accept(Void paramA) {
                                        Target t = connection.getTarget();
                                        BroadcastAck ba = new BroadcastAck(send.getReplyTo(), t.getId(), t
                                                .getLatestPosition());
                                        source.messageSend(ba);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

        return send.createReply();
    }

    void broadCastTo(BroadcastSend bs, Target target, BroadcastDeliver bd) {

    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        bus.subscribe(BroadcastSend.class, new RequestProcessor<BroadcastSend, BroadcastSendAck>() {
            public BroadcastSendAck process(ServerConnection connection, BroadcastSend message) throws RequestException {
                return broadcast(connection, message);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {}
}
