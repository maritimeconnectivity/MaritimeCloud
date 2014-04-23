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

/**
 *
 * @author Kasper Nielsen
 */
public class CodegenMethod extends CodegenBlock {
    private final AnnotationSupport annotations = new AnnotationSupport();

    // as defined in http://download.oracle.com/javase/tutorial/java/javaOO/methods.html
    private String declaration;

    private final JavadocBuilder builder = new JavadocBuilder();

    /**
     * @return the builder
     */
    public JavadocBuilder javadoc() {
        return builder;
    }

    public CodegenMethod addJavadoc(Object... definition) {
        return addJavadoc(toStringg(definition));
    }

    public CodegenMethod addJavadoc(String javadoc) {
        builder.set(javadoc);
        return this;
    }

    public CodegenMethod addJavadocParameter(String paramName, String paramValue) {
        builder.addParameter(paramName, paramValue);
        return this;
    }

    public CodegenMethod addJavadocParameter(String paramName, Object... definition) {
        return addJavadocParameter(paramName, toStringg(definition));
    }

    public CodegenMethod addAnnotation(String annotation) {
        return annotations.addAnnotation(this, annotation);
    }

    public CodegenMethod addAnnotation(Class<?> annotation) {
        return annotations.addAnnotation(this, annotation);
    }

    public final CodegenMethod addParameter(Object... definition) {
        return addParameter(toStringg(definition));
    }

    public final CodegenMethod addParameter(String parameter) {
        if (declaration == null) {
            throw new IllegalStateException("Method declaration must be set before extra parameters can be added");
        }
        int l = declaration.indexOf('(');
        int r = declaration.indexOf(')');
        declaration = declaration.substring(0, r) + (r == l + 1 ? "" : ", ") + parameter + declaration.substring(r);
        return this;
    }

    public final String getDeclaration() {
        return declaration;
    }

    public final CodegenMethod setDeclaration(Object... definition) {
        return setDeclaration(toStringg(definition));
    }

    public final CodegenMethod setDeclaration(String declaration) {
        if (this.declaration != null) {
            throw new IllegalStateException("Declaration has already been set");
        }
        this.declaration = requireNonNull(declaration);
        return this;
    }

    public final CodegenMethod throwNewUnsupportedOperationException(String message) {
        add("throw new UnsupportedOperationException(\"", message, "\");");
        return this;
    }

    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    StringBuilder toString(StringBuilder sb) {
        if (declaration == null) {
            throw new IllegalStateException("Method declaration has not been set, type="
                    + getClass().getCanonicalName());
        }
        // I think we can move some of this up to set declaration

        boolean isAbstract = declaration.startsWith("abstract ") || declaration.contains(" abstract ")
                || getDeclaringClass().getType() == CodegenClass.Type.INTERFACE;
        boolean isConstructor = true;
        int leftBracket = declaration.indexOf('(');
        String beforeBracket = leftBracket == -1 ? declaration : declaration.substring(0, leftBracket);
        for (String s : beforeBracket.split("\\s")) {
            if (s.length() > 0) {
                int m = CodegenUtil.getModifier(s);
                if (m == -1) {
                    isConstructor = false;
                    break;
                }
            }
        }
        if (isConstructor && parent != null && !declaration.equals("static")) {
            CodegenClass cc = (CodegenClass) parent;
            declaration = declaration.substring(0, leftBracket) + cc.getSimpleName()
                    + declaration.substring(leftBracket);
        }
        int indent = depth();
        builder.toString(sb, indent);
        annotations.toString(sb, indent);
        sb.append(indent(indent)).append(declaration);
        if (isAbstract) {
            sb.append(";");
        } else if (body == null) {
            return sb.append(" {}").append(LS);
        } else {
            sb.append(" {");
        }
        sb.append(LS);
        new BlockRenderer(indent + 1, sb, this).render();
        if (!isAbstract) {
            sb.append(indent(indent)).append("}");// dont add } to abstract methods
            sb.append(LS);
        }

        return sb;
    }
}
