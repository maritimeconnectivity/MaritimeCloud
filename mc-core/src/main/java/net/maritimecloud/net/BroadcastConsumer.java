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
package net.maritimecloud.net;

/**
 * A callback interface for receiving broadcast messages of a specific type.
 *
 * @author Kasper Nielsen
 */
@FunctionalInterface
public interface BroadcastConsumer<T extends BroadcastMessage> {

    /**
     * Invoked whenever a broadcast message is received.
     *
     * @param header
     *            header information about the broadcast
     * @param broadcast
     *            the broadcast message that was received
     */
    void onMessage(MessageHeader header, T broadcast);
}
