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
package net.maritimecloud.msdl.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.maritimecloud.msdl.model.BroadcastMessageDeclaration;
import net.maritimecloud.msdl.model.FileDeclaration;
import net.maritimecloud.msdl.model.ServiceDeclaration;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.BroadcastDeclarationContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.ServiceBodyContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.ServiceDeclarationContext;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedService implements ServiceDeclaration {

    final AnnotationContainer ac;

    final ArrayList<ParsedBroadcastMessage> broadcastMessages = new ArrayList<>();

    final ParsedFile file;

    final List<ParsedEnum> messages = new ArrayList<>();

    String name;

    ParsedService(ParsedFile file, AnnotationContainer ac) {
        this.file = requireNonNull(file);
        this.ac = ac;
    }


    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<BroadcastMessageDeclaration> getBroadcastMessages() {
        return (List) Collections.unmodifiableList(broadcastMessages);
    }

    /** {@inheritDoc} */
    @Override
    public FileDeclaration getFile() {
        return file;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }


    /**
     * @param child
     * @return
     */
    public ParsedService parse(ServiceDeclarationContext c) {
        name = c.Identifier().getText();

        for (ServiceBodyContext tdc : c.serviceBody()) {
            AnnotationContainer ac = new AnnotationContainer(file).parse(tdc);
            ParseTree child = tdc.getChild(tdc.getChildCount() - 1);
            if (child instanceof BroadcastDeclarationContext) {
                ParsedBroadcastMessage bm = new ParsedBroadcastMessage(file, ac);
                bm.parse((BroadcastDeclarationContext) child);
                broadcastMessages.add(bm);
                // services.add(new ParsedService(this, ac).parse((ServiceDeclarationContext) child));
            }
        }
        //
        // for (FieldContext ec : c.fields().field()) {
        // ParsedField pf = new ParsedField(this);
        // pf.parse(ec);
        // if (pf != null) {
        // if (byNameIdentifiers.containsKey(pf.name)) {
        // file.error(ec, "variable name '" + pf.tag + "' is defined multiple times in the message '"
        // + pf.name + "'");
        // } else if (byIdIdentifiers.containsKey(pf.tag)) {
        // file.error(ec, "enum value '" + pf.tag + "' is used for multiple variables in the message '"
        // + pf.name + "'");
        // } else {
        // byNameIdentifiers.put(pf.name, pf);
        // byIdIdentifiers.put(pf.tag, pf);
        // }
        // }
        // }

        return this;
    }
}
