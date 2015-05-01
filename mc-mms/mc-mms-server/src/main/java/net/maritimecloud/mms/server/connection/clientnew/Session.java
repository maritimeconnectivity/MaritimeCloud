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
package net.maritimecloud.mms.server.connection.clientnew;

import static java.util.Objects.requireNonNull;

import java.util.LinkedList;
import java.util.Queue;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class Session {

    /** The client that this session is attached to. */
    final InternalClient client;

    volatile ServerTransport connected;

    volatile long latestReceivedAndAckedMessageId;

    final SessionListener listener;

    private Queue<SessionMessageFuture> notAcked = new LinkedList<>();

    /** The unique session id. */
    private final Binary sessionId = Binary.random(32);

    Session(InternalClient client) {
        this.client = requireNonNull(client);
        this.listener = client.manager.sessionLister;
    }

    /** Invoked whenever the session is killed permanently. Makes sure all outstanding writes are marked as failed. */
    void disconnectedWithWriteLock(boolean destroy) {
        this.connected = null;
        // loeb igennem alle, marker dem som doede
        // og toem alle koere
    }

    // called while readlocked on the client.
    // might be called concurrently, so we lock it for now, but might find another solution in the future
    synchronized SessionMessageFuture enqueueMessageWithReadLock(MmsMessage m) {
        SessionMessageFuture smf = new SessionMessageFuture(m);
        notAcked.add(smf);
        if (connected != null) {
            connected.sendMessage(m);
        }
        // Sometimes the onMessageWithReadLock will call in here. But never the other way around
        // for example, if a handler of a received message sends a reply
        return smf;
    }

    /**
     * @return the client
     */
    public InternalClient getClient() {
        return client;
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
     * Invoked whenever the underlying transport has been successfully connected. Takes care of resending all messages.
     *
     * @param connected
     */
    void onConnectWithWriteLock(ServerTransport connected) {
        this.connected = requireNonNull(connected);
        for (SessionMessageFuture f : notAcked) {
            connected.sendMessage(f.cm);
        }
    }

    /**
     * Receives a message while connected. This is always invoked one at a time.
     *
     * @param message
     *            the message that was received
     */
    void onMessageWithReadLock(MmsMessage message) {
        listener.onMessage(this, message);

        latestReceivedAndAckedMessageId = message.getMessageId();
        long ackUntil = message.getLatestReceivedId();

        // mark acked

        // smid den paa en eller anden processing queue
        // og lav en mulighed for completable callback.
    }

    public SessionMessageFuture send(MmsMessage message) {
        return client.manager.sendMessage(client, this, message);
    }
}
