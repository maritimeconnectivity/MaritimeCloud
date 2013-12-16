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
package net.maritimecloud.internal.net.server.services;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import jsr166e.ConcurrentHashMapV8;
import net.maritimecloud.internal.net.messages.s2c.service.FindService;
import net.maritimecloud.internal.net.messages.s2c.service.FindServiceResult;
import net.maritimecloud.internal.net.messages.s2c.service.RegisterService;
import net.maritimecloud.internal.net.messages.s2c.service.RegisterServiceResult;
import net.maritimecloud.internal.net.server.connection.ServerConnection;
import net.maritimecloud.internal.net.server.requests.RequestException;
import net.maritimecloud.internal.net.server.requests.RequestProcessor;
import net.maritimecloud.internal.net.server.requests.ServerMessageBus;
import net.maritimecloud.internal.net.server.targets.Target;
import net.maritimecloud.internal.net.server.targets.TargetManager;
import net.maritimecloud.util.function.BiConsumer;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.CoordinateSystem;
import net.maritimecloud.util.geometry.PositionTime;

import org.picocontainer.Startable;

/**
 * Manages services for all connected targets.
 * 
 * @author Kasper Nielsen
 */
public class ServiceManager implements Startable {

    final TargetManager tracker;

    private final ServerMessageBus bus;

    public ServiceManager(TargetManager tm, ServerMessageBus bus) {
        this.tracker = requireNonNull(tm);
        this.bus = requireNonNull(bus);
    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        bus.subscribe(RegisterService.class, new RequestProcessor<RegisterService, RegisterServiceResult>() {
            @Override
            public RegisterServiceResult process(ServerConnection connection, RegisterService message)
                    throws RequestException {
                TargetServiceManager services = connection.getTarget().getServices();
                services.registerService(message);
                return message.createReply();
            }
        });

        bus.subscribe(FindService.class, new RequestProcessor<FindService, FindServiceResult>() {
            @Override
            public FindServiceResult process(ServerConnection connection, FindService r) throws RequestException {
                List<Entry<Target, PositionTime>> findService = findService(connection.getTarget(), r);
                List<String> list = new ArrayList<>();
                for (Entry<Target, PositionTime> e : findService) {
                    list.add(e.getKey().getId().toString());
                }
                return r.createReply(list.toArray(new String[list.size()]));
            }
        });
    }

    /**
     * Finds services in proximity to the specified target.
     * 
     * @param target
     *            the target that is trying to find the service
     * @param request
     *            the find service request
     * @return a sorted list of the targets that was found sorted by distance to the target doing the search
     */
    public List<Entry<Target, PositionTime>> findService(Target target, final FindService request) {
        final PositionTime pos = target.getLatestPosition();
        double meters = request.getMeters() <= 0 ? Integer.MAX_VALUE : request.getMeters();
        Area a = new Circle(pos, meters, CoordinateSystem.GEODETIC);
        // Find all services with the area
        final ConcurrentHashMapV8<Target, PositionTime> map = new ConcurrentHashMapV8<>();
        tracker.forEachWithinArea(a, new BiConsumer<Target, PositionTime>() {
            public void accept(Target target, PositionTime r) {

                if (target.getServices().hasService(request.getServiceName())) {
                    map.put(target, r);
                }
            }
        });
        // We remove ourself
        map.remove(target);

        // Sort by distance
        List<Entry<Target, PositionTime>> l = new ArrayList<>(map.entrySet());
        Collections.sort(l, new Comparator<Entry<Target, PositionTime>>() {
            public int compare(Entry<Target, PositionTime> o1, Entry<Target, PositionTime> o2) {
                return Double.compare(o1.getValue().distanceTo(pos, CoordinateSystem.GEODETIC), o2.getValue()
                        .distanceTo(pos, CoordinateSystem.GEODETIC));
            }
        });

        // If we have a maximum number of results, filter the list
        if (l.size() > request.getMax()) {
            l = l.subList(0, request.getMax());
        }

        return l;
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {}
}
