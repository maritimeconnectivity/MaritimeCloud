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

import net.maritimecloud.internal.msdl.parser.antlr.StringUtil;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.EndpointDefinition;
import net.maritimecloud.msdl.model.EndpointMethod;
import net.maritimecloud.msdl.model.FieldOrParameter;
import net.maritimecloud.msdl.model.MsdlFile;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.LocalEndpoint;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.util.Binary;

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

    final List<EndpointMethod> functions;

    final MsdlFile file;

    JavaGenEndpointGenerator(CodegenClass parent, EndpointDefinition ed) {
        this.parent = parent;
        this.ed = ed;
        this.functions = ed.getFunctions();
        this.cClient = new CodegenClass();
        this.cServer = new CodegenClass();
        this.file = ed.getFile();
    }


    /** {@inheritDoc} */
    JavaGenEndpointGenerator generate() {
        generateClient();
        generateServer();
        return this;
    }

    void generateClient() {
        cClient.addImport(LocalEndpoint.class);
        cClient.addImport(EndpointInvocationFuture.class);

        cClient.setDefinition("public final class ", ed.getName(), " extends ", LocalEndpoint.class);// ,
        // "<",

        cClient.addFieldWithJavadoc("The name of the endpoint.", "public static final String NAME = \"", ed.getName(),
                "\";");
        CodegenMethod con = cClient.addMethod("public ", cClient.getSimpleName(), "(", LocalEndpoint.Invocator.class,
                " ei)");
        con.add("super(ei);");

        for (EndpointMethod f : functions) {
            CodegenMethod m = cClient.addMethod("public ", generateSignature(cClient, f, true));
            String className = StringUtil.capitalizeFirstLetter(f.getName());
            // String args = f.getParameters().stream().map(e -> e.getName()).collect(Collectors.joining(", "));
            m.add(className, " arguments = new ", className, "();");
            for (FieldOrParameter p : f.getParameters()) {
                m.add("arguments.", new JavaGenType(p.getType()).setOrGetAll(p), "(", p.getName(), ");");
            }
            // new ", className, "(", args, ")
            m.add("return invokeRemote(\"", ed.getName(), ".", f.getName(), "\", arguments, ", className,
                    ".SERIALIZER, ", JavaGenMessageGenerator.complexParser(cClient, f.getReturnType(), file), ");");
            generateTmpClass(className, f);
        }
    }

    void generateTmpClass(String className, EndpointMethod f) {
        new JavaGenMessageGenerator(cClient, className, ed, f).generate();
    }

    void generateTmpClassOld(String className, EndpointMethod ef) {
        cClient.addImport(Message.class);
        CodegenClass cc = cClient.addInnerClass("static final class ", className, " implements ", Message.class);
        for (FieldOrParameter f : ef.getParameters()) {
            JavaGenType ty = new JavaGenType(f.getType());
            cc.addField("private final ", ty.render(cc, file), " ", f.getName(), ";");
            if (f.getType().getBaseType() == BaseType.BINARY) {
                cClient.addImport(Binary.class);
            }
        }
        if (ef.getParameters().size() > 0) {
            String args = ef.getParameters().stream()
                    .map(e -> JavaGenType.render(cClient, e.getType(), file) + " " + e.getName())
                    .collect(Collectors.joining(", "));
            CodegenMethod con = cc.addMethod(className, "(", args, ")");
            for (FieldOrParameter f : ef.getParameters()) {
                con.add("this.", f.getName(), " = ", f.getName(), ";");
            }
        }
        JavaGenMessageGenerator.generateWriteTo(cc, ef.getParameters(), file);
    }

    void generateServer() {
        cServer.setDefinition("public abstract class Abstract", ed.getName(), " implements ",
                EndpointImplementation.class);
        cServer.addImport(EndpointImplementation.class);
        for (EndpointMethod f : functions) {
            cServer.addMethod("protected abstract ", generateSignature(cServer, f, false));
            // m.throwNewUnsupportedOperationException("Method is not supported");
        }


        cServer.addImport(MessageReader.class, IOException.class, ValueWriter.class, MessageHeader.class);
        CodegenMethod m = cServer
                .addMethod("public final void invoke(", String.class, " name, ", MessageHeader.class, " header, ",
                        MessageReader.class, " reader, ", ValueWriter.class, " writer) throws ", IOException.class);
        m.addJavadoc("{@inheritDoc}").addAnnotation(Override.class);
        for (EndpointMethod f : functions) {
            CodegenBlock b = m.newNestedBlock("if (name.equals(\"", f.getName(), "\"))");
            for (FieldOrParameter fd : f.getParameters()) {
                b.add(JavaGenType.render(cServer, fd.getType(), file), " ", fd.getName(), "_ = ",
                        JavaGenMessageGenerator.generateParseMethod("reader", cServer, fd, file), ";");
            }
            String args = f.getParameters().stream().map(e -> e.getName() + "_").collect(Collectors.joining(", "));
            String met = f.getName() + "(header" + (args.length() > 0 ? ", " : "") + args + ");";
            if (f.getReturnType() == null) {
                b.add(met);
                // b.add("return null;");
            } else {
                b.add(new JavaGenType(f.getReturnType()).render(cServer, file), " result = ", met);
                b.add("writer.", new JavaGenType(f.getReturnType()).write(cServer, "result", null, file), ";");
            }
            b.add("return;");
        }

        m.throwNewUnsupportedOperationException("Unknown method '\" + name + \"'");

        CodegenMethod name = cServer.addMethod("public final String getEndpointName()");
        name.add("return \"", ed.getName(), "\";");

    }

    String generateSignature(CodegenClass cc, EndpointMethod f, boolean isClient) {
        String s = type(cc, f, isClient) + " " + f.getName() + "(";
        boolean first = true;
        if (!isClient) {
            // cc.addI .imports().addExplicitImport(ClassDefinitions.ENDPOINT_INVOCATION_CONTEXT_CLASS);
            s += MessageHeader.class.getSimpleName() + " header";
            first = false;
        }

        for (FieldOrParameter d : f.getParameters()) {
            if (!first) {
                s += ", ";
            }
            first = false;
            s += new JavaGenType(d.getType()).render(cc, file);
            s += " " + d.getName();
        }
        return s + ")";
    }

    String type(CodegenClass cc, EndpointMethod f, boolean isClient) {
        String s;
        if (f.getReturnType() == null) {
            s = isClient ? "Void" : "void";
        } else {
            JavaGenType los = new JavaGenType(f.getReturnType());
            // if (f.getReturnType().getBaseType()
            // .isAnyOf(BaseType.BOOL, BaseType.BOOL, BaseType.BOOL, BaseType.BOOL, BaseType.BOOL)) {
            //
            // } else {
            s = los.render(cc, file);
            // }
        }
        if (isClient) {
            s = EndpointInvocationFuture.class.getSimpleName() + "<" + s + ">";
        }
        return s;
    }
}
