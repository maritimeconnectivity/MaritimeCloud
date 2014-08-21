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
package net.maritimecloud.net;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

import net.maritimecloud.util.Binary;

/**
 * A future interface returned when invoking a remote endpoint method.
 *
 * @author Kasper Nielsen
 */
public interface EndpointInvocationFuture<T> {

    /**
     * A future that can be used to determine when the message have been received by the server. This is done on a best
     * effort basis. The broadcast message might be delivered to remote clients before the returned future completes.
     *
     * @return a completion stage that can be used to execute actions when the broadcast has been delivered to a central
     *         server
     *
     * @throws UnsupportedOperationException
     *             if the underlying communication mechanism does not support acknowledgement of the broadcast message.
     */
    Acknowledgement acknowledgement();

    /**
     * Returns a unique hash of the message send.
     *
     * @return a unique hash of the message send
     */
    Binary getMessageHash();

    T join();

    EndpointInvocationFuture<Void> thenRun(Runnable action);


    /**
     * Waits if necessary for the computation to complete, and then retrieves its result.
     *
     * @return the computed result
     * @throws CancellationException
     *             if the computation was cancelled
     * @throws ExecutionException
     *             if the computation threw an exception
     * @throws InterruptedException
     *             if the current thread was interrupted while waiting
     */
    T get() throws InterruptedException, ExecutionException;

    /**
     * Waits if necessary for at most the given time for completion, and then retrieves its result, if available.
     *
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return the computed result
     * @throws CancellationException
     *             if the computation was cancelled
     * @throws ExecutionException
     *             if the computation threw an exception
     * @throws InterruptedException
     *             if the current thread was interrupted while waiting
     * @throws TimeoutException
     *             if the wait timed out
     */
    T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;

    /**
     * Returns the result value (or throws any encountered exception) if completed, else returns the given
     * valueIfAbsent.
     *
     * @param valueIfAbsent
     *            the value to return if not completed
     * @return the result value, if completed, else the given valueIfAbsent
     * @throws CancellationException
     *             if the computation was cancelled
     */
    T getNow(T valueIfAbsent);

    /**
     * The given function is invoked with the result (or {@code null} if none) and the exception (or {@code null} if
     * none) of this NetworkFuture when complete.
     *
     * @param consumer
     *            the composer used for processing the result
     */
    void handle(BiConsumer<T, Throwable> consumer);

    /**
     * Returns {@code true} if completed in any fashion: normally, exceptionally, or via cancellation.
     *
     * @return {@code true} if completed
     */
    boolean isDone();

    /**
     * Creates a new MmsFuture that will time out via {@link TimeoutException} if this task has not completed within the
     * specified time.
     *
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return the new network future
     */
    EndpointInvocationFuture<T> timeout(long timeout, TimeUnit unit);
}
