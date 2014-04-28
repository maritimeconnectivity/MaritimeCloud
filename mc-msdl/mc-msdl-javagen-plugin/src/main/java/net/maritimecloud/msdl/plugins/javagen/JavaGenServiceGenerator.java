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

import net.maritimecloud.msdl.model.BroadcastMessageDeclaration;
import net.maritimecloud.msdl.model.ServiceDeclaration;

import org.cakeframework.internal.codegen.CodegenClass;

/**
 *
 * @author Kasper Nielsen
 */
public class JavaGenServiceGenerator {
    final CodegenClass c = new CodegenClass();

    final ServiceDeclaration msg;

    JavaGenServiceGenerator(ServiceDeclaration msg) {
        this.msg = msg;
    }

    void generateClass() {

        c.setDefinition("public class ", msg.getName());

        // c.addFieldWithJavadoc("A message parser that can create new instances of this class.",
        // "public static final ",
        // MessageParser.class, "<", msg.getName(), "> PARSER = new Parser();");
    }

    JavaGenServiceGenerator generate() {
        generateClass();
        generateBroadcastMessages();
        return this;
    }

    void generateBroadcastMessages() {
        for (BroadcastMessageDeclaration d : msg.getBroadcastMessages()) {
            new JavaGenBroadcastMessageGenerator(c, d).generate(msg);
        }
    }
}
