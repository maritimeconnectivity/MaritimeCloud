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

import net.maritimecloud.core.message.MessageParser;
import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.msdl.model.FieldDeclaration;
import net.maritimecloud.msdl.model.type.AnyType;
import net.maritimecloud.msdl.model.type.ListOrSetType;
import net.maritimecloud.msdl.model.type.MSDLBaseType;
import net.maritimecloud.msdl.model.type.MapType;
import net.maritimecloud.msdl.parser.antlr.StringUtil;

import org.cakeframework.internal.codegen.CodegenClass;
import org.cakeframework.internal.codegen.CodegenMethod;

/**
 *
 * @author Kasper Nielsen
 */
class JavaGenMessageParserGenerator {
    final JavaGenMessageGenerator g;

    final CodegenClass c;

    JavaGenMessageParserGenerator(JavaGenMessageGenerator g) {
        this.g = g;
        this.c = g.c.newInnerClass();
        c.setDefinition("static class Parser extends ", MessageParser.class, "<", g.c.getSimpleName(), ">");
        c.addJavadoc("A parser for parsing instances of ", g.c.getSimpleName(), ".");
    }


    void generateParser() {
        CodegenMethod m = c.newMethod("public ", g.c.getSimpleName(), " parse(", MessageReader.class,
                " reader) throws IOException");
        m.addImport(MessageReader.class);
        m.addAnnotation(Override.class).addJavadoc("{@inheritDoc}");
        m.add(g.c.getSimpleName(), " instance = new ", g.c.getSimpleName(), "();");
        for (FieldDeclaration f : g.fields) {
            MSDLBaseType type = f.getType().getBaseType();
            if (type.isPrimitive()) {
                String s = StringUtil.capitalizeFirstLetter(type.name().toLowerCase());
                m.add("instance.", f.getName(), " = reader.read", s, "(", f.getTag(), ", \"", f.getName(), "\", null);");
            } else if (type == MSDLBaseType.ENUM) {
                JavaGenType ty = new JavaGenType(f.getType());
                m.add("instance.", f.getName(), " = reader.readEnum(", f.getTag(), ", \"", f.getName(), "\", ",
                        ty.render(), ".PARSER);");
            } else if (type == MSDLBaseType.MESSAGE) {
                JavaGenType ty = new JavaGenType(f.getType());
                m.add("instance.", f.getName(), " = reader.readMessage(", f.getTag(), ", \"", f.getName(), "\", ",
                        ty.render(), ".PARSER);");
            } else if (type == MSDLBaseType.LIST) { // Complex type
                ListOrSetType los = (ListOrSetType) f.getType();
                m.add("instance.", f.getName(), " = reader.readList(", f.getTag(), ", \"", f.getName(), "\", ",
                        complexParser(los.getElementType()), ");");
            } else if (type == MSDLBaseType.SET) { // Complex type
                ListOrSetType los = (ListOrSetType) f.getType();
                m.add("instance.", f.getName(), " = reader.readSet(", f.getTag(), ", \"", f.getName(), "\", ",
                        complexParser(los.getElementType()), ");");
            } else { // Complex type
                MapType los = (MapType) f.getType();
                m.add("instance.", f.getName(), " = reader.readMap(", f.getTag(), ", \"", f.getName(), "\", ",
                        complexParser(los.getKeyType()), ", ", complexParser(los.getValueType()), ");");
            }

            // if (f.getType() .isMessage()) {
            // m.add("instance.", f.getName(), " = ", f.getMSDLBaseType().getRefType(), ".PARSER.parse(reader);");
            // }
        }
        m.add("return instance;");
    }

    String complexParser(AnyType type) {
        MSDLBaseType b = type.getBaseType();
        if (b.isPrimitive()) {
            return MessageParser.class.getSimpleName() + ".OF_" + b.name().toUpperCase();
        } else if (b.isReferenceType()) {
            JavaGenType ty = new JavaGenType(type);
            return ty.render() + ".PARSER";
        } else if (b == MSDLBaseType.LIST) {
            ListOrSetType los = (ListOrSetType) type;
            return "MessageParser.ofList(" + complexParser(los.getElementType()) + ")";
        } else if (b == MSDLBaseType.SET) {
            ListOrSetType los = (ListOrSetType) type;
            return "MessageParser.ofSet(" + complexParser(los.getElementType()) + ")";
        } else {
            MapType los = (MapType) type;
            return "MessageParser.ofMap(" + complexParser(los.getKeyType()) + ", " + complexParser(los.getValueType())
                    + ")";
        }
    }
}
