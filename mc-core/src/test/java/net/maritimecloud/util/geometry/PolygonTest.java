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
package net.maritimecloud.util.geometry;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class PolygonTest extends AbstractAreaTest {
    static Polygon P = Polygon.create(P1, P3, P9, P7, P1);


    @Test
    public void contains() {


        // ThreadLocalRandom r = ThreadLocalRandom.current();
        // int count = 0;
        // for (int i = 0; i < 100000; i++) {
        // Position p = Position.create(r.nextDouble(-20, 20), r.nextDouble(-20, 20));
        // if (P.contains(p)) {
        // count++;
        // }
        // }
        // System.out.println(count);

        // assertTrue(P.contains(Position.create(9.9999999, -10.0)));

        // for (Position p : PALL) {
        // assertTrue(P.contains(p));
        // }
        // assertFalse(P.contains(Position.create(10.0001, -10)));
        // assertFalse(P.contains(Position.create(11, -10)));
    }
}
