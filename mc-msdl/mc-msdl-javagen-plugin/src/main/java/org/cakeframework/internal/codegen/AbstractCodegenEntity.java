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
import static org.cakeframework.internal.codegen.CodegenUtil.toStringg;

import java.util.ArrayList;

/**
 * 
 * @author Kasper Nielsen
 */
public class AbstractCodegenEntity {

    ArrayList<DeclaredField> fieldDefinitions;

    ImportSet importSet;

    ArrayList<CodegenClass> innerClasses;

    AbstractCodegenEntity parent;

    AbstractCodegenEntity addField(Object... definition) {
        return addField(toStringg(definition));
    }

    AbstractCodegenEntity addField(String field) {
        fields().add(new DeclaredField(field));
        return this;
    }

    AbstractCodegenEntity addFieldWithJavadoc(String javadoc, Object... definition) {
        fields().add(new DeclaredField(toStringg(definition), javadoc));
        return this;
    }

    AbstractCodegenEntity addImport(Class<?>... classes) {
        imports().add(classes);
        return this;
    }

    public AbstractCodegenEntity addImport(String name) {
        try {
            return addImport(Class.forName(name));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds the specified class to the list of imports. If the specified class is an array type of any dimension. The
     * component type will be added.
     * 
     * @param clazz
     *            the class to add to imports.
     * @return this class
     */
    public AbstractCodegenEntity addImport(Class<?> clazz) {
        imports().add(clazz);
        return this;
    }

    AbstractCodegenEntity addImports(ImportSet imports) {
        imports().add(imports);
        return this;
    }

    public final void addInnerClass(CodegenClass innerClass) {
        if (innerClass.getPackage() != null) {// or just ignore??
            throw new IllegalStateException("Cannot add an inner class with a package name");
        }
        // We do not allow nested inner classes, always add to top class
        CodegenClass topClass = getTopClass();
        topClass.innerClasses().add(innerClass);
        innerClass.setParent(topClass);
    }

    int depth() {
        int depth = 0;
        AbstractCodegenEntity p = parent;
        while (p != null) {
            depth++;
            p = p.parent;
        }
        return depth;
    }

    /**
     * Returns a list of all field definitions.
     * 
     * @return a list of fields definitions
     */
    final ArrayList<DeclaredField> fields() {
        if (fieldDefinitions != null) {
            return fieldDefinitions;
        } else if (parent != null && !(this instanceof CodegenClass)) {
            return fieldDefinitions = parent.fields();
        } else {
            return fieldDefinitions = new ArrayList<>(5);
        }
    }

    private CodegenClass getTopClass() {
        AbstractCodegenEntity e = this;
        while (e.parent != null) {
            e = e.parent;
        }
        return (CodegenClass) e;
    }

    public CodegenClass getDeclaringClass() {
        AbstractCodegenEntity e = this;
        while (e.parent != null) {
            e = e.parent;
            if (e instanceof CodegenClass) {
                break;
            }
        }
        return (CodegenClass) e;
    }

    /**
     * A set of all imports.
     * 
     * @return the imports for this entity
     */
    public final ImportSet imports() {
        if (importSet != null) {
            return importSet;
        } else if (parent != null) {
            return importSet = parent.imports();
        } else {
            return importSet = new ImportSet();
        }
    }

    /**
     * Returns a list of all field definitions.
     * 
     * @return a list of fields definitions
     */
    final ArrayList<CodegenClass> innerClasses() {
        if (innerClasses != null) {
            return innerClasses;
        } else if (parent != null && !(this instanceof CodegenClass)) {
            return innerClasses = parent.innerClasses();
        } else {
            return innerClasses = new ArrayList<>(5);
        }
    }

    public boolean isParentInstanceOf(Class<?> type) {
        return parent != null && parent.getClass().isAssignableFrom(type);
    }

    void setParent(AbstractCodegenEntity parent) {
        if (this.parent != null) {
            throw new IllegalStateException(parent + " has already been added to another parent");
        }
        this.parent = requireNonNull(parent);
        if (importSet != null) {
            if (parent.importSet != null) {
                parent.importSet.add(importSet);
            } else {// set all parents imports to this.imports
                AbstractCodegenEntity p = parent;
                while (p != null) {
                    p.importSet = importSet;
                    p = p.parent;
                }
            }
        }
        // This is strictly only needed if we allow to add fields on methods
        if (fieldDefinitions != null && !(this instanceof CodegenClass)) {
            if (parent.fieldDefinitions != null) {
                parent.fieldDefinitions.addAll(fieldDefinitions);
            } else {// set all parents fields to this.fields
                AbstractCodegenEntity p = parent;
                while (p != null) {
                    p.fieldDefinitions = fieldDefinitions;
                    p = p.parent;
                }
            }
        }

        if (innerClasses != null && !(this instanceof CodegenClass)) {
            if (parent.innerClasses != null) {
                parent.innerClasses.addAll(innerClasses);
            } else {// set all parents fields to this.fields
                AbstractCodegenEntity p = parent;
                while (p != null) {
                    p.innerClasses = innerClasses;
                    p = p.parent;
                }
            }
        }
    }
}
