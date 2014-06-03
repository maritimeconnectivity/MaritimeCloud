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
package net.maritimecloud.internal.net.client.service;

import static java.util.Objects.requireNonNull;
import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.net.service.ServiceEndpoint;
import net.maritimecloud.net.service.ServiceInvocationFuture;
import net.maritimecloud.net.service.spi.ServiceInitiationPoint;
import net.maritimecloud.net.service.spi.ServiceMessage;

/**
 *
 * @author Kasper Nielsen
 */
public class DefaultRemoteServiceEndpoint<T, E extends ServiceMessage<T>> implements ServiceEndpoint<E, T> {
    final MaritimeId id;

    final ServiceInitiationPoint<E> sip;

    final ClientServiceManager csm;

    DefaultRemoteServiceEndpoint(ClientServiceManager csm, MaritimeId id, ServiceInitiationPoint<E> sip) {
        this.csm = requireNonNull(csm);
        this.id = requireNonNull(id);
        this.sip = requireNonNull(sip);
    }

    /** {@inheritDoc} */
    @Override
    public MaritimeId getId() {
        return id;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public ServiceInvocationFuture<T> invoke(E message) {
        return csm.invokeService(id, (ServiceMessage) message);
    }
}
