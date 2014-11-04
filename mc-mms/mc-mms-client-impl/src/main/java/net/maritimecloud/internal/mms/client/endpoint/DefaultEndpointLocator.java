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
package net.maritimecloud.internal.mms.client.endpoint;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.mms.messages.services.Services;
import net.maritimecloud.internal.net.endpoint.EndpointMirror;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.LocalEndpoint;
import net.maritimecloud.net.mms.MmsEndpointLocator;

/**
 *
 * @author Kasper Nielsen
 */
class DefaultEndpointLocator<T extends LocalEndpoint> implements MmsEndpointLocator<T> {

    final EndpointMirror mirror;

    final ClientEndpointManager cem;

    final int distance;

    DefaultEndpointLocator(ClientEndpointManager cem, EndpointMirror mirror, int distance) {
        this.mirror = mirror;
        this.cem = requireNonNull(cem);
        this.distance = distance;
    }

    /** {@inheritDoc} */
    @Override
    public MmsEndpointLocator<T> withinDistanceOf(int meters) {
        if (meters <= 0 || meters >= 100_000_000) {
            throw new IllegalArgumentException("Meters must be greater >0 and <100.000.000");
        }
        return new DefaultEndpointLocator<>(cem, mirror, meters);
    }

    /** {@inheritDoc} */
    @Override
    public EndpointInvocationFuture<T> findWithMMSINumber(int mmsiNumber) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** {@inheritDoc} */
    @Override
    public EndpointInvocationFuture<T> findNearest() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** {@inheritDoc} */
    public EndpointInvocationFuture<List<T>> findAll() {
        Services s = cem.endpointFrom(null, Services.class);
        EndpointInvocationFuture<List<String>> f = s.locate(mirror.getName(), distance, Integer.MAX_VALUE);
        return f.thenApply(e -> fromIds(e));
    }

    @SuppressWarnings("unchecked")
    private List<T> fromIds(List<String> ids) {
        List<T> l = new ArrayList<>();
        for (String str : ids) {
            MaritimeId id = MaritimeId.create(str);
            l.add((T) mirror.instantiate(new DefaultEndpointInvocator(cem, id, mirror)));
        }
        return l;
    }
}
