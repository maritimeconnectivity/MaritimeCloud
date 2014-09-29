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
package net.maritimecloud.internal.security;

import static java.util.Objects.requireNonNull;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.message.Message;
import net.maritimecloud.net.BroadcastMessage;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

/**
 *
 * @author Kasper Nielsen
 */
public class MessageSigner {

    private final PrivateKey key;

    private Timestamp timestamp;

    private Position position;

    private final MaritimeId id;

    public MessageSigner(MaritimeId id, PrivateKey key) {
        this.key = requireNonNull(key);
        this.id = requireNonNull(id);
    }

    public MessageSigner setTime(Timestamp time) {
        this.timestamp = requireNonNull(time);
        return this;
    }

    MessageDigest setup() {
        Signature s = SecurityTools.newSignature(key);
        // s.update(id.toString().getBytes(StandardCharsets.UTF_8));
        return null;
    }

    public MessageSigner setPosition(Position position) {
        this.position = requireNonNull(position);
        return this;
    }

    public byte[] signBroadcastMessage(BroadcastMessage msg) {
        throw new UnsupportedOperationException();
    }

    public byte[] signServiceInvoke(byte[] serviceInvokeHash, Message msg) {
        throw new UnsupportedOperationException();
    }

    public byte[] signServiceResult(byte[] serviceInvokeHash, Message msg) {
        throw new UnsupportedOperationException();
    }
}
