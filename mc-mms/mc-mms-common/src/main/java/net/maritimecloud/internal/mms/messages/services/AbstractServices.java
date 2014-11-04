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

import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.MessageHeader;

public abstract class AbstractServices implements EndpointImplementation {

    protected abstract List<String> locate(MessageHeader header, String endpointName, Integer meters, Integer max);

    protected abstract void registerEndpoint(MessageHeader header, String endpointName);

    protected abstract void unregisterEndpoint(MessageHeader header, String endpointName);

    protected abstract void subscribe(MessageHeader header, List<String> name, net.maritimecloud.util.geometry.Area area);

    /** {@inheritDoc} */
    @Override
    public final void invoke(String name, MessageHeader header, MessageReader reader, ValueWriter writer)
            throws IOException {
        if (name.equals("locate")) {
            String endpointName_ = reader.readText(1, "endpointName", null);
            Integer meters_ = reader.readInt(2, "meters", null);
            Integer max_ = reader.readInt(3, "max", null);
            List<String> result = locate(header, endpointName_, meters_, max_);
            writer.writeList(result, ValueSerializer.TEXT);
            return;
        }
        if (name.equals("registerEndpoint")) {
            String endpointName_ = reader.readText(1, "endpointName", null);
            registerEndpoint(header, endpointName_);
            return;
        }
        if (name.equals("unregisterEndpoint")) {
            String endpointName_ = reader.readText(1, "endpointName", null);
            unregisterEndpoint(header, endpointName_);
            return;
        }
        if (name.equals("subscribe")) {
            List<String> name_ = MessageHelper.readList(1, "name", reader, ValueSerializer.TEXT);
            net.maritimecloud.util.geometry.Area area_ = reader.readMessage(2, "area",
                    net.maritimecloud.util.geometry.Area.SERIALIZER);
            subscribe(header, name_, area_);
            return;
        }
        throw new UnsupportedOperationException("Unknown method '" + name + "'");
    }

    public final String getEndpointName() {
        return "Services";
    }
}
