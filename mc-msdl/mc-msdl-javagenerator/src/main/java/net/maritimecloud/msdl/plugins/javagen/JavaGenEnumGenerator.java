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

import java.io.IOException;

import net.maritimecloud.message.MessageEnum;
import net.maritimecloud.message.MessageEnumSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.msdl.model.EnumDeclaration;
import net.maritimecloud.msdl.model.EnumDeclaration.Constant;

import org.cakeframework.internal.codegen.CodegenClass;
import org.cakeframework.internal.codegen.CodegenEnum;
import org.cakeframework.internal.codegen.CodegenMethod;

/**
 * Takes care of generating a Java enum from an {@link EnumDeclaration}.
 *
 * @author Kasper Nielsen
 */
class JavaGenEnumGenerator {

    private static void createEnumSerializer(CodegenEnum c, EnumDeclaration def) {
        // The serializer is added as an inner class.
        CodegenClass i = c.addInnerClass();
        i.addImport(MessageEnumSerializer.class);
        i.setDefinition("static class Serializer extends ", MessageEnumSerializer.class, "<", c.getSimpleName(), ">");

        CodegenMethod m = i.addMethod("public ", c.getSimpleName(), " from(int value)");
        m.addJavadoc("{@inheritDoc}").addAnnotation(Override.class);
        m.add("return ", c.getSimpleName(), ".valueOf(value);");

        m = i.addMethod("public ", c.getSimpleName(), " from(String name)");
        m.addJavadoc("{@inheritDoc}").addAnnotation(Override.class);

        m.add("switch (name) {");
        for (Constant ed : def) {
            m.add("case \"", ed.getName(), "\": return ", c.getSimpleName(), ".", ed.getName(), ";");
        }
        m.add("default: return null;");
        m.add("}");


        c.addImport(MessageWriter.class, IOException.class);

        m = i.addMethod("public void write(int tag, String name,", c.getSimpleName(),
                " value, MessageWriter writer) throws IOException");

        m.add("writer.writeEnum(tag, name, value);");
    }


    static CodegenEnum generateEnum(CodegenClass parent, EnumDeclaration def) {
        CodegenEnum c = new CodegenEnum();
        c.addImport(MessageEnum.class);
        c.setDefinition("public enum ", def.getName(), " implements ", MessageEnum.class);

        for (Constant ed : def) {
            String s = ed.getName();// CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, ed.getName());
            s += "(" + ed.getValue() + ")";
            c.newConstant(s);
        }

        c.addImport(MessageEnumSerializer.class);
        c.addFieldWithJavadoc("An enum parser that can create new instances of this class.", "public static final ",
                MessageEnumSerializer.class, "<", def.getName(), "> SERIALIZER = new Serializer();");

        c.addField("private final int value;");
        CodegenMethod m = c.addMethod("private ", def.getName(), "(int value)");
        m.add("this.value = value;");

        CodegenMethod getValue = c.addMethod("public int getValue()");
        getValue.addJavadoc("{@inheritDoc}").addAnnotation(Override.class);
        getValue.add("return value;");

        CodegenMethod getString = c.addMethod("public String getName()");
        getString.addJavadoc("{@inheritDoc}").addAnnotation(Override.class);
        getString.add("return toString();");

        CodegenMethod valueOf = c.addMethod("public static ", def.getName(), " valueOf(int value)");
        valueOf.add("switch (value) {");
        for (Constant ed : def) {
            String s = ed.getName();// CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, ed.getName());
            valueOf.add("case ", ed.getValue(), ": return ", s, ";");

        }
        valueOf.add("default: return null;");
        valueOf.add("}");

        createEnumSerializer(c, def);
        return c;
    }
}
