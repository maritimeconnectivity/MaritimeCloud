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
import jsr166e.ConcurrentHashMapV8;
import net.maritimecloud.internal.net.messages.s2c.service.RegisterService;
import net.maritimecloud.internal.net.server.targets.Target;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages services for a single connected client.
 * 
 * @author Kasper Nielsen
 */
public class TargetServiceManager {

    /** A logger. */
    private static final Logger LOG = LoggerFactory.getLogger(TargetServiceManager.class);

    /** The client */
    final Target target;

    /** A map of all registered services at the client. */
    final ConcurrentHashMapV8<String, String> services = new ConcurrentHashMapV8<>();

    public TargetServiceManager(Target target) {
        this.target = requireNonNull(target);
    }

    public void registerService(RegisterService s) {
        LOG.debug("Registered remote service " + s.getServiceName() + "@" + target.getId());
        services.put(s.getServiceName(), s.getServiceName());
    }

    public boolean hasService(String name) {
        for (String c : services.keySet()) {
            if (c.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
