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
import net.maritimecloud.msdl.model.CommentDeclaration;
import net.maritimecloud.msdl.model.FieldDeclaration;
import net.maritimecloud.msdl.model.Type;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.FieldContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.TypeContext;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedField implements FieldDeclaration {

    MsdlComment comment;

    final ParsedMessage message;

    String name;

    Type publicType;

    int tag;

    ParsedType type;

    ParsedField(ParsedMessage message) {
        this.message = requireNonNull(message);
    }


    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public int getTag() {
        return tag;
    }

    /** {@inheritDoc} */
    @Override
    public Type getType() {
        if (publicType == null) {
            return publicType = type.toType();
        }
        return publicType;
    }

    void parse(FieldContext c) {
        comment = MsdlComment.parseComments(message.file.antlrFile.getTokenStream(), c);
        name = parseName(c);
        String tag = c.Digits().getText();
        this.tag = Integer.parseInt(tag);

        TypeContext type = c.type();
        this.type = new ParsedType();
        this.type.parse(type);
    }

    String parseName(FieldContext c) {
        String name = c.Identifier().getText();
        return name;
    }

    Integer parseValue(FieldContext c) {
        String value = c.Digits().getText();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            message.file.error(c, "The specified enum value must be less than " + Integer.MAX_VALUE + ", was " + value);
        }
        return null;
    }


    /** {@inheritDoc} */
    @Override
    public CommentDeclaration getComment() {
        return comment;
    }
}
