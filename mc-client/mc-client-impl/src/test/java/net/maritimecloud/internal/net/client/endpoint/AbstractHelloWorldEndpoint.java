package net.maritimecloud.internal.net.client.endpoint;

import java.io.IOException;

import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.net.broadcast.MessageContext;
import net.maritimecloud.net.endpoint.EndpointImplementation;

public abstract class AbstractHelloWorldEndpoint implements EndpointImplementation {

    protected abstract String helloWorld(MessageContext context);

    public final Object invoke(String name, MessageContext context, MessageReader reader) throws IOException {
        if (name.equals("helloWorld")) {
            return helloWorld(context);
        }
        throw new UnsupportedOperationException("Unknown method '" + name + "'");
    }

    public final String getEndpointName() {
        return "HelloWorldEndpoint";
    }
}
