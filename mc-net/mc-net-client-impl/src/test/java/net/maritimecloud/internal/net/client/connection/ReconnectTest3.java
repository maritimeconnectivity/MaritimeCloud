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
package net.maritimecloud.internal.net.client.connection;

import net.maritimecloud.internal.net.client.AbstractClientConnectionTest;
import net.maritimecloud.internal.net.messages.Connected;
import net.maritimecloud.internal.net.messages.Hello;
import net.maritimecloud.net.MaritimeCloudClient;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class ReconnectTest3 extends AbstractClientConnectionTest {

    @Test
    public void disConnectWhileConnecting() throws Exception {
        @SuppressWarnings("unused")
        MaritimeCloudClient c = create();
        t.take(Hello.class);
        t.close();
        // we will try to reconnect anyways
        t.take(Hello.class);
        t.send(new Connected().setConnectionId("ABC").setLastReceivedMessageId(0L));

    }
}
