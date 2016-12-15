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
package net.maritimecloud.internal.mms.client.connection.session;

import static java.util.Objects.requireNonNull;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.internal.mms.client.connection.transport.ClientTransport;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.util.concurrent.CompletableFuture;
import net.maritimecloud.message.Message;

/**
 *
 * @author Kasper Nielsen
 */
class SessionSender extends Thread {

    final Session session;

    volatile long nextMsgId = 1L;

    final ConcurrentSkipListMap<Long, UnAcked> futures = new ConcurrentSkipListMap<>();

    final LinkedList<Msg> messages = new LinkedList<>();

    /** Signaled when the state of the connection manager changes. */
    final Condition stateChange;

    final Writer writer = new Writer();

    static final Executor e;

    static {
        e = Executors.newSingleThreadExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            }
        });
    }

    SessionSender(Session session) {
        this.session = requireNonNull(session);
        stateChange = session.lock.newCondition();
        setDaemon(true);
        setName("MMSClient-SessionSender");

    }

    void completeAll() {
        for (UnAcked m : futures.values()) {
            m.msg.onAck.complete(null);
        }
    }

    void onAck(long id) {
        session.lock.lock();
        try {
            Entry<Long, SessionSender.UnAcked> e = futures.firstEntry();
            while (e != null && e.getKey() <= id) {
                e = futures.pollFirstEntry();
                e.getValue().msg.onAck.complete(null);
                e = futures.firstEntry();
            }
        } finally {
            session.lock.unlock();
        }
    }

    void reconnectUnderLock(long lastReceivedId) {
        onAck(lastReceivedId);
        nextMsgId = lastReceivedId + 1;
        for (UnAcked u : futures.descendingMap().values()) {
            messages.addFirst(u.msg);
        }
        futures.clear();
        stateChange.signalAll();
    }

    public void run() {
        while (!session.isClosed) {
            session.lock.lock();
            try {
                SessionState s = session.state;

                if (s instanceof SessionStateConnected) {
                    SessionStateConnected ssc = (SessionStateConnected) s;
                    Msg poll = messages.poll();
                    if (poll == Msg.SUP) {
                        poll = null;
                    }

                    if (poll != null) {
                        MmsMessage mms = new MmsMessage(poll.message);
                        long id = nextMsgId;
                        mms.setMessageId(id);

                        futures.put(id, new UnAcked(poll, mms));
                        nextMsgId++;
                        mms.setLatestReceivedId(session.latestReceivedId);
                        writer.send(ssc.transport, mms, e);
                    }
                }

                if (!session.isClosed && messages.isEmpty()) {
                    try {
                        stateChange.await();
                    } catch (InterruptedException probablyShutdown) {}
                }
            } finally {
                session.lock.unlock();
            }
        }
    }

    void send(Message message, CompletableFuture<Void> onAck) {
        session.lock.lock();
        try {
            messages.add(new Msg(message, onAck));
            stateChange.signalAll();
        } finally {
            session.lock.unlock();
        }
    }

    void sup() {
        session.lock.lock();
        try {
            messages.add(null);
            stateChange.signalAll();
        } finally {
            session.lock.unlock();
        }
    }

    static class UnAcked {
        final Msg msg;

        final MmsMessage mm;

        UnAcked(Msg msg, MmsMessage mm) {
            this.msg = msg;
            this.mm = mm;
        }
    }

    static class Msg {
        static final Msg SUP = new Msg();

        final Message message;

        final CompletableFuture<Void> onAck;

        Msg() {
            this.message = null;
            this.onAck = null;
        }

        Msg(Message message, CompletableFuture<Void> onAck) {
            this.message = requireNonNull(message);
            this.onAck = requireNonNull(onAck);
        }
    }

    static class Writer implements Runnable {
        private final ReentrantLock executorLock = new ReentrantLock();

        private final BlockingQueue<Map.Entry<ClientTransport, MmsMessage>> q = new LinkedBlockingQueue<>();

        /** {@inheritDoc} */
        public void run() {
            // We need retry check for extra elements one more time, if we have successfully polled elements
            // This is because of a rare race condition where
            // T1[Executor Thread] : q.poll() returns null
            // T2 : submits an message to be send and queues this runnable, T3 picks it up.
            // T3[Executor Thread] can not obtain the lock because T1 has not yet released it and returns emptyhanded

            boolean sholdRetry;
            do {
                sholdRetry = false;
                if (executorLock.tryLock()) {
                    try {
                        Entry<ClientTransport, MmsMessage> s = q.poll();
                        while (s != null) {
                            sholdRetry = true;
                            try {
                                s.getKey().sendMessage(s.getValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            s = q.poll();
                        }
                    } finally {
                        executorLock.unlock();
                    }
                }
            } while (sholdRetry);
        }

        void send(ClientTransport transport, MmsMessage message, Executor e) {
            q.add(new AbstractMap.SimpleImmutableEntry<>(transport, message));
            e.execute(this);
        }
    }
}
