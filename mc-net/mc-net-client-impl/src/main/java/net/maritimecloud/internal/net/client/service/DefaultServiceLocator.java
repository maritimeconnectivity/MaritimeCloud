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

import java.util.ArrayList;
import java.util.List;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.util.DefaultConnectionFuture;
import net.maritimecloud.internal.net.client.util.ThreadManager;
import net.maritimecloud.messages.FindService;
import net.maritimecloud.messages.FindServiceAck;
import net.maritimecloud.net.NetworkFuture;
import net.maritimecloud.net.service.ServiceEndpoint;
import net.maritimecloud.net.service.ServiceLocator;
import net.maritimecloud.net.service.spi.ServiceInitiationPoint;
import net.maritimecloud.net.service.spi.ServiceMessage;

/**
 * The default implementation of ServiceLocator.
 *
 * @author Kasper Nielsen
 */
class DefaultServiceLocator<T, E extends ServiceMessage<T>> implements ServiceLocator<T, E> {

    /** The distance in meters for where to look for the service. */
    final int distance;

    final ClientServiceManager csm;

    final ServiceInitiationPoint<E> sip;

    final ThreadManager threadManager;

    DefaultServiceLocator(ThreadManager threadManager, ServiceInitiationPoint<E> sip, ClientServiceManager csm,
            int distance) {
        this.csm = requireNonNull(csm);
        this.distance = distance;
        this.sip = requireNonNull(sip);
        this.threadManager = threadManager;
    }

    /** {@inheritDoc} */
    @Override
    public ServiceLocator<T, E> withinDistanceOf(int meters) {
        if (meters <= 0 || meters >= 100_000_000) {
            throw new IllegalArgumentException("Meters must be greater >0 and <100.000.000");
        }
        return new DefaultServiceLocator<>(threadManager, sip, csm, meters);
    }

    /** {@inheritDoc} */
    @Override
    public NetworkFuture<ServiceEndpoint<E, T>> nearest() {
        // public FindService(String serviceName, int meters, int max) {
        DefaultConnectionFuture<FindServiceAck> f = csm.serviceFindOne(new FindService().setServiceName(sip.getName())
                .setMeters(distance).setMax(1));
        final DefaultConnectionFuture<ServiceEndpoint<E, T>> result = threadManager.create();
        f.thenAcceptAsync(new DefaultConnectionFuture.Action<FindServiceAck>() {
            @Override
            public void accept(FindServiceAck ack) {
                List<String> st = ack.getRemoteIDS();
                if (st.size() > 0) {
                    result.complete(new DefaultRemoteServiceEndpoint<>(csm, MaritimeId.create(st.get(0)), sip));
                } else {
                    result.complete(null);
                    // result.completeExceptionally(new ServiceNotFoundException(""));
                }
            }
        });
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public NetworkFuture<List<ServiceEndpoint<E, T>>> nearest(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("The specified limit must be positive (>=1), was " + limit);
        }
        DefaultConnectionFuture<FindServiceAck> f = csm.serviceFindOne(new FindService().setServiceName(sip.getName())
                .setMeters(distance).setMax(limit));
        final DefaultConnectionFuture<List<ServiceEndpoint<E, T>>> result = threadManager.create();
        f.thenAcceptAsync(new DefaultConnectionFuture.Action<FindServiceAck>() {
            @Override
            public void accept(FindServiceAck ack) {
                List<String> st = ack.getRemoteIDS();
                List<ServiceEndpoint<E, T>> l = new ArrayList<>();
                if (st.size() > 0) {
                    for (String s : st) {
                        l.add(new DefaultRemoteServiceEndpoint<>(csm, MaritimeId.create(s), sip));
                    }
                }
                result.complete(l);
            }
        });
        return result;
    }
}
