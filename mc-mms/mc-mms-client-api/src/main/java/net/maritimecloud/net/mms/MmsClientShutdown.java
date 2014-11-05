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
package net.maritimecloud.net.mms;

import java.util.concurrent.CompletionStage;

/**
 *
 * @author Kasper Nielsen
 */
interface MmsClientShutdown {

    /**
     * Returns {@code true} if the shutdown completed. The return value of this method is identical to
     * {@code container.getState() == State.TERMINATED}.
     *
     * @return {@code true} if the container was fully shutdown, otherwise {@code false}
     */
    boolean isTerminated();

    /**
     * Returns the client when is has completed shutting down the container.
     *
     * @return the client that was shutdown
     */
    MmsClient join();

    /**
     * Returns a new CompletionStage that, when shutdown of the container completes, executes the given action using
     * this future's default asynchronous execution facility. If the container has already terminated the specified
     * action is scheduled for execution immediately.
     * <p>
     * Invoking this method is equivalent to invoking <code>toCompletionStage().thenRunAsync(action)</code>.
     *
     * @param action
     *            the action to perform before completing the returned CompletionStage
     * @return the new CompletionStage
     * @throws NullPointerException
     *             if the specified action is null
     */
    CompletionStage<Void> thenRunAsync(Runnable action);
}
