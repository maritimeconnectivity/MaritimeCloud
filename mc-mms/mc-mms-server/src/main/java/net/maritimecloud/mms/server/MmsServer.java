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
package net.maritimecloud.mms.server;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.ServerId;

import org.cakeframework.container.spi.AbstractContainer;
import org.cakeframework.container.spi.AbstractContainerConfiguration;
import org.cakeframework.container.spi.ContainerComposer;

/**
 *
 * @author Kasper Nielsen
 */
public class MmsServer extends AbstractContainer {

    /**
     * @param configuration
     * @param composer
     */
    public MmsServer(AbstractContainerConfiguration<?> configuration, ContainerComposer composer) {
        super(configuration, composer);
        this.serverId = composer.getService(ServerId.class);
    }

    private final ServerId serverId;


    public static MmsServer create(int port) {
        return new MmsServerConfiguration().setServerPort(port).build();
    }

    /**
     * @return the serverId
     */
    public ServerId getServerId() {
        return serverId;
    }

    /**
     * @param i
     * @param seconds
     * @return
     * @throws InterruptedException
     */
    public boolean awaitTerminated(long timeout, TimeUnit seconds) throws InterruptedException {
        return awaitState(State.TERMINATED, timeout, seconds);
    }
}
