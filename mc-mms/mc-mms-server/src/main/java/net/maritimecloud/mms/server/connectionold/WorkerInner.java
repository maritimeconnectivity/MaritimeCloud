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
package net.maritimecloud.mms.server.connectionold;

import static java.util.Objects.requireNonNull;

import java.util.LinkedList;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.mms.server.connectionold.transport.ServerTransport;

/**
 *
 * @author Kasper Nielsen
 */
public class WorkerInner {

    private boolean nextIsReceived /* = false */;

    private final LinkedList<OutstandingMessage> unwritten = new LinkedList<>();

    private final LinkedList<OutstandingMessage> written = new LinkedList<>();

    private final LinkedList<MmsMessage> received = new LinkedList<>();

    final Worker worker;

    long latestAck /* = 0 */;

    long nextSendId = 1;

    long latestReceivedMessageId;

    /**
     * @return the latestReceivedMessageId
     */
    public long getLatestReceivedMessageId() {
        return latestReceivedMessageId;
    }

    WorkerInner(Worker worker) {
        this.worker = requireNonNull(worker);
    }

    ServerTransport transport;

    public long onConnect(ServerTransport transport, long id, boolean isReconnected) {
        while (!written.isEmpty()) {
            OutstandingMessage om = written.pollLast();
            if (om.id > id) {
                // System.out.println("Resending message with id: " + om.id);
                unwritten.addFirst(om);
            }
        }
        if (isReconnected) {
            nextSendId = id + 1;
            if (!unwritten.isEmpty()) {}
        }
        // System.out.println("======== NextSendId=" + nextSendId + " " + id);
        this.transport = transport;
        while (processNext()) {}
        return latestReceivedMessageId;
    }

    public void fromQueue(Object o) {
        if (o instanceof OutstandingMessage) {
            unwritten.add((OutstandingMessage) o);
        } else {
            received.add((MmsMessage) o);
        }
    }

    /**
     * @return false if there are no messages to process
     */
    boolean processNext() {
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
        MmsMessage cm = received.poll();
        // System.out.println("GOT MSG with " + cm.getLatestReceivedId() + " " + cm.toJSON());
        latestReceivedMessageId = cm.getMessageId();
        latestAck = cm.getLatestReceivedId();
        worker.connection.bus.onMessage(worker.connection, cm.getM());
        for (;;) {
            OutstandingMessage m = written.peek();
            if (m == null || m.id > latestAck) {
                return;
            }
            m.protocolAcked().complete(null);
            written.poll();
        }
    }

    private void processWritten() {
        // System.out.println("Prep to send");
        ServerTransport transport = worker.connection.transport;
        if (transport != null && transport == this.transport) {
            OutstandingMessage om = unwritten.poll();
            MmsMessage cm = om.cm;
            om.id = nextSendId++;
            cm.setMessageId(om.id);
            cm.setLatestReceivedId(latestReceivedMessageId);
            written.add(om);
            transport.sendMessage(cm);
        } else {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignore) {}
        }
    }
}
