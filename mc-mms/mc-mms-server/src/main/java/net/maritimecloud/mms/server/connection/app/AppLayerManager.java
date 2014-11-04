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
package net.maritimecloud.mms.server.connection.app;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.mms.server.MmsServer;

/**
 *
 * @author Kasper Nielsen
 */
public class AppLayerManager {

    MmsServer server;

    private final ConcurrentHashMap<String, AppLayer> targets = new ConcurrentHashMap<>();

    public AppLayer get(MaritimeId id) {
        return targets.get(id.toString());
    }

    public AppLayer getOrCreate(MaritimeId id) {
        return targets.computeIfAbsent(id.toString(), key -> new AppLayer(this, id));
    }

    public void compute(MaritimeId id, Consumer<AppLayer> consumer) {
        targets.compute(id.toString(), (k, v) -> {
            if (v == null) {
                v = new AppLayer(this, id);
            }
            consumer.accept(v);
            return v;
        });
    }
}
