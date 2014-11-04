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
package net.maritimecloud.internal.mms.client.connection;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.net.mms.MmsConnection;

/**
 *
 * @author Kasper Nielsen
 */
public class DefaultMmsConnection implements MmsConnection {

    final ClientConnection connection;

    /**
     * @param connection
     */
    public DefaultMmsConnection(ClientConnection connection) {
        this.connection = requireNonNull(connection);
    }

    /** {@inheritDoc} */
    @Override
    public boolean awaitConnected(long timeout, TimeUnit unit) throws InterruptedException {
        return connection.await(true, timeout, unit);
    }

    /** {@inheritDoc} */
    @Override
    public boolean awaitDisconnected(long timeout, TimeUnit unit) throws InterruptedException {
        return connection.await(false, timeout, unit);
    }

    /** {@inheritDoc} */
    @Override
    public void disable() {
        connection.setEnabled(false);
    }

    /** {@inheritDoc} */
    @Override
    public void enable() {
        connection.setEnabled(true);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isConnected() {
        return connection.isConnected();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEnabled() {
        return connection.isEnabled();
    }
}
