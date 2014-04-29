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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.FieldDeclaration;
import net.maritimecloud.msdl.model.FileDeclaration;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.FieldContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.FieldsContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.MessageDeclarationContext;

import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedMessage implements MessageDeclaration {

    final AnnotationContainer ac;

    final LinkedHashMap<Integer, ParsedField> byIdIdentifiers = new LinkedHashMap<>();

    final LinkedHashMap<String, ParsedField> byNameIdentifiers = new LinkedHashMap<>();

    final List<ParsedEnum> enums = new ArrayList<>();

    final ParsedFile file;

    final List<ParsedEnum> messages = new ArrayList<>();

    String name;

    ParsedMessage(ParsedFile file, AnnotationContainer ac) {
        this.file = requireNonNull(file);
        this.ac = ac;
    }

    ParsedMessage parse(MessageDeclarationContext c) {
        // System.out.println(c.getText());
        // System.out.println(c.getStart().getLine());
        // System.out.println(c.getStart().getCharPositionInLine());
        // System.out.println(c.getStop().getLine());
        // System.out.println(c.getStop().getCharPositionInLine());
        parse(c.Identifier(), c.fields());
        return this;
    }

    ParsedMessage parse(TerminalNode identifier, FieldsContext fields) {
        // System.out.println(c.getText());
        // System.out.println(c.getStart().getLine());
        // System.out.println(c.getStart().getCharPositionInLine());
        // System.out.println(c.getStop().getLine());
        // System.out.println(c.getStop().getCharPositionInLine());

        name = identifier.getText();
        for (FieldContext ec : fields.field()) {
            ParsedField pf = new ParsedField(this);
            pf.parse(ec);
            if (pf != null) {
                if (byNameIdentifiers.containsKey(pf.name)) {
                    file.error(ec, "variable name '" + pf.tag + "' is defined multiple times in the message '"
                            + pf.name + "'");
                } else if (byIdIdentifiers.containsKey(pf.tag)) {
                    file.error(ec, "enum value '" + pf.tag + "' is used for multiple variables in the message '"
                            + pf.name + "'");
                } else {
                    byNameIdentifiers.put(pf.name, pf);
                    byIdIdentifiers.put(pf.tag, pf);
                }
            }
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public BaseType getBaseType() {
        return BaseType.MESSAGE;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<FieldDeclaration> getFields() {
        return (List) Collections.unmodifiableList(new ArrayList<>(byIdIdentifiers.values()));
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> type) {
        return ac.getAnnotation(type);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
        return ac.isAnnotationPresent(annotation);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<FieldDeclaration> iterator() {
        return getFields().iterator();
    }

    /** {@inheritDoc} */
    @Override
    public FileDeclaration getFile() {
        return file;
    }
}
