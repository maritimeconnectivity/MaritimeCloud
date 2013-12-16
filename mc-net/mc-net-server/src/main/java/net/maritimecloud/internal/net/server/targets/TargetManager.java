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
package net.maritimecloud.internal.net.server.targets;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import jsr166e.ConcurrentHashMapV8;
import jsr166e.ConcurrentHashMapV8.Action;
import jsr166e.ConcurrentHashMapV8.Fun;
import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.server.connection.ServerConnection;
import net.maritimecloud.internal.net.server.tracker.PositionTracker;
import net.maritimecloud.util.function.BiConsumer;
import net.maritimecloud.util.function.Consumer;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.PositionTime;

import org.picocontainer.Startable;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 
 * @author Kasper Nielsen
 */
public class TargetManager implements Startable, Iterable<Target> {

    private final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
            .setNameFormat("PositionTrackerUpdate").setDaemon(true).build());

    private final ConcurrentHashMapV8<String, Target> targets = new ConcurrentHashMapV8<>();

    final PositionTracker<Target> tracker = new PositionTracker<>();

    public Target find(MaritimeId id) {
        return targets.get(id.toString());
    }

    public void reportPosition(Target target, PositionTime pt) {
        tracker.update(target, pt);
    }

    public void forEachTarget(final Consumer<Target> consumer) {
        requireNonNull(consumer);
        targets.forEachValue(10, new Action<Target>() {
            public void apply(Target target) {
                consumer.accept(target);
            }
        });
    }

    public void forEachConnection(final Consumer<ServerConnection> consumer) {
        requireNonNull(consumer);
        targets.forEachValue(10, new Action<Target>() {
            public void apply(Target target) {
                ServerConnection c = target.getConnection();
                if (c != null) {
                    consumer.accept(c);
                }
            }
        });
    }

    /**
     * @param shape
     * @param block
     * @see net.maritimecloud.internal.net.server.tracker.PositionTracker#forEachWithinArea(net.maritimecloud.util.geometry.Area,
     *      net.maritimecloud.util.function.BiConsumer)
     */
    public void forEachWithinArea(Area shape, BiConsumer<Target, PositionTime> block) {
        tracker.forEachWithinArea(shape, block);
    }

    public Target getTarget(final MaritimeId id) {
        Target target = targets.computeIfAbsent(id.toString(), new Fun<String, Target>() {
            public Target apply(String key) {
                return new Target(TargetManager.this, id);
            }
        });
        return target;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Target> iterator() {
        return Collections.unmodifiableCollection(targets.values()).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        tracker.schedule(ses, 1000);
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {
        ses.shutdown();
    }
}
