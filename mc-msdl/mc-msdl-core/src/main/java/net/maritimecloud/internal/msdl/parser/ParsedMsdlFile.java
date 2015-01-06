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
package net.maritimecloud.internal.msdl.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.maritimecloud.internal.msdl.parser.antlr.AntlrFile;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.BroadcastDeclarationContext;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.EndpointDeclarationContext;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.EnumDeclarationContext;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.ImportDeclarationContext;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.MessageDeclarationContext;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.NamespaceDeclarationContext;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.TypeDeclarationContext;
import net.maritimecloud.msdl.model.BroadcastMessageDeclaration;
import net.maritimecloud.msdl.model.EndpointDefinition;
import net.maritimecloud.msdl.model.EnumDeclaration;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.MsdlFile;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author Kasper Nielsen
 */
class ParsedMsdlFile implements MsdlFile {

    final AntlrFile antlrFile;

    final List<AbstractContainer> containers = new ArrayList<>();

    final ArrayList<Import> imports = new ArrayList<>();

    String namespace;

    final ParsedProject project;

    final ArrayList<ParsedMsdlFile> resolvedImports = new ArrayList<>();

    ParsedMsdlFile(ParsedProject project, AntlrFile antlrFile) {
        this.antlrFile = requireNonNull(antlrFile);
        this.project = requireNonNull(project);

    }

    void error(ParserRuleContext context, String msg) {
        String st = antlrFile.getPath() + ":[" + context.getStart().getLine() + ":"
                + context.getStart().getCharPositionInLine() + "] ";
        st += msg;
        project.logger.error(st);
    }

    /**
     * @return the broadcasts
     */
    public List<BroadcastMessageDeclaration> getBroadcasts() {
        return listOf(BroadcastMessageDeclaration.class);
    }

    /**
     * @return the endpoints
     */
    public List<EndpointDefinition> getEndpoints() {
        return listOf(EndpointDefinition.class);
    }

    /** {@inheritDoc} */
    @Override
    public List<EnumDeclaration> getEnums() {
        return listOf(EnumDeclaration.class);
    }

    /** {@inheritDoc} */
    @Override
    public List<MessageDeclaration> getMessages() {
        List<MessageDeclaration> msgs = listOf(MessageDeclaration.class);
        return msgs.stream().filter(e -> !(e instanceof BroadcastMessageDeclaration)).collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        String filename = antlrFile.getPath().getFileName().toString();
        return filename.substring(0, filename.length() - 5);
    }

    /** {@inheritDoc} */
    @Override
    public String getNamespace() {
        return namespace;
    }

    @SuppressWarnings("unchecked")
    <T> List<T> listOf(Class<T> type) {
        ArrayList<T> result = new ArrayList<>();
        for (Object o : containers) {
            if (type.isAssignableFrom(o.getClass())) {
                result.add((T) o);
            }
        }
        return result;
    }

    void parse() {
        parseNamespace();
        parseImports();
        parseTypes();
    }

    private void parseImports() {
        for (ImportDeclarationContext importContext : antlrFile.getCompilationUnit().importDeclaration()) {
            if (importContext.getChild(1) != null) {
                imports.add(new Import(importContext));
            }
        }
    }

    private void parseNamespace() {
        NamespaceDeclarationContext namespaceContext = antlrFile.getCompilationUnit().namespaceDeclaration();
        // namespace is optional
        if (namespaceContext != null) {
            namespace = namespaceContext.getChild(1).getText();
            // TODO validate name spaces
        }
    }

    private void parseTypes() {
        for (TypeDeclarationContext tdc : antlrFile.getCompilationUnit().typeDeclaration()) {
            AnnotationContainer ac = new AnnotationContainer(this).parse(tdc);
            ParseTree child = tdc.getChild(tdc.getChildCount() - 1);
            if (child instanceof EnumDeclarationContext) {
                containers.add(new ParsedEnum(this, ac).parse((EnumDeclarationContext) child));
            } else if (child instanceof MessageDeclarationContext) {
                containers.add(new ParsedMessage(this, ac).parse((MessageDeclarationContext) child));
            } else if (child instanceof BroadcastDeclarationContext) {
                containers.add(new ParsedBroadcastMessage(this, ac).parse((BroadcastDeclarationContext) child));
            } else if (child instanceof EndpointDeclarationContext) {
                containers.add(new ParsedEndpoint(this, ac).parse((EndpointDeclarationContext) child));
            }
        }
    }

    public String toString() {
        return antlrFile.getPath().toString();
    }

    static class Import {
        final ImportDeclarationContext importContext;

        Import(ImportDeclarationContext importContext) {
            this.importContext = requireNonNull(importContext);
        }

        public String getName() {
            String p = importContext.getChild(1).getText();
            return p.substring(1, p.length() - 1);
        }
    }
}
