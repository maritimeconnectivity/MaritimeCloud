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
package net.maritimecloud.internal.net.server;

import static java.util.Objects.requireNonNull;
import jsr166e.LongAdder;
import net.maritimecloud.internal.net.server.targets.Target;
import net.maritimecloud.internal.net.server.targets.TargetManager;
import net.maritimecloud.util.function.Consumer;

/**
 * Provides information about the status of the maritime cloud
 * 
 * @author Kasper Nielsen
 */
public class ServerInfo {

    /** The target manager. */
    private final TargetManager targetManager;

    public ServerInfo(TargetManager tm) {
        this.targetManager = requireNonNull(tm);
    }

    /**
     * Returns the current number of connections to the cloud.
     * 
     * @return the current number of connections to the cloud
     */
    public int getConnectionCount() {
        final LongAdder i = new LongAdder();
        targetManager.forEachTarget(new Consumer<Target>() {
            public void accept(Target t) {
                if (t.isConnected()) {
                    i.increment();
                }
            }
        });
        return i.intValue();
    }
}
