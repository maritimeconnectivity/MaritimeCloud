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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.EnumDeclarationContext;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.EnumTypeDeclarationContext;
import net.maritimecloud.msdl.model.BaseType;
import net.maritimecloud.msdl.model.EnumDeclaration;

/**
 *
 * @author Kasper Nielsen
 */
public class ParsedEnum extends AbstractContainer implements EnumDeclaration {

    /** The enum values keyed by their int value. */
    private final LinkedHashMap<Integer, Constant> byId = new LinkedHashMap<>();

    /** The enum values keyed by their string name. */
    private final LinkedHashMap<String, Constant> byName = new LinkedHashMap<>();

    ParsedEnum(ParsedMsdlFile file, AnnotationContainer ac) {
        super(file, ac);
    }

    ParsedEnum parse(EnumDeclarationContext c) {
        setName(c.Identifier().getText());
        for (EnumTypeDeclarationContext ec : c.enumBody().enumTypeDeclaration()) {
            String name = parseEnumValueName(ec);
            Integer value = parseEnumValueIntValue(ec);
            if (name != null && value != null) {
                if (byName.containsKey(name)) {
                    file.error(ec, "variable '" + value + "' is defined multiple times in the enum '" + name + "'");
                } else if (byId.containsKey(value)) {
                    file.error(ec, "enum value '" + value + "' is used for multiple variables in the enum '" + name
                            + "'");
                } else {
                    EnumValueImpl impl = new EnumValueImpl(name, value);
                    byName.put(name, impl);
                    byId.put(value, impl);
                }
            }
        }
        return this;
    }

    private String parseEnumValueName(EnumTypeDeclarationContext c) {
        String name = c.Identifier().getText();
        if (Checks.checkFirstUpper(file, c, "An enum value name", name)
                && Checks.checkAsciiUpperLowercase(file, c, "An enum value name", name)) {
            return name;
        }
        return null;
    }

    private Integer parseEnumValueIntValue(EnumTypeDeclarationContext c) {
        String value = c.Digits().getText();
        try {
            int i = Integer.parseInt(value);
            if (i > 0) {
                return i;
            }
            file.error(c, "An enum value must be non-negative, was '" + value + "'");
        } catch (NumberFormatException e) {
            file.error(c, "An enum value must be less than " + Integer.MAX_VALUE + ", was " + value);
        }
        return null;
    }

    // / Public interfaces

    /** {@inheritDoc} */
    @Override
    public BaseType getBaseType() {
        return BaseType.ENUM;
    }

    /** {@inheritDoc} */
    @Override
    public List<Constant> getConstants() {
        return Collections.unmodifiableList(new ArrayList<>(byId.values()));
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Constant> iterator() {
        return getConstants().iterator();
    }

    /** The default implementation of EnumValueDefinition. */
    static class EnumValueImpl implements Constant {

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
