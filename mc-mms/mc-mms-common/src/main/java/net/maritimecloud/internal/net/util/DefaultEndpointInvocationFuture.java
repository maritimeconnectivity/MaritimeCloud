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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import net.maritimecloud.internal.util.AbstractCompletor;
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
public class DefaultEndpointInvocationFuture<T> extends AbstractCompletor<T> implements EndpointInvocationFuture<T> {

    public CompletableFuture<Void> recivedByCloud;

    final String requestId;

    final Binary messageId;

    public DefaultEndpointInvocationFuture(ScheduledExecutorService tm, CompletableFuture<T> f, Binary messageId) {
        super(f, tm);
        this.requestId = "fixme";
        this.messageId = requireNonNull(messageId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    /**
     * @param m
     */
    public boolean complete(T m) {
        return delegate.complete(m);
    }

    /**
     * @param ex
     * @return
     * @see jsr166e.CompletableFuture#completeExceptionally(java.lang.Throwable)
     */
    public boolean completeExceptionally(Throwable ex) {
        return delegate.completeExceptionally(ex);
    }

    /** {@inheritDoc} */
    @Override
    public Binary getMessageId() {
        return messageId;
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

    /**
     * @param valueIfAbsent
     * @return
     * @see jsr166e.CompletableFuture#getNow(java.lang.Object)
     */
    public T join() {
        return delegate.join();
    }

    /** {@inheritDoc} */
    @Override
    public Acknowledgement relayed() {
        return new DefaultAcknowledgement(recivedByCloud, timeoutExecutor);
    }

    /**
     * @param block
     * @return
     * @see jsr166e.CompletableFuture#thenAcceptAsync(jsr166e.CompletableFuture.Action)
     */
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> block) {
        return delegate.thenAcceptAsync(e -> block.accept(e), timeoutExecutor);
    }

    /** {@inheritDoc} */
    @Override
    public <U> EndpointInvocationFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        return new DefaultEndpointInvocationFuture<>(timeoutExecutor, delegate.thenApply(fn), messageId);
    }

    /**
     * @param block
     * @return
     * @see jsr166e.CompletableFuture#thenAcceptAsync(jsr166e.CompletableFuture.Action)
     */
    public void thenRun(Runnable runnable) {
        delegate.thenRun(runnable);
    }

    public DefaultEndpointInvocationFuture<T> timeout(final long timeout, final TimeUnit unit) {
        return new DefaultEndpointInvocationFuture<>(timeoutExecutor, withTimeout(timeout, unit), messageId);

    }
}
