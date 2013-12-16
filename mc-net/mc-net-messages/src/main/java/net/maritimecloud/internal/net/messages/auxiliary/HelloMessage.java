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
package net.maritimecloud.internal.net.messages.auxiliary;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.internal.net.messages.TransportMessage;

/**
 * 
 * @author Kasper Nielsen
 */
public class HelloMessage extends TransportMessage {

    // include connection type
    private final MaritimeId clientId;

    private final String clientInfo;

    private final double lat;

    private final double lon;

    private final String reconnectId;

    private final long lastReceivedMessageId;

    /**
     * @return the lastReceivedMessageId
     */
    public long getLastReceivedMessageId() {
        return lastReceivedMessageId;
    }

    /**
     * @param messageType
     */
    public HelloMessage(MaritimeId clientId, String clientInfo, String reconnectId, long lastReceivedMessageId,
            double lat, double lon) {
        super(MessageType.HELLO);
        this.clientId = requireNonNull(clientId);
        this.clientInfo = requireNonNull(clientInfo);
        this.reconnectId = reconnectId;
        this.lat = lat;
        this.lon = lon;
        this.lastReceivedMessageId = lastReceivedMessageId;
    }

    public HelloMessage(TextMessageReader pr) throws IOException {
        this(MaritimeId.create(pr.takeString()), pr.takeString(), pr.takeString(), pr.takeLong(), pr.takeDouble(), pr
                .takeDouble());
    }

    /**
     * @return the clientId
     */
    public MaritimeId getClientId() {
        return clientId;
    }

    /**
     * @return the clientInfo
     */
    public String getClientInfo() {
        return clientInfo;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @return the lon
     */
    public double getLon() {
        return lon;
    }

    /**
     * @return the reconnectId
     */
    public String getReconnectId() {
        return reconnectId;
    }

    /** {@inheritDoc} */
    @Override
    protected void write(TextMessageWriter w) {
        w.writeString(clientId.toString());
        w.writeString(clientInfo);
        w.writeString(reconnectId);
        w.writeLong(lastReceivedMessageId);
        w.writeDouble(lat);
        w.writeDouble(lon);
    }
}
