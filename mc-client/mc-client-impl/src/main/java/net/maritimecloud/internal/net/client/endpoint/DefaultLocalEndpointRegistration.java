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
package net.maritimecloud.internal.net.client.endpoint;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.net.client.connection.ConnectionMessageBus;
import net.maritimecloud.net.endpoint.EndpointImplementation;
import net.maritimecloud.net.endpoint.EndpointRegistration;

/**
 *
 * @author Kasper Nielsen
 */
class DefaultLocalEndpointRegistration implements EndpointRegistration {
    private final EndpointImplementation i;

    final CountDownLatch replied = new CountDownLatch(1);

    /**
     * @param connection
     * @param implementation
     */
    public DefaultLocalEndpointRegistration(ConnectionMessageBus connection, EndpointImplementation implementation) {
        this.i = requireNonNull(implementation);
    }

    String getName() {
        return i.getEndpointName();
    }

    /** {@inheritDoc} */
    @Override
    public void cancel() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public boolean awaitRegistered(long timeout, TimeUnit unit) throws InterruptedException {
        return replied.await(timeout, unit);
    }
}
