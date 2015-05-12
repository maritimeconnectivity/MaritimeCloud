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

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import net.maritimecloud.internal.util.concurrent.CompletableFuture;
import net.maritimecloud.net.Acknowledgement;

/**
 *
 * @author Kasper Nielsen
 */
public class DefaultAcknowledgement implements Acknowledgement {

    public final CompletableFuture<Void> delegate;

    public DefaultAcknowledgement() {
        this(new CompletableFuture<Void>());
    }

    DefaultAcknowledgement(CompletableFuture<Void> delegate) {
        this.delegate = requireNonNull(delegate);
    }

    public boolean complete() {
        return delegate.complete(null);
    }

    public boolean completeExceptionally(Throwable ex) {
        return delegate.completeExceptionally(ex);
    }

    /** {@inheritDoc} */
    @Override
    public Acknowledgement handle(Consumer<Throwable> fn) {
        return new DefaultAcknowledgement(delegate.handle((a, b) -> {
            fn.accept(b);
            return null;
        }));
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAcknowledged() {
        return delegate.isDone() && !delegate.isCompletedExceptionally();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    /** {@inheritDoc} */
    @Override
    public void join() {
        delegate.join();
    }

    /** {@inheritDoc} */
    @Override
    public Acknowledgement orTimeout(long timeout, TimeUnit unit) {
        return new DefaultAcknowledgement(delegate.orTimeout(timeout, unit));
    }

    /** {@inheritDoc} */
    @Override
    public Acknowledgement thenRun(Runnable runnable) {
        return new DefaultAcknowledgement(delegate.thenRun(runnable));
    }

    /** {@inheritDoc} */
    @Override
    public java.util.concurrent.CompletableFuture<Void> toCompletableFuture() {
        return delegate.toCompletableFutureJUC();
    }
}
