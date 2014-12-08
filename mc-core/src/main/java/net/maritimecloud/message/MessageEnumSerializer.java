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
package net.maritimecloud.message;

import java.io.IOException;

/**
 * A parser for creating message enums based on an integer or string value.
 *
 * @param <T>
 *            the type of enum
 * @author Kasper Nielsen
 */
public abstract class MessageEnumSerializer<T extends /* Enum<T> & */MessageEnum> extends ValueSerializer<T> {

    /**
     * Creates the enum from the specified integer value.
     *
     * @param value
     *            the integer value to create the enum from
     * @return the new enum
     */
    public abstract T from(int value) throws IOException;

    /**
     * Creates the enum from the specified string
     *
     * @param name
     *            the string value of this enum
     * @return the string value of this enum
     */
    public abstract T from(String name) throws IOException;

    /** {@inheritDoc} */
    @Override
    public final T read(ValueReader reader) throws IOException {
        return reader.readEnum(this);
    }

    /** {@inheritDoc} */
    @Override
    public final void write(T value, ValueWriter writer) throws IOException {
        writer.writeEnum(value);
    }
}
