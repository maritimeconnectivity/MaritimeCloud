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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import net.maritimecloud.msdl.MsdlPlugin;
import net.maritimecloud.msdl.MsdlProcessor;
import net.maritimecloud.msdl.model.Project;
import net.maritimecloud.msdl.parser.TestLogger.Entry;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

/**
 *
 * @author Kasper Nielsen
 */
public class TestFs {

    final FileSystem fs = Jimfs.newFileSystem(Configuration.unix());

    final Path sources = fs.getPath("/sources");

    TestFs() throws IOException {
        Files.createDirectory(sources);
    }

    public void add(String name, String... lines) throws IOException {
        Path hello = sources.resolve(name); // /foo/hello.txt
        Files.write(hello, Arrays.asList(lines), StandardCharsets.UTF_8);
    }

    Project get() throws IOException {
        MsdlProcessor pro = new MsdlProcessor();
        pro.setSourceDirectory(sources);
        // for (sources.)


        Files.walkFileTree(sources, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!attrs.isDirectory()) {
                    pro.addFile(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        // pro.addFile(hello);
        AtomicReference<Project> ar = new AtomicReference<>();
        pro.addPlugin(new MsdlPlugin() {
            protected void process(Project project) throws Exception {
                try {
                    ar.set(project);
                } catch (Exception t) {
                    throw new AssertionError(t);
                }
            }
        });
        TestLogger tl = new TestLogger();
        pro.setLogger(tl);
        pro.executePlugins();

        Entry e = tl.nextError();
        if (e != null) {
            throw new AssertionError(e.getMessage(), e.getError());
        }
        return requireNonNull(ar.get());
    }
}
