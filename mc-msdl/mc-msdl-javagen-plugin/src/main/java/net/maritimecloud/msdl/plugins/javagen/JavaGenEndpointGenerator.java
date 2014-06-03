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
import java.util.List;
import java.util.stream.Collectors;

import net.maritimecloud.core.message.MessageReader;
import net.maritimecloud.core.message.MessageSerializable;
import net.maritimecloud.msdl.model.EndpointDefinition;
import net.maritimecloud.msdl.model.EndpointFunction;
import net.maritimecloud.msdl.model.FieldDeclaration;
import net.maritimecloud.msdl.parser.antlr.StringUtil;
import net.maritimecloud.net.broadcast.MessageContext;
import net.maritimecloud.net.endpoint.EndpointImplementation;
import net.maritimecloud.net.endpoint.EndpointInvocationFuture;
import net.maritimecloud.net.endpoint.EndpointInvocator;
import net.maritimecloud.net.endpoint.EndpointLocal;

import org.cakeframework.internal.codegen.CodegenBlock;
import org.cakeframework.internal.codegen.CodegenClass;
import org.cakeframework.internal.codegen.CodegenMethod;

/**
 *
 * @author Kasper Nielsen
 */
public class JavaGenEndpointGenerator {

    final CodegenClass cClient;

    final CodegenClass cServer;

    final CodegenClass parent;

    final EndpointDefinition ed;

    final List<EndpointFunction> functions;

    JavaGenEndpointGenerator(CodegenClass parent, EndpointDefinition ed) {
        this.parent = parent;
        this.ed = ed;
        this.functions = ed.getFunctions();
        this.cClient = new CodegenClass();
        this.cServer = new CodegenClass();
    }


    /** {@inheritDoc} */
    JavaGenEndpointGenerator generate() {
        generateClient();
        generateServer();
        return this;
    }

    void generateClient() {
        cClient.addImport(EndpointLocal.class);
        cClient.addImport(EndpointInvocator.class);
        cClient.addImport(EndpointInvocationFuture.class);
        cClient.setDefinition("public final class ", ed.getName(), " extends ", EndpointLocal.class);// , "<",

        cClient.addField("public static final String NAME = \"", ed.getName(), "\";");
        CodegenMethod con = cClient.newMethod("public ", cClient.getSimpleName(), "(EndpointInvocator ei)");
        con.add("super(ei);");

        for (EndpointFunction f : functions) {
            CodegenMethod m = cClient.newMethod("public ", generateSignature(cClient, f, true));
            String className = StringUtil.capitalizeFirstLetter(f.getName());
            String args = f.getArguments().stream().map(e -> e.getName()).collect(Collectors.joining(", "));
            m.add("return invoke(\"", ed.getName(), ".", f.getName(), "\", new ", className, "(", args, "));");
            generateTmpClass(className, f);
        }
    }

    void generateTmpClass(String className, EndpointFunction ef) {
        cClient.addImport(MessageSerializable.class);
        CodegenClass cc = cClient.newInnerClass("static final class ", className, " implements ",
                MessageSerializable.class);
        for (FieldDeclaration f : ef.getArguments()) {
            JavaGenType ty = new JavaGenType(f.getType());
            cc.addField("private final ", ty.render(), " ", f.getName(), ";");
        }
        if (ef.getArguments().size() > 0) {
            String args = ef.getArguments().stream().map(e -> JavaGenType.render(e.getType()) + " " + e.getName())
                    .collect(Collectors.joining(", "));
            CodegenMethod con = cc.newMethod(className, "(", args, ")");
            for (FieldDeclaration f : ef.getArguments()) {
                con.add("this.", f.getName(), " = ", f.getName(), ";");
            }
        }
        JavaGenMessageGenerator.generateWriteTo(cc, ef.getArguments());
    }

    void generateServer() {
        cServer.setDefinition("public abstract class Abstract", ed.getName(), " implements ",
                EndpointImplementation.class);
        cServer.addImport(EndpointImplementation.class);
        for (EndpointFunction f : functions) {
            cServer.newMethod("protected abstract ", generateSignature(cServer, f, false));
            // m.throwNewUnsupportedOperationException("Method is not supported");
        }


        cServer.addImport(MessageContext.class, MessageReader.class, IOException.class);
        CodegenMethod m = cServer.newMethod("public final ", Object.class, " invoke(", String.class, " name, ",
                MessageContext.class, " context, ", MessageReader.class, " reader) throws ", IOException.class);

        for (EndpointFunction f : functions) {
            CodegenBlock b = m.newNestedBlock("if (name.equals(\"", f.getName(), "\"))");
            for (FieldDeclaration fd : f.getArguments()) {
                b.add(JavaGenType.render(fd.getType()), " ", fd.getName(), " = null;");
            }
            String args = f.getArguments().stream().map(e -> e.getName()).collect(Collectors.joining(", "));
            String met = f.getName() + "(context" + (args.length() > 0 ? ", " : "") + args + ");";
            if (f.getReturnType() == null) {
                b.add(met);
                b.add("return null;");
            } else {
                b.add("return ", met);
            }
        }

        m.throwNewUnsupportedOperationException("Unknown method '\" + name + \"'");

        CodegenMethod name = cServer.newMethod("public final String getEndpointName()");
        name.add("return \"", ed.getName(), "\";");

        // /Object invoke(String name, MessageContext context, MessageReader reader) throws IOException;

    }

    String generateSignature(CodegenClass cc, EndpointFunction f, boolean isClient) {
        String s = type(f, isClient) + " " + f.getName() + "(";
        boolean first = true;
        if (!isClient) {
            cc.addImport(MessageContext.class);
            s += MessageContext.class.getSimpleName() + " context";
            first = false;
        }

        for (FieldDeclaration d : f.getArguments()) {
            if (!first) {
                s += ", ";
            }
            first = false;
            s += new JavaGenType(d.getType()).render();
            s += " " + d.getName();
        }
        return s + ")";
    }

    String type(EndpointFunction f, boolean isClient) {
        String s;
        if (f.getReturnType() == null) {
            s = isClient ? "Void" : "void";
        } else {
            JavaGenType los = new JavaGenType(f.getReturnType());
            // if (f.getReturnType().getBaseType()
            // .isAnyOf(BaseType.BOOL, BaseType.BOOL, BaseType.BOOL, BaseType.BOOL, BaseType.BOOL)) {
            //
            // } else {
            s = los.render();
            // }
        }
        if (isClient) {
            s = EndpointInvocationFuture.class.getSimpleName() + "<" + s + ">";
        }
        return s;
    }
}
