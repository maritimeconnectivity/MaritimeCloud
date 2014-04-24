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
package net.maritimecloud.internal.message.json;

import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.Position;

/**
 *
 * @author Kasper Nielsen
 */
public class Ttt {

    public static void main(String[] args) {

        // Area.createUnion(null);
        Circle c1 = Circle.create(Position.create(1.23423, 1), 123f);
        Circle c2 = Circle.create(Position.create(1.23423, 1), 123f);

        Area a = c1.unionWith(c2);
        // c.setCenter(Position.create(1.23423, 1));
        // c.setRadius(123f);
        String circle = a.toJSON();
        System.out.println(circle);
        Area cc = MessageSerializers.readFromJSON(Area.PARSER, circle);
        System.out.println("DDD");
        System.out.println(cc.toJSON());
        System.out.println(a.equals(cc));


    }
}
