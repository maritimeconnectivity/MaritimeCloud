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
package net.maritimecloud.internal.util;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;

/**
 *
 * @author Kasper Nielsen
 */
public class AbstractCompletor<T> {

    public final CompletableFuture<T> delegate;

    protected final ScheduledExecutorService timeoutExecutor;

    public AbstractCompletor(CompletableFuture<T> delegate, ScheduledExecutorService timeoutExecutor) {
        this.delegate = requireNonNull(delegate);
        this.timeoutExecutor = requireNonNull(timeoutExecutor);
    }

    /**
     * @param value
     * @return
     * @see java.util.concurrent.CompletableFuture#complete(java.lang.Object)
     */
    public boolean complete(T value) {
        return delegate.complete(value);
    }

    /**
     * @param ex
     * @return
     * @see java.util.concurrent.CompletableFuture#completeExceptionally(java.lang.Throwable)
     */
    public boolean completeExceptionally(Throwable ex) {
        return delegate.completeExceptionally(ex);
    }

    public final T get() throws InterruptedException, ExecutionException {
        return delegate.get();
    }

    public final T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.get(timeout, unit);
    }

    public final T getNow(T valueIfAbsent) {
        return delegate.getNow(valueIfAbsent);
    }

    public final boolean isCancelled() {
        return delegate.isCancelled();
    }

    public final boolean isCompletedExceptionally() {
        return delegate.isCompletedExceptionally();
    }

    public final boolean isCompletedNormally() {
        return isDone() && !(!isCancelled() && !isCompletedExceptionally());
    }

    public final boolean isDone() {
        return delegate.isDone();
    }

    protected CompletableFuture<T> withImmutable() {
        CompletableFuture<T> cf = new CompletableFuture<>();
        delegate.handle((a, b) -> {
            if (b == null) {
                cf.complete(a);
            } else {
                cf.completeExceptionally(b);
            }
            return null;
        });
        return cf;
    }

    protected CompletableFuture<T> withTimeout(long timeout, TimeUnit unit) {
        final CompletableFuture<T> cf = new CompletableFuture<>();
        final Future<?> f;
        try {
            f = timeoutExecutor.schedule(new Runnable() {
                public void run() {
                    if (!isDone()) {
                        cf.completeExceptionally(new TimeoutException("Timed out after " + timeout + " "
                                + unit.toString().toLowerCase()));
                    }
                }
            }, timeout, unit);
        } catch (RejectedExecutionException e) {
            // Unfortunately TimeoutException does not allow exceptions in its constructor
            cf.completeExceptionally(new RuntimeException("Could not scedule task, ", e));
            return cf;
        }
        if (f.isCancelled()) {
            cf.completeExceptionally(new RuntimeException("Could not scedule task"));
        }
        delegate.handle(new BiFunction<T, Throwable, Void>() {
            public Void apply(T t, Throwable throwable) {
                // Users must manually purge if many outstanding tasks
                f.cancel(false);
                if (throwable != null) {
                    cf.completeExceptionally(throwable);
                } else {
                    cf.complete(t);
                }
                return null;
            }
        });
        return cf;
    }
}
