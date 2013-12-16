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
package net.maritimecloud.internal.net.server.store;

/**
 * Stores messages, in case the server goes down.
 * 
 * @author Kasper Nielsen
 */
public class MessageStore {

    public void storeReceived(String id, String connectId, String transportId, long receiveTime, String message) {

    }

    public void storeSend(String id, String connectId, String transportId, long sendTime, String message) {

    }

    static class StoredMessage {
        String id;

        String conenctId;

        String transportId;

        long receiveTime;

        String message;
    }
}
