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

import net.maritimecloud.msdl.model.BroadcastMessageDeclaration;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.BroadcastDeclarationContext;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedBroadcastMessage extends ParsedMessage implements BroadcastMessageDeclaration {

    /**
     * @param file
     * @param ac
     */
    ParsedBroadcastMessage(ParsedFile file, AnnotationContainer ac) {
        super(file, ac);
    }

    /**
     * @param child
     * @return
     */
    public ParsedBroadcastMessage parse(BroadcastDeclarationContext c) {
        // System.out.println(c.getText());
        // System.out.println(c.getStart().getLine());
        // System.out.println(c.getStart().getCharPositionInLine());
        // System.out.println(c.getStop().getLine());
        // System.out.println(c.getStop().getCharPositionInLine());
        parse(c.Identifier(), c.fields());
        return this;
    }
}
