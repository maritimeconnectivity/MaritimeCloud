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
package net.maritimecloud.mms.server.targets;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.messages.services.AbstractClients;
import net.maritimecloud.internal.mms.messages.services.ClientInfo;
import net.maritimecloud.internal.mms.messages.services.ClientList;
import net.maritimecloud.mms.server.connectionold.ServerConnection;
import net.maritimecloud.mms.server.tracker.PositionTracker;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.PositionTime;

import org.cakeframework.container.RunOnStart;
import org.cakeframework.util.concurrent.ThreadManager;

/**
 *
 * @author Kasper Nielsen
 */

public class TargetManager extends AbstractClients implements Iterable<Target> {

    private final ConcurrentHashMap<String, Target> targets = new ConcurrentHashMap<>();

    final PositionTracker<Target> tracker = new PositionTracker<>();

    public Target find(MaritimeId id) {
        return targets.get(id.toString());
    }

    public void forEachConnection(final Consumer<ServerConnection> consumer) {
        requireNonNull(consumer);
        targets.forEachValue(10, target -> {
            ServerConnection c = target.getActiveConnection();
            if (c != null) {
                consumer.accept(c);
            }
        });
    }

    public void forEachTarget(BiConsumer<Target, PositionTime> consumer) {
        tracker.forEach(consumer);
    }

    public void forEachTarget(Consumer<Target> consumer) {
        requireNonNull(consumer);
        targets.forEachValue(10, consumer);
    }

    public void forEachWithinArea(Area shape, BiConsumer<Target, PositionTime> block) {
        tracker.forEachWithinArea(shape, block);
    }

    /** {@inheritDoc} */
    @Override
    protected List<ClientInfo> getAllClient(MessageHeader header) {
        return getAllClients().getClients();
    }

    public ClientList getAllClients() {
        ClientList cl = new ClientList();
        for (Target t : this) {
            ClientInfo ci = new ClientInfo();
            ci.setId(t.getId().toString());
            PositionTime pt = t.getLatestPosition();
            if (pt != null) {
                ci.setLastSeen(pt.timestamp());
                ci.setLatestPosition(pt);
            }

            TargetProperties p = t.getProperties();
            ci.setName(p.getName());
            ci.setDescription(p.getDescription());
            ci.setOrganization(p.getOrganization());

            cl.addClients(ci);
        }
        return cl;
    }

    /** {@inheritDoc} */
    @Override
    protected Integer getConnectionCount(MessageHeader header) {
        final LongAdder i = new LongAdder();
        forEachTarget(t -> {
            if (t.isConnected()) {
                i.increment();
            }
        });
        return i.intValue();
    }

    public Target getTarget(MaritimeId id) {
        return targets.computeIfAbsent(id.toString(), key -> new Target(this, id));
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Target> iterator() {
        return Collections.unmodifiableCollection(targets.values()).iterator();
    }

    public void reportPosition(Target target, PositionTime pt) {
        tracker.update(target, pt);
    }

    @RunOnStart
    public void start(ThreadManager tm) {
        tracker.schedule(tm.getScheduledExecutor(), 1000);
    }
}
