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

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import net.maritimecloud.net.BroadcastMessage;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

/**
 *
 * @author Kasper Nielsen
 */
public class BroadcastWrapper {
    private byte[] contents;

    final BroadcastMessage message;

    Position position;

    Timestamp timestamp;

    BroadcastWrapper(BroadcastMessage message) {
        this.message = message.immutable();
    }

    private void generateContents() {
        String str = message.toJSON();
        contents = str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Binary hash() {
        return null;
    }

    /**
     * @param position
     *            the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Binary sign(PrivateKey key) throws SignatureException {
        Signature dsa = SecurityTools.newSignature(key);

        generateContents();
        dsa.update(contents);

        byte[] signature = dsa.sign();

        return Binary.copyFrom(signature);
    }

    public void verify(PublicKey key) {

    }
}
