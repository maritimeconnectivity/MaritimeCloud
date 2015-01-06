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
package net.maritimecloud.mms.server.endpoints;

import java.util.Collections;

import net.maritimecloud.internal.net.endpoint.EndpointManager;
import net.maritimecloud.internal.net.messages.MethodInvoke;
import net.maritimecloud.mms.server.connectionold.ServerConnection;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.MessageHeader;

import org.cakeframework.container.Container;
import org.cakeframework.container.lifecycle.InstanceofHandler;

/**
 *
 * @author Kasper Nielsen
 */
public class ServerEndpointManager extends InstanceofHandler<Container, EndpointImplementation, Void> {

    static final String CONNECTION = "CONNECTION";

    private final EndpointManager em = new EndpointManager();

    /** {@inheritDoc} */
    @Override
    public void componentInitialize(Context<Container, EndpointImplementation, Void> context) throws Exception {
        em.endpointRegister(context.getInstance());
    }

    public void invokeLocally(ServerConnection con, MethodInvoke i) {
        em.execute(i, Collections.singletonMap(CONNECTION, con), e -> con.messageSend(e));
    }

    public static ServerConnection connection(MessageHeader header) {
        return (ServerConnection) header.context().get(CONNECTION);
    }
}
