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

import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.ListOrSetType;
import net.maritimecloud.msdl.model.MapType;
import net.maritimecloud.msdl.model.Type;
import net.maritimecloud.msdl.parser.antlr.MsdlParser;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.ComplexTypeContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.PrimitiveTypeContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.QualifiedNameContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.TypeContext;

import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedType {

    BaseType type;

    /** If this type is an enum or a message. The name of the referenced type. */
    String referenceName;

    Object messageOrEnum;

    List<ParsedType> arguments = new ArrayList<>();

    ParsedType parse(TypeContext c) {
        if (c.primitiveType() != null) {
            PrimitiveTypeContext ptc = c.primitiveType();
            int t = ptc.start.getType();
            if (t == MsdlParser.INT32) {
                type = BaseType.INT32;
            } else if (t == MsdlParser.INT64) {
                type = BaseType.INT64;
            } else if (t == MsdlParser.FLOAT) {
                type = BaseType.FLOAT;
            } else if (t == MsdlParser.DOUBLE) {
                type = BaseType.DOUBLE;
            } else if (t == MsdlParser.BOOLEAN) {
                type = BaseType.BOOL;
            } else if (t == MsdlParser.BINARY) {
                type = BaseType.BINARY;
            } else if (t == MsdlParser.STRING) {
                type = BaseType.STRING;
            } else {
                throw new Error("Unknown type " + t);
            }
        } else if (c.complexType() != null) {
            ComplexTypeContext ctc = c.complexType();
            int t = ((TerminalNode) ctc.getChild(0)).getSymbol().getType();
            if (t == MsdlParser.LIST) {
                type = BaseType.LIST;
                ParsedType element = new ParsedType().parse(ctc.type(0));
                arguments.add(element);
            } else if (t == MsdlParser.SET) {
                type = BaseType.SET;
                ParsedType element = new ParsedType().parse(ctc.type(0));
                arguments.add(element);
            } else if (t == MsdlParser.MAP) {
                type = BaseType.MAP;
                ParsedType key = new ParsedType().parse(ctc.type(0));
                ParsedType value = new ParsedType().parse(ctc.type(1));
                arguments.add(key);
                arguments.add(value);
            } else {
                throw new Error("Unknown type " + t);
            }
        } else {
            QualifiedNameContext con = c.qualifiedName();
            referenceName = con.Identifier().get(0).getText();
            // System.out.println(referenceName);
        }
        return this;
    }

    Type toType() {
        if (type.isPrimitive()) {
            return new PrimitiveType(type);
        } else if (type == BaseType.LIST) {
            Type anyType = arguments.get(0).toType();
            return new ListTypeImpl(anyType);
        } else if (type == BaseType.SET) {
            Type anyType = arguments.get(0).toType();
            return new SetTypeImpl(anyType);
        } else if (type == BaseType.MAP) {
            Type keyType = arguments.get(0).toType();
            Type valueType = arguments.get(1).toType();
            return new MapTypeImpl(keyType, valueType);
        } else {
            return requireNonNull((Type) messageOrEnum);
        }
    }

    static class MapTypeImpl implements MapType {
        final Type key;

        final Type value;

        MapTypeImpl(Type key, Type value) {
            this.key = requireNonNull(key);
            this.value = requireNonNull(value);
        }

        /** {@inheritDoc} */
        @Override
        public BaseType getBaseType() {
            return BaseType.MAP;
        }

        /** {@inheritDoc} */
        @Override
        public Type getKeyType() {
            return key;
        }

        /** {@inheritDoc} */
        @Override
        public Type getValueType() {
            return value;
        }
    }

    static class SetTypeImpl implements ListOrSetType {
        final Type type;

        SetTypeImpl(Type type) {
            this.type = requireNonNull(type);
        }

        /** {@inheritDoc} */
        @Override
        public BaseType getBaseType() {
            return BaseType.SET;
        }

        /** {@inheritDoc} */
        @Override
        public Type getElementType() {
            return type;
        }
    }

    static class ListTypeImpl implements ListOrSetType {
        final Type type;

        ListTypeImpl(Type type) {
            this.type = requireNonNull(type);
        }

        /** {@inheritDoc} */
        @Override
        public BaseType getBaseType() {
            return BaseType.LIST;
        }

        /** {@inheritDoc} */
        @Override
        public Type getElementType() {
            return type;
        }
    }

    static class PrimitiveType implements Type {
        BaseType type;

        PrimitiveType(BaseType type) {
            this.type = type;
        }

        /** {@inheritDoc} */
        @Override
        public BaseType getBaseType() {
            return type;
        }

    }
}
