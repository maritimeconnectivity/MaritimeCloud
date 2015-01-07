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
package net.maritimecloud.mms.tests.service;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.mms.stubs.AbstractHelloWorldEndpoint;
import net.maritimecloud.mms.tests.AbstractNetworkTest;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.net.mms.MmsClient;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractServiceTest extends AbstractNetworkTest {

    public MmsClient registerService(MmsClient pnc, String reply) throws Exception {
        pnc.endpointRegister(new AbstractHelloWorldEndpoint() {
            @Override
            protected String hello(MessageHeader context) {
                return "hello";
            }
        }).awaitRegistered(5, TimeUnit.SECONDS);
        return pnc;
    }

}
