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
import java.util.Map;
import java.util.Set;

import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.EnumDeclaration;
import net.maritimecloud.msdl.model.ListOrSetType;
import net.maritimecloud.msdl.model.MapType;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.Type;
import net.maritimecloud.msdl.parser.antlr.StringUtil;
import net.maritimecloud.msdl.plugins.javagen.annotation.JavaImplementation;
import net.maritimecloud.util.Binary;

import org.cakeframework.internal.codegen.AbstractCodegenEntity;

/**
 *
 * @author Kasper Nielsen
 */
public class JavaGenType {

    final Type type;

    final BaseType t;

    final List<JavaGenType> parameters = new ArrayList<>();

    public JavaGenType(Type type) {
        this.type = requireNonNull(type);
        t = type.getBaseType();
        if (type instanceof ListOrSetType) {
            parameters.add(new JavaGenType(((ListOrSetType) type).getElementType()));
        } else if (type instanceof MapType) {
            parameters.add(new JavaGenType(((MapType) type).getKeyType()));
            parameters.add(new JavaGenType(((MapType) type).getValueType()));
        }
    }

    public void addImports(AbstractCodegenEntity e) {
        if (t == BaseType.LIST) {
            e.addImport(List.class);
        } else if (t == BaseType.SET) {
            e.addImport(Set.class);
        } else if (t == BaseType.MAP) {
            e.addImport(Map.class);
        } else if (t == BaseType.BINARY) {
            e.addImport(Binary.class);
        }
        for (JavaGenType t : parameters) {
            t.addImports(e);
        }
    }

    public String writeReadName() {
        String s = t.toString().toLowerCase();
        return StringUtil.capitalizeFirstLetter(s);
    }

    public String render() {
        switch (t) {
        case INT32:
            return "Integer";
        case INT64:
            return "Long";
        case FLOAT:
            return "Float";
        case DOUBLE:
            return "Double";
        case BOOL:
            return "Boolean";
        case STRING:
            return "String";
        case BINARY:
            return "Binary";
        case LIST:
            return "List<" + parameters.get(0).render() + ">";
        case SET:
            return "Set<" + parameters.get(0).render() + ">";
        case MAP:
            return "Map<" + parameters.get(0).render() + ", " + parameters.get(1).render() + ">";
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
            throw new Error();
        }
    }

}
