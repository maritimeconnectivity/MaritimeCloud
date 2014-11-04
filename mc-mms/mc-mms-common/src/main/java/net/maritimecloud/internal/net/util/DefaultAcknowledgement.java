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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import net.maritimecloud.internal.util.AbstractCompletor;
import net.maritimecloud.net.Acknowledgement;

/**
 *
 * @author Kasper Nielsen
 */
public class DefaultAcknowledgement extends AbstractCompletor<Void> implements Acknowledgement {

    public DefaultAcknowledgement(ScheduledExecutorService timeoutExecutor) {
        super(new CompletableFuture<Void>(), timeoutExecutor);
    }

    public DefaultAcknowledgement(CompletableFuture<Void> f, ScheduledExecutorService timeoutExecutor) {
        super(f, timeoutExecutor);
    }

    /** {@inheritDoc} */
    @Override
    public Acknowledgement handle(Consumer<Throwable> fn) {
        return new DefaultAcknowledgement(delegate.handle((a, b) -> {
            fn.accept(b);
            return null;
        }), timeoutExecutor);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAcknowledged() {
        return isCompletedNormally();
    }

    /** {@inheritDoc} */
    @Override
    public void join() {
        delegate.join();
    }

    /** {@inheritDoc} */
    @Override
    public Acknowledgement thenRun(Runnable runnable) {
        return new DefaultAcknowledgement(delegate.thenRun(runnable), timeoutExecutor);
    }

    /** {@inheritDoc} */
    @Override
    public Acknowledgement timeout(long timeout, TimeUnit unit) {
        return new DefaultAcknowledgement(withTimeout(timeout, unit), timeoutExecutor);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Void> toCompletableFuture() {
        return withImmutable();
    }
}
