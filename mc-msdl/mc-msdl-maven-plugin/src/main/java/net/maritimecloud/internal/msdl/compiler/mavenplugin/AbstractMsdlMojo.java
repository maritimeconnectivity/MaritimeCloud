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
package net.maritimecloud.internal.msdl.compiler.mavenplugin;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.codehaus.plexus.util.FileUtils.cleanDirectory;
import static org.codehaus.plexus.util.FileUtils.copyStreamToFile;
import static org.codehaus.plexus.util.FileUtils.getFiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.maritimecloud.msdl.MsdlProcessor;
import net.maritimecloud.msdl.MsdlProcessorResult;
import net.maritimecloud.msdl.plugins.javagen.JavaGenPlugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.io.RawInputStreamFacade;
import org.sonatype.plexus.build.incremental.BuildContext;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

/**
 * Abstract Mojo implementation.
 *
 *
 */
abstract class AbstractMsdlMojo extends AbstractMojo {

    private static final String MSDL_FILE_SUFFIX = ".msdl";

    private static final String DEFAULT_INCLUDES = "**/*" + MSDL_FILE_SUFFIX;

    /** The current Maven project. */
    @Component
    protected MavenProject project;

    /** The current Maven Session Object. */
    @Component
    private MavenSession session;

    /** Build context that tracks changes to the source and target files. */
    @Component
    protected BuildContext buildContext;

    /** A helper used to add resources to the project. */
    @Component
    protected MavenProjectHelper projectHelper;

    /** This is the path to the local maven {@code repository}. */
    @Parameter(required = true, readonly = true, property = "localRepository")
    private ArtifactRepository localRepository;

    /** This is the path to the local maven {@code repository}. */
    @Parameter(required = false)
    private boolean implementsSerializable;

    /** Additional source paths for {@code .proto} definitions. */
    @Parameter(required = false)
    private File[] additionalProtoPathElements = {};

    /** Flag to indicate whether or not to generate EJB classes. */
    @Parameter(required = false)
    private boolean generateEJB;

    /**
     * Since {@code protoc} cannot access jars, proto files in dependencies are extracted to this location and deleted
     * on exit. This directory is always cleaned during execution.
     */
    @Parameter(required = true, defaultValue = "${project.build.directory}/msdl-dependencies")
    private File temporaryMSDLDirectory;

    /**
     * Set this to {@code false} to disable hashing of dependent jar paths.
     * <p/>
     * This plugin expands jars on the classpath looking for embedded {@code .proto} files. Normally these paths are
     * hashed (MD5) to avoid issues with long file names on windows. However if this property is set to {@code false}
     * longer paths will be used.
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean hashDependentPaths;

    /**
     * A list of &lt;include&gt; elements specifying the protobuf definition files (by pattern) that should be included
     * in compilation. When not specified, the default includes will be: <code><br/>
     * &lt;includes&gt;<br/>
     * &nbsp;&lt;include&gt;**&#47;*.proto&lt;/include&gt;<br/>
     * &lt;/includes&gt;<br/>
     * </code>
     */
    @Parameter(required = false)
    private Set<String> includes = ImmutableSet.of(DEFAULT_INCLUDES);

    /**
     * A list of &lt;exclude&gt; elements specifying the protobuf definition files (by pattern) that should be excluded
     * from compilation. When not specified, the default excludes will be empty: <code><br/>
     * &lt;excludes&gt;<br/>
     * &lt;/excludes&gt;<br/>
     * </code>
     */
    @Parameter(required = false)
    private Set<String> excludes = ImmutableSet.of();

    /** The descriptor set file name. Only used if {@code writeDescriptorSet} is set to {@code true}. */
    @Parameter(required = true, defaultValue = "${project.build.finalName}.protobin")
    private String descriptorSetFileName;

    /**
     * If set to {@code true}, the compiler will generate a binary descriptor set file for the specified {@code .msdl}
     * files.
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean writeDescriptorSet;

    /**
     * If {@code true} and {@code writeDescriptorSet} has been set, the compiler will include all dependencies in the
     * descriptor set making it "self-contained".
     */
    @Parameter(required = false, defaultValue = "false")
    private boolean includeDependenciesInDescriptorSet;

    /**
     * Sets the granularity in milliseconds of the last modification date for testing whether source protobuf
     * definitions need recompilation.
     *
     * <p>
     * This parameter is only used when {@link #checkStaleness} parameter is set to {@code true}.
     *
     * <p>
     * If the project is built on NFS it's recommended to set this parameter to {@code 10000}.
     */
    @Parameter(required = false, defaultValue = "0")
    private long staleMillis;

