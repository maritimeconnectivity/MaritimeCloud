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

import static java.util.Objects.requireNonNull;
import net.maritimecloud.mms.server.connection.transport.ServerTransport;

/**
 * For example, because of a failure, of if the client is switching from 3g to XX.
 *
 * @author Kasper Nielsen
 */
public class InternalStateDisconnected extends InternalState {
    final Session session;

    InternalStateDisconnected(ServerTransport transport, Session session) {
        super(transport);
        this.session = requireNonNull(session);
    }

}
