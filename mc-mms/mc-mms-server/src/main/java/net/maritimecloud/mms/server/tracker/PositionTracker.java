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
package net.maritimecloud.mms.server.tracker;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import net.maritimecloud.mms.server.connection.client.Client;
import net.maritimecloud.mms.server.connection.client.ClientManager;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.PositionTime;

import org.cakeframework.container.Container;
import org.cakeframework.container.concurrent.Daemon;

/**
 * An object that tracks positions.
 *
 * @author Kasper Nielsen
 */
public class PositionTracker {
    // Good description of why we use brute force
    // http://www.jandrewrogers.com/2015/03/02/geospatial-databases-are-hard/?h
    /** Magic constant. */
    static final int THRESHOLD = 1;

    /** All targets at last update. Must be read via synchronized */
    private ConcurrentHashMap<Client, PositionTime> latest = new ConcurrentHashMap<>();

    /** All current subscriptions. */
    final ConcurrentHashMap<PositionUpdatedHandler, Subscription> subscriptions = new ConcurrentHashMap<>();

    private final ClientManager clientManager;

    public PositionTracker(ClientManager clientManager) {
        this.clientManager = requireNonNull(clientManager);
    }

    @Daemon
    public void run(Container c) {
        long start = System.currentTimeMillis();
        while (!c.getState().isShutdown()) {
            try {
                doRun0();
                long now = System.currentTimeMillis();
                long sleep = 1000 - Math.min(1000, now - start);
                if (sleep > 0) {
                    Thread.sleep(sleep);
                }
                start = now;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Should be scheduled to run every x second to update handlers. */
    private void doRun0() {
        ConcurrentHashMap<Client, PositionTime> current = new ConcurrentHashMap<>();
        final Map<Client, PositionTime> latest = this.latest;

        // We only want to process those that have been updated since last time
        final ConcurrentHashMap<Client, PositionTime> updates = new ConcurrentHashMap<>();
        clientManager.forEach(pt -> {
            PositionTime p = latest.get(pt);
            PositionTime currentPt = pt.getLatestPosition();
            if (p == null || !p.positionEquals(currentPt)) {
                updates.put(pt, currentPt);
            }
            current.put(pt, currentPt);
        });
        // update each subscription with new positions
        subscriptions.forEachValue(THRESHOLD, s -> s.updateWith(updates));
        // update latest with current latest
        this.latest = current;
    }

    /**
     * Invokes the callback for every tracked object within the specified area of interest.
     *
     * @param shape
     *            the area of interest
     * @param block
     *            the callback
     */
    public void forEachWithinArea(Area shape, BiConsumer<Client, PositionTime> block) {
        requireNonNull(shape, "shape is null");
        requireNonNull(block, "block is null");
        clientManager.forEach(c -> {
            PositionTime pt = c.getLatestPosition();
            if (shape.contains(pt)) {
                block.accept(c, pt);
            }
        });
    }

    /**
     * Returns the number of subscriptions.
     *
     * @return the number of subscriptions
     */
    public int getNumberOfSubscriptions() {
        return subscriptions.size();
    }

    /**
     * Returns a map of all tracked objects and their latest position.
     *
     * @param shape
     *            the area of interest
     * @return a map of all tracked objects within the area as keys and their latest position as the value
     */
    public Map<Client, PositionTime> getTargetsWithin(Area shape) {
        final ConcurrentHashMap<Client, PositionTime> result = new ConcurrentHashMap<>();
        forEachWithinArea(shape, (a, b) -> {
            if (shape.contains(b)) {
                result.put(a, b);
            }
        });
        return result;
    }

    public boolean remove(Client t) {
        return latest.remove(t) != null;
    }

    /**
     * Subscribes to changes in the specified area.
     *
     * @param area
     *            the area to monitor
     * @param handler
     *            a subscription that can be used to cancel the subscription
     * @return a subscription object
     */
    public Subscription subscribe(Area area, PositionUpdatedHandler handler) {
        return subscribe(area, handler, 100);
    }

    /**
     * Subscribes to changes in the specified area.
     *
     * @param area
     *            the area to monitor
     * @param handler
     *            a subscription that can be used to cancel the subscription
     * @param slack
     *            is the precision in meters with which we want to report entering/exiting messages. We use it to avoid
     *            situations where a boat sails on a boundary line and keeps changing from being inside to outside of it
     * @return a subscription object
     */
    public Subscription subscribe(Area area, PositionUpdatedHandler handler, double slack) {
        Area exitShape = requireNonNull(area, "area is null");
        if (slack < 0) {
            throw new IllegalArgumentException("Slack must be non-negative, was " + slack);
        }
        if (slack > 0) {
            if (area instanceof Circle) {
                Circle c = (Circle) area;
                exitShape = c.withRadius(c.getRadius() + slack);
            } else {
                throw new UnsupportedOperationException("Only circles allowed for now");
            }
        }
        Subscription s = new Subscription(this, handler, area, exitShape);
        if (subscriptions.putIfAbsent(handler, s) != null) {
            throw new IllegalArgumentException("The specified handler has already been registered");
        }
        return s;
    }
}
//
// /**
// * Returns the latest position time updated for the specified target. Or <code>null</code> if no position has ever
// * been recorded for the target.
// *
// *
// * @param target
// * the target
// * @return the latest position time updated
// */
// public PositionTime getLatest(Client target) {
// return latest.get(target);
// }
//
// public PositionTime getLatestIfLaterThan(Client target, long time) {
// PositionTime t = getLatest(target);
// return t != null && time < t.getTime() ? t : null;
// }
