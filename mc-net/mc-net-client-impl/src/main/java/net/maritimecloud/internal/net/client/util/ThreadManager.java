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

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import net.maritimecloud.net.ConnectionClosedException;

import org.picocontainer.Startable;

/**
 * 
 * @author Kasper Nielsen
 */
public class ThreadManager implements Startable {

    /** The prefix of each thread created by the client. */
    static final String THREAD_PREFIX = "MaritimeCloud";

    /** An {@link ExecutorService} for running various tasks. */
    final ThreadPoolExecutor es = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new DefaultThreadFactory("GeneralPool", Executors.defaultThreadFactory()));

    /** A list of all outstanding futures. Is used to cancel each future in case of shutdown. */
    final Set<DefaultConnectionFuture<?>> futures = Collections
            .newSetFromMap(new CustomConcurrentHashMap<DefaultConnectionFuture<?>, Boolean>(
                    CustomConcurrentHashMap.WEAK, CustomConcurrentHashMap.EQUALS, CustomConcurrentHashMap.STRONG,
                    CustomConcurrentHashMap.EQUALS, 0));

    /** A {@link ScheduledExecutorService} for scheduling various tasks. */
    final ScheduledThreadPoolExecutor ses = new ScheduledThreadPoolExecutor(2, new DefaultThreadFactory("Scheduler",
            Executors.defaultThreadFactory()));

    public <T> DefaultConnectionFuture<T> create() {
        DefaultConnectionFuture<T> t = new DefaultConnectionFuture<>(this);
        futures.add(t);
        return t;
    }


    /**
     * @param runnable
     */
    public void startCloseThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(THREAD_PREFIX + "-ClosingThread");
        t.setDaemon(true);
        t.start();
    }


    public void startConnectingManager(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(THREAD_PREFIX + "-ConnectionManager");
        t.setDaemon(true);
        t.start();
    }

    public void startDisconnectingThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(THREAD_PREFIX + "-DisconnectingThread");
        t.setDaemon(true);
        t.start();
    }

    public void startConnectingThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(THREAD_PREFIX + "-ConnectingThread");
        t.setDaemon(true);
        t.start();
    }

    public void startWorkerThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(THREAD_PREFIX + "-MessageProcessor");
        t.setDaemon(true);
        t.start();
    }

    public void execute(Runnable r) {
        es.execute(r);
    }

    /**
     * Returns the scheduled executor.
     * 
     * @return the scheduled executor
     */
    public ScheduledThreadPoolExecutor getScheduler() {
        return ses;
    }

    /**
     * @param command
     * @param initialDelay
     * @param period
     * @param unit
     * @return
     * @see java.util.concurrent.ScheduledThreadPoolExecutor#scheduleAtFixedRate(java.lang.Runnable, long, long,
     *      java.util.concurrent.TimeUnit)
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return ses.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    /**
     * @param command
     * @param initialDelay
     * @param delay
     * @param unit
     * @return
     * @see java.util.concurrent.ScheduledThreadPoolExecutor#scheduleWithFixedDelay(java.lang.Runnable, long, long,
     *      java.util.concurrent.TimeUnit)
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return ses.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    /** {@inheritDoc} */
    @Override
    public void start() {}

    /** {@inheritDoc} */
    @Override
    public void stop() {
        es.shutdown();
        ses.shutdown();
        for (DefaultConnectionFuture<?> f : futures) {
            if (!f.isDone()) {
                f.completeExceptionally(new ConnectionClosedException("OOps"));
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

    /**
     * @param task
     * @return
     * @see java.util.concurrent.AbstractExecutorService#submit(java.util.concurrent.Callable)
     */
    public <T> Future<T> submit(Callable<T> task) {
        return es.submit(task);
    }

    static class DefaultThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private final ThreadFactory delegate;

        private final String prefix;

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
