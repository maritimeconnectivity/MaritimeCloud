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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.mms.stubs.HelloWorldEndpoint;
import net.maritimecloud.net.mms.MmsClient;

import org.junit.Test;


/**
 *
 * @author Kasper Nielsen
 */
public class ServiceFindTest extends AbstractServiceTest {

    /**
     * Tests that one ship cannot find it self.
     *
     * @throws Exception
     */
    @Test
    public void findNearest() throws Exception {
        MmsClient s = registerService(newClient(), "foo123");
        MmsClient c = newClient();
        HelloWorldEndpoint end = c.endpointLocate(HelloWorldEndpoint.class).findNearest().get(6, TimeUnit.SECONDS);
        assertEquals(s.getClientId(), end.getRemoteId());
    }

    @Test
    public void findNearestOutOf2() throws Exception {
        MmsClient s1 = registerService(newClient(1, 1), "A");
        MmsClient s2 = registerService(newClient(5, 5), "B");
        HelloWorldEndpoint e;

        e = newClient(2, 2).endpointLocate(HelloWorldEndpoint.class).findNearest().get(6, TimeUnit.SECONDS);
        assertEquals(s1.getClientId(), e.getRemoteId());

        e = newClient(3, 4).endpointLocate(HelloWorldEndpoint.class).findNearest().get(6, TimeUnit.SECONDS);
        assertEquals(s2.getClientId(), e.getRemoteId());

        e = newClient(4, 4).endpointLocate(HelloWorldEndpoint.class).findNearest().get(6, TimeUnit.SECONDS);
        assertEquals(s2.getClientId(), e.getRemoteId());
    }


    @Test
    public void findNearestOfMany() throws Exception {
        Map<Integer, MmsClient> m = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            m.put(i, registerService(newClient(i, i), "A" + i));
        }

        List<HelloWorldEndpoint> e;

        e = newClient(4.4, 4.4).endpointLocate(HelloWorldEndpoint.class).findAll(1).get(6, TimeUnit.SECONDS);
        assertEquals(1, e.size());
        assertEquals(e.get(0).getRemoteId(), m.get(4).getClientId());

        e = newClient(5.4, 5.4).endpointLocate(HelloWorldEndpoint.class).findAll(3).get(6, TimeUnit.SECONDS);
        assertEquals(3, e.size());
        assertEquals(e.get(0).getRemoteId(), m.get(5).getClientId());
        assertEquals(e.get(1).getRemoteId(), m.get(6).getClientId());
        assertEquals(e.get(2).getRemoteId(), m.get(4).getClientId());
    }

    /**
     * Tests that one ship cannot find it self as a service.
     *
     * @throws Exception
     */
    @Test
    public void cannotFindSelf() throws Exception {
        MmsClient s = registerService(newClient(), "foo123");
        assertNull(s.endpointLocate(HelloWorldEndpoint.class).findNearest().get(6, TimeUnit.SECONDS));
    }

    // @Test
    // public void foo() {
    // System.out.println("XXXXXXXXXXXXXXX " + Position.create(4.4, 4.4).geodesicDistanceTo(Position.create(4, 4)));
    // System.out.println("XXXXXXXXXXXXXXX " + Position.create(4.4, 4.4).geodesicDistanceTo(Position.create(5, 5)));
    // }
    //
    @Test
    public void findWithMaxDistance() throws Exception {
        Map<Integer, MmsClient> m = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            m.put(i, registerService(newClient(i, i), "A" + i));
        }

        List<HelloWorldEndpoint> e;

        e = newClient(4.4, 4.4).endpointLocate(HelloWorldEndpoint.class).withinDistanceOf(62678).findAll(2)
                .get(6, TimeUnit.SECONDS);

        assertEquals(0, e.size());
        e = newClient(4.4, 4.4).endpointLocate(HelloWorldEndpoint.class).withinDistanceOf(62679).findAll(2)
                .get(6000, TimeUnit.SECONDS);
        assertEquals(1, e.size());
        assertEquals(e.get(0).getRemoteId(), m.get(4).getClientId());
    }
}
