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

import java.util.List;

import net.maritimecloud.msdl.model.CommentDeclaration;
import net.maritimecloud.msdl.parser.antlr.MsdlLexer;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

/**
 *
 * @author Kasper Nielsen
 */
public class MsdlComment implements CommentDeclaration {
    String comment;

    /**
     * @return the comment
     */
    public String getMain() {
        return comment;
    }

    public String getMainUncapitalized() {
        if (comment == null) {
            return null;
        } else if (comment.length() == 1) {
            return Character.toLowerCase(comment.charAt(0)) + "";
        }
        return Character.toLowerCase(comment.charAt(0)) + comment.substring(1);
    }

    /**
     * @param comment
     *            the comment to set
     */
    public MsdlComment setComment(String comment) {
        this.comment = comment;
        return this;
    }

    static MsdlComment parseComments(BufferedTokenStream bts, ParserRuleContext context) {
        return parseComments(bts.getHiddenTokensToLeft(context.getStart().getTokenIndex(), MsdlLexer.COMMENTS));
    }

    static MsdlComment parseComments(List<Token> comments) {
        if (comments != null && comments.size() == 1) {
            Token t = comments.get(0);
            String text = t.getText();
            text = text.replace("/**", "");
            text = text.replace("/*", "");
            text = text.replace("*/", "");
            text = text.trim();
            return new MsdlComment().setComment(text);
        }
        return new MsdlComment();
    }
}
