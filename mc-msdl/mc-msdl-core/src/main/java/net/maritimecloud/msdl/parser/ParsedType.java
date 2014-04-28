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

import net.maritimecloud.msdl.model.type.AnyType;
import net.maritimecloud.msdl.model.type.ListOrSetType;
import net.maritimecloud.msdl.model.type.MSDLBaseType;
import net.maritimecloud.msdl.model.type.MapType;
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

    MSDLBaseType type;

    /** If this type is an enum or a message. The name of the referenced type. */
    String referenceName;

    Object messageOrEnum;

    List<ParsedType> arguments = new ArrayList<>();

    ParsedType parse(TypeContext c) {
        if (c.primitiveType() != null) {
            PrimitiveTypeContext ptc = c.primitiveType();
            int t = ptc.start.getType();
            if (t == MsdlParser.INT32) {
                type = MSDLBaseType.INT32;
            } else if (t == MsdlParser.INT64) {
                type = MSDLBaseType.INT64;
            } else if (t == MsdlParser.FLOAT) {
                type = MSDLBaseType.FLOAT;
            } else if (t == MsdlParser.DOUBLE) {
                type = MSDLBaseType.DOUBLE;
            } else if (t == MsdlParser.BOOLEAN) {
                type = MSDLBaseType.BOOL;
            } else if (t == MsdlParser.BINARY) {
                type = MSDLBaseType.BINARY;
            } else if (t == MsdlParser.STRING) {
                type = MSDLBaseType.STRING;
            } else {
                throw new Error("Unknown type " + t);
            }
        } else if (c.complexType() != null) {
            ComplexTypeContext ctc = c.complexType();
            int t = ((TerminalNode) ctc.getChild(0)).getSymbol().getType();
            if (t == MsdlParser.LIST) {
                type = MSDLBaseType.LIST;
                ParsedType element = new ParsedType().parse(ctc.type(0));
                arguments.add(element);
            } else if (t == MsdlParser.SET) {
                type = MSDLBaseType.SET;
                ParsedType element = new ParsedType().parse(ctc.type(0));
                arguments.add(element);
            } else if (t == MsdlParser.MAP) {
                type = MSDLBaseType.MAP;
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

    AnyType toType() {
        if (type.isPrimitive()) {
            return new PrimitiveType(type);
        } else if (type == MSDLBaseType.LIST) {
            AnyType anyType = arguments.get(0).toType();
            return new ListTypeImpl(anyType);
        } else if (type == MSDLBaseType.SET) {
            AnyType anyType = arguments.get(0).toType();
            return new SetTypeImpl(anyType);
        } else if (type == MSDLBaseType.MAP) {
            AnyType keyType = arguments.get(0).toType();
            AnyType valueType = arguments.get(1).toType();
            return new MapTypeImpl(keyType, valueType);
        } else {
            return requireNonNull((AnyType) messageOrEnum);
        }
    }

    static class MapTypeImpl implements MapType {
        final AnyType key;

        final AnyType value;

        MapTypeImpl(AnyType key, AnyType value) {
            this.key = requireNonNull(key);
            this.value = requireNonNull(value);
        }

        /** {@inheritDoc} */
        @Override
        public MSDLBaseType getBaseType() {
            return MSDLBaseType.MAP;
        }

        /** {@inheritDoc} */
        @Override
        public AnyType getKeyType() {
            return key;
        }

        /** {@inheritDoc} */
        @Override
        public AnyType getValueType() {
            return value;
        }
    }

    static class SetTypeImpl implements ListOrSetType {
        final AnyType type;

        SetTypeImpl(AnyType type) {
            this.type = requireNonNull(type);
        }

        /** {@inheritDoc} */
        @Override
        public MSDLBaseType getBaseType() {
            return MSDLBaseType.SET;
        }

        /** {@inheritDoc} */
        @Override
        public AnyType getElementType() {
            return type;
        }
    }

    static class ListTypeImpl implements ListOrSetType {
        final AnyType type;

        ListTypeImpl(AnyType type) {
            this.type = requireNonNull(type);
        }

        /** {@inheritDoc} */
        @Override
        public MSDLBaseType getBaseType() {
            return MSDLBaseType.LIST;
        }

        /** {@inheritDoc} */
        @Override
        public AnyType getElementType() {
            return type;
        }
    }

    static class PrimitiveType implements AnyType {
        MSDLBaseType type;

        PrimitiveType(MSDLBaseType type) {
            this.type = type;
        }

        /** {@inheritDoc} */
        @Override
        public MSDLBaseType getBaseType() {
            return type;
        }

    }
}
