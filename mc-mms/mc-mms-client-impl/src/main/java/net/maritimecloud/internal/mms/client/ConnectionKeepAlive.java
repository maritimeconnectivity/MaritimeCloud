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
package net.maritimecloud.internal.mms.client;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.mms.client.connection.ClientConnection;
import net.maritimecloud.internal.mms.messages.PositionReport;
import net.maritimecloud.internal.util.logging.Logger;
import net.maritimecloud.net.mms.MmsClientConfiguration;

import org.cakeframework.util.concurrent.ScheduleAtFixedRate;

/**
 * Takes care of sending out keep alive signals.
 *
 * @author Kasper Nielsen
 */
public class ConnectionKeepAlive {

    /** The logger. */
    private static final Logger LOGGER = Logger.get(ConnectionKeepAlive.class);

    /** Information about the client. */
    private final ClientInfo clientInfo;

    /** The connection to the server. */
    private final ClientConnection connection;

    /** When did we send the last keep alive. */
    private volatile long latestTime = System.nanoTime();

    /** Send out signals no more often than. */
    private final long minimumSignalDuration;

    public ConnectionKeepAlive(ClientConnection connection, MmsClientConfiguration builder, ClientInfo clientInfo) {
        this.connection = requireNonNull(connection);
        this.clientInfo = requireNonNull(clientInfo);
        this.minimumSignalDuration = builder.getKeepAlive(TimeUnit.NANOSECONDS);
    }

    @ScheduleAtFixedRate(value = 1, unit = TimeUnit.SECONDS)
    public void sendKeepAlive() {
        sendKeepAlive(false);
    }

    public void sendKeepAlive(boolean force) {
        long now = System.nanoTime();
        // Only send a message if it is more MINIMUM_SIGNAL_DURATION time since the last signal
        if (force || now - latestTime > minimumSignalDuration) {
            PositionReport pr = new PositionReport();
            clientInfo.getCurrentPosition().ifPresent(pt -> pr.setPositionTime(pt)); // set available position
            connection.sendMessage(pr);
            LOGGER.debug("Sending ping, [position = " + pr.getPositionTime() + "]");
            latestTime = now;
        }
    }
}
