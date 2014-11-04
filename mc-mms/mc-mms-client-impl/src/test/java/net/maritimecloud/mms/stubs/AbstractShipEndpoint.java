package net.maritimecloud.mms.stubs;

import java.io.IOException;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.MessageHeader;

public abstract class AbstractShipEndpoint implements EndpointImplementation {

    protected abstract void hello(MessageHeader context, Long foo, String fff);

    protected abstract Long fff(MessageHeader context);

    public final void invoke(String name, MessageHeader context, MessageReader reader, ValueWriter writer)
            throws IOException {
        if (name.equals("hello")) {
            Long foo = reader.readInt64(1, "foo", null);
            ;
            String fff = reader.readText(2, "fff", null);
            ;
            hello(context, foo, fff);
            return;
        }
        if (name.equals("fff")) {
            Long result = fff(context);
            writer.writeInt64(result);
            return;
        }
        throw new UnsupportedOperationException("Unknown method '" + name + "'");
    }

    public final String getEndpointName() {
        return "ShipEndpoint";
    }
}
