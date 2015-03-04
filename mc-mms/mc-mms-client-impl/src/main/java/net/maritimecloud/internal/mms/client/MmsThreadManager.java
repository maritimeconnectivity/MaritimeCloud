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
package net.maritimecloud.internal.mms.client;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import net.maritimecloud.internal.net.util.DefaultEndpointInvocationFuture;
import net.maritimecloud.internal.util.concurrent.CompletableFuture;
import net.maritimecloud.internal.util.concurrent.ConcurrentWeakHashSet;
import net.maritimecloud.net.mms.MmsClientClosedException;
import net.maritimecloud.util.Binary;

import org.cakeframework.container.lifecycle.RunOnStart;
import org.cakeframework.container.lifecycle.RunOnStop;

/**
 * A central place for all threads that are spawned within the MMS Client.
 * <p>
 * This does not include the threads used for handling websocket connections.
 *
 * @author Kasper Nielsen
 */
public class MmsThreadManager {

    /** The prefix of each thread created by the client. */
    static final String THREAD_PREFIX = "MMSClient";

    /** An {@link ExecutorService} for running various tasks. */
    final ThreadPoolExecutor es = new ThreadPoolExecutor(0, 100, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new DefaultThreadFactory("GeneralPool", Executors.defaultThreadFactory()));

    /** A list of all outstanding futures. Is used to cancel each future in case of shutdown. */
    final ConcurrentWeakHashSet<DefaultEndpointInvocationFuture<?>> futures = new ConcurrentWeakHashSet<>();

    /** A {@link ScheduledExecutorService} for scheduling various tasks. */
    final ScheduledThreadPoolExecutor ses = new ScheduledThreadPoolExecutor(2, new DefaultThreadFactory("Scheduler",
            Executors.defaultThreadFactory()));

    public <T> DefaultEndpointInvocationFuture<T> create(Binary messageId) {
        DefaultEndpointInvocationFuture<T> t = new DefaultEndpointInvocationFuture<>(new CompletableFuture<T>(),
                messageId);
        futures.add(t);
        return t;
    }


    public void broadcastReceived(Runnable r) {
        es.execute(r);
    }

    @RunOnStart
    public void start() {
        // Clean up weak references
        ses.schedule(new Runnable() {
            public void run() {
                futures.cleanup();
            }
        }, 1, TimeUnit.MINUTES);
    }

    /**
     * @param runnable
     */
    public void startCloseThread(Runnable runnable) {
        runDaemonThread(THREAD_PREFIX + "-ClosingThread", runnable);
    }

    public void startConnectingManager(Runnable runnable) {
        runDaemonThread(THREAD_PREFIX + "-ConnectionManager", runnable);
    }

    public void startConnectingThread(Runnable runnable) {
        runDaemonThread(THREAD_PREFIX + "-ConnectingThread", runnable);
    }

    public void startDisconnectingThread(Runnable runnable) {
        runDaemonThread(THREAD_PREFIX + "-DisconnectingThread", runnable);
    }

    public void startWorkerThread(Runnable runnable) {
        runDaemonThread(THREAD_PREFIX + "-MessageProcessor", runnable);
    }

    void runDaemonThread(String name, Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(name);
        t.setDaemon(true);
        t.start();
    }

    @RunOnStop
    public void stop() {
        es.shutdown();
        ses.shutdown();
        for (DefaultEndpointInvocationFuture<?> f : futures) {
            if (!f.isDone()) {
                f.completeExceptionally(new MmsClientClosedException("OOps"));
            }
        }

        for (Runnable r : ses.getQueue()) {
            ScheduledFuture<?> sf = (ScheduledFuture<?>) r;
            sf.cancel(false);
        }
        ses.purge(); // remove all the tasks we just cancelled
        try {
            ses.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        try {
            es.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }


    static class DefaultThreadFactory implements ThreadFactory {
        private final ThreadFactory delegate;

        private final String prefix;

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        DefaultThreadFactory(String prefix, ThreadFactory delegate) {
            this.delegate = requireNonNull(delegate);
            this.prefix = prefix;
        }

        public Thread newThread(Runnable r) {
            Thread t = delegate.newThread(r);
            t.setDaemon(true);
            t.setName(THREAD_PREFIX + "-" + prefix + "-" + threadNumber.getAndIncrement());
            return t;
        }
    }
}
