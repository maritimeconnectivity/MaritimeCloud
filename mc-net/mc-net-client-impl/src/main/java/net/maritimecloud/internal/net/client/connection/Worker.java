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
package net.maritimecloud.internal.net.client.connection;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.internal.net.messages.ConnectionMessage;

/**
 * 
 * @author Kasper Nielsen
 */
public class Worker implements Runnable {

    private final ReentrantLock sendLock = new ReentrantLock();

    private final ReentrantLock receiveLock = new ReentrantLock();

    private final ReentrantLock workLock = new ReentrantLock();

    private final LinkedBlockingQueue<Object> q = new LinkedBlockingQueue<>();

    private volatile boolean isShutdown;

    final ClientConnection connection;

    final WorkerInner wi = new WorkerInner(this);

    public Worker(ClientConnection connection) {
        this.connection = connection;
    }

    public OutstandingMessage messageSend(ConnectionMessage message) {
        sendLock.lock();
        try {
            if (isShutdown) {

            }
            OutstandingMessage m = new OutstandingMessage(message);
            q.add(m);
            return m;
        } finally {
            sendLock.unlock();
        }
    }

    public void onConnect(ConnectionTransport transport, long id, boolean isReconnected) {
        sendLock.lock();
        receiveLock.lock();
        workLock.lock();
        try {
            wi.onConnect(transport, id, isReconnected);
        } finally {
            workLock.unlock();
            receiveLock.unlock();
            sendLock.unlock();
        }
    }

    public void messageReceived(ConnectionMessage message) {
        receiveLock.lock();
        try {
            if (!isShutdown) {
                q.add(message);
            }
        } finally {
            receiveLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        boolean sleepLong = true;
        while (!isShutdown) {
            Object o = null;
            if (sleepLong) {
                try {
                    o = q.take();
                } catch (InterruptedException ignore) {}
            } else {
                o = q.poll();
            }

            if (o != null) {
                wi.fromQueue(o);
                while ((o = q.poll()) != null) {
                    wi.fromQueue(o);
                }
            }

            workLock.lock();
            try {
                sleepLong = wi.processNext();
            } finally {
                workLock.unlock();
            }
        }
    }

    public void shutdown() {
        sendLock.lock();
        receiveLock.lock();
        workLock.lock();
        try {
            isShutdown = true;
        } finally {
            workLock.unlock();
            receiveLock.unlock();
            sendLock.unlock();
        }
    }
}
