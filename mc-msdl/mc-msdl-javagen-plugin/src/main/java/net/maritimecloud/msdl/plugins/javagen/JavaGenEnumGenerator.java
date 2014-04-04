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

import net.maritimecloud.core.message.MessageEnumParser;
import net.maritimecloud.core.message.MessageEnum;
import net.maritimecloud.msdl.model.EnumDeclaration;
import net.maritimecloud.msdl.model.EnumConstantDeclaration;

import org.cakeframework.internal.codegen.CodegenClass;
import org.cakeframework.internal.codegen.CodegenEnum;
import org.cakeframework.internal.codegen.CodegenMethod;

import com.google.common.base.CaseFormat;

/**
 * Takes care of generating a Java enum from an {@link EnumDeclaration}.
 *
 * @author Kasper Nielsen
 */
class JavaGenEnumGenerator {

    static CodegenEnum generateEnum(CodegenClass parent, EnumDeclaration def) {
        CodegenEnum c = new CodegenEnum();
        c.addImport(MessageEnum.class);
        c.setDefinition("public enum ", def.getName(), " implements ", MessageEnum.class);

        for (EnumConstantDeclaration ed : def) {
            String s = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, ed.getName());
            s += "(" + ed.getValue() + ")";
            c.newConstant(s);
        }

        c.addImport(MessageEnumParser.class);
        c.addFieldWithJavadoc("An enum parser that can create new instances of this class.", "public static final ",
                MessageEnumParser.class, "<", def.getName(), "> PARSER = new Parser();");

        c.addField("private final int value;");
        CodegenMethod m = c.addNewMethod();
        m.setDeclaration("private ", def.getName(), "(int value)");
        m.add("this.value = value;");

        CodegenMethod getValue = c.addNewMethod();
        getValue.addJavadoc("{@inheritDoc}").addAnnotation(Override.class);
        getValue.setDeclaration("public int getValue()");
        getValue.add("return value;");

        CodegenMethod getString = c.addNewMethod();
        getString.addJavadoc("{@inheritDoc}").addAnnotation(Override.class);
        getString.setDeclaration("public String getName()");
        getString.add("return toString();");

        CodegenMethod valueOf = c.addNewMethod();
        valueOf.setDeclaration("public static ", def.getName(), " valueOf(int value)");
        valueOf.add("switch (value) {");
        for (EnumConstantDeclaration ed : def) {
            String s = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, ed.getName());
            valueOf.add("case ", ed.getValue(), ": return ", s, ";");

        }
        valueOf.add("default: return null;");
        valueOf.add("}");

        createEnumParser(c);
        return c;
    }

    private static void createEnumParser(CodegenEnum c) {
        CodegenClass i = c.newInnerClass();
        i.addImport(MessageEnumParser.class);
        i.setDefinition("static class Parser implements ", MessageEnumParser.class, "<", c.getSimpleName(), ">");

        CodegenMethod m = i.newMethod("public ", c.getSimpleName(), " from(int value)");
        m.addJavadoc("{@inheritDoc}").addAnnotation(Override.class);
        m.add("return ", c.getSimpleName(), ".valueOf(value);");

        m = i.newMethod("public ", c.getSimpleName(), " from(String name)");
        m.addJavadoc("{@inheritDoc}").addAnnotation(Override.class);
        m.throwNewUnsupportedOperationException("not implemented yet");
    }
}
