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
package net.maritimecloud.mms.server;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;

/**
 *
 * @author Kasper Nielsen
 */
public class TestIt {

    public static void main(String[] args) throws InterruptedException {
        MmsClientConfiguration conf = MmsClientConfiguration.create("mmsi:65487");
        conf.setHost("wss://localhost:9999");
        MmsClient cc = conf.build();
        System.out.println(cc);
        cc.connection().awaitConnected(5, TimeUnit.SECONDS);
        System.out.println(cc.connection().isConnected());
        cc.shutdown();
    }
}
