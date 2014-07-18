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
import java.util.LinkedHashMap;
import java.util.List;

import net.maritimecloud.internal.msdl.parser.antlr.MsdlParser.FunctionArgumentContext;
import net.maritimecloud.internal.msdl.parser.antlr.MsdlParser.FunctionContext;
import net.maritimecloud.internal.msdl.parser.antlr.MsdlParser.ReturnTypeContext;
import net.maritimecloud.msdl.model.CommentDeclaration;
import net.maritimecloud.msdl.model.EndpointMethod;
import net.maritimecloud.msdl.model.FieldOrParameter;
import net.maritimecloud.msdl.model.Type;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedEndpointFunction implements EndpointMethod {

    final LinkedHashMap<Integer, ParsedEndpointFunctionArgument> parametersByTag = new LinkedHashMap<>();

    final LinkedHashMap<String, ParsedEndpointFunctionArgument> parametersByName = new LinkedHashMap<>();

    MsdlComment comment;

    final ParsedEndpoint endpoint;

    String name;

    ParsedType returnType;

    Type publicReturnType;

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
            return null; // void
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
            this.returnType.parse(endpoint.file, type.type());
        }

        for (FunctionArgumentContext ec : c.functionArgument()) {
            ParsedEndpointFunctionArgument pf = ParsedEndpointFunctionArgument.create(this, ec);
            if (pf != null) {
                if (parametersByName.containsKey(pf.getName())) {
                    endpoint.file.error(ec, "A field named '" + pf.getName()
                            + "' is defined multiple times in the message '" + getName() + "'");
                } else if (parametersByTag.containsKey(pf.getTag())) {
                    endpoint.file.error(ec, "Multiple fields with the same tag '" + pf.getTag()
                            + "' is defined in the message '" + pf.name + "'");
                } else {
                    parametersByName.put(pf.getName(), pf);
                    parametersByTag.put(pf.getTag(), pf);
                }
            }
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<FieldOrParameter> getParameters() {
        return (List) new ArrayList<>(parametersByName.values());
    }
}
