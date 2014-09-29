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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An acknowledgement that a given message has been received. Either by a central relay server or by the client itself.
 *
 * @author Kasper Nielsen
 */
public interface Acknowledgement {

    /**
     * Returns whether or not the message has been acknowledge.
     *
     * @return whether or not the message has been acknowledge
     */
    boolean isAckowledged();

    /**
     * Returns a new Acknowledgement that, when the message has been acknowledged, executes the given action.
     *
     * See the {@link CompletionStage} documentation for rules covering exceptional completion.
     *
     * @param action
     *            the action to perform before completing the returned Acknowledgement
     * @return the new Acknowledgement
     */
    Acknowledgement thenRun(Runnable runnable);

    /**
     * Creates a new acknowledgement that will time out via {@link TimeoutException} if the message has not been
     * acknowledgement within the specified time.
     *
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return the new acknowledgement
     */
    Acknowledgement timeout(long timeout, TimeUnit unit);

    /**
     * Returns the result value when complete, or throws an (unchecked) exception if completed exceptionally. To better
     * conform with the use of common functional forms, if a computation involved in the completion of this
     * CompletableFuture threw an exception, this method throws an (unchecked) {@link CompletionException} with the
     * underlying exception as its cause.
     *
     * @return the result value
     * @throws CancellationException
     *             if the computation was cancelled
     * @throws CompletionException
     *             if this future completed exceptionally or a completion computation threw an exception
     */
    void join();

    /**
     * Returns a {@link CompletableFuture} maintaining the same completion properties as this acknowledgement.
     *
     * @return the CompletableFuture
     */
    CompletableFuture<Void> toCompletableFuture();
}
