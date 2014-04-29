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

import java.io.IOException;

import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.EnumDeclaration;
import net.maritimecloud.msdl.model.FileDeclaration;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class EnumParserTest extends AbstractParserTest {

    @Test
    public void singleEnum() throws IOException {
        singleFile(e -> {
            assertEquals(1, e.getFiles().size());
            FileDeclaration f = e.getFiles().get(0);

            assertEquals(1, f.getEnums().size());

            EnumDeclaration en = f.getEnums().get(0);
            assertEquals(BaseType.ENUM, en.getBaseType());
            assertEquals("Foof", en.getName());
            assertEquals(1, en.getValues().size());
            assertEquals("Foo", en.getValues().get(0).getName());
            assertEquals(1, en.getValues().get(0).getValue());
        }, "enum Foof {", " Foo = 1;", "}");
    }

    @Test
    public void multiEnum() throws IOException {
        singleFile(e -> {
            assertEquals(1, e.getFiles().size());
            FileDeclaration f = e.getFiles().get(0);
            assertEquals(1, f.getEnums().size());
            EnumDeclaration en = f.getEnums().get(0);
            assertEquals(BaseType.ENUM, en.getBaseType());
            assertEquals("Foof", en.getName());

            assertEquals(3, en.getValues().size());

            assertEquals("Foo", en.getValues().get(0).getName());
            assertEquals(1, en.getValues().get(0).getValue());

            assertEquals("Fo1", en.getValues().get(1).getName());
            assertEquals(123, en.getValues().get(1).getValue());

            assertEquals("Fo2", en.getValues().get(2).getName());
            assertEquals(12, en.getValues().get(2).getValue());

        }, "enum Foof {", " Foo = 1;", " Fo1 = 123;", " Fo2 = 12;", "}");
    }


    @Test
    public void sameId() throws IOException {
        singleError(e -> {
            System.out.println(e);
        }, "enum Foof {", " Foo = 1;", " Food = 1;", "}");
    }

}
