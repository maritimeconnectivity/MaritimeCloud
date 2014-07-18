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
package net.maritimecloud.internal.message;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.util.Timestamp;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractValueWriter implements ValueWriter {

    protected void flush() {}

    /** {@inheritDoc} */
    @Override
    public <T> void writeList(List<T> list, ValueSerializer<T> serializer) throws IOException {
        writeListOrSet(list, serializer);
    }

    protected abstract <T> void writeListOrSet(Collection<T> list, ValueSerializer<T> serializer) throws IOException;;


    /** {@inheritDoc} */
    @Override
    public <T> void writeSet(Set<T> set, ValueSerializer<T> serializer) throws IOException {
        writeListOrSet(set, serializer);
    }

    /** {@inheritDoc} */
    @Override
    public void writeDouble(Double value) throws IOException {
        if (!Double.isFinite(value)) {
            throw new IOException("Cannot write double value " + value);
        }
        writeDouble0(value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeFloat(Float value) throws IOException {
        if (!Float.isFinite(value)) {
            throw new IOException("Cannot write float value " + value);
        }
        writeFloat0(value);
    }

    protected abstract void writeFloat0(Float value) throws IOException;

    protected abstract void writeDouble0(Double value) throws IOException;

    protected abstract AbstractValueWriter writeTag(int tag, String name) throws IOException;

    /** {@inheritDoc} */
    @Override
    public void writeTimestamp(Timestamp value) throws IOException {
        writeInt64(value.getTime());
    }
}
