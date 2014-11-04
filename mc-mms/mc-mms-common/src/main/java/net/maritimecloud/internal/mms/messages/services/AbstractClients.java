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
