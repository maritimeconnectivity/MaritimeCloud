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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * An acknowledgement that a given message has been received. For example, it is used for the MMS client to indicate
 * whether or not the MMS Server has received a message.
 *
 * @author Kasper Nielsen
 */
public interface Acknowledgement {

    /**
     * Returns a new Acknowledgement that, when the message that was send has been acknowledged is executed. If the
     * message fails to be acknowledged the specified throwable is non-null.
     *
     * See the {@link CompletionStage} documentation for rules covering exceptional completion.
     *
     * @param fn
     *            the consumer that will invoked before completing the returned Acknowledgement. If the message failed
     *            to be acknowledge the specified throwable is non-null
     * @return the new Acknowledgement
     */
    Acknowledgement handle(Consumer<Throwable> fn);

    /**
     * Returns whether or not the message has been successfully acknowledge by the remote relay server.
     *
     * @return whether or not the message has been successfully acknowledge by the remote relay server
     */
    boolean isAcknowledged();

    /**
     * Returns {@code true} if completed in any fashion: normally or exceptionally.
     *
     * @return {@code true} if completed
     */
    boolean isDone();

    /**
     * Returns once the message has been acknowledged, or throws an (unchecked) exception if completed exceptionally. To
     * better conform with the use of common functional forms, if a computation involved in the completion of this
     * instance threw an exception, this method throws an (unchecked) {@link CompletionException} with the underlying
     * exception as its cause.
     *
     * @throws CompletionException
     *             if this acknowledgement completed exceptionally or a completion computation threw an exception
     */
    void join();

    /**
     * Returns a new Acknowledgement that, when the message that was send has been successfully acknowledged, executes
     * the given action.
     *
     * See the {@link CompletionStage} documentation for rules covering exceptional completion.
     *
     * @param runnable
     *            the action to perform before completing the returned Acknowledgement
     * @return the new Acknowledgement
     */
    Acknowledgement thenRun(Runnable runnable);


    /** use {@link #orTimeout(long, TimeUnit)} */
    @Deprecated
    default Acknowledgement timeout(long timeout, TimeUnit unit) {
        return orTimeout(timeout, unit);
    }

    /**
     * Creates a new acknowledgement that will time out via {@link TimeoutException} if the message has not been
     * acknowledgement within the specified timeout.
     *
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return the new acknowledgement
     */
    Acknowledgement orTimeout(long timeout, TimeUnit unit);

    /**
     * Returns a {@link CompletableFuture} maintaining the same completion properties as this acknowledgement.
     *
     * @return the CompletableFuture
     */
    CompletableFuture<Void> toCompletableFuture();
}
