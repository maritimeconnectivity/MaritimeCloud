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

import net.maritimecloud.internal.net.messages.ConnectionOldMessage;
import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.PositionTimeMessage;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
// Not sure this needs to be a connection message
class PositionReportMessage extends ConnectionOldMessage implements PositionTimeMessage {

    private final PositionTime positionTime;

    /**
     * @param messageType
     */
    public PositionReportMessage(double lat, double lon, long time) {
        this(PositionTime.create(lat, lon, time));
    }

    public PositionReportMessage(PositionTime position) {
        super(MessageType.POSITION_REPORT);
        this.positionTime = requireNonNull(position);
    }

    public PositionReportMessage(TextMessageReader pr) throws IOException {
        super(MessageType.POSITION_REPORT, pr);
        this.positionTime = PositionTime.create(pr.takeDouble(), pr.takeDouble(), pr.takeLong());
    }

    /**
     * @return the clientId
     */
    public PositionTime getPositionTime() {
        return positionTime;
    }

    /** {@inheritDoc} */
    @Override
    protected void write0(TextMessageWriter w) {
        w.writeDouble(positionTime.getLatitude());
        w.writeDouble(positionTime.getLongitude());
        w.writeLong(positionTime.getTime());
    }
}
