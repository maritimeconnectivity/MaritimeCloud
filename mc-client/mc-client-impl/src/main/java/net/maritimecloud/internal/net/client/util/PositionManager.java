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
package net.maritimecloud.internal.net.client.util;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.internal.net.client.connection.ConnectionMessageBus;
import net.maritimecloud.internal.net.messages.PositionReport;
import net.maritimecloud.net.MaritimeCloudClientConfiguration;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionTime;

import org.picocontainer.Startable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A runnable that will keep sending a keep alive signal.
 *
 * @author Kasper Nielsen
 */
public class PositionManager implements Startable {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(PositionManager.class);

    /** Send out a signal no more often than. */
    static long minimumSignalDuration;

    /** Responsible for creating a current position and time. */
    private final PositionReader positionReader;

    /** The connection to the server. */
    private final ConnectionMessageBus connection;

    /** When we send the last message */
    private volatile long latestTime; // <0 so we always send it first time

    private final ThreadManager threadManager;

    /**
     * @param transport
     * @param positionSupplier
     */
    public PositionManager(ConnectionMessageBus connection, MaritimeCloudClientConfiguration builder,
            ThreadManager threadManager) {
        this.connection = requireNonNull(connection);
        this.positionReader = requireNonNull(builder.getPositionReader());
        this.threadManager = threadManager;
        minimumSignalDuration = builder.getKeepAlive(TimeUnit.NANOSECONDS);
        latestTime = System.nanoTime();// -minimumSignalDuration;
    }

    void sendSignal() {
        long now = System.nanoTime();
        // Only send a message if it is more MINIMUM_SIGNAL_DURATION time since the last signal
        if (now - latestTime < minimumSignalDuration) {
            return;
        }

        PositionTime t = null;
        try {
            t = getPositionTime();
        } catch (Exception e) {
            LOG.error("Could not create a KeepAlive position", e);
            return;
        }

        latestTime = now;
        connection.sendConnectionMessage(new PositionReport().setPositionTime(t));
    }

    public PositionTime getPositionTime() {
        PositionTime pt = positionReader == null ? null : positionReader.getCurrentPosition();
        if (pt == null) {
            // We just send a dummy position
            // We should probably just send ", ," instead of "0,0," as the position
            pt = new PositionTime(0, 0, System.currentTimeMillis());
        }
        return pt;
    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        threadManager.scheduleAtFixedRate(new Runnable() {
            public void run() {
                sendSignal();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {}
}
