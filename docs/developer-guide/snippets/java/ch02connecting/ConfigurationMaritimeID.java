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

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionReaderSimulator;
import net.maritimecloud.util.geometry.PositionTime;
import net.maritimecloud.util.units.SpeedUnit;

/**
 *
 * @author Kasper Nielsen
 */
public class ConfigurationMaritimeID {
// tag::code[]
public static void main(String[] args) {
  MmsClientConfiguration conf = MmsClientConfiguration.create();
  MaritimeId id = MaritimeId.create("mmsi:123456789");
  conf.setId(id);
}
// end::code[]

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
