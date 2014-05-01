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

import java.io.IOException;
import java.io.StringWriter;

import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializers;
import net.maritimecloud.core.message.MessageWriter;

/**
 *
 * @author Kasper Nielsen
 */
public class WTest {

    public static void main(String[] args) throws IOException {
        StringWriter sw = new StringWriter();
        MessageWriter w = MessageSerializers.newJSONFragmentWriter(sw);


        Position p = Position.create(13, 23);
        Circle c = Circle.create(p, 123);

        // Area unionOf2 = Area.createUnion(c, c, c, unionOf);

        // w.writeMessage(1, "pos", c);

        System.out.println(MessageSerializers.toJSONString("circle", c));

        w.writeMessage(1, "pos", c);

        String pm = sw.toString();
        System.out.println(pm);

        MessageReader r = MessageSerializers.newJSONReader(MessageSerializers.toJSONString("circle", c), false);

        Circle a = r.readMessage(1, "pos", Circle.PARSER);
        Circle b = r.readMessage(1, "pos", Circle.PARSER);

        System.out.println(a);
        System.out.println(b);
    }

}
