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
import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.maritimecloud.internal.msdl.parser.antlr.AntlrFile;
import net.maritimecloud.msdl.MsdlLogger;
import net.maritimecloud.msdl.model.MsdlFile;
import net.maritimecloud.msdl.model.Project;

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

    final Map<String, ParsedFile> files = new TreeMap<>();

    public ParsedProject(Map<String, Path> sourceFiles, List<Path> directories, MsdlLogger logger) {
        this.sourceFiles = requireNonNull(sourceFiles);
        this.importResolver = new ImportResolver(directories, logger);
        this.logger = MsdlLogger.errorCountingLogger(logger, errorCounter);
    }

    public Project parse() {
        try {
            return parse0();
        } catch (Exception e) {
            logger.error("Internal compiler error (please report bug) " + e.getMessage(), e);
        }
        return null;
    }

    Project parse0() throws Exception {
        // parse all source files
        for (Map.Entry<String, Path> e : sourceFiles.entrySet()) {
            ParsedFile file = parseFile(e.getValue());
            if (file != null) {
                files.put(e.getKey(), file);
                importResolver.resolvedDependency.put(e.getKey(), file);
            }
        }
        if (errorCounter.get() > 0) {
            return null;
        }

        // resolve and parse imports
        importResolver.resolveAll(this, files.values());
        if (errorCounter.get() > 0) {
            return null;
        }

        // lookup references
        new TypeResolver(logger, importResolver.resolvedDependency.values()).resolve();
        if (errorCounter.get() > 0) {
            return null;
        }
        return new ProjectImpl(new TreeMap<>(files));
    }

    ParsedFile parseFile(Path p) throws IOException {
        return cr(new AntlrFile(p, logger));
    }

    ParsedFile parseFile(URL p) throws IOException {
        return cr(new AntlrFile(p, logger));
    }

    ParsedFile cr(AntlrFile af) {
        af.getCompilationUnit();
        if (errorCounter.get() > 0) {
            return null;
        }
        ParsedFile f = new ParsedFile(this, af);
        f.parse();
        return f;
    }

    static class ProjectImpl implements Project {
        final Map<String, MsdlFile> files;

        ProjectImpl(Map<String, MsdlFile> files) {
            this.files = Collections.unmodifiableMap(files);
        }

        /** {@inheritDoc} */
        @Override
        public Iterator<MsdlFile> iterator() {
            return files.values().iterator();
        }

        /** {@inheritDoc} */
        @Override
        public Map<String, MsdlFile> getFiles() {
            return files;
        }
    }
}
