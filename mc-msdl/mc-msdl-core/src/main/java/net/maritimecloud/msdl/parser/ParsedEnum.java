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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import net.maritimecloud.msdl.model.EnumDeclaration;
import net.maritimecloud.msdl.model.EnumConstantDeclaration;
import net.maritimecloud.msdl.model.FileDeclaration;
import net.maritimecloud.msdl.model.type.MSDLBaseType;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.EnumDeclarationContext;
import net.maritimecloud.msdl.parser.antlr.MsdlParser.EnumTypeDeclarationContext;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedEnum implements EnumDeclaration {
    final AnnotationContainer ac;

    final LinkedHashMap<Integer, EnumConstantDeclaration> byIdIdentifiers = new LinkedHashMap<>();

    final LinkedHashMap<String, EnumConstantDeclaration> byNameIdentifiers = new LinkedHashMap<>();

    final ParsedFile file;

    String name;

    ParsedEnum(ParsedFile file, AnnotationContainer ac) {
        this.file = requireNonNull(file);
        this.ac = ac;
    }

    ParsedEnum parse(EnumDeclarationContext c) {
        name = c.Identifier().getText();
        for (EnumTypeDeclarationContext ec : c.enumBody().enumTypeDeclaration()) {
            String name = parseName(ec);
            Integer value = parseValue(ec);
            if (name != null && value != null) {
                if (byNameIdentifiers.containsKey(name)) {
                    file.error(ec, "variable '" + value + "' is defined multiple times in the enum '" + name + "'");
                } else if (byIdIdentifiers.containsKey(value)) {
                    file.error(ec, "enum value '" + value + "' is used for multiple variables in the enum '" + name
                            + "'");
                } else {
                    EnumValueImpl impl = new EnumValueImpl(name, value);
                    byNameIdentifiers.put(name, impl);
                    byIdIdentifiers.put(value, impl);
                }
            }
        }
        return this;
    }

    String parseName(EnumTypeDeclarationContext c) {
        String name = c.Identifier().getText();
        return name;
    }

    Integer parseValue(EnumTypeDeclarationContext c) {
        String value = c.Digits().getText();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            file.error(c, "The specified enum value must be less than " + Integer.MAX_VALUE + ", was " + value);
        }
        return null;
    }

    // / Public interfaces

    /** {@inheritDoc} */
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> type) {
        return ac.getAnnotation(type);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
        return ac.isAnnotationPresent(annotation);
    }

    /** {@inheritDoc} */
    @Override
    public MSDLBaseType getBaseType() {
        return MSDLBaseType.ENUM;
    }

    /** {@inheritDoc} */
    @Override
    public FileDeclaration getFile() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public List<EnumConstantDeclaration> getValues() {
        return Collections.unmodifiableList(new ArrayList<>(byIdIdentifiers.values()));
    }


    /** {@inheritDoc} */
    @Override
    public Iterator<EnumConstantDeclaration> iterator() {
        return getValues().iterator();
    }


    /** The default implementation of EnumValueDefinition. */
    static class EnumValueImpl implements EnumConstantDeclaration {

        /** The name of the enum value. */
        private final String name;

        /** The value of the enum value. */
        private final int value;

        /**
         * @param name
         * @param value
         */
        EnumValueImpl(String name, int value) {
            this.name = name;
            this.value = value;
        }

        /** {@inheritDoc} */
        @Override
        public String getName() {
            return name;
        }

        /** {@inheritDoc} */
        @Override
        public int getValue() {
            return value;
        }
    }
}
