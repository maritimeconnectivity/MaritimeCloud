package net.maritimecloud.mms.stubs;

import java.io.IOException;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.MessageHeader;

public abstract class AbstractHelloWorldEndpoint implements EndpointImplementation {

    protected abstract String hello(MessageHeader context);

    public final void invoke(String name, MessageHeader context, MessageReader reader, ValueWriter writer)
            throws IOException {
        if (name.equals("hello")) {
            String result = hello(context);
            writer.writeText(result);
            return;
        }
        throw new UnsupportedOperationException("Unknown method '" + name + "'");
    }

    public final String getEndpointName() {
        return "HelloWorldEndpoint";
    }
}
