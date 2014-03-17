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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.maritimecloud.net.ConnectionFuture;
import net.maritimecloud.net.service.ServiceInvocationFuture;
import net.maritimecloud.util.function.BiConsumer;

/**
 *
 * @author Kasper Nielsen
 */
public class DefaultServiceInvocationFuture<T> implements ServiceInvocationFuture<T> {

    final ConnectionFuture<T> delegate;

    final ConnectionFuture<Object> receivedOnClient;

    final ConnectionFuture<Object> receivedOnServer;

    /**
     * @param tm
     */
    protected DefaultServiceInvocationFuture(ConnectionFuture<T> f, ConnectionFuture<Object> receivedOnClient,
            ConnectionFuture<Object> receivedOnServer) {
        this.delegate = requireNonNull(f);
        this.receivedOnClient = requireNonNull(receivedOnClient);
        this.receivedOnServer = requireNonNull(receivedOnServer);
    }

    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    /** {@inheritDoc} */
    public T get() throws InterruptedException, ExecutionException {
        return delegate.get();
    }

    /** {@inheritDoc} */
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.get(timeout, unit);
    }

    /** {@inheritDoc} */
    public T getNow(T valueIfAbsent) {
        return delegate.getNow(valueIfAbsent);
    }

    /** {@inheritDoc} */
    public void handle(BiConsumer<T, Throwable> consumer) {
        delegate.handle(consumer);
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return delegate.hashCode();
    }

    /** {@inheritDoc} */
    public boolean isDone() {
        return delegate.isDone();
    }

    /** {@inheritDoc} */
    @Override
    public ConnectionFuture<Object> receivedByCloud() {
        return receivedOnServer;
    }

    /** {@inheritDoc} */
    @Override
    public ConnectionFuture<T> timeout(long timeout, TimeUnit unit) {
        return delegate.timeout(timeout, unit);
    }

    /** {@inheritDoc} */
    public String toString() {
        return delegate.toString();
    }

}
