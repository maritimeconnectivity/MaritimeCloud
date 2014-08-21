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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Kasper Nielsen
 */
// Skal den fungere som CompletableFuture eller som en builder
// Som completable future vil jeg sige
public interface Acknowledgement {

    boolean isAckowledged();

    Acknowledgement thenRun(Runnable runnable);

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
    Acknowledgement timeout(long timeout, TimeUnit unit);

    void join();

    CompletableFuture<Void> toCompletableFuture();
}
