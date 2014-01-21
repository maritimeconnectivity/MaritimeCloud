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

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import net.maritimecloud.internal.net.server.connection.ServerConnection;
import net.maritimecloud.internal.net.server.util.CustomConcurrentHashMap;
import net.maritimecloud.internal.net.server.util.CustomConcurrentHashMap.Strength;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.Element;

/**
 * 
 * @author Kasper Nielsen
 */
public class BroadcastTypeArea {
    final CustomConcurrentHashMap<ServerConnection, CopyOnWriteArrayList<Area>> c = new CustomConcurrentHashMap<>(
            Strength.weak, Strength.strong);

    Set<ServerConnection> find(Element position) {
        for (Entry<ServerConnection, CopyOnWriteArrayList<Area>> e : c.entrySet()) {
            ServerConnection c = e.getKey();
            if (c != null) {
                for (Area area : e.getValue()) {}
            }
        }
        return null;
    }

    void add(ServerConnection connection, Area a) {
        requireNonNull(a);
        CopyOnWriteArrayList<Area> l = c.get(connection);
        if (l == null) {
            c.putIfAbsent(connection, new CopyOnWriteArrayList<Area>());
            l = c.get(connection);
        }
        l.add(a);
    }

}
