package net.maritimecloud.internal.mms.messages.services;

import java.io.IOException;
import java.util.List;

import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.LocalEndpoint;

public final class Clients extends LocalEndpoint {

    /** The name of the endpoint. */
    public static final String NAME = "Clients";

    public Clients(Invocator ei) {
        super(ei);
    }

    public EndpointInvocationFuture<List<ClientInfo>> getAllClient() {
        GetAllClient arguments = new GetAllClient();
        return invokeRemote("Clients.getAllClient", arguments, GetAllClient.SERIALIZER, ClientInfo.SERIALIZER.listOf());
    }

    public EndpointInvocationFuture<Integer> getConnectionCount() {
        GetConnectionCount arguments = new GetConnectionCount();
        return invokeRemote("Clients.getConnectionCount", arguments, GetConnectionCount.SERIALIZER, ValueSerializer.INT);
    }

    static class GetAllClient implements Message {

        /** The full name of this message. */
        public static final String NAME = "net.maritimecloud.internal.mms.messages.services.GetAllClient";

        /** A message serializer that can read and write instances of this class. */
        public static final MessageSerializer<GetAllClient> SERIALIZER = new GetAllClientSerializer();

        /** {@inheritDoc} */
        @Override
        public GetAllClient immutable() {
            throw new UnsupportedOperationException("method not supported");
        }

        /** Returns a JSON representation of this message */
        public String toJSON() {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    /** A serializer for reading and writing instances of GetAllClient. */
    static class GetAllClientSerializer extends MessageSerializer<GetAllClient> {

        /** {@inheritDoc} */
        @Override
        public GetAllClient read(MessageReader reader) throws IOException {
            return new GetAllClient();
        }

        /** {@inheritDoc} */
        @Override
        public void write(GetAllClient message, MessageWriter writer) throws IOException {}
    }

    static class GetConnectionCount implements Message {

        /** The full name of this message. */
        public static final String NAME = "net.maritimecloud.internal.mms.messages.services.GetConnectionCount";

        /** A message serializer that can read and write instances of this class. */
        public static final MessageSerializer<GetConnectionCount> SERIALIZER = new GetConnectionCountSerializer();

        /** {@inheritDoc} */
        @Override
        public GetConnectionCount immutable() {
            throw new UnsupportedOperationException("method not supported");
        }

        /** Returns a JSON representation of this message */
        public String toJSON() {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    /** A serializer for reading and writing instances of GetConnectionCount. */
    static class GetConnectionCountSerializer extends MessageSerializer<GetConnectionCount> {

        /** {@inheritDoc} */
        @Override
        public GetConnectionCount read(MessageReader reader) throws IOException {
            return new GetConnectionCount();
        }

        /** {@inheritDoc} */
        @Override
        public void write(GetConnectionCount message, MessageWriter writer) throws IOException {}
    }
}
