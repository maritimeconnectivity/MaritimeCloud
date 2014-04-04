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
package net.maritimecloud.internal.net.messages.s2c.service;

import java.io.IOException;

import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.s2c.ServerResponseMessage;

/**
 * 
 * @author Kasper Nielsen
 */
public class RegisterServiceResult extends ServerResponseMessage {

    // Area
    public RegisterServiceResult(TextMessageReader pr) throws IOException {
        super(MessageType.REGISTER_SERVICE_RESULT, pr);
    }

    /**
     * @param messageType
     */
    public RegisterServiceResult(long id) {
        super(MessageType.REGISTER_SERVICE_RESULT, id);
    }
}
