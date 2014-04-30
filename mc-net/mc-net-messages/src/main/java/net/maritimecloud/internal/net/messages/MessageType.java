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

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.internal.messages.TransportMessage;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastAck;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastDeliver;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastListen;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastListenAck;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastSend;
import net.maritimecloud.internal.net.messages.c2c.broadcast.BroadcastSendAck;
import net.maritimecloud.internal.net.messages.c2c.service.InvokeService;
import net.maritimecloud.internal.net.messages.c2c.service.InvokeServiceResult;
import net.maritimecloud.internal.net.messages.s2c.service.FindService;
import net.maritimecloud.internal.net.messages.s2c.service.FindServiceResult;
import net.maritimecloud.internal.net.messages.s2c.service.RegisterService;
import net.maritimecloud.internal.net.messages.s2c.service.RegisterServiceResult;
import net.maritimecloud.messages.Connected;
import net.maritimecloud.messages.Hello;
import net.maritimecloud.messages.PositionReport;
import net.maritimecloud.messages.Welcome;

/**
 * The type of messages that can be sent around in the system.
 *
 * @author Kasper Nielsen
 */
public enum MessageType {
    /* ***************** Auxiliary messages ******** */
    // 0 - 9 : lifecycle, connect/reconnect/disconnect.. keep/alive

    /** This is the first message sent by the server to client. Whenever a Websocket connection has been created. */
    WELCOME(1, Welcome.class, Welcome.PARSER), // 1. message from server 2 client

    /** This is the first message from the client to server. Contains an optional reconnect token. */
    HELLO(2, Hello.class, Hello.PARSER), // 1. message from client 2 server

    /** The final handshake massage from the server, contains the connection id */
    CONNECTED(3, Connected.class, Connected.PARSER), // 2. message from server 2 client

    /** A keep alive message sent periodically. Contains current position/time. */
    POSITION_REPORT(9, PositionReport.class, PositionReport.PARSER),

    // Channel Switched + men er jo naesten det samme som reconnect
    // nej lige saa snart man er connected, starter man med at sende beskeder der
    // Client maa saa vente til den har receivet faerdigt paa den anden hvorefter den
    // lukker den gamle kanal
    // Man kunne ogsaa receive beskeder over begge kanaller.
    // Hvis de har et fortloebende id kan man jo bare smide dublikater vaek

    /* ******************** Communication client<->server ******************* */

    /** Registers a service with server. (client->server) */
    REGISTER_SERVICE(100, RegisterService.class), // throws ServiceRegisterException
    REGISTER_SERVICE_RESULT(101, RegisterServiceResult.class), // just an ack of the service???

    // servicen der skal unregistreres
    UNREGISTER_SERVICE(110, RegisterService.class), //
    UNREGISTER_SERVICE_ACK(111, RegisterServiceResult.class), // throws ServiceUnregisterException

    FIND_SERVICE(120, FindService.class), //
    FIND_SERVICE_ACK(121, FindServiceResult.class), // throws ServiceFindException

    /* Broadcast */

    /** Broadcasts a message (client->server). */
    BROADCAST_SEND(150, BroadcastSend.class), // client->server

    /** Acknowledgment of broadcast message (server->client). */
    BROADCAST_SEND_ACK(151, BroadcastSendAck.class),

    /** Relay of broadcast from server (server->client). */
    BROADCAST_DELIVER(152, BroadcastDeliver.class),

    /** Acknowledgment of successful broadcast for each client (server->client). */
    BROADCAST_DELIVER_ACK(153, BroadcastAck.class),

    /** Registers a service with server. (client->server) */
    BROADCAST_LISTEN(160, BroadcastListen.class), // throws ServiceRegisterException
    BROADCAST_LISTEN_RESULT(161, BroadcastListenAck.class), // just an ack of the service???

    /** The standard error message sent for an invalid request from the client */
    // REQUEST_ERROR(199, ServerRequestError.class), // <- requestId, int error_code, String message

    /* ******************** Communication client<->client ******************* */

    /* Service invocation */
    /** Invokes a service. */
    SERVICE_INVOKE(200, InvokeService.class), //

    /** The successful result of invoking a service. */
    SERVICE_INVOKE_RESULT(201, InvokeServiceResult.class); //

    /** Invoking a service failed. */
    // SERVICE_INVOKE_ERROR(255, ServerRequestError.class);// indeholder lidt additional info taenker jeg

    final Class<? extends TransportMessage> cl;

    final int type;

    final MessageParser<? extends TransportMessage> p;

    MessageType(int type, Class<? extends TransportMessage> cl) {
        this(type, cl, null);
    }

    MessageType(int type, Class<? extends TransportMessage> cl, MessageParser<? extends TransportMessage> p) {
        if (type < 1 || type > 255) {
            throw new IllegalArgumentException("type must be 1>= type <=255");
        }
        this.type = type;
        this.cl = requireNonNull(cl);
        this.p = p;
    }

    public static Class<? extends TransportMessage> getType(int type) {
        return HelperHolder.TYPES[type].cl;
    }

    public static MessageParser<? extends TransportMessage> getParser(int type) {
        return HelperHolder.TYPES[type].p;
    }

    public static int getType(Class<? extends TransportMessage> c) {
        return HelperHolder.map.get(c).type;
    }

    public static MessageParser<? extends TransportMessage> getParser(Class<? extends TransportMessage> c) {
        return HelperHolder.map.get(c).p;
    }

    /** A little initialization-on-demand holder idiom helper class */
    private static class HelperHolder {
        static MessageType[] TYPES;

        static final Map<Class<? extends TransportMessage>, MessageType> map = new HashMap<>();
        static {
            TreeMap<Integer, MessageType> m = new TreeMap<>();
            for (MessageType mt : MessageType.values()) {
                m.put(mt.type, mt);
                map.put(mt.cl, mt);
            }
            TYPES = new MessageType[m.lastKey() + 1];
            for (Entry<Integer, MessageType> e : m.entrySet()) {
                TYPES[e.getKey()] = e.getValue();
            }
        }
    }
}
