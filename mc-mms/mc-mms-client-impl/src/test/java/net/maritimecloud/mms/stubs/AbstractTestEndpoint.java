package net.maritimecloud.mms.stubs;

import java.io.IOException;
import java.util.List;

import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.MessageHeader;

public abstract class AbstractTestEndpoint implements EndpointImplementation {

    protected abstract List<Long> invokeIt(MessageHeader context, List<TestMessage> li);

    public final void invoke(String name, MessageHeader context, MessageReader reader, ValueWriter writer)
            throws IOException {
        if (name.equals("invokeIt")) {
            List<TestMessage> li = MessageHelper.readList(1, "li", reader, TestMessage.SERIALIZER);
            ;
            List<Long> result = invokeIt(context, li);
            writer.writeList(result, ValueSerializer.INT64);
            return;
        }
        throw new UnsupportedOperationException("Unknown method '" + name + "'");
    }

    public final String getEndpointName() {
        return "TestEndpoint";
    }
}
