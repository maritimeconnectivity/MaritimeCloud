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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Consumer;

import net.maritimecloud.msdl.MsdlPlugin;
import net.maritimecloud.msdl.MsdlProcessor;
import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.FieldOrParameter;
import net.maritimecloud.msdl.model.Project;
import net.maritimecloud.msdl.parser.TestLogger.Entry;

import org.junit.Before;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractParserTest {

    TestFs fs;

    @Before
    public void setup() throws IOException {
        fs = new TestFs();
    }

    public static void assertField(int tag, String name, BaseType type, FieldOrParameter def) {
        assertEquals(tag, def.getTag());
        assertEquals(name, def.getName());
        assertEquals(type, def.getType().getBaseType());
    }

    void singleError(Consumer<String> consumer, String... lines) throws IOException {
        try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
            Path foo = fs.getPath("/dir");
            Files.createDirectory(foo);

            Path hello = foo.resolve("file.msdl"); // /foo/hello.txt
            Files.write(hello, Arrays.asList(lines), StandardCharsets.UTF_8);

            MsdlProcessor pro = new MsdlProcessor();
            TestLogger tl = new TestLogger();
            pro.setSourceDirectory(foo);
            pro.addFile(hello);

            pro.addPlugin(new MsdlPlugin() {
                protected void process(Project project) throws Exception {

                }
            });
            pro.setLogger(tl);
            pro.executePlugins();

            TestLogger.Entry e = tl.nextError();
            assertNotNull(e);

            assertNull(tl.nextError());
            System.out.println(e.getMessage());
        }
    }

    void singleFile(Consumer<Project> consumer, String... lines) throws IOException {
        try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
            TestLogger tl = new TestLogger();
            Path foo = fs.getPath("/foo");
            Files.createDirectory(foo);

            Path hello = foo.resolve("hello.msdl"); // /foo/hello.txt
            Files.write(hello, Arrays.asList(lines), StandardCharsets.UTF_8);

            MsdlProcessor pro = new MsdlProcessor();
            pro.setSourceDirectory(foo);
            pro.addFile(hello);

            pro.addPlugin(new MsdlPlugin() {
                protected void process(Project project) throws Exception {
                    try {
                        consumer.accept(project);
                    } catch (Exception t) {
                        throw new AssertionError(t);
                    }
                }
            });
            pro.setLogger(tl);
            pro.executePlugins();

            Entry e = tl.nextError();
            if (e != null) {
                throw new AssertionError(e.getMessage(), e.getError());
            }
        }
    }
}
