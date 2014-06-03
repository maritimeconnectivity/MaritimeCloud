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
import java.util.List;

import net.maritimecloud.msdl.model.CommentDeclaration;
import net.maritimecloud.msdl.model.EndpointFunction;
import net.maritimecloud.msdl.model.FieldDeclaration;
import net.maritimecloud.msdl.model.Type;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.FunctionArgumentContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.FunctionContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.ReturnTypeContext;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedEndpointFunction implements EndpointFunction {

    MsdlComment comment;

    final ParsedEndpoint endpoint;

    String name;

    ParsedType returnType;

    Type publicReturnType;

    final List<ParsedEndpointFunctionArgument> arguments = new ArrayList<>();

    ParsedEndpointFunction(ParsedEndpoint endpoint) {
        this.endpoint = requireNonNull(endpoint);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public CommentDeclaration getComment() {
        return comment;
    }

    /** {@inheritDoc} */
    @Override
    public Type getReturnType() {
        if (returnType == null) {
            return null;
        }
        if (publicReturnType == null) {
            return publicReturnType = returnType.toType();
        }
        return publicReturnType;
    }

    /**
     * @param ec
     */
    void parse(FunctionContext c) {
        name = c.Identifier().getText();

        ReturnTypeContext type = c.returnType();
        if (type.type() != null) {
            this.returnType = new ParsedType();
            this.returnType.parse(type.type());

        }
        for (FunctionArgumentContext s : c.functionArgument()) {
            arguments.add(new ParsedEndpointFunctionArgument(this).parse(s));
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<FieldDeclaration> getArguments() {
        return (List) arguments;
    }
}
