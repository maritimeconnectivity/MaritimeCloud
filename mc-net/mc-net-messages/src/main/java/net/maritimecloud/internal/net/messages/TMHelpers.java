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
package net.maritimecloud.internal.net.messages;

import java.io.IOException;

import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.messages.Connected;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 *
 * @author Kasper Nielsen
 */
public class TMHelpers {


    public static void main(String[] args) {
        Connected c = new Connected();
        c.setConnectionId("fff");
        String msg = c.toText().replace("3:", "");
        c = MessageSerializers.readFromJSON(Connected.PARSER, msg);
        System.out.println(c.toText());
    }

    public static TransportMessage parseMessage(String msg) throws IOException {
        int io = msg.indexOf(':');
        String t = msg.substring(0, io);
        msg = msg.substring(io + 1);
        int type = Integer.parseInt(t);// pr.takeInt();
        MessageParser<? extends TransportMessage> p = MessageType.getParser(type);
        if (p != null) {
            // System.out.println("Got " + msg);
            TransportMessage tm = MessageSerializers.readFromJSON(p, msg);
            return tm;
        }
        TextMessageReader pr = new TextMessageReader(msg);
        try {
            Class<? extends TransportMessage> cl = MessageType.getType(type);
            TransportMessage message = cl.getConstructor(TextMessageReader.class).newInstance(pr);
            // message.rawMessage = msg;// for debugging purposes
            return message;
        } catch (ReflectiveOperationException e) {
            throw new IOException(e);
        }
    }

    private static String persist(Object o) {
        ObjectMapper om = new ObjectMapper();
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            return om.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not be persisted", e);
        }
    }

    public static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static String persistAndEscape(Object o) {
        return escape(persist(o));
    }
}
