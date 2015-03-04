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
package net.maritimecloud.internal.net.util;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import net.maritimecloud.internal.util.concurrent.CompletableFuture;
import net.maritimecloud.net.Acknowledgement;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

/**
 * The default implementation of ConnectionFuture.
 *
 * @author Kasper Nielsen
 */
public class DefaultEndpointInvocationFuture<T> implements EndpointInvocationFuture<T> {

    public final CompletableFuture<T> delegate;

    final Binary messageId;

    public CompletableFuture<Void> recivedByCloud;

    final String requestId;

    public DefaultEndpointInvocationFuture(CompletableFuture<T> f, Binary messageId) {
        this.delegate = requireNonNull(f);
        this.requestId = "fixme";
        this.messageId = requireNonNull(messageId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    /**
     * @param m
     */
    public boolean complete(T m) {
        return delegate.complete(m);
    }

    /** {@inheritDoc} */
    public boolean completeExceptionally(Throwable ex) {
        return delegate.completeExceptionally(ex);
    }

    /** {@inheritDoc} */
    @Override
    public T get() throws InterruptedException, ExecutionException {
        return delegate.get();
    }

    /** {@inheritDoc} */
    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.get(timeout, unit);
    }

    /** {@inheritDoc} */
    @Override
    public Binary getMessageId() {
        return messageId;
    }

    /** {@inheritDoc} */
    @Override
    public T getNow(T valueIfAbsent) {
        return delegate.getNow(valueIfAbsent);
    }

    /** {@inheritDoc} */
    @Override
    public Position getPosition() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp getTime() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void handle(final BiConsumer<T, Throwable> consumer) {
        requireNonNull(consumer, "consumer is null");
        delegate.handle(new BiFunction<T, Throwable, Void>() {
            public Void apply(T a, Throwable b) {
                consumer.accept(a, b);
                return null;
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    /** {@inheritDoc} */
    public T join() {
        return delegate.join();
    }

    public DefaultEndpointInvocationFuture<T> orTimeout(final long timeout, final TimeUnit unit) {
        return new DefaultEndpointInvocationFuture<>(delegate.orTimeout(timeout, unit), messageId);
    }

    /** {@inheritDoc} */
    @Override
    public Acknowledgement relayed() {
        return new DefaultAcknowledgement(recivedByCloud);
    }

    /** {@inheritDoc} */
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> block) {
        return delegate.thenAcceptAsync(e -> block.accept(e));
    }

    /** {@inheritDoc} */
    @Override
    public <U> EndpointInvocationFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        return new DefaultEndpointInvocationFuture<>(delegate.thenApply(fn), messageId);
    }

    /** {@inheritDoc} */
    public void thenRun(Runnable runnable) {
        delegate.thenRun(runnable);
    }
}
