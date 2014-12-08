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
package net.maritimecloud.internal.msdl.dynamic;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import net.maritimecloud.message.MessageEnum;
import net.maritimecloud.message.MessageEnumSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.msdl.model.EnumDeclaration;
import net.maritimecloud.msdl.model.EnumDeclaration.Constant;

/**
 *
 * @author Kasper Nielsen
 */
public class DynamicEnum implements MessageEnum {
    final EnumDeclaration ed;

    private final Integer value;

    private final String stringValue;

    DynamicEnum(EnumDeclaration ed) {
        this.ed = requireNonNull(ed);
        this.value = null;
        this.stringValue = null;
    }

    DynamicEnum(EnumDeclaration ed, int value, String name) {
        this.ed = requireNonNull(ed);
        this.value = value;
        this.stringValue = requireNonNull(name);
    }

    /** {@inheritDoc} */
    @Override
    public int getValue() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return stringValue;
    }

    static class Serializer extends MessageEnumSerializer<DynamicEnum> {

        final static Serializer WRITEABLE = new Serializer();

        final EnumDeclaration ed;

        private Serializer() {
            this.ed = null;
        }

        Serializer(EnumDeclaration ed) {
            this.ed = requireNonNull(ed);
        }

        /** {@inheritDoc} */
        @Override
        public DynamicEnum from(int value) throws IOException {
            if (ed == null) {
                throw new UnsupportedOperationException("Cannot deserialize");
            }
            for (Constant c : ed.getConstants()) {
                if (c.getValue() == value) {
                    return new DynamicEnum(ed, c.getValue(), c.getName());
                }
            }
            throw new IOException("'" + value + "' is not a valid tag for Enum '" + ed.getName() + "'");
        }

        /** {@inheritDoc} */
        @Override
        public DynamicEnum from(String name) throws IOException {
            if (ed == null) {
                throw new UnsupportedOperationException("Cannot deserialize");
            }
            for (Constant c : ed.getConstants()) {
                if (c.getName().equals(name)) {
                    return new DynamicEnum(ed, c.getValue(), c.getName());
                }
            }
            throw new IOException("'" + name + "' is not a valid name for Enum '" + ed.getName() + "'");
        }

        /** {@inheritDoc} */
        @Override
        public void write(int tag, String name, DynamicEnum t, MessageWriter writer) throws IOException {
            writer.writeEnum(tag, name, t);
        }
    }
}
