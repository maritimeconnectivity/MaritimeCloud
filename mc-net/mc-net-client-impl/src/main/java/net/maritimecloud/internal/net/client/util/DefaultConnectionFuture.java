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
package net.maritimecloud.internal.net.client.util;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jsr166e.CompletableFuture;
import jsr166e.CompletableFuture.BiFun;
import net.maritimecloud.net.ConnectionFuture;
import net.maritimecloud.util.function.BiConsumer;

/**
 * The default implementation of ConnectionFuture.
 * 
 * @author Kasper Nielsen
 */
public class DefaultConnectionFuture<T> implements ConnectionFuture<T> {
    final CompletableFuture<T> delegate;

    final String requestId;


    final ThreadManager tm;

    DefaultConnectionFuture(ThreadManager tm) {
        this.tm = tm;
        this.requestId = "fixme";
        delegate = new CompletableFuture<>();
    }

    /**
     * @param m
     */
    public void complete(T m) {
        delegate.complete(m);
    }

    /**
     * @param ex
     * @return
     * @see jsr166e.CompletableFuture#completeExceptionally(java.lang.Throwable)
     */
    public boolean completeExceptionally(Throwable ex) {
        return delegate.completeExceptionally(ex);
    }

    /**
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @see jsr166e.CompletableFuture#get()
     */
    public T get() throws InterruptedException, ExecutionException {
        return delegate.get();
    }

    /**
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     * @see jsr166e.CompletableFuture#get(long, java.util.concurrent.TimeUnit)
     */
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.get(timeout, unit);
    }

    /**
     * @param valueIfAbsent
     * @return
     * @see jsr166e.CompletableFuture#getNow(java.lang.Object)
     */
    public T getNow(T valueIfAbsent) {
        return delegate.getNow(valueIfAbsent);
    }

    /** {@inheritDoc} */
    @Override
    public void handle(final BiConsumer<T, Throwable> consumer) {
        requireNonNull(consumer, "consumer is null");
        delegate.handle(new BiFun<T, Throwable, Void>() {
            public Void apply(T a, Throwable b) {
                consumer.accept(a, b);
                return null;
            }
        });
    }

    /**
     * @return
     * @see jsr166e.CompletableFuture#isDone()
     */
    public boolean isDone() {
        return delegate.isDone();
    }

    /**
     * @param block
     * @return
     * @see jsr166e.CompletableFuture#thenAcceptAsync(jsr166e.CompletableFuture.Action)
     */
    public CompletableFuture<Void> thenAcceptAsync(final DefaultConnectionFuture.Action<? super T> block) {
        return delegate.thenAcceptAsync(new CompletableFuture.Action<T>() {
            public void accept(T paramA) {
                block.accept(paramA);
            }
        }, tm.es);
    }

    public DefaultConnectionFuture<T> timeout(final long timeout, final TimeUnit unit) {
        // timeout parameters checked by ses.schedule
        final DefaultConnectionFuture<T> cf = new DefaultConnectionFuture<>(requireNonNull(tm, "executor is null"));
        final Future<?> f;
        try {
            f = tm.getScheduler().schedule(new Runnable() {
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
        // Check if scheduler is shutdown
        // do it after cf.f is set (reversed in shutdown code)
        delegate.handle(new BiFun<T, Throwable, Void>() {
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


    public interface Action<A> {
        void accept(A paramA);
    }
}
