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
import static org.cakeframework.internal.codegen.CodegenUtil.LS;
import static org.cakeframework.internal.codegen.CodegenUtil.indent;
import static org.cakeframework.internal.codegen.CodegenUtil.toStringg;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cakeframework.internal.codegen.CodegenEnum.CodegenEnumConstant;

/**
 *
 * @author Kasper Nielsen
 */
public class CodegenClass extends AbstractCodegenEntity {
    private final AnnotationSupport annotations = new AnnotationSupport();

    private ClassLoader classLoader;

    private String definition;

    private final ArrayList<CodegenMethod> methods = new ArrayList<>(5);

    private String license;

    /**
     * @return the license
     */
    public String getLicense() {
        return license;
    }

    /**
     * @param license
     *            the license to set
     */
    public CodegenClass setLicense(String license) {
        this.license = license;
        return this;
    }

    private String packageName;

    /** The simple name of the class, for example, Map for java.util.Map */
    private String simpleName;

    private Type type;

    private final JavadocBuilder javadoc = new JavadocBuilder();

    public CodegenClass addJavadoc(String javadoc) {
        this.javadoc.set(javadoc);
        return this;
    }

    public CodegenClass addJavadoc(Object... definition) {
        return addJavadoc(toStringg(definition));
    }

    public CodegenClass addAnnotation(String annotation) {
        return annotations.addAnnotation(this, annotation);
    }

    public CodegenClass addAnnotation(Class<?> annotation) {
        return annotations.addAnnotation(this, annotation);
    }

    @Override
    public CodegenClass addField(Object... definition) {
        return (CodegenClass) super.addField(definition);
    }

    @Override
    public CodegenClass addField(String field) {
        return (CodegenClass) super.addField(field);
    }

    /** {@inheritDoc} */
    @Override
    public CodegenClass addFieldWithJavadoc(String javadoc, Object... definition) {
        return (CodegenClass) super.addFieldWithJavadoc(javadoc, definition);
    }

    @Override
    public CodegenClass addImport(Class<?>... classes) {
        return (CodegenClass) super.addImport(classes);
    }

    @Override
    public CodegenClass addImports(ImportSet imports) {
        return (CodegenClass) super.addImports(imports);
    }

    public final CodegenMethod addMethod(boolean predicate, CodegenMethod method) {
        if (predicate) {
            addMethod(method);
        }
        return method;
    }

    public final CodegenMethod addMethod(CodegenMethod method) {
        methods.add(requireNonNull(method));
        method.setParent(this);
        return method;
    }

    public final void addMethods(CodegenMethod... methods) {
        for (CodegenMethod o : methods) {
            addMethod(o);
        }
    }

    public final CodegenMethod addNewMethod() {
        return addMethod(new CodegenMethod());
    }

    @SuppressWarnings("unchecked")
    public final <T> Class<T> compile() {
        if (definition == null) {
            throw new IllegalStateException("No definition set");
        }
        if (classLoader == null) {
            throw new IllegalStateException("A classloader must be set via #setClassLoader");
        }
        try {
            return (Class<T>) classLoader.loadClass(getQualifiedName());
        } catch (ClassNotFoundException e) {
            if (e.getCause() != null) {
                throw new IllegalStateException(e.getMessage(), e.getCause());
            }
            // /CLOVER:OFF, We are are compiling this class, so this should
            throw new RuntimeException("Class not found, this is strange, please open a bug report", e);
            // /CLOVER:ON
        }
    }

    public final Object compileAndInstantiate(Class<?> singleConstructor, Object parameter) {
        return compileAndInstantiate(new Class<?>[] { singleConstructor }, parameter);
    }

