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
package net.maritimecloud.msdl;

import static java.util.Objects.requireNonNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import net.maritimecloud.msdl.model.Project;
import net.maritimecloud.msdl.parser.ParsedProject;

/**
 * A processor of MSDL plugins.
 *
 * @author Kasper Nielsen
 */
public class MsdlProcessor {

    final List<Path> dependencyDirectories = new ArrayList<>();

    /** The files that be processed by each plugin. */
    final Set<Path> files = new LinkedHashSet<>();

    MsdlLogger logger;

    /** A list of all plugins that should be processed. */
    final List<MsdlPlugin> plugins = new ArrayList<>();

    Path sourceDirectory;

    public MsdlProcessor() {
        sourceDirectory = Paths.get("").toAbsolutePath(); // current working directory
    }

    public MsdlProcessor addDependencyDirectory(Path p) {
        dependencyDirectories.add(p);
        return this;
    }

    /**
     * Adds a .MSDL file.
     *
     * @param path
     *            the path of the file
     */
    public MsdlProcessor addFile(Path path) {
        files.add(requireNonNull(path));
        return this;
    }

    public MsdlProcessor addFile(String path) {
        return addFile(Paths.get(path));
    }

    /**
     * Adds a plugin that should be processed
     *
     * @param plugin
     *            the plugin to processed
     * @return this processor
     */
    public MsdlProcessor addPlugin(MsdlPlugin plugin) {
        plugins.add(requireNonNull(plugin));
        return this;
    }

    private Map<String, Path> checkFiles(MsdlLogger logger) {
        LinkedHashMap<String, Path> m = new LinkedHashMap<>();
        for (Path p : getFiles()) {
            if (p.isAbsolute()) {
                if (!Files.exists(p)) {
                    logger.error("Could not find file " + p);
                }
                Path parent = p.getParent();
                while (parent != null) {
                    if (parent.equals(getSourceDirectory())) {
                        m.put(getSourceDirectory().relativize(p).toString(), p);
                        break;
                    }
                    parent = parent.getParent();
                }
                if (parent == null) {
                    logger.error("All .msdl files must must be located in " + getSourceDirectory() + ", was " + p);
                }
            } else {
                Path file = getSourceDirectory().resolve(p);
                if (!Files.exists(file)) {
                    logger.error("Could not find file " + p + " at " + file);
                }
                m.put(getSourceDirectory().relativize(file).toString(), file.toAbsolutePath());
            }
        }
        return m;
    }

    public List<Path> getDependencyDirectories() {
        return dependencyDirectories;
    }

    public Collection<Path> getFiles() {
        return files;
    }

    /**
     * @return the logger
     */
    public MsdlLogger getLogger() {
        return logger;
    }

    /**
     * @return the sourceDirectory
     */
    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    public MsdlProcessorResult process() {
        MsdlLogger log = logger;
        if (log == null) {
            Logger lo = Logger.getLogger("msdl");
            for (Handler h : lo.getParent().getHandlers()) {
                h.setFormatter(new SimpleFormatter() {
                    public String format(LogRecord record) {
                        String throwable = "";
                        if (record.getThrown() != null) {
                            StringWriter sw = new StringWriter();
                            try (PrintWriter pw = new PrintWriter(sw)) {
                                record.getThrown().printStackTrace(pw);
                            }
                            throwable = sw.toString();
                        }
                        return new java.util.Date() + " " + record.getLevel() + " " + record.getMessage() + "\r\n"
                                + throwable;
                    }
                });
            }
            log = MsdlLogger.wrapJUL(lo);
        }

        AtomicInteger errorCounter = new AtomicInteger();
        log = MsdlLogger.errorCountingLogger(log, errorCounter);

        Map<String, Path> sourceFiles = checkFiles(log);
        if (errorCounter.get() > 0) {
            return new MsdlProcessorResult("One or more files could not be found");
        }

        // Parse all files and resolve imports
        ParsedProject pp = new ParsedProject(sourceFiles, dependencyDirectories, log);
        Project p = pp.parse();
        if (errorCounter.get() > 0 || p == null) {
            return new MsdlProcessorResult("Failed to properly parse MSDL files");
        }

        // Invoke each plugin
        for (MsdlPlugin plugin : plugins) {
            plugin.logger = log;
            log.info("Processing plugin: " + plugin.getClass().getSimpleName());
            try {
                plugin.process(p);
            } catch (MsdlPluginException e) {
                return new MsdlProcessorResult("Plugin failed");
            } catch (Exception e) {
                log.error("Plugin failed", e);
                // return new MsdlProcessorResult("Plugin failed");
            }
            if (errorCounter.get() > 0) {
                return new MsdlProcessorResult("Plugin failed");
            }
        }
        return new MsdlProcessorResult(null); // success
    }

    public MsdlProcessor setLogger(MsdlLogger logger) {
        this.logger = requireNonNull(logger);
        return this;
    }

    public MsdlProcessor setSourceDirectory(Path p) {
        this.sourceDirectory = p.toAbsolutePath();
        return this;
    }
}
