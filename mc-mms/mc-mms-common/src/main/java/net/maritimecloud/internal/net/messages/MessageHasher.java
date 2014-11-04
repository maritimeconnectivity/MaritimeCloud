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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import net.maritimecloud.internal.security.SecurityTools;
import net.maritimecloud.message.Message;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

/**
 *
 * @author Kasper Nielsen
 */
public class MessageHasher {
    static Binary binaryOf(Broadcast br) {
        Binary b = Binary.EMPTY;
        b = b.concat(from(br.getBroadcastType(), false, "broadcastType"));
        b = b.concat(from(br.getSenderId(), false, "senderId"));
        b = b.concat(from(br.getSenderTimestamp(), false, "senderTimestamp"));
        b = b.concat(from(br.getSenderPosition(), true, "senderPosition"));
        b = b.concat(from(br.getPayload(), true, "payload"));
        return b;
    }

    static Binary binaryOf(BroadcastAck br) {
        Binary b = Binary.EMPTY;
        b = b.concat(from(br.getAckForMessageId(), false, "broadcastType"));
        b = b.concat(from(br.getOriginalSenderId(), false, "senderId"));
        b = b.concat(from(br.getReceiverId(), false, "senderTimestamp"));
        b = b.concat(from(br.getReceiverTimestamp(), true, "senderPosition"));
        return b.concat(from(br.getReceiverPosition(), true, "payload"));
    }

    static Binary binaryOf(MethodInvoke mi) {
        Binary b = Binary.EMPTY;
        b = b.concat(from(mi.getEndpointMethod(), false, "endpointMethod"));
        b = b.concat(from(mi.getSenderId(), false, "senderId"));
        b = b.concat(from(mi.getSenderTimestamp(), false, "senderTimestamp"));
        b = b.concat(from(mi.getSenderPosition(), true, "senderPosition"));
        b = b.concat(from(mi.getReceiverId(), true, "receiverId")); // null=server
        return b.concat(from(mi.getParameters(), true, "parameters")); // true
    }

    static Binary binaryOf(MethodInvokeResult mir) {
        Binary b = Binary.EMPTY;
        b = b.concat(from(mir.getResultForMessageId(), false, "resultForMessageId"));
        b = b.concat(from(mir.getOriginalSenderId(), false, "originalSenderId"));
        b = b.concat(from(mir.getReceiverId(), false, "receiverId"));
        b = b.concat(from(mir.getReceiverTimestamp(), true, "senderPosition"));
        b = b.concat(from(mir.getResult(), true, "receiverId")); // null=server
        return b.concat(from(mir.getFailure(), true, "parameters")); // true
    }

    public static Binary calculateSHA256(Broadcast mi) {
        return binaryOf(mi).sha256();
    }

    public static Binary calculateSHA256(BroadcastAck mi) {
        return binaryOf(mi).sha256();
    }

    public static Binary calculateSHA256(MethodInvoke mi) {
        return binaryOf(mi).sha256();
    }

    public static Binary calculateSHA256(MethodInvokeResult mi) {
        return binaryOf(mi).sha256();
    }

    static Binary checkEmpty(boolean optional, String name) {
        if (optional) {
            return Binary.EMPTY;
        }
        throw new IllegalArgumentException("The field '" + name + "' was not filled out");
    }

    static Binary from(Binary binary, boolean optional, String name) {
        return binary == null ? checkEmpty(optional, name) : binary;
    }

    static Binary from(Message mes, boolean optional, String name) {
        return mes == null ? checkEmpty(optional, name) : Binary.copyFromUtf8(mes.toJSON());
    }

    static Binary from(Position position, boolean optional, String name) {
        return position == null ? checkEmpty(optional, name) : position.toBinary();
    }

    static Binary from(String str, boolean optional, String name) {
        return str == null ? checkEmpty(optional, name) : Binary.copyFromUtf8(str);
    }

    static Binary from(Timestamp timestamp, boolean optional, String name) {
        return timestamp == null ? checkEmpty(optional, name) : timestamp.toBinary();
    }

    static Binary sign(Binary b, PrivateKey key) throws SignatureException {
        Signature dsa = SecurityTools.newSignatureForSigning(key);
        dsa.update(b.toByteArray());
        byte[] signature = dsa.sign();
        return Binary.copyFrom(signature);
    }

    public static Binary sign(Broadcast mi, PrivateKey key) throws SignatureException {
        return sign(binaryOf(mi), key);
    }

    public static Binary sign(BroadcastAck mi, PrivateKey key) throws SignatureException {
        return sign(binaryOf(mi), key);
    }

    public static Binary sign(MethodInvoke mi, PrivateKey key) throws SignatureException {
        return sign(binaryOf(mi), key);
    }

    public static Binary sign(MethodInvokeResult mi, PrivateKey key) throws SignatureException {
        return sign(binaryOf(mi), key);
    }

    static boolean verify(Binary b, PublicKey key) throws SignatureException {
        Signature dsa = SecurityTools.newSignatureForVerify(key);
        return dsa.verify(b.toByteArray());
    }

    public static boolean verify(Broadcast mi, PublicKey key) throws SignatureException {
        return verify(binaryOf(mi), key);
    }

    public static boolean verify(BroadcastAck mi, PublicKey key) throws SignatureException {
        return verify(binaryOf(mi), key);
    }

    public static boolean verify(MethodInvoke mi, PublicKey key) throws SignatureException {
        return verify(binaryOf(mi), key);
    }

    public static boolean verify(MethodInvokeResult mi, PublicKey key) throws SignatureException {
        return verify(binaryOf(mi), key);
    }
}
