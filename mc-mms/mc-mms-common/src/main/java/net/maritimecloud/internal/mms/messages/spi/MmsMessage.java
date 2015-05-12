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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.internal.message.binary.protobuf.ProtobufMessageReader;
import net.maritimecloud.internal.message.binary.protobuf.ProtobufMessageWriter;
import net.maritimecloud.internal.message.text.json.JsonMessageReader;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.net.BroadcastMessage;

/**
 * This class is a bit of mess, will be cleaned up a later point.
 *
 * @author Kasper Nielsen
 */
public class MmsMessage {
    boolean fastAck;

    Long latestReceivedId;

    Message m;

    Long oldMessageId;

    boolean ignoreForNewSession;

    /** Flags if the message has be sent or received */
    boolean inbound;

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

    public MmsMessage setLatestReceivedId(long latestReceivedId) {
        this.latestReceivedId = latestReceivedId;
        return this;
    }

    public boolean ignoreReconnect() {
        return getType().type <= MmsMessageType.POSITION_REPORT.type;
    }

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

    /**
     * Returns a binary representation of the MmsMessage
     *
     * @return a binary representation of the MmsMessage
     */
    public byte[] toBinary() throws IOException {
        MmsMessageType mt = MmsMessageType.getTypeOf(m.getClass());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ProtobufMessageWriter bvw = new ProtobufMessageWriter(baos)) {
            bvw.writeInt(1, null, mt.type);
            if (mt.isConnectionMessage()) {
                bvw.writeInt64(2, null, oldMessageId);
                bvw.writeInt64(3, null, latestReceivedId);
            }
            bvw.writeMessage(4, null, m, MessageHelper.getSerializer(m));
            bvw.flush();
        }
        return baos.toByteArray();
    }

    /**
     * Parses the byte array as an MmsMessage
     *
     * @param msg
     *            the bytes of the message
     * @return the parsed message
     */
    public static MmsMessage parseBinaryMessage(byte[] msg) throws IOException {
        MmsMessage pm = new MmsMessage();
        ByteArrayInputStream bain = new ByteArrayInputStream(msg);
        try (ProtobufMessageReader bmr = new ProtobufMessageReader(bain)) {
            int type = bmr.readInt(1, null);
            if (type > 7) {
                pm.setMessageId(bmr.readInt64(2, null));
                pm.setLatestReceivedId(bmr.readInt64(3, null));
            }
            pm.m = bmr.readMessage(4, null, MmsMessageType.getParser(type));
        }
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

    public boolean isInbound() {
        return inbound;
    }

    public void setInbound(boolean inbound) {
        this.inbound = inbound;
    }
}
