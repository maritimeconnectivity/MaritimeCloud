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

import net.maritimecloud.util.function.BiConsumer;

/**
 * 
 * @author Kasper Nielsen
 */
public interface ConnectionFuture<T> {

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
     * @param fn
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
     * Creates a new NetworkFuture that will time out via {@link TimeoutException} if this task has not completed within
     * the specified time.
     * 
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return the new network future
     */
    ConnectionFuture<T> timeout(long timeout, TimeUnit unit);

}
