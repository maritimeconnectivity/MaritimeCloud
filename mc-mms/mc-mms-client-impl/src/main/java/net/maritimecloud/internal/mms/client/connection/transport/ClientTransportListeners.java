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
package net.maritimecloud.internal.mms.client.connection.transport;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

/**
 * An implementation of {@linkplain ClientTransportListener} that manages an internal list of the
 * ClientTransportListener and delegates all events to each listener
 */
public class ClientTransportListeners implements ClientTransportListener {

    List<ClientTransportListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Factory method that combines the list of listeners into a combined client transport listener
     * 
     * @param listeners
     *            the listeners to add
     * @return the combined client transport listeners
     */
    public static ClientTransportListeners of(List<ClientTransportListener> listeners) {
        ClientTransportListeners result = new ClientTransportListeners();
        if (listeners != null) {
            result.getListeners().addAll(listeners);
        }
        return result;
    }

    /**
     * Factory method that combines the list of listeners into a combined client transport listener
     * 
     * @param listeners
     *            the listeners to add
     * @return the combined client transport listeners
     */
    public static ClientTransportListeners of(ClientTransportListener... listeners) {
        return of(listeners == null ? null : Arrays.asList(listeners));
    }

    /**
     * Adds a listener to the internal list of listeners
     * 
     * @param listener
     *            the listener to add
     */
    public void addListener(ClientTransportListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the internal list of listeners
     * 
     * @param listener
     *            the listener to remove
     */
    public void removeListener(ClientTransportListener listener) {
        listeners.add(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void onOpen() {
        listeners.forEach(ClientTransportListener::onOpen);
    }

    /** {@inheritDoc} */
    @Override
    public void onClose(MmsConnectionClosingCode closingCode) {
        listeners.forEach(l -> l.onClose(closingCode));
    }

    /** {@inheritDoc} */
    @Override
    public void onMessageReceived(MmsMessage message) {
        listeners.forEach(l -> l.onMessageReceived(message));
    }

    /** {@inheritDoc} */
    @Override
    public void onMessageSent(MmsMessage message) {
        listeners.forEach(l -> l.onMessageSent(message));
    }

    public List<ClientTransportListener> getListeners() {
        return listeners;
    }
}
