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

import java.io.IOException;
import java.util.Map;

import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.MsdlFile;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class ImportTest extends AbstractParserTest {

    @Test(expected = AssertionError.class)
    public void aNoB() throws IOException {
        fs.add("a.msdl", "import \"b.msdl\";");
        fs.get().getFiles();// should fail
    }

    @Test
    public void ab() throws IOException {
        fs.add("a.msdl", "import \"b.msdl\";");
        fs.add("b.msdl", "");
        fs.get().getFiles();// should not fail
    }

    @Test
    public void aba() throws IOException {
        fs.add("a.msdl", "import \"b.msdl\";");
        fs.add("b.msdl", "import \"b.msdl\";");
        fs.get().getFiles();// should not fail
    }

    @Test
    public void foo() throws IOException {
        fs.add("a.msdl", "message Aa { 1:int b;", "}");
        fs.add("b.msdl", "message Aa { 1:int b;", "}");
        fs.get().getFiles();
    }

    @Test
    public void abMessage() throws IOException {
        fs.add("a.msdl", "import \"b.msdl\";", "message Aa { 1:Bb b;", "}");
        fs.add("b.msdl", "message Bb { 1:int b;", "}");

        Map<String, MsdlFile> files = fs.get().getFiles();
        MessageDeclaration aa = files.get("a.msdl").getMessages().get(0);
        MessageDeclaration bb = files.get("b.msdl").getMessages().get(0);
        assertEquals("Aa", aa.getName());
        assertSame(bb, aa.getFields().get(0).getType());
    }

    @Test
    public void abaMessage() throws IOException {
        fs.add("a.msdl", "import \"b.msdl\";", "message Aa { 1:Bb b;", "}");
        fs.add("b.msdl", "import \"a.msdl\";", "message Bb { 1:Aa a;", "}");
        fs.get().getFiles();// should not fail
        Map<String, MsdlFile> files = fs.get().getFiles();
        MessageDeclaration aa = files.get("a.msdl").getMessages().get(0);
        MessageDeclaration bb = files.get("b.msdl").getMessages().get(0);
        assertEquals("Aa", aa.getName());
        assertEquals("Bb", bb.getName());
        assertSame(bb, aa.getFields().get(0).getType());
        assertSame(aa, bb.getFields().get(0).getType());
    }
}
