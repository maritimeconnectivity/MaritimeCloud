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
package net.maritimecloud.internal.mms.messages.spi;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.maritimecloud.internal.mms.messages.Close;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.PositionReport;
import net.maritimecloud.internal.mms.messages.Welcome;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.net.messages.BroadcastAck;
import net.maritimecloud.internal.net.messages.MethodInvoke;
import net.maritimecloud.internal.net.messages.MethodInvokeResult;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;

/**
 * The type of messages that can be sent around in the system.
 *
 * @author Kasper Nielsen
 */
enum MmsMessageType {
    /* ***************** Auxiliary messages ******** */
    // 0 - 7 : lifecycle, connect/reconnect/disconnect.. keep/alive

    /** This is the first message sent by the server to client. Whenever a Websocket connection has been created. */
    WELCOME(1, Welcome.class, Welcome.SERIALIZER), // 1. message from server 2 client

    /** This is the first message from the client to server. Contains an optional reconnect token. */
    HELLO(2, Hello.class, Hello.SERIALIZER), // 1. message from client 2 server

    /** The final handshake massage from the server, contains the connection id */
    CONNECTED(3, Connected.class, Connected.SERIALIZER), // 2. message from server 2 client


    /** The final handshake massage from the server, contains the connection id */
    CLOSE(4, Close.class, Close.SERIALIZER), // 2. message from server 2 client


    /** A keep alive message sent periodically. Contains current position/time. */
    POSITION_REPORT(8, PositionReport.class, PositionReport.SERIALIZER),

    // Channel Switched + men er jo naesten det samme som reconnect
    // nej lige saa snart man er connected, starter man med at sende beskeder der
    // Client maa saa vente til den har receivet faerdigt paa den anden hvorefter den
    // lukker den gamle kanal
    // Man kunne ogsaa receive beskeder over begge kanaller.
    // Hvis de har et fortloebende id kan man jo bare smide dublikater vaek

    /* ******************** Communication client<->server ******************* */

    /** Invokes a remote service. */
    METHOD_INVOKE(10, MethodInvoke.class, MethodInvoke.SERIALIZER), //

    /** The result of invoking a remote service. */
    METHOD_INVOKE_RESULT(11, MethodInvokeResult.class, MethodInvokeResult.SERIALIZER), //

    /** Broadcasts a message. */
    BROADCAST(12, Broadcast.class, Broadcast.SERIALIZER), // client->server

    /** Acknowledgment of a received broadcast by a remote client. */
    BROADCAST_ACK(13, BroadcastAck.class, BroadcastAck.SERIALIZER),

    /** The standard error message sent for an invalid request from the client */
    // REQUEST_ERROR(15, ServerRequestError.class), // <- requestId, int error_code, String message
    ;
    final Class<? extends Message> cl;

    final int type;

    final MessageSerializer<? extends Message> p;

    MmsMessageType(int type, Class<? extends Message> cl, MessageSerializer<? extends Message> p) {
        if (type < 1 || type > 15) {
            throw new IllegalArgumentException("type must be 1>= type <=15");
        }
        this.type = type;
        this.cl = requireNonNull(cl);
        this.p = p;
    }

    public static MessageSerializer<? extends Message> getParser(int type) {
        return HelperHolder.TYPES[type].p;
    }

    public static MmsMessageType getTypeOf(Class<? extends Message> c) {
        requireNonNull(c);
        return HelperHolder.MAP.get(c);
    }

    public boolean isConnectionMessage() {
        return type > 7;
    }

    /** A little initialization-on-demand holder idiom helper class */
    private static class HelperHolder {
        static MmsMessageType[] TYPES;

        static final Map<Class<? extends Message>, MmsMessageType> MAP = new HashMap<>();
        static {
            TreeMap<Integer, MmsMessageType> m = new TreeMap<>();
            for (MmsMessageType mt : MmsMessageType.values()) {
                m.put(mt.type, mt);
                MAP.put(mt.cl, mt);
            }
            TYPES = new MmsMessageType[m.lastKey() + 1];
            for (Entry<Integer, MmsMessageType> e : m.entrySet()) {
                TYPES[e.getKey()] = e.getValue();
            }
        }
    }
}
