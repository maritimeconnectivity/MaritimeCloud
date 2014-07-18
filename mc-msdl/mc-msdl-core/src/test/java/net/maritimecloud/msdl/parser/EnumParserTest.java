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
import net.maritimecloud.msdl.model.MsdlFile;

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
            MsdlFile f = e.iterator().next();

            assertEquals(1, f.getEnums().size());

            EnumDeclaration en = f.getEnums().get(0);
            assertEquals(BaseType.ENUM, en.getBaseType());
            assertEquals("Foof", en.getName());
            assertEquals(1, en.getConstants().size());
            assertEquals("FOO", en.getConstants().get(0).getName());
            assertEquals(1, en.getConstants().get(0).getValue());
        }, "enum Foof {", " FOO = 1;", "}");
    }

    @Test
    public void multiEnum() throws IOException {
        singleFile(e -> {
            assertEquals(1, e.getFiles().size());
            MsdlFile f = e.iterator().next();
            assertEquals(1, f.getEnums().size());
            EnumDeclaration en = f.getEnums().get(0);
            assertEquals(BaseType.ENUM, en.getBaseType());
            assertEquals("Foof", en.getName());

            assertEquals(3, en.getConstants().size());

            assertEquals("FOO", en.getConstants().get(0).getName());
            assertEquals(1, en.getConstants().get(0).getValue());

            assertEquals("FO1", en.getConstants().get(1).getName());
            assertEquals(123, en.getConstants().get(1).getValue());

            assertEquals("FO2", en.getConstants().get(2).getName());
            assertEquals(12, en.getConstants().get(2).getValue());

        }, "enum Foof {", " FOO = 1;", " FO1 = 123;", " FO2 = 12;", "}");
    }

    @Test
    public void illegalName() throws IOException {
        singleError(e -> {
            System.out.println(e);
        }, "enum Foof {", " oS = 1;", "}");
        singleError(e -> {
            System.out.println(e);
        }, "enum Foof {", " So = 1;", "}");
        singleError(e -> {
            System.out.println(e);
        }, "enum Foof {", " 1S = 1;", "}");
        singleError(e -> {
            System.out.println(e);
        }, "enum Foof {", " _S1 = 1;", "}");

    }

    @Test
    public void sameId() throws IOException {
        singleError(e -> {
            System.out.println(e);
        }, "enum Foof {", " FOO = 1;", " FOOD = 1;", "}");
    }

}
