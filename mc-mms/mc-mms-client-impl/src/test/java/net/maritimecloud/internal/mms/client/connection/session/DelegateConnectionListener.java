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

import java.net.URI;

import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;

/**
 *
 * @author Kasper Nielsen
 */
public class DelegateConnectionListener implements MmsConnection.Listener {

    final MmsConnection.Listener l;

    DelegateConnectionListener(MmsConnection.Listener l) {
        this.l = requireNonNull(l);
    }

    /**
     * @param message
     * @see net.maritimecloud.net.mms.MmsConnection.Listener#binaryMessageReceived(byte[])
     */
    public void binaryMessageReceived(byte[] message) {
        l.binaryMessageReceived(message);
    }

    /**
     * @param message
     * @see net.maritimecloud.net.mms.MmsConnection.Listener#binaryMessageSend(byte[])
     */
    public void binaryMessageSend(byte[] message) {
        l.binaryMessageSend(message);
    }

    /**
     *
     * @see net.maritimecloud.net.mms.MmsConnection.Listener#connected()
     */
    public void connected(URI host) {
        l.connected(host);
    }

    /**
     * @param host
     * @see net.maritimecloud.net.mms.MmsConnection.Listener#connecting(java.net.URI)
     */
    public void connecting(URI host) {
        l.connecting(host);
    }

    /**
     * @param closeReason
     * @see net.maritimecloud.net.mms.MmsConnection.Listener#disconnected(net.maritimecloud.net.mms.MmsConnectionClosingCode)
     */
    public void disconnected(MmsConnectionClosingCode closeReason) {
        l.disconnected(closeReason);
    }

    /**
     * @param message
     * @see net.maritimecloud.net.mms.MmsConnection.Listener#textMessageReceived(java.lang.String)
     */
    public void textMessageReceived(String message) {
        l.textMessageReceived(message);
    }

    /**
     * @param message
     * @see net.maritimecloud.net.mms.MmsConnection.Listener#textMessageSend(java.lang.String)
     */
    public void textMessageSend(String message) {
        l.textMessageSend(message);
    }


}
