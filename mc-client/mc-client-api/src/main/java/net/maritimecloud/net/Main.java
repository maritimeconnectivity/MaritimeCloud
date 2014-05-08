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

import net.maritimecloud.net.broadcast.BroadcastFuture;
import net.maritimecloud.net.broadcast.BroadcastMessageReceived;
import net.maritimecloud.net.broadcast.BroadcastSendOptions;
import net.maritimecloud.util.function.Consumer;

/**
 *
 * @author Kasper Nielsen
 */
public class Main {


    public static void main(String[] args) {
        MaritimeCloudClientConfiguration conf = MaritimeCloudClientConfiguration.create("mmsi://123");
        MaritimeCloudClient cc = conf.build();


        BroadcastSendOptions options = new BroadcastSendOptions();
        options.setReceiverAckEnabled(true);
        options.setBroadcastRadius(10000);
        BroadcastFuture f = cc.broadcast(null, options);


        f.onReceived(new Consumer<BroadcastMessageReceived>() {
            public void accept(BroadcastMessageReceived t) {
                System.out.println("Received by " + t.getId());
            }
        });
    }
}
