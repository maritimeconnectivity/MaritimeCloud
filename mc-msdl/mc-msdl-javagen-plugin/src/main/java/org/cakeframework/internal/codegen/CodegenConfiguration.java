/*
 * Copyright (c) 2008 Kasper Nielsen.
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
package org.cakeframework.internal.codegen;

import static java.util.Objects.requireNonNull;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Many Cake data structures uses dynamic code generation to generate highly efficient code.
 * 
 * This class can be used to provide greater control about how these classes are loaded into the JVM. As well as provide
 * support for outputting the generate code. For example, for use in debugging. Or just for inspecting the generated
 * code.
 * 
 * @author Kasper Nielsen
 */
// Move to x properties for now
public class CodegenConfiguration {

    /** The classloader that should be used as a parent for the generated classes. */
    private ClassLoader classLoaderParent;

    /** A list of printers where all code is written out to before being compiled. */
    private final List<PrintWriter> codePrinters = new ArrayList<>(0);

    /** The default base package name. */
    private String packageName;

    /** A path to write .java files to. */
    private Path sourcePath;

    /**
     * Sets a print stream where the source code of all generated classes will be sent to. The code is added to the
     * stream before it is compiled.
     * <p>
     * This can, for example, be used to output all generated source code to system.out:
     * 
     * <pre>
     * codegenConfiguration.setCompilationStream(System.out);
     * </pre>
     * 
     * @param stream
     *            the print stream
     * @return this configuration
     * @throws NullPointerException
     *             if the specified stream is null
     */
    public final CodegenConfiguration addCodeWriter(PrintStream stream) {
        return addCodeWriter(new PrintWriter(requireNonNull(stream, "stream is null")));
    }

    /**
     * Equivalent to {@link #addCodeWriter(PrintStream)} except that this method takes a print writer instead of a print
     * stream.
     * 
     * @param writer
     *            the print writer
     * @return this configuration
     * @throws NullPointerException
     *             if the specified writer is null
     */
    public final CodegenConfiguration addCodeWriter(PrintWriter writer) {
        codePrinters.add(requireNonNull(writer, "writer is null"));
        return this;
    }

    /**
     * Returns any class loader parent set by {@link #setClassLoaderParent(ClassLoader)}. Or {@code null} if no class
     * loader parent has been set.
     * 
     * @return a class loader parent if one has been set, other {@code null}
     * @see #setClassLoaderParent(ClassLoader)
     */
    public final ClassLoader getClassLoaderParent() {
        return classLoaderParent;
    }

    /**
     * Returns a list of writers set by {@link #addCodeWriter(PrintStream)} or {@link #addCodeWriter(PrintWriter)}.
     * 
     * @return a list of writers
     */
    public final List<PrintWriter> getCodeWriters() {
        return new ArrayList<>(codePrinters);
    }

    /**
     * The default package as set by {@link #setDefaultPackage(Package)} or {@link #setDefaultPackage(String)}. Or
     * {@code null} if no default package has been set.
     * 
     * @return the default package
     */
    public final String getDefaultPackage() {
        return packageName;
    }

    /**
     * Returns any source path set by {@link #setSourcePath(String)} or {@link #setSourcePath(Path)}.
     * 
     * @return any source path that been set
     */
    public final Path getSourcePath() {
        return sourcePath;
    }

    /**
     * Sets the class loader that should be used as a parent for generated classes. If no class loader parent is defined
     * by this method. The code using dynamic code generation will use the class loader available from
     * <code>Thread.currentThread().getContextClassLoader()</code> as a parent. This class loader is acquired when the
     * container is constructed via its constructor, {@link org.cakeframework.container.ContainerConfiguration#create()}
     * or a similar method.
     * 
     * @param parentClassLoader
     *            the class loader parent
     * @return this configuration
     * @see #getClassLoaderParent()
     */
    public final CodegenConfiguration setClassLoaderParent(ClassLoader parentClassLoader) {
        this.classLoaderParent = parentClassLoader;
        return this;
    }

    /**
     * Sets the package that all generated classes are located in.
     * 
     * Equivalent to calling:
     * 
     * <pre>
     * setPackage(p.getName());
     * </pre>
     * 
     * @param defaultPackage
     *            the package
     * @return this configuration
     */
    public final CodegenConfiguration setDefaultPackage(Package defaultPackage) {
        return setDefaultPackage(defaultPackage == null ? null : defaultPackage.getName());
    }

    /**
     * Sets the package that all generated classes are located in.
     * 
     * @param defaultPackage
     *            the package
     * @return this configuration
     */
    public final CodegenConfiguration setDefaultPackage(String defaultPackage) {
        // Do we want to verify it???
        // regex ^([a-z_]{1}[a-z0-9_]*(\.[a-z_]{1}[a-z0-9_]*)*)$
        this.packageName = defaultPackage;
        return this;
    }

    /**
     * Sets a source path where all generated source code is written to. This can be useful, for example, for debugging
     * generated classes. For example, an IDE can be set up to point to the specified source path specified in this
     * method.
     * <p>
     * The placement of files are relative to the specified source path. Matching any package naming scheme set by
     * {@link #setDefaultPackage(String)}.
     * 
     * @param directory
     *            the output directory
     * @return this configuration
     */
    public final CodegenConfiguration setSourcePath(Path directory) {
        this.sourcePath = directory;
        return this;
    }

    /**
     * Sets a source path where all generated source code is written to.
     * <p>
     * Equivalent to calling:
     * 
     * <pre>
     * setSourcePath(Paths.get(directory));
     * </pre>
     * 
     * @param directory
     *            the root
     * @return this configuration
     */
    public final CodegenConfiguration setSourcePath(String directory) {
        return setSourcePath(directory == null ? null : Paths.get(directory));
    }
}
