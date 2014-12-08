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

import java.io.IOException;
import java.lang.reflect.Field;

import net.maritimecloud.internal.message.text.json.JsonMessageReader;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.net.BroadcastMessage;

/**
 *
 * @author Kasper Nielsen
 */
public class MmsMessage {
    boolean fastAck;

    Long latestReceivedId;

    Message m;

    Long oldMessageId;

    boolean ignoreForNewSession;

    public MmsMessage() {

    }

    public MmsMessage(Message m) {
        this.m = m;
    }

    /**
     * @return the latestReceivedId
     */
    public long getLatestReceivedId() {
        return latestReceivedId;
    }

    /**
     * @return the m
     */
    public Message getM() {
        return m;
    }

    public MmsMessageType getType() {
        return MmsMessageType.getTypeOf(m.getClass());
    }

    public boolean isConnectionMessage() {
        MmsMessageType mt = MmsMessageType.getTypeOf(m.getClass());
        return mt.isConnectionMessage();
    }

    public Message getMessage() {
        return m;
    }

    public <T extends Message> T cast(Class<T> c) {
        return requireNonNull(c.cast(getM()));
    }

    /**
     * @return the oldMessageId
     */
    public long getMessageId() {
        return oldMessageId;
    }

    /**
     * @return the fastAck
     */
    public boolean isFastAck() {
        return fastAck;
    }

    /**
     * @param fastAck
     *            the fastAck to set
     */
    public MmsMessage setFastAck(boolean fastAck) {
        this.fastAck = fastAck;
        return this;
    }

    /**
     * @param latestReceivedId
     *            the latestReceivedId to set
     * @return
     */
    public MmsMessage setLatestReceivedId(long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public boolean ignoreReconnect() {
        return getType().type <= MmsMessageType.POSITION_REPORT.type;
    }

    /**
     * @param m
     *            the m to set
     * @return
     */
    public MmsMessage setM(Message m) {
        this.m = m;
        return this;
    }

    /**
     * @param oldMessageId
     *            the oldMessageId to set
     */
    public MmsMessage setMessageId(long oldMessageId) {
        this.oldMessageId = oldMessageId;
        return this;
    }

    public String toText() {
        StringBuilder sb = new StringBuilder();
        MmsMessageType mt = MmsMessageType.getTypeOf(m.getClass());
        sb.append(mt.type);
        sb.append(":");
        if (mt.isConnectionMessage()) {
            sb.append(oldMessageId);
            sb.append(":");
            sb.append(latestReceivedId);
            sb.append(":");
        }
        sb.append(m.toJSON());
        return sb.toString();
    }

    public static String toText(Message m) {
        return new MmsMessage().setM(m).toText();
    }


    public static MmsMessage parseTextMessage(String msg) {
        MmsMessage pm = new MmsMessage();
        int io = msg.indexOf(':');
        String t = msg.substring(0, io);
        msg = msg.substring(io + 1);
        int type = Integer.parseInt(t);// pr.takeInt();
        if (type > 7) {
            io = msg.indexOf(':');
            String id1 = msg.substring(0, io);
            pm.setMessageId(Long.parseLong(id1));
            msg = msg.substring(io + 1);
            io = msg.indexOf(':');
            String id2 = msg.substring(0, io);
            msg = msg.substring(io + 1);
            pm.setLatestReceivedId(Long.parseLong(id2));
        }
        MessageSerializer<? extends Message> p = MmsMessageType.getParser(type);

        Message tm = MessageSerializer.readFromJSON(p, msg);
        pm.m = tm;
        return pm;
    }

    public static BroadcastMessage tryRead(Broadcast bd) throws ReflectiveOperationException {
        return tryRead(bd.getBroadcastType(), bd.getPayload().toStringUtf8());
    }

    @SuppressWarnings("unchecked")
    public static BroadcastMessage tryRead(String name, String contents) throws ReflectiveOperationException {
        Class<BroadcastMessage> cl = (Class<BroadcastMessage>) Class.forName(name);
        Field field = cl.getField("SERIALIZER");
        MessageSerializer<BroadcastMessage> p = (MessageSerializer<BroadcastMessage>) field.get(null);
        JsonMessageReader r = new JsonMessageReader(contents);
        try {
            return p.read(r);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read message from JSON", e);
        }
    }
}
