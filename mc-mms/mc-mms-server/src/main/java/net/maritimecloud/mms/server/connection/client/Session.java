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
package net.maritimecloud.mms.server.connection.client;

import static java.util.Objects.requireNonNull;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.message.Message;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;
import net.maritimecloud.util.Binary;

import org.cakeframework.container.concurrent.ThreadManager;

/**
 *
 * @author Kasper Nielsen
 */
public class Session {

    /** The client that this session is attached to. */
    private final Client client;

    /** A context map used for attaching state to the session. */
    private final ConcurrentHashMap<String, Object> contextMap = new ConcurrentHashMap<>();

    private long latestMessageIdAckedByRemote /* = 0 */;

    long latestMessageIdReceivedByRemote;

    private long nextMessageIdToSend = 1;

    /** A queue of messages that have not yet been acked. */
    private final Queue<SessionMessageFuture> unAckedMessages = new LinkedBlockingQueue<>();

    /**
     * A executor that is used to asynchronous write messages. The reason is websocket.asyncwrite will sometime call
     * into @onClose on the transport. onClose will try to acquire a write lock, to properly lock it. However, a receive
     * function (also holding a read lock) might be try to do the same thing. So instead of running into problems. We
     * let another thread write the message to the websocket. Avoid calling recursively into close.writeLock()
     */
    private final Executor sendExecutor;

    /** The unique session id. */
    private final Binary sessionId = Binary.random(32);

    /** A listener of incoming messages */
    private final Session.Listener sessionMessageListener;

    /** The system time of the last received message. */
    private volatile long timeOfLastReceivedMessage = System.nanoTime();

    /** The transport to send messages on. Might be null, for example, if the remote client is disconnected. */
    private Writer writer;

    Session(Client client) {
        this.client = requireNonNull(client);
        this.sessionMessageListener = requireNonNull(client.clientManager.mmsServer.getService(Session.Listener.class));
        ThreadManager tm = client.clientManager.mmsServer.getService(ThreadManager.class);
        this.sendExecutor = tm.getExecutor("mms");
    }

    /** Invoked whenever the session is killed permanently. Makes sure all outstanding writes are marked as failed. */
    void disconnectedWithWriteLock(boolean destroy) {
        this.writer = null;
        // loeb igennem alle, marker dem som doede
        // og toem alle koere
    }

    // called while readlocked on the client.
    // might be called concurrently, so we lock it for now, but might find another solution in the future
    SessionMessageFuture enqueueMessageWithReadLock(Message msg) {
        // We need to have another thread write the message. The problem is that even though
        MmsMessage m = new MmsMessage(msg);
        synchronized (unAckedMessages) {
            SessionMessageFuture smf = new SessionMessageFuture(m, nextMessageIdToSend++);
            m.setMessageId(smf.messageId);
            m.setLatestReceivedId(latestMessageIdReceivedByRemote);
            unAckedMessages.add(smf);

            // only write if connected, otherwise leave in notAcked queue
            if (writer != null) {
                writer.send(smf, sendExecutor);
            }
            return smf;
        }

    }

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    public Object getContext(String key) {
        return contextMap.get(key);
    }

    /**
     * Returns the session id of this session.
     *
     * @return the session id of this session
     */
    public Binary getSessionId() {
        return sessionId;
    }

    /**
     * @return the timeOfLastReceivedMessage
     */
    public long getTimeOfLastReceivedMessage() {
        return timeOfLastReceivedMessage;
    }

    /**
     * Invoked whenever the underlying transport has been successfully connected. Takes care of resending all messages.
     *
     * @param transport
     */
    void onConnectWithWriteLock(ServerTransport transport, long msgId) {
        // Start by removing messages that already been acked according to msgId
        removeAckedExclusively(msgId);
        writer = new Writer(transport);

        // Technically it is okay to send messages directly but we should probably send them async as well
        for (SessionMessageFuture f : unAckedMessages) {
            transport.sendMessage(f.message);
            nextMessageIdToSend = f.messageId + 1;
        }
    }

    /**
     * Receives a message while connected. This is always invoked one at a time.
     *
     * @param message
     *            the message that was received
     */
    void onMessageWithReadLock(MmsMessage message) {
        timeOfLastReceivedMessage = System.nanoTime();
        latestMessageIdReceivedByRemote = message.getMessageId();
        latestMessageIdAckedByRemote = message.getLatestReceivedId();
        // So hmm, the two above why are they above this line.
        // Mainly because the listener will most likely send a reply message
        // And it will strange that latestReceivedMessageId has not been updated
        // to include the latest received message (the initiating message of the reply message)
        sessionMessageListener.onMessage(this, message.getM());

        removeAckedExclusively(latestMessageIdAckedByRemote);
    }

    private void removeAckedExclusively(long id) {
        for (;;) {
            SessionMessageFuture f = unAckedMessages.peek();
            if (f == null || f.messageId > id) {
                return;
            }
            f.protocolAcked().complete(null);
            unAckedMessages.poll();// remove peeked element
        }
    }

    public SessionMessageFuture send(Message message) {
        // delegate to client manager to make sure we have the latest and greatest
        return client.sendMessage(this, message);
    }

    public interface Listener {

        /**
         *
         * <p>
         * The reason it is marked acked and we do not supply some kind og completion token is that messages cannot be
         * acked in a random order. Before message x can be acked, message x-1 must already have been acked.
         *
         * @param session
         * @param message
         */
        // is under readlock, is invoked in order one at a time
        void onMessage(Session session, Message message);
    }

    static class Writer implements Runnable {
        private final ReentrantLock executorLock = new ReentrantLock();

        private final BlockingQueue<SessionMessageFuture> q = new LinkedBlockingQueue<>();

        final ServerTransport transport;

        Writer(ServerTransport transport) {
            this.transport = requireNonNull(transport);
        }

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
                        SessionMessageFuture s = q.poll();
                        while (s != null) {
                            sholdRetry = true;
                            try {
                                transport.sendMessage(s.message);
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

        void send(SessionMessageFuture f, Executor e) {
            q.add(f);
            e.execute(this);
        }
    }
}
