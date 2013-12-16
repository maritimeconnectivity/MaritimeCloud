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
package net.maritimecloud.tests.service;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.tests.AbstractNetworkTest;
import test.stubs.HelloService;

/**
 * 
 * @author Kasper Nielsen
 */
public abstract class AbstractServiceTest extends AbstractNetworkTest {

    public MaritimeCloudClient registerService(MaritimeCloudClient pnc, String reply) throws Exception {
        pnc.serviceRegister(HelloService.GET_NAME, HelloService.create(reply)).awaitRegistered(5, TimeUnit.SECONDS);
        return pnc;
    }

}
