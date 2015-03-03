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

    final AtomicInteger errorCounter = new AtomicInteger();

    final TreeMap<String, ParsedMsdlFile> files = new TreeMap<>();

    final ImportResolver importResolver;

    final MsdlLogger logger;

    /** All source files that should be processed */
    final Map<String, Path> sourceFiles;

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
        logger.debug("Start parsing of source files");
        // parse all source files
        for (Map.Entry<String, Path> e : sourceFiles.entrySet()) {
            ParsedMsdlFile file = parseFile(e.getValue());
            if (file != null) { // only add it if successfully parsed
                files.put(e.getKey(), file);
                importResolver.addResolvedFile(e.getKey(), file, false);
            }
        }
        // If we experience any errors exit now. Before trying to parse referenced files
        if (errorCounter.get() > 0) {
            return null;
        }

        logger.debug("All source files was succesfully parsed, resolving imports");
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

    private ParsedMsdlFile parseFile(AntlrFile af) {
        af.getCompilationUnit();
        if (errorCounter.get() > 0) {
            return null;
        }
        ParsedMsdlFile f = new ParsedMsdlFile(this, af);
        f.parse();
        return f;
    }

    ParsedMsdlFile parseFile(Path p) throws IOException {
        return parseFile(new AntlrFile(p, logger));
    }

    ParsedMsdlFile parseFile(URL p) throws IOException {
        return parseFile(new AntlrFile(p, logger));
    }

    /** The default implementation of Project. */
    static class ProjectImpl implements Project {
        final Map<String, MsdlFile> files;

        ProjectImpl(Map<String, MsdlFile> files) {
            this.files = Collections.unmodifiableMap(files);
        }

        /** {@inheritDoc} */
        @Override
        public Map<String, MsdlFile> getFiles() {
            return files;
        }

        /** {@inheritDoc} */
        @Override
        public Iterator<MsdlFile> iterator() {
            return files.values().iterator();
        }
    }
}
