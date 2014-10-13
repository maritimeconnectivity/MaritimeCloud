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

import net.maritimecloud.internal.msdl.parser.antlr.MsdlParser.TypeContext;
import net.maritimecloud.msdl.model.CommentDeclaration;
import net.maritimecloud.msdl.model.FieldOrParameter;
import net.maritimecloud.msdl.model.Type;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Kasper Nielsen
 */
public class AbstractFieldOrArgument implements FieldOrParameter {

    private MsdlComment comment;

    final ParsedMsdlFile file;

    String name;

    Type publicType;

    private Integer tag;

    ParsedType type;

    AbstractFieldOrArgument(ParsedMsdlFile file) {
        this.file = file;
    }

    public final CommentDeclaration getComment() {
        return comment;
    }

    public final String getName() {
        return name;
    }

    public final int getTag() {
        return tag; // this class is never exported out if tag==null
    }


    /** {@inheritDoc} */
    public final Type getType() {
        if (publicType == null) {
            return publicType = type.toType();
        }
        return publicType;
    }

    private String n() {
        return this instanceof ParsedField ? " field" : " parameter";
    }


    AbstractFieldOrArgument parse(ParserRuleContext c, TerminalNode nameReader, TerminalNode tagReader, TypeContext type) {
        comment = MsdlComment.parseComments(file.antlrFile.getTokenStream(), c);
        parseName(c, nameReader);
        parseTag(c, tagReader);
        this.type = new ParsedType();
        this.type.parse(file, type);
        return this;
    }

    void parseName(ParserRuleContext c, TerminalNode n) {
        name = n.getText();
    }

    void parseTag(ParserRuleContext c, TerminalNode n) {
        String value = n.getText();
        try {
            int i = Integer.parseInt(value);
            if (i >= 0) {
                tag = i;
            } else {
                file.error(c, "A" + n() + " value must be non-negative, was '" + value + "'");
            }
        } catch (NumberFormatException e) {
            file.error(c, "A" + n() + " value must be less than " + Integer.MAX_VALUE + ", was " + value);
        }
    }
}
