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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.FieldContext;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.FieldsContext;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.MessageDeclarationContext;
import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.FieldOrParameter;
import net.maritimecloud.msdl.model.MessageDeclaration;

import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedMessage extends AbstractContainer implements MessageDeclaration {

    final LinkedHashMap<Integer, ParsedField> fieldsByTag = new LinkedHashMap<>();

    final LinkedHashMap<String, ParsedField> fieldsByName = new LinkedHashMap<>();

    final List<ParsedEnum> enums = new ArrayList<>();

    final List<ParsedEnum> messages = new ArrayList<>();

    ParsedMessage(ParsedMsdlFile file, AnnotationContainer ac) {
        super(file, ac);
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
        setName(identifier.getText());
        for (FieldContext ec : fields.field()) {
            ParsedField pf = ParsedField.create(this, ec);
            if (pf != null) {
                if (fieldsByName.containsKey(pf.getName())) {
                    file.error(ec, "A field named '" + pf.getName() + "' is defined multiple times in the message '"
                            + getName() + "'");
                } else if (fieldsByTag.containsKey(pf.getTag())) {
                    file.error(ec, "Multiple fields with the same tag '" + pf.getTag()
                            + "' is defined in the message '" + pf.name + "'");
                } else {
                    fieldsByName.put(pf.getName(), pf);
                    fieldsByTag.put(pf.getTag(), pf);
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
    public final List<FieldOrParameter> getFields() {
        return (List) Collections.unmodifiableList(new ArrayList<>(fieldsByTag.values()));
    }

    /** {@inheritDoc} */
    @Override
    public final Iterator<FieldOrParameter> iterator() {
        return getFields().iterator();
    }

    public String toString() {
        return getFullName();
    }
}
