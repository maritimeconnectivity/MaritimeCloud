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

import static java.util.Objects.requireNonNull;

import java.util.LinkedList;

import net.maritimecloud.internal.net.messages.spi.ConnectionMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kasper Nielsen
 */
public class WorkerInner {
    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(WorkerInner.class);

    private boolean nextIsReceived /* = false */;

    private final LinkedList<OutstandingMessage> unwritten = new LinkedList<>();

    private final LinkedList<OutstandingMessage> written = new LinkedList<>();

    private final LinkedList<ConnectionMessage> received = new LinkedList<>();

    final Worker worker;

    long latestAck /* = 0 */;

    long nextSendId = 1;

    long latestReceivedMessageId;

    WorkerInner(Worker worker) {
        this.worker = requireNonNull(worker);
    }

    ConnectionTransport transport;

    public void onConnect(ConnectionTransport transport, long id, boolean isReconnected) {
        LinkedList<Long> idsToResend = new LinkedList<>();
        while (!written.isEmpty()) {
            OutstandingMessage om = written.pollLast();
            if (om.id > id) {
                idsToResend.addFirst(om.id);
                unwritten.addFirst(om);
            }
        }
        if (isReconnected) {
            nextSendId = id + 1;
            if (!unwritten.isEmpty()) {}
        }
        if (idsToResend.size() > 0) {
            LOG.debug("Resending messages with id(s): " + idsToResend);
        }
        this.transport = transport;
        while (processNext()) {}
    }

    public void fromQueue(Object o) {
        if (o instanceof OutstandingMessage) {
            unwritten.add((OutstandingMessage) o);
        } else {
            received.add((ConnectionMessage) o);
        }
    }

    /**
     * @return false if there are no messages to process
     */
    boolean processNext() {
        // System.out.println("ProcessNext" + Thread.currentThread().getName());
        // System.out.println(System.currentTimeMillis() + " XXXXXXXXXXXXX");
        boolean nextIsReceived = this.nextIsReceived;
        this.nextIsReceived = !nextIsReceived;
        if (nextIsReceived) {
            if (received.size() > 0) {
                processReceived();
            } else if (unwritten.size() > 0) {
                processWritten();
            } else {
                return false;
            }
        } else {
            if (unwritten.size() > 0) {
                processWritten();
            } else if (received.size() > 0) {
                processReceived();
            } else {
                return false;
            }
        }
        return true;
    }


    private void processReceived() {
        ConnectionMessage cm = received.poll();
        // System.out
        // .println(System.currentTimeMillis() + " GOT MSG with " + cm.getLatestReceivedId() + " " + cm.toJSON());
        latestReceivedMessageId = cm.getMessageId();
        latestAck = cm.getLatestReceivedId();
        worker.connection.getBus().onMsg(cm);
        for (;;) {
            OutstandingMessage m = written.peek();
            if (m == null || m.id > latestAck) {
                return;
            }
            // System.out.println(System.currentTimeMillis() + " ACKED " + m.cm.getClass() + " " + m.uuid);
            m.acked().complete(null);
            written.poll();
        }
    }

    private void processWritten() {
        // System.out.println("Proc written " + Thread.currentThread().getName());
        ConnectionTransport transport = worker.connection.getTransport();
        if (transport != null && transport == this.transport) {
            OutstandingMessage om = unwritten.poll();
            ConnectionMessage cm = om.cm;
            om.id = nextSendId++;
            cm.setMessageId(om.id);
            cm.setLatestReceivedId(latestReceivedMessageId);
            String message = cm.toText();
            written.add(om);
            // System.out.println("Adding " + om.id + " to written");
            transport.sendText(message);
        } else {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignore) {}
        }
    }
}
