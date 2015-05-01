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
package net.maritimecloud.mms.server.connection.clientnew;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;

/**
 *
 * @author Kasper Nielsen
 */
public interface SessionListener {

    /**
     *
     * <p>
     * The reason it is marked acked and we do not supply some kind og completion token is that messages cannot be acked
     * in a random order. Before message x can be acked, message x-1 must already have been acked.
     *
     * @param session
     * @param message
     */
    // is under readlock, is invoked in order one at a time
    void onMessage(Session session, MmsMessage message);
}