    private final Object compileAndInstantiate(Class<?>[] constructorParameters, Object... parameters) {
        Class<?> clazz = compile();
        try {
            Constructor<?> con = clazz.getConstructor(constructorParameters);
            return con.newInstance(parameters);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    "This is highly unexpected. Could not find constructor, available constructors = "
                            + Arrays.asList(clazz.getConstructors()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the classLoader
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public final String getDefinition() {
        return definition;
    }

    final String getJavaName() {
        return getQualifiedName().replace(".", "/") + ".java";
    }

    public final List<CodegenMethod> getMethods() {
        return methods;
    }

    /** @return the package name of this class, or {@code null} if no package name has been set */
    public final String getPackage() {
        return packageName;
    }

    public String getQualifiedName() {
        return packageName == null ? simpleName : packageName + "." + simpleName;
    }

    public String getSimpleName() {
        return requireNonNull(simpleName);
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    public final CodegenClass newInnerClass() {
        CodegenClass cc = new CodegenClass();
        addInnerClass(cc);
        return cc;
    }

    public final CodegenMethod newMethod(Object... definition) {
        return newMethod(toStringg(definition));
    }

    /** Creates a new method using the specified definition. */
    public final CodegenMethod newMethod(String definition) {
        CodegenMethod m = addMethod(new CodegenMethod());
        m.setDeclaration(definition);
        return m;
    }

    /**
     * @param classLoader
     *            the classLoader to set
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public final CodegenClass setDefinition(Object... definition) {
        return setDefinition(toStringg(definition));
    }

    public final CodegenClass setDefinition(String definition) {
        this.definition = definition;
        String[] split = definition.split("[< ]");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("class")) {
                simpleName = split[i + 1];
                type = Type.CLASS;
            } else if (split[i].equals("interface")) {
                simpleName = split[i + 1];
                type = Type.INTERFACE;
            } else if (split[i].equals("@interface")) {
                simpleName = split[i + 1];
                type = Type.ANNOTATION;
            } else if (split[i].equals("enum")) {
                simpleName = split[i + 1];
                type = Type.ENUM;
            }
        }
        return this;
    }

    public final CodegenClass setPackage(Package p) {
        return setPackage(p.getName());
    }

    public final CodegenClass setPackage(String packageName) {
        if (parent != null) {
            throw new IllegalStateException("Cannot add an inner class with a package name");
        }
        this.packageName = packageName;
        return this;
    }

    public final String toString() {
        return toString(new StringBuilder()).toString();
    }

    public Path writeSource(Path root) throws IOException {
        return writeSource(toString(new StringBuilder()), root);
    }

    Path writeSource(StringBuilder s, Path root) throws IOException {
        if (!Files.exists(root)) {
            throw new IllegalArgumentException("The specified root path does not exist, path = " + root);
        } else if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException("The specified root path was not a directory, path = " + root);
        }
        Path dir = root;
        String p = getPackage();
        if (p != null) {
            for (String ss : p.split("\\.")) {
                dir = dir.resolve(ss);
            }
        }
        Files.createDirectories(dir);
        Path file = dir.resolve(getSimpleName() + ".java");
        final ByteArrayOutputStream srcOS = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(srcOS, StandardCharsets.US_ASCII), true);
        pw.append(s).flush();
        byte[] bytes = srcOS.toByteArray();

        if (Files.exists(file)) {
            byte[] previous = Files.readAllBytes(file);
            if (Arrays.equals(bytes, previous)) {
                return null;
            }
        }
        try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(file));) {
            os.write(bytes);
        }
        return file;
    }

    final StringBuilder toString(StringBuilder sb) {
        if (parent == null) {
            if (license != null) {
                sb.append(license);
            }
            if (packageName != null) {
                sb.append("package ").append(packageName).append(";").append(LS).append(LS);
            }
            if (importSet != null) {
                importSet.toString(sb);
            }
        }
        int indent = depth();
        // if we are an inner class add a line shift
        if (indent > 0) {
            sb.append(LS);
        }
        javadoc.toString(sb, indent);
        annotations.toString(sb, indent);
        // add modifier, class name, extends
        sb.append(indent(indent)).append(definition).append(" {").append(LS);
        if (this instanceof CodegenEnum) {
            ArrayList<CodegenEnumConstant> list = ((CodegenEnum) this).constants;
            for (int i = 0; i < list.size(); i++) {
                CodegenEnumConstant cec = list.get(i);
                sb.append(indent(indent + 1)).append(cec.definition);
                if (!cec.methods.isEmpty()) {

                    sb.append(" {").append(LS);
                    for (CodegenMethod m : cec.methods) {
                        m.toString(sb);
                    }
                    sb.append(indent(indent + 1)).append("}");
                }
                if (i != list.size() - 1) {
                    sb.append(",");
                } else {
                    sb.append(";");
                }
                sb.append(LS);
            }
        }
        // add fields
        if (fieldDefinitions != null) {
            for (DeclaredField field : fieldDefinitions) {
                field.render(sb, indent + 1);
            }
        }
        for (CodegenMethod m : methods) {
            sb.append(LS);
            m.toString(sb);
        }
        if (innerClasses != null) {
            for (CodegenClass cc : innerClasses) {
                cc.toString(sb);
            }
        }
        sb.append(indent(indent)).append("}").append(LS);
        return sb;
    }

    // TopTypes: Class, Interface, Enum, Annotation,
    // Executable: Methods, Constructors
    enum Type {
        ANNOTATION, CLASS, INTERFACE, ENUM;
    }
}