    /**
     * Normally {@code protoc} is invoked on every execution of the plugin. Setting this parameter to {@code true} will
     * enable checking timestamps of source protobuf definitions vs. generated sources.
     *
     * @see #staleMillis
     */
    @Parameter(required = false, defaultValue = "false")
    private boolean checkStaleness;

    /** When {@code true}, skip the execution. */
    @Parameter(required = false, property = "msdl.skip", defaultValue = "false")
    private boolean skip;

    /** Specifies the location of the header or license file that can be appended to each generated source file. */
    @Parameter(required = false)
    protected String headerLocation;

    // If we want to loader headers from jar files at some point. checkout
    // https://github.com/apache/maven-plugins/blob/trunk/maven-checkstyle-plugin/src/main/java/org/apache/maven/plugin/checkstyle/DefaultCheckstyleExecutor.java

    /**
     * Usually most of protobuf mojos will not get executed on parent poms (i.e. projects with packaging type 'pom').
     * Setting this parameter to {@code true} will force the execution of this mojo, even if it would usually get
     * skipped in this case.
     */
    @Parameter(required = false, property = "msdl.force", defaultValue = "false")
    private boolean forceMojoExecution;


    /** Executes this mojo. */
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skipMojo()) {
            return;
        }
        checkParameters();
        File msdlSourceRoot = getMsdlSourceRoot();
        if (msdlSourceRoot.exists()) {
            try {
                Set<File> msdlFiles = findMsdlFilesInDirectory(msdlSourceRoot);
                File outputDirectory = getOutputDirectory();
                Set<File> outputFiles = findGeneratedFilesInDirectory(getOutputDirectory());

                if (msdlFiles.isEmpty()) {
                    getLog().info("No msdl files to compile.");
                } else if (!hasDelta(msdlFiles)) {
                    getLog().info("Skipping compilation because build context has no changes.");
                    attachFiles();
                } else if (checkStaleness && checkFilesUpToDate(msdlFiles, outputFiles)) {
                    getLog().info("Skipping compilation because target directory newer than sources.");
                    attachFiles();
                } else {
                    Set<File> derivedMsdlPathElements = makeMsdlPathFromJars(temporaryMSDLDirectory,
                            getDependencyArtifactFiles());
                    FileUtils.mkdir(outputDirectory.getAbsolutePath());

                    // Quick fix to fix issues with two mvn installs in a row (ie no clean)
                    cleanDirectory(outputDirectory);

                    if (writeDescriptorSet) {
                        File descriptorSetOutputDirectory = getDescriptorSetOutputDirectory();
                        FileUtils.mkdir(descriptorSetOutputDirectory.getAbsolutePath());
                        // See above
                        cleanDirectory(descriptorSetOutputDirectory);
                    }

                    MsdlProcessor g = new MsdlProcessor();


                    g.setSourceDirectory(msdlSourceRoot.toPath());

                    for (File f : derivedMsdlPathElements) {
                        g.addDependencyDirectory(f.toPath());
                    }
                    for (File f : msdlFiles) {
                        g.addFile(f.toPath());
                    }
                    JavaGenPlugin javaPlugin = JavaGenPlugin.create(outputDirectory.toPath());

                    javaPlugin.setImplementsSerializable(implementsSerializable);
                    getLog().debug("Setting implements serialiable = " + implementsSerializable);

                    javaPlugin.setGenerateEJB(generateEJB);
                    getLog().debug("Setting generate EJB = " + generateEJB);

                    g.addPlugin(javaPlugin);

                    if (headerLocation != null) {
                        Path headerPath = Paths.get(headerLocation).toAbsolutePath();
                        if (!Files.exists(headerPath)) {
                            getLog().warn("Could not find headerFile: " + headerPath);
                        } else {
                            javaPlugin.setHeader(headerPath);
                        }
                    }


                    if (getLog().isDebugEnabled()) {
                        getLog().debug("Msdl source root:");
                        getLog().debug(" " + msdlSourceRoot);

                        if (derivedMsdlPathElements != null && !derivedMsdlPathElements.isEmpty()) {
                            getLog().debug("Derived msdl paths:");
                            for (final File path : derivedMsdlPathElements) {
                                getLog().debug(" " + path);
                            }
                        }

                        if (additionalProtoPathElements != null && additionalProtoPathElements.length > 0) {
                            getLog().debug("Additional proto paths:");
                            for (final File path : additionalProtoPathElements) {
                                getLog().debug(" " + path);
                            }
                        }
                    }
                    g.setLogger(new AdaptedMsdlLogger(getLog()));
                    MsdlProcessorResult result = g.executePlugins();
                    if (result.isSuccesfull()) {
                        getLog().info("Files succesfully generated");
                    } else {
                        // for (Notification n : result) {
                        // if (n.isError()) {
                        // getLog().error(n.getMessage());
                        // } else {
                        // getLog().warn(n.getMessage());
                        // }
                        // }
                        throw new MojoFailureException("Compilation failed. Review output for more information.");
                    }
                    attachFiles();
                }
            } catch (IOException e) {
                throw new MojoExecutionException("An IO error occured", e);
            } catch (IllegalArgumentException e) {
                throw new MojoFailureException("Compiler failed to execute because: " + e.getMessage(), e);
            }
        } else {
            getLog().info(
                    format("%s does not exist. Review the configuration or consider disabling the plugin.",
                            msdlSourceRoot));
        }
    }

    /**
     * <p>
     * Determine if the mojo execution should get skipped.
     * </p>
     * This is the case if:
     * <ul>
     * <li>{@link #skip} is <code>true</code></li>
     * <li>if the mojo gets executed on a project with packaging type 'pom' and {@link #forceMojoExecution} is
     * <code>false</code></li>
     * </ul>
     *
     * @return <code>true</code> if the mojo execution should be skipped.
     */
    protected boolean skipMojo() {
        if (skip) {
            getLog().info("Skipping msdl mojo execution");
            return true;
        }

        if (!forceMojoExecution && "pom".equals(this.project.getPackaging())) {
            getLog().info("Skipping protoc mojo execution for project with packaging type 'pom'");
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    protected static Set<File> findGeneratedFilesInDirectory(File directory) throws IOException {
        if (directory == null || !directory.isDirectory()) {
            return Collections.emptySet();
        }
        return new HashSet<>(getFiles(directory, "**/*.java", null));
    }

    /**
     * Checks that the source files don't have modification time that is later than the target files.
     *
     * @param sourceFiles
     *            a set of source files.
     * @param targetFiles
     *            a set of target files.
     * @return {@code true}, if source files are not later than the target files; {@code false}, otherwise.
     */
    protected boolean checkFilesUpToDate(final Set<File> sourceFiles, Set<File> targetFiles) {
        return FileTools.lastModified(sourceFiles) + staleMillis < FileTools.lastModified(targetFiles);
    }

    /**
     * Checks if the injected build context has changes in any of the specified files.
     *
     * @param files
     *            files to be checked for changes.
     * @return {@code true}, if at least one file has changes; {@code false}, if no files have changes.
     */
    protected boolean hasDelta(Set<File> files) {
        for (File file : files) {
            if (buildContext.hasDelta(file)) {
                return true;
            }
        }
        return false;
    }

    protected void checkParameters() {
        checkNotNull(project, "project");
        checkNotNull(projectHelper, "projectHelper");

        File msdlSourceRoot = getMsdlSourceRoot();
        checkNotNull(msdlSourceRoot);
        checkArgument(!msdlSourceRoot.isFile(), "msdlSourceRoot is a file, not a directory");
        checkNotNull(temporaryMSDLDirectory, "temporaryMsdlFileDirectory");
        checkState(!temporaryMSDLDirectory.isFile(), "temporaryMsdlFileDirectory is a file, not a directory");

        File outputDirectory = getOutputDirectory();
        checkNotNull(outputDirectory);
        checkState(!outputDirectory.isFile(), "the outputDirectory is a file, not a directory");
    }

    protected abstract File getMsdlSourceRoot();

    // TODO add artifact filtering (inclusions and exclusions)
    // TODO add filtering for proto definitions in included artifacts
    protected abstract List<Artifact> getDependencyArtifacts();

    /**
     * Returns the output directory for generated sources. Depends on build phase so must be defined in concrete
     * implementation.
     *
     * @return output directory for generated sources.
     */
    protected abstract File getOutputDirectory();

    /**
     * Returns output directory for descriptor set file. Depends on build phase so must be defined in concrete
     * implementation.
     *
     * @return output directory for generated descriptor set.
     *
     * @since 0.3.0
     */
    protected abstract File getDescriptorSetOutputDirectory();

    protected abstract void attachFiles();

    /**
     * Gets the {@link File} for each dependency artifact.
     *
     * @return A set of all dependency artifacts.
     */
    protected Set<File> getDependencyArtifactFiles() {
        final Set<File> dependencyArtifactFiles = new HashSet<>();
        for (final Artifact artifact : getDependencyArtifacts()) {
            dependencyArtifactFiles.add(artifact.getFile());
        }
        return dependencyArtifactFiles;
    }

    /**
     * @throws IOException
     */
    @SuppressWarnings("resource")
    protected Set<File> makeMsdlPathFromJars(File temporaryProtoFileDirectory, Iterable<File> classpathElementFiles)
            throws IOException, MojoExecutionException {
        checkNotNull(classpathElementFiles, "classpathElementFiles");
        if (!classpathElementFiles.iterator().hasNext()) {
            return Collections.emptySet();
        }
        // clean the temporary directory to ensure that stale files aren't used
        if (temporaryProtoFileDirectory.exists()) {
            cleanDirectory(temporaryProtoFileDirectory);
        }
        Set<File> protoDirectories = new HashSet<>();
        for (final File classpathElementFile : classpathElementFiles) {
            // for some reason under IAM, we receive poms as dependent files
            // I am excluding .xml rather than including .jar as there may be other extensions in use (sar, har, zip)
            if (classpathElementFile.isFile() && classpathElementFile.canRead()
                    && !classpathElementFile.getName().endsWith(".xml")) {

                // create the jar file. the constructor validates.
                final JarFile classpathJar;
                try {
                    classpathJar = new JarFile(classpathElementFile);
                } catch (IOException e) {
                    throw new IllegalArgumentException(format("%s was not a readable artifact", classpathElementFile),
                            e);
                }
                try {
                    final Enumeration<JarEntry> jarEntries = classpathJar.entries();
                    while (jarEntries.hasMoreElements()) {
                        final JarEntry jarEntry = jarEntries.nextElement();
                        final String jarEntryName = jarEntry.getName();
                        // TODO try using org.codehaus.plexus.util.SelectorUtils.matchPath() with DEFAULT_INCLUDES
                        if (jarEntryName.endsWith(MSDL_FILE_SUFFIX)) {
                            File jarDirectory = new File(temporaryProtoFileDirectory,
                                    truncatePath(classpathJar.getName()));
                            File uncompressedCopy = new File(jarDirectory, jarEntryName);
                            FileUtils.mkdir(uncompressedCopy.getParentFile().getAbsolutePath());
                            copyStreamToFile(new RawInputStreamFacade(classpathJar.getInputStream(jarEntry)),
                                    uncompressedCopy);
                            protoDirectories.add(jarDirectory);

                        }
                    }
                } finally {
                    classpathJar.close();
                }

            } else if (classpathElementFile.isDirectory()) {
                final List<?> protoFiles = getFiles(classpathElementFile, DEFAULT_INCLUDES, null);
                if (!protoFiles.isEmpty()) {
                    protoDirectories.add(classpathElementFile);
                }
            }
        }
        return ImmutableSet.copyOf(protoDirectories);
    }

    @SuppressWarnings("unchecked")
    protected Set<File> findMsdlFilesInDirectory(File directory) throws IOException {
        requireNonNull(directory);
        checkArgument(directory.isDirectory(), "%s is not a directory", directory);
        Joiner joiner = Joiner.on(',');
        List<File> protoFilesInDirectory = getFiles(directory, joiner.join(includes), joiner.join(excludes));
        return ImmutableSet.copyOf(protoFilesInDirectory);
    }


    /**
     * Truncates the path of jar files so that they are relative to the local repository.
     *
     * @param jarPath
     *            the full path of a jar file.
     * @return the truncated path relative to the local repository or root of the drive.
     */
    protected String truncatePath(final String jarPath) throws MojoExecutionException {
        if (hashDependentPaths) {
            try {
                return toHexString(MessageDigest.getInstance("MD5").digest(jarPath.getBytes()));
            } catch (NoSuchAlgorithmException e) {
                throw new MojoExecutionException("Failed to expand dependent jar", e);
            }
        }

        String repository = localRepository.getBasedir().replace('\\', '/');
        if (!repository.endsWith("/")) {
            repository += "/";
        }

        String path = jarPath.replace('\\', '/');
        final int repositoryIndex = path.indexOf(repository);
        if (repositoryIndex != -1) {
            path = path.substring(repositoryIndex + repository.length());
        }

        // By now the path should be good, but do a final check to fix windows machines.
        final int colonIndex = path.indexOf(':');
        if (colonIndex != -1) {
            // 2 = :\ in C:\
            path = path.substring(colonIndex + 2);
        }

        return path;
    }

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    protected static String toHexString(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder(2 * byteArray.length);
        for (final byte b : byteArray) {
            hexString.append(HEX_CHARS[(b & 0xF0) >> 4]).append(HEX_CHARS[b & 0x0F]);
        }
        return hexString.toString();
    }
}
