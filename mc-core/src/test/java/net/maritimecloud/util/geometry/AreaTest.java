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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import net.maritimecloud.message.MessageSerializer;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class AreaTest {

    @Test
    public void circle() {
        for (int i = 0; i < 100; i++) {
            Circle c = Circle.random();
            String fromCircle = MessageSerializer.writeToJSON(c, Circle.SERIALIZER);
            String fromArea = MessageSerializer.writeToJSON(c, Area.SERIALIZER);
            assertNotEquals(fromCircle, fromArea);
            assertEquals(c, Circle.fromJSON(fromCircle));
            assertEquals(c, Area.fromJSON(fromArea));
        }
    }

    @Test
    @Ignore
    public void boundingBox() {
        Circle c = new Circle(Position.create(1, -1), 123);
        String fromCircle = MessageSerializer.writeToJSON(c, Circle.SERIALIZER);
        String fromArea = MessageSerializer.writeToJSON(c, Area.SERIALIZER);
        assertNotEquals(fromCircle, fromArea);

        assertEquals(c, Circle.fromJSON(fromCircle));
        assertEquals(c, Area.fromJSON(fromArea));
    }
}
