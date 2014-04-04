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

import net.maritimecloud.msdl.model.FileDeclaration;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.Project;
import net.maritimecloud.msdl.model.type.ListOrSetType;
import net.maritimecloud.msdl.model.type.MSDLBaseType;
import net.maritimecloud.msdl.model.type.MapType;

import org.junit.Test;


/**
 *
 * @author Kasper Nielsen
 */
public class MessageParserTest extends AbstractParserTest {

    MessageDeclaration singleMessage(Project p, String name) {
        assertEquals(1, p.getFiles().size());
        FileDeclaration f = p.getFiles().get(0);

        assertEquals(1, f.getMessages().size());

        MessageDeclaration en = f.getMessages().get(0);
        assertEquals(MSDLBaseType.MESSAGE, en.getBaseType());
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
            assertField(1, "fi", MSDLBaseType.INT32, msg.getFields().get(0));
        }, "message Foof {", "1: int32 fi;", "}");
    }

    @Test
    public void primitiveFields() throws IOException {
        String[] manyFields = new String[] { "message Foof {", "1: int32 fi;", "3: int64 ifi;", "2: float ffi;",
                "99: double dfi;", "11: boolean boi;", "23: string sfi;", "123: binary bfi;", "}" };

        singleFile(e -> {
            MessageDeclaration msg = singleMessage(e, "Foof");

            assertEquals(7, msg.getFields().size());

            assertField(1, "fi", MSDLBaseType.INT32, msg.getFields().get(0));
            assertField(3, "ifi", MSDLBaseType.INT64, msg.getFields().get(1));
            assertField(2, "ffi", MSDLBaseType.FLOAT, msg.getFields().get(2));
            assertField(99, "dfi", MSDLBaseType.DOUBLE, msg.getFields().get(3));
            assertField(11, "boi", MSDLBaseType.BOOL, msg.getFields().get(4));
            assertField(23, "sfi", MSDLBaseType.STRING, msg.getFields().get(5));
            assertField(123, "bfi", MSDLBaseType.BINARY, msg.getFields().get(6));
        }, manyFields);
    }

    @Test
    public void complexTypes() throws IOException {
        singleFile(e -> {
            MessageDeclaration msg = singleMessage(e, "Foof");
            assertEquals(3, msg.getFields().size());

            assertField(1, "fi", MSDLBaseType.LIST, msg.getFields().get(0));
            assertField(3, "ifi", MSDLBaseType.SET, msg.getFields().get(1));
            assertField(2, "ffi", MSDLBaseType.MAP, msg.getFields().get(2));

            ListOrSetType listType = (ListOrSetType) msg.getFields().get(0).getType();
            assertSame(MSDLBaseType.INT64, listType.getElementType().getBaseType());

            ListOrSetType setType = (ListOrSetType) msg.getFields().get(1).getType();
            assertSame(MSDLBaseType.STRING, setType.getElementType().getBaseType());

            MapType mapType = (MapType) msg.getFields().get(2).getType();
            assertSame(MSDLBaseType.INT32, mapType.getKeyType().getBaseType());
            assertSame(MSDLBaseType.FLOAT, mapType.getValueType().getBaseType());
        }, "message Foof {", "1: list<int64> fi;", "3: set<string> ifi;", "2: map<int32, float> ffi;", "}");
    }
}
