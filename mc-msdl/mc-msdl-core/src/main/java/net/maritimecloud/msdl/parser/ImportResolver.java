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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.maritimecloud.msdl.MsdlLogger;
import net.maritimecloud.msdl.MsdlPluginException;

/**
 *
 * @author Kasper Nielsen
 */
class ImportResolver implements Iterable<ParsedFile> {
    Map<String, Path> dependencies;

    Map<String, ParsedFile> resolvedDependency = new LinkedHashMap<>();

    final List<Path> directories;

    final MsdlLogger logger;

    /**
     * @param directories
     */
    ImportResolver(List<Path> directories, MsdlLogger logger) {
        this.directories = requireNonNull(directories);
        this.logger = requireNonNull(logger);
    }

    ParsedFile resolveImport(ParsedProject project, String name) throws IOException {
        ParsedFile f = resolvedDependency.get(name);
        if (f != null) {
            return f;
        }
        if (dependencies == null) {
            dependencies = new LinkedHashMap<>();
            for (final Path root : directories) {
                logger.debug("Looking for .msdl dependencies in " + root);
                SimpleFileVisitor<Path> sfv = new SimpleFileVisitor<Path>() {
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.getFileName().toString().endsWith(".msdl")) {
                            String p = root.relativize(file).toString();
                            if (dependencies.containsKey(p)) {
                                logger.warn("Multiple files named '" + p + " existed, ignore second file, first = "
                                        + dependencies.get(p) + ", second = " + file);
                            } else {
                                dependencies.put(p, file);
                            }
                        }
                        return super.visitFile(file, attrs);
                    }
                };
                try {
                    Files.walkFileTree(root, sfv);
                } catch (IOException e) {
                    logger.error("Failed to process dependencies", e);
                    throw new MsdlPluginException();
                }
            }
        }
        Path p = dependencies.get(name);
        if (p == null) {
            // add-> "Looked in ...." list of directories
            logger.error("Could not find import " + name);
        } else {
            f = project.parseFile(p);
            resolvedDependency.put(name, f);
        }
        return f;
    }

    void resolveAll(ParsedProject project, Collection<ParsedFile> initial) throws IOException {
        LinkedHashSet<ParsedFile> allFiles = new LinkedHashSet<>(initial);
        LinkedList<ParsedFile> unImportedFiles = new LinkedList<>(initial);
        while (!unImportedFiles.isEmpty()) {
            ParsedFile pf = unImportedFiles.poll();
            for (String importLocation : pf.imports) {
                ParsedFile imp = resolveImport(project, importLocation);
                if (imp != null) {
                    if (!allFiles.contains(imp)) {
                        allFiles.add(imp);
                        unImportedFiles.add(imp);
                    }
                    pf.resolvedImports.add(imp);
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<ParsedFile> iterator() {
        return resolvedDependency.values().iterator();
    }
}
