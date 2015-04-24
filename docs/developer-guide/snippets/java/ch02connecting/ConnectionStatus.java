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
package ch02connecting;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.net.mms.MmsConnection;
import net.maritimecloud.net.mms.MmsConnectionClosingCode;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionReaderSimulator;
import net.maritimecloud.util.geometry.PositionTime;
import net.maritimecloud.util.units.SpeedUnit;

/**
 *
 * @author Kasper Nielsen
 */
@SuppressWarnings("null")
public class ConnectionStatus {
public static void main(String[] args) throws Exception {
  MmsClient client = null;
//tag::connectionStatus[]
System.out.println("Is connected = " + client.connection().isConnected());
//end::connectionStatus[]

//tag::manuallyConnect[]
client.connection().disable();
// Now lets reconnect again
client.connection().enable();
//end::manuallyConnect[]


//tag::await[]
client.connection().disable();
//Now lets wait up to ten seconds for a fully disconnect
client.connection().awaitDisconnected(10, TimeUnit.SECONDS);
//end::await[]


//tag::listener[]
MmsClientConfiguration conf = MmsClientConfiguration.create();
conf.addListener(new MmsConnection.Listener() {
  public void connecting(URI host) {
    System.out.println("Trying to connect to " + host);
  }

  public void connected(URI host) {
    System.out.println("Connected");
  }

  public void disconnected(MmsConnectionClosingCode closeReason) {
    System.out.println("Disconnected " + closeReason);
  }
});
//end::listener[]
}

public static void setProperties() {
MmsClientConfiguration conf = MmsClientConfiguration.create();
//tag::properties[]
conf.properties().setName("MobyDick");
conf.properties().setOrganization("Danish Maritime Authority");
conf.properties().setDescription("A fictiv vessel used in the Mona Lisa 2 project");
//end::properties[]
}

public static void setStaticPosition() {
MmsClientConfiguration conf = MmsClientConfiguration.create();
//tag::staticReader[]
PositionReader staticReader = new PositionReader() {
    public PositionTime getCurrentPosition() {
        return PositionTime.create(10, 20, System.currentTimeMillis());
    }
};
conf.setPositionReader(staticReader);
//end::staticReader[]
}

public static void positionSimulator() {
MmsClientConfiguration conf = MmsClientConfiguration.create();
//tag::simulatedReader[]
PositionReader simulatedReader = new PositionReaderSimulator().setSpeedVariable(10, 30, SpeedUnit.KNOTS).forArea(Circle.create(55, 55, 1));

conf.setPositionReader(simulatedReader);
//end::simulatedReader[]
}
}
