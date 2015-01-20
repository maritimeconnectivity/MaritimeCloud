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
package net.maritimecloud.internal.msdl.parser;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.maritimecloud.internal.msdl.parser.ParsedMsdlFile.Import;
import net.maritimecloud.msdl.MsdlLogger;
import net.maritimecloud.msdl.MsdlPluginException;

/**
 *
 * @author Kasper Nielsen
 */
class ImportResolver implements Iterable<ParsedMsdlFile> {

    /** A map of all msdl files. */
    Map<String, Path> dependencies;

    /** A map of all parsed msdl files. */
    Map<String, ParsedMsdlFile> resolvedDependency = new LinkedHashMap<>();

    final List<Path> directories;

    final MsdlLogger logger;

    /**
     * @param directories
     */
    ImportResolver(List<Path> directories, MsdlLogger logger) {
        this.directories = requireNonNull(directories);
        this.logger = requireNonNull(logger);
    }

    void addResolvedFile(String name, ParsedMsdlFile f) {
        logger.debug("Adding resolved file " + name + " -> " + f.antlrFile.getPath() + " as dependency");
        name = name.replace('\\', '/'); // handling windows paths
        resolvedDependency.put(name, f);
    }

    ParsedMsdlFile resolveImport(ParsedProject project, ParsedMsdlFile file, Import imp) throws IOException {

        logger.debug("Trying to resolve import: " + imp.importContext);

        String name = imp.getName();
        // lets see if we already resolved it
        ParsedMsdlFile f = resolvedDependency.get(name);
        if (f != null) {
            return f;
        }

        // make sure we have found all dependencies
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
                                logger.debug("Adding " + p + " -> " + file + " as dependency");
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
                    throw new MsdlPluginException("Failed to process dependencies", e);
                }
            }
        }


        Path p = dependencies.get(name);
        if (p == null) {
            // add-> "Looked in ...." list of directories
            Enumeration<URL> resources = ImportResolver.class.getClassLoader().getResources(name);
            List<URL> l = Collections.list(resources);
            if (l.isEmpty()) {
                file.error(imp.importContext, "Could not find import '" + name + "'");
            } else {
                f = project.parseFile(l.get(0));
                if (l.size() > 1) {
                    logger.warn("Multiple files named '" + name + "' existed on the classpath, will use first file: "
                            + l.get(0) + " All = " + l);
                }
            }
        } else {
            f = project.parseFile(p);
        }

        if (f != null) {
            addResolvedFile(name, f);
        }
        return f;
    }

    void resolveAll(ParsedProject project, Collection<ParsedMsdlFile> initial) throws IOException {
        LinkedHashSet<ParsedMsdlFile> allFiles = new LinkedHashSet<>(initial);
        LinkedList<ParsedMsdlFile> unImportedFiles = new LinkedList<>(initial);
        while (!unImportedFiles.isEmpty()) {
            ParsedMsdlFile pf = unImportedFiles.poll();
            for (Import importLocation : pf.imports) {
                ParsedMsdlFile imp = resolveImport(project, pf, importLocation);
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
    public Iterator<ParsedMsdlFile> iterator() {
        return resolvedDependency.values().iterator();
    }
}
