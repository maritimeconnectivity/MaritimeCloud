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
package net.maritimecloud.internal.mms.messages.services;

import java.io.IOException;
import java.util.List;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.MessageHeader;

public abstract class AbstractClients implements EndpointImplementation {

    protected abstract List<ClientInfo> getAllClient(MessageHeader header);

    protected abstract Integer getConnectionCount(MessageHeader header);

    /** {@inheritDoc} */
    @Override
    public final void invoke(String name, MessageHeader header, MessageReader reader, ValueWriter writer) throws IOException {
        if (name.equals("getAllClient")) {
            List<ClientInfo> result = getAllClient(header);
            writer.writeList(result, ClientInfo.SERIALIZER);
            return;
        }
        if (name.equals("getConnectionCount")) {
            Integer result = getConnectionCount(header);
            writer.writeInt(result);
            return;
        }
        throw new UnsupportedOperationException("Unknown method '" + name + "'");
    }

    public final String getEndpointName() {
        return "Clients";
    }
}
