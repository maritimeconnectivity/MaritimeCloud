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

import static java.lang.reflect.Modifier.isPublic;
import static java.util.Objects.requireNonNull;
import static org.cakeframework.internal.codegen.CodegenUtil.LS;
import static org.cakeframework.internal.codegen.CodegenUtil.countOccurrences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * A set of unique class imports.
 * 
 * @author Kasper Nielsen
 */
public final class ImportSet {
    //
    // /** The standard eclipse formatter. */
    // public static final Function<String[], String[][]> ECLIPSE_IMPORT_SORTER = new StandardEclipseImportSorter();

    /** A set of all imports. */
    private final HashSet<String> imports = new HashSet<>();

    //
    // @SuppressWarnings("unused")
    // private final Function<String[], String[][]> importSorter = ECLIPSE_IMPORT_SORTER;

    /**
     * Equivalent to calling {@link #add(Class)} for each specified class.
     * 
     * @param classes
     *            the classes to add
     * @return this import set
     * @throws IllegalArgumentException
     *             if ant of the specified classes (or if array, component class) is not a public class. Or if
     *             attempting to import a class from the default package
     * @throws NullPointerException
     *             if classes is null
     */
    public ImportSet add(Class<?>... classes) {
        for (Class<?> c : classes) {
            add(c);
        }
        return this;
    }

    /**
     * Adds the specified class to the list of imports. If the specified class is an array type of any dimension. The
     * component type will be added.
     * <p>
     * if the specified class is null it is ignored.
     * 
     * @param clazz
     *            the class to add to imports.
     * @return this import set
     * @throws IllegalArgumentException
     *             if the specified class (or if array, component class) is not a public class. Or if attempting to
     *             import a class from the default package
     */
    public ImportSet add(Class<?> clazz) {
        if (clazz != null && clazz != Object.class) {
            // if the specified class is an array type, we need to find the component type
            while (clazz.isArray()) {
                clazz = clazz.getComponentType();
            }
            // Do not add primitive classes and Object.class
            if (!clazz.isPrimitive() && !clazz.equals(Object.class)) {
                if (!isPublic(clazz.getModifiers())) {
                    throw new IllegalArgumentException("Cannot import non-public class " + clazz.getName()
                            + ", consider making it public. Even though Java allows package private classes from the "
                            + "same package to be imported, this is not currently supported");
                }
                // Let us get the name to be imported
                String name = clazz.getCanonicalName();

                // No need to import packages from the default package
                if (!(name.startsWith("java.lang") && countOccurrences(name, '.') == 2)) {
                    if (!name.contains(".")) {
                        throw new IllegalArgumentException(
                                "It is a compile time error to import a type from the default package. type = " + name);
                    }
                    imports.add(name);
                }
            }
        }
        return this;
    }

    /**
     * Adds the specified import set to this set.
     * 
     * @param other
     *            the import set to add to this import set
     * @return this import set
     * @throws NullPointerException
     *             if the specified import set is null
     */
    public ImportSet add(ImportSet other) {
        imports.addAll(other.imports);
        return this;
    }

    /**
     * Returns a copy of this import set.
     * 
     * @return a copy of this import set
     */
    public ImportSet clone() {
        return new ImportSet().add(this);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ImportSet && imports.equals(((ImportSet) obj).imports);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return imports.hashCode();
    }

    @SuppressWarnings("unused")
    private String[][] orderImports(String[] imports) {
        ArrayList<String[]> importGroups = new ArrayList<>();

        return importGroups.toArray(new String[importGroups.size()][]);
    }

    /**
     * Returns the number of imports.
     * 
     * @return the number of imports
     */
    public int size() {
        return imports.size();
    }

    /** {@inheritDoc} */
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    /**
     * Writes the contents of this import set to the specified StringBuilder.
     * <p>
     * Before being written all imports are sorted using default eclipse import state formatting routine. Meaning
     * java.*, javax.*, org.*, com.* come first and then other classes in alphabetic order.
     * <p>
     * Unix line ending are used even on windows platforms.
     * 
     * @param sb
     *            the string builder to write to
     * @return the specified string builder
     * @throws NullPointerException
     *             if the specified string builder is null
     */
    public StringBuilder toString(StringBuilder sb) {
        requireNonNull(sb, "sb is null");
        if (imports.size() > 0) {
            String[] array = imports.toArray(new String[imports.size()]);
            int idx = toStringWriteGroup(sb, array, 0, "java.");
            idx = toStringWriteGroup(sb, array, idx, "javax.");
            idx = toStringWriteGroup(sb, array, idx, "org.");
            idx = toStringWriteGroup(sb, array, idx, "com.");

            // Add remaining imports in alphabetic order
            if (idx < array.length) {
                Arrays.sort(array, idx, array.length);
                String lastRootPackageName = ""; // xxx.*.*
                for (int j = idx; j < array.length; j++) {
                    String ss = array[j];
                    // New line for each dk.*.*, acme.*.*, net.*.*
                    String first = ss.substring(0, ss.indexOf('.'));
                    if (!first.equals(lastRootPackageName)) {
                        if (!lastRootPackageName.equals("")) {
                            sb.append(LS);// Add empty line
                        }
                        lastRootPackageName = first;
                    }
                    sb.append("import ").append(ss).append(";").append(LS);
                }
                sb.append(LS);// Add empty line after imports
            }
        }
        return sb;
    }

    /**
     * Writes a single group of imports. Basically it makes sure to insert some spaces between imports.
     * 
     * @param sb
     *            the string builder
     * @param array
     *            the array of string imports
     * @param offset
     *            the offset of the array
     * @param importPrefix
     *            the current import prefix
     * @return the next index into the specified array
     */
    private static int toStringWriteGroup(StringBuilder sb, String[] array, int offset, String importPrefix) {
        int i = offset;
        for (int j = offset; j < array.length; j++) {
            String s = array[j];
            if (s.startsWith(importPrefix)) {
                if (i != j) {
                    String tmp = array[i];
                    array[i] = s;
                    array[j] = tmp;
                }
                i++;
            }
        }
        if (i != offset) {
            Arrays.sort(array, offset, i);
            for (int j = offset; j < i; j++) {
                sb.append("import ").append(array[j]).append(";").append(LS);
            }
            sb.append(LS);// Add empty line
        }
        return i;
    }
    //
    // static class StandardEclipseImportSorter implements Function<String[], String[][]> {
    //
    // /** {@inheritDoc} */
    // @Override
    // public String[][] apply(String[] t) {
    // return null;
    // }
    // }
}
