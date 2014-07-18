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
package net.maritimecloud.msdl.plugins.javagen;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import net.maritimecloud.internal.msdl.parser.antlr.StringUtil;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.EnumDeclaration;
import net.maritimecloud.msdl.model.FieldOrParameter;
import net.maritimecloud.msdl.model.ListOrSetType;
import net.maritimecloud.msdl.model.MapType;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.Type;
import net.maritimecloud.msdl.plugins.javagen.annotation.JavaImplementation;
import net.maritimecloud.util.Binary;

import org.cakeframework.internal.codegen.AbstractCodegenEntity;
import org.cakeframework.internal.codegen.CodegenClass;

/**
 *
 * @author Kasper Nielsen
 */
class JavaGenType {

    final Type type;

    final BaseType t;

    final List<JavaGenType> parameters = new ArrayList<>();

    JavaGenType(Type type) {
        this.type = requireNonNull(type);
        this.t = type.getBaseType();
        if (type instanceof ListOrSetType) {
            parameters.add(new JavaGenType(((ListOrSetType) type).getElementType()));
        } else if (type instanceof MapType) {
            parameters.add(new JavaGenType(((MapType) type).getKeyType()));
            parameters.add(new JavaGenType(((MapType) type).getValueType()));
        }
    }

    void addImports(AbstractCodegenEntity e) {
        if (t != BaseType.MESSAGE) {
            e.addImport(t.getJavaType());
        }
        for (JavaGenType t : parameters) { // add imports for each import
            t.addImports(e);
        }
    }

    String writeReadName() {
        if (t == BaseType.VARINT) {
            return "VarInt";
        } else if (t == BaseType.POSITION_TIME) {
            return "PositionTime";
        }
        String s = t.toString().toLowerCase();
        return StringUtil.capitalizeFirstLetter(s);
    }

    static String render(CodegenClass c, Type t) {
        return new JavaGenType(t).render(c);
    }

    String setOrGetAll(FieldOrParameter f) {
        if (t.isComplexType()) {
            String beanPrefix2 = StringUtil.capitalizeFirstLetter(f.getName());
            return (t == BaseType.MAP ? "putAll" : "addAll") + beanPrefix2;
        } else {
            return "set" + StringUtil.capitalizeFirstLetter(f.getName());
        }
    }

    String render(CodegenClass c) {
        requireNonNull(c);
        addImports(c);
        switch (t) {
        case LIST:
            return "List<" + parameters.get(0).render(c) + ">";
        case SET:
            return "Set<" + parameters.get(0).render(c) + ">";
        case MAP:
            return "Map<" + parameters.get(0).render(c) + ", " + parameters.get(1).render(c) + ">";
        case MESSAGE:
            MessageDeclaration md = (MessageDeclaration) type;
            if (md.isAnnotationPresent(JavaImplementation.class)) {
                return md.getAnnotation(JavaImplementation.class).value();
            }
            return md.getName();
        case ENUM:
            EnumDeclaration d = (EnumDeclaration) type;
            if (d.isAnnotationPresent(JavaImplementation.class)) {
                return d.getAnnotation(JavaImplementation.class).value();
            }
            return d.getName();
        default:
            return t.getJavaType().getSimpleName();
        }
    }

    String write(CodegenClass c, String name, FieldOrParameter f) {
        c.addImport(f == null ? ValueWriter.class : MessageWriter.class);
        StringBuilder sb = new StringBuilder();
        sb.append("write").append(writeReadName()).append("(");
        if (f != null) {
            sb.append(f.getTag()).append(", \"").append(f.getName()).append("\", ");
        }
        sb.append(name);
        if (type.getBaseType() == BaseType.MESSAGE) {
            sb.append(", ").append(render(c)).append(".SERIALIZER");
        } else if (type.getBaseType().isComplexType()) {
            sb.append(", ");
            if (type instanceof ListOrSetType) {
                ListOrSetType lt = (ListOrSetType) type;
                sb.append(complexParser(c, lt.getElementType()));
            } else {
                MapType lt = (MapType) type;
                sb.append(complexParser(c, lt.getKeyType()));
                sb.append(", ").append(complexParser(c, lt.getValueType()));
            }
        }
        sb.append(")");
        return sb.toString();
    }

    static String complexParser(CodegenClass c, Type type) {
        if (type == null) {
            return "null";
        }
        BaseType b = type.getBaseType();
        if (b.isPrimitive()) {
            c.addImport(ValueSerializer.class);
            if (b == BaseType.BINARY) {
                c.addImport(Binary.class);
            }
            return ValueSerializer.class.getSimpleName() + "." + b.name().toUpperCase();
        } else if (b.isReferenceType()) {
            JavaGenType ty = new JavaGenType(type);
            return ty.render(c) + ".SERIALIZER";
        } else if (b == BaseType.LIST) {
            ListOrSetType los = (ListOrSetType) type;
            return complexParser(c, los.getElementType()) + ".listOf()";
        } else if (b == BaseType.SET) {
            ListOrSetType los = (ListOrSetType) type;
            return complexParser(c, los.getElementType()) + ".setOf()";
        } else {
            MapType los = (MapType) type;
            return "MessageParser.ofMap(" + complexParser(c, los.getKeyType()) + ", "
            + complexParser(c, los.getValueType()) + ")";
        }
    }
}
