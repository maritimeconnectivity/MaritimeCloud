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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.maritimecloud.msdl.MsdlLogger;
import net.maritimecloud.msdl.model.FileDeclaration;
import net.maritimecloud.msdl.model.Project;
import net.maritimecloud.msdl.parser.antlr.AntlrFile;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedProject {

    /** All source files that should be processed */
    final Map<String, Path> sourceFiles;

    final ImportResolver importResolver;

    final MsdlLogger logger;

    final AtomicInteger errorCounter = new AtomicInteger();

    final List<ParsedFile> files = new ArrayList<>();

    public ParsedProject(Map<String, Path> sourceFiles, List<Path> directories, MsdlLogger logger) {
        this.sourceFiles = requireNonNull(sourceFiles);
        this.importResolver = new ImportResolver(directories, logger);
        this.logger = MsdlLogger.errorCountingLogger(logger, errorCounter);
    }

    public Project parse() {
        try {
            return parse0();
        } catch (Exception e) {
            logger.error("Failed to parse files " + e.getMessage(), e);
        }
        return null;
    }

    Project parse0() throws Exception {
        // parse all source files
        for (Map.Entry<String, Path> e : sourceFiles.entrySet()) {
            ParsedFile file = parseFile(e.getValue());
            files.add(file);
            importResolver.resolvedDependency.put(e.getKey(), file);
        }
        if (errorCounter.get() > 0) {
            return null;
        }

        // resolve and parse imports
        importResolver.resolveAll(this, files);
        if (errorCounter.get() > 0) {
            return null;
        }

        // lookup references
        new TypeResolver(logger, importResolver.resolvedDependency.values()).resolve();
        if (errorCounter.get() > 0) {
            return null;
        }
        return new ProjectImpl(new ArrayList<>(files));
    }

    ParsedFile parseFile(Path p) throws IOException {
        ParsedFile f = new ParsedFile(this, new AntlrFile(p, logger));
        f.parse();
        return f;
    }

    static class ProjectImpl implements Project {
        final List<FileDeclaration> files;

        @SuppressWarnings({ "rawtypes", "unchecked" })
        ProjectImpl(List<ParsedFile> files) {
            this.files = (List) Collections.unmodifiableList(new ArrayList<>(files));
        }

        /** {@inheritDoc} */
        @Override
        public Iterator<FileDeclaration> iterator() {
            return files.iterator();
        }

        /** {@inheritDoc} */
        @Override
        public List<FileDeclaration> getFiles() {
            return files;
        }
    }
}
