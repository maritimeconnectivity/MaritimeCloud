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
package net.maritimecloud.msdl.plugins.javagen;

import net.maritimecloud.internal.msdl.parser.antlr.StringUtil;
import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.FieldOrParameter;

import org.cakeframework.internal.codegen.CodegenClass;
import org.cakeframework.internal.codegen.CodegenMethod;

/**
 *
 * @author Kasper Nielsen
 */
class JavaGenMessageImmutableGenerator {
    final JavaGenMessageGenerator g;

    final CodegenClass c;

    private JavaGenMessageImmutableGenerator(JavaGenMessageGenerator g) {
        this.g = g;
        this.c = g.c.addInnerClass();
        c.setDefinition("static class Immutable extends ", g.c.getSimpleName());
        c.addJavadoc("An immutable version of ", g.c.getSimpleName(), ".");
    }

    static void generate(JavaGenMessageGenerator g) {
        CodegenMethod m = g.c.addMethod("public ", g.c.getSimpleName(), " immutable()");
        m.addAnnotation(Override.class).addJavadoc("{@inheritDoc}");
        if (!g.isMessage) {
            m.throwNewUnsupportedOperationException("method not supported");
        } else if (g.fields.isEmpty()) {
            m.add("return this;");
        } else {
            m.add("return new Immutable(this);");
            JavaGenMessageImmutableGenerator ig = new JavaGenMessageImmutableGenerator(g);
            ig.generateConstructor();
            ig.generateImmutable();
        }
    }

    void generateConstructor() {

        CodegenMethod m = c.addMethod("Immutable(", g.c.getSimpleName(), " instance)");
        m.addJavadoc("Creates a new Immutable instance.");
        m.addJavadocParameter("instance", "the instance to make an immutable copy of");

        m.add("super(instance);");
    }

    void generateImmutable() {

        CodegenMethod m = c.addMethod("public ", g.c.getSimpleName(), " immutable()");
        m.addAnnotation(Override.class).addJavadoc("{@inheritDoc}");
        m.add("return this;");

        for (FieldOrParameter f : g.fields) {
            String beanPrefix = StringUtil.capitalizeFirstLetter(f.getName());
            BaseType t = f.getType().getBaseType();
            if (t == BaseType.LIST || t == BaseType.SET || t == BaseType.MAP) {
                m = g.generateComplexAccessor(c, f);
                // CodegenMethod cm = g.generateComplexAccessorAll(c, f);
                // cm.addAnnotation(Override.class).addJavadoc("{@inheritDoc}");
                // cm.add("throw new UnsupportedOperationException(\"Instance is immutable\");");
            } else {
                // HAS

                // SETTER
                m = c.addMethod("public ", g.c.getSimpleName(), " set", beanPrefix, "(",
                        new JavaGenType(f.getType()).render(c, g.file), " ", f.getName(), ")");
            }
            m.addAnnotation(Override.class).addJavadoc("{@inheritDoc}");
            m.add("throw new UnsupportedOperationException(\"Instance is immutable\");");
        }
    }
}
