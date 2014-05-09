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
package test;

import imo.route.intendedroute.IntendedRoute;
import imo.route.intendedroute.IntendedRouteService;
import imo.route.intendedroute.IntendedRouteService.IntendedRouteBroadcast;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.net.MaritimeCloudClientConfiguration;
import net.maritimecloud.net.broadcast.BroadcastFuture;
import net.maritimecloud.net.broadcast.BroadcastMessageReceived;
import net.maritimecloud.net.broadcast.BroadcastSendOptions;
import net.maritimecloud.util.function.Consumer;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public class Main {

    public static void madin(String[] args) throws ClassNotFoundException {
        System.out.println(new IntendedRouteService.IntendedRouteBroadcast().getClass().getName());
        Class.forName("imo.route.intendedroute.IntendedRouteService$IntendedRouteBroadcast");
    }

    public static void main(String[] args) throws InterruptedException {
        MaritimeCloudClientConfiguration conf = MaritimeCloudClientConfiguration.create("mmsi://1243");
        conf.setPositionReader(PositionReader.fixedPosition(PositionTime.create(1, 1, 1000)));
        MaritimeCloudClient cc = conf.build();

        BroadcastSendOptions options = new BroadcastSendOptions();
        options.setReceiverAckEnabled(true);
        options.setBroadcastRadius(10000);
        IntendedRouteBroadcast irb = new IntendedRouteBroadcast();

        IntendedRoute ir = new IntendedRoute();
        irb.setIndendedRoute(ir);
        ir.setActiveWaypointIndex(2223);


        BroadcastFuture f = cc.broadcast(irb, options);

        f.onReceived(new Consumer<BroadcastMessageReceived>() {
            public void accept(BroadcastMessageReceived t) {
                System.out.println("Received by " + t.getId());
            }
        });

        Thread.sleep(10000);
    }
}
