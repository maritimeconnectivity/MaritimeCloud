package net.maritimecloud.internal.net.client.endpoint;

import java.io.IOException;

import net.maritimecloud.core.message.MessageSerializable;
import net.maritimecloud.core.message.MessageWriter;
import net.maritimecloud.net.endpoint.EndpointInvocationFuture;
import net.maritimecloud.net.endpoint.EndpointInvocator;
import net.maritimecloud.net.endpoint.EndpointLocal;

public final class HelloWorldEndpoint extends EndpointLocal {

    public static final String NAME = "HelloWorldEndpoint";

    public HelloWorldEndpoint(EndpointInvocator ei) {
        super(ei);
    }

    public EndpointInvocationFuture<String> helloWorld() {
        return invoke("HelloWorldEndpoint.helloWorld", new HelloWorld());
    }

    static final class HelloWorld implements MessageSerializable {

        /** {@inheritDoc} */
        @Override
        public void writeTo(MessageWriter w) throws IOException {}
    }
}
