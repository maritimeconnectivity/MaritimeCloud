package net.maritimecloud.mms.tck.stubs;

import java.io.IOException;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.MessageHeader;

public abstract class AbstractTestEndpoint implements EndpointImplementation {

    protected abstract Long hello(MessageHeader context, Long testId);

    public final void invoke(String name, MessageHeader context, MessageReader reader, ValueWriter writer)
            throws IOException {
        if (name.equals("hello")) {
            Long testId = reader.readInt64(1, "testId", null);
            ;
            Long result = hello(context, testId);
            writer.writeInt64(result);
            return;
        }
        throw new UnsupportedOperationException("Unknown method '" + name + "'");
    }

    public final String getEndpointName() {
        return "TestEndpoint";
    }
}
