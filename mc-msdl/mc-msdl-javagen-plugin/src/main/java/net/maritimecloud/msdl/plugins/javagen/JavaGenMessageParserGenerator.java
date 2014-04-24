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

import org.cakeframework.internal.codegen.CodegenClass;
import org.cakeframework.internal.codegen.CodegenMethod;

/**
 *
 * @author Kasper Nielsen
 */
class JavaGenMessageParserGenerator {

    static void generateParser(JavaGenMessageGenerator g) {
        CodegenClass c = g.c.newInnerClass();
        c.setDefinition("static class Parser extends ", MessageParser.class, "<", g.c.getSimpleName(), ">");
        c.addJavadoc("A parser for parsing instances of ", g.c.getSimpleName(), ".");

        CodegenMethod m = c.newMethod("public ", g.c.getSimpleName(), " parse(", MessageReader.class,
                " reader) throws IOException");
        m.addImport(MessageReader.class);
        m.addAnnotation(Override.class).addJavadoc("{@inheritDoc}");
        if (g.fields.isEmpty()) {
            m.add("return new ", g.c.getSimpleName(), "();");
        } else {
            m.add("return new ", g.c.getSimpleName(), "(reader);");
        }
    }
}
