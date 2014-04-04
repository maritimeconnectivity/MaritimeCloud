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
import net.maritimecloud.core.id.ServerId;
import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.internal.net.messages.TransportMessage;

/**
 * 
 * @author Kasper Nielsen
 */
public class WelcomeMessage extends TransportMessage {

    /** The version of the protocol, must be one. */
    private final int protocolVersion;

    private final MaritimeId serverId;

    private final String serverInfo;

    public WelcomeMessage(TextMessageReader pr) throws IOException {
        this(pr.takeInt(), new ServerId(pr.takeString()), pr.takeString());
    }

    /**
     * @param messageType
     */
    public WelcomeMessage(int protocolVersion, MaritimeId serverId, String serverInfo) {
        super(MessageType.WELCOME);
        this.protocolVersion = 1;
        this.serverId = requireNonNull(serverId);
        this.serverInfo = requireNonNull(serverInfo);
    }

    /**
     * @return the protocolVersion
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * @return the serverId
     */
    public MaritimeId getServerId() {
        return serverId;
    }

    /**
     * @return the serverInfo
     */
    public String getServerInfo() {
        return serverInfo;
    }

    /** {@inheritDoc} */
    @Override
    public void write(TextMessageWriter w) {
        w.writeInt(protocolVersion);
        w.writeString(serverId.toString());
        w.writeString(serverInfo);
    }
}
