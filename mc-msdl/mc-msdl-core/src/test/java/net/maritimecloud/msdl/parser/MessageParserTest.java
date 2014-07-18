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
package net.maritimecloud.msdl.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.ListOrSetType;
import net.maritimecloud.msdl.model.MapType;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.MsdlFile;
import net.maritimecloud.msdl.model.Project;

import org.junit.Test;


/**
 *
 * @author Kasper Nielsen
 */
public class MessageParserTest extends AbstractParserTest {

    MessageDeclaration singleMessage(Project p, String name) {
        assertEquals(1, p.getFiles().size());
        MsdlFile f = p.iterator().next();

        assertEquals(1, f.getMessages().size());

        MessageDeclaration en = f.getMessages().get(0);
        assertEquals(BaseType.MESSAGE, en.getBaseType());
        assertEquals("Foof", en.getName());
        return en;
    }

    @Test
    public void emptyMessage() throws IOException {
        singleFile(e -> {
            MessageDeclaration msg = singleMessage(e, "Foof");
            assertTrue(msg.getFields().isEmpty());
        }, "message Foof {", "}");
    }

    @Test
    public void singleField() throws IOException {
        singleFile(e -> {
            MessageDeclaration msg = singleMessage(e, "Foof");

            assertEquals(1, msg.getFields().size());
            assertField(1, "fi", BaseType.INT, msg.getFields().get(0));
        }, "message Foof {", "1: int fi;", "}");
    }

    @Test
    public void primitiveFields() throws IOException {
        String[] manyFields = new String[] { "message Foof {", //
                "1: int fi;", //
                "3: int64 ifi;", //
                "2: float ffi;", //
                "99: double dfi;", //
                "11: boolean boi;", //
                "23: text sfi;", //

                "30: position pos1;", //
                "31: positiontime post;", //
                "32: varint vari;", //
                "33: decimal dec;", //
                "34: timestamp ts;", //

                "123: binary bfi;" //


                , "}" };

        singleFile(e -> {
            MessageDeclaration msg = singleMessage(e, "Foof");

            assertEquals(12, msg.getFields().size());

            assertField(1, "fi", BaseType.INT, msg.getFields().get(0));
            assertField(3, "ifi", BaseType.INT64, msg.getFields().get(1));
            assertField(2, "ffi", BaseType.FLOAT, msg.getFields().get(2));
            assertField(99, "dfi", BaseType.DOUBLE, msg.getFields().get(3));
            assertField(11, "boi", BaseType.BOOLEAN, msg.getFields().get(4));
            assertField(23, "sfi", BaseType.TEXT, msg.getFields().get(5));
            assertField(30, "pos1", BaseType.POSITION, msg.getFields().get(6));
            assertField(31, "post", BaseType.POSITION_TIME, msg.getFields().get(7));
            assertField(32, "vari", BaseType.VARINT, msg.getFields().get(8));
            assertField(33, "dec", BaseType.DECIMAL, msg.getFields().get(9));
            assertField(34, "ts", BaseType.TIMESTAMP, msg.getFields().get(10));
            assertField(123, "bfi", BaseType.BINARY, msg.getFields().get(11));
        }, manyFields);
    }

    @Test
    public void complexTypes() throws IOException {
        singleFile(e -> {
            MessageDeclaration msg = singleMessage(e, "Foof");
            assertEquals(3, msg.getFields().size());

            assertField(1, "fi", BaseType.LIST, msg.getFields().get(0));
            assertField(3, "ifi", BaseType.SET, msg.getFields().get(1));
            assertField(2, "ffi", BaseType.MAP, msg.getFields().get(2));

            ListOrSetType listType = (ListOrSetType) msg.getFields().get(0).getType();
            assertSame(BaseType.INT64, listType.getElementType().getBaseType());

            ListOrSetType setType = (ListOrSetType) msg.getFields().get(1).getType();
            assertSame(BaseType.TEXT, setType.getElementType().getBaseType());

            MapType mapType = (MapType) msg.getFields().get(2).getType();
            assertSame(BaseType.INT, mapType.getKeyType().getBaseType());
            assertSame(BaseType.FLOAT, mapType.getValueType().getBaseType());
        }, "message Foof {", "1: list<int64> fi;", "3: set<text> ifi;", "2: map<int, float> ffi;", "}");
    }
}
