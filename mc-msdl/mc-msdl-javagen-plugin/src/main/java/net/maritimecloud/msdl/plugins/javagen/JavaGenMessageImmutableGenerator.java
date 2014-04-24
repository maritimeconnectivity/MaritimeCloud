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

import net.maritimecloud.msdl.model.FieldDeclaration;
import net.maritimecloud.msdl.model.type.MSDLBaseType;
import net.maritimecloud.msdl.parser.antlr.StringUtil;

import org.cakeframework.internal.codegen.CodegenClass;
import org.cakeframework.internal.codegen.CodegenMethod;

/**
 *
 * @author Kasper Nielsen
 */
class JavaGenMessageImmutableGenerator {
    final JavaGenMessageGenerator g;

    final CodegenClass c;

    JavaGenMessageImmutableGenerator(JavaGenMessageGenerator g) {
        this.g = g;
        this.c = g.c.newInnerClass();
        c.setDefinition("static class Immutable extends ", g.c.getSimpleName());
        c.addJavadoc("An immutable version of ", g.c.getSimpleName(), ".");
    }

    void generate() {
        CodegenMethod m = g.c.newMethod("public ", g.c.getSimpleName(), " immutable()");
        m.addAnnotation(Override.class).addJavadoc("{@inheritDoc}");
        m.add("return new Immutable(this);");
        generateConstructor();
        generateImmutable();
    }

    void generateConstructor() {

        CodegenMethod m = c.newMethod("Immutable(", g.c.getSimpleName(), " instance)");
        m.addJavadoc("Creates a new Immutable instance.");
        m.addJavadocParameter("instance", "the instance to make an immutable copy of");

        m.add("super(instance);");
    }

    void generateImmutable() {

        CodegenMethod m = c.newMethod("public ", g.c.getSimpleName(), " immutable()");
        m.addAnnotation(Override.class).addJavadoc("{@inheritDoc}");
        m.add("return this;");

        for (FieldDeclaration f : g.fields) {
            String beanPrefix = StringUtil.capitalizeFirstLetter(f.getName());
            MSDLBaseType t = f.getType().getBaseType();
            if (t == MSDLBaseType.LIST || t == MSDLBaseType.SET || t == MSDLBaseType.MAP) {
                m = g.generateComplexAccessor(c, f);
            } else {
                // HAS

                // SETTER
                m = c.newMethod("public ", g.c.getSimpleName(), " set", beanPrefix, "(",
                        new JavaGenType(f.getType()).render(), " ", f.getName(), ")");
            }
            m.addAnnotation(Override.class).addJavadoc("{@inheritDoc}");
            m.add("throw new UnsupportedOperationException(\"Instance is immutable\");");
        }
    }
}
