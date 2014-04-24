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
package net.maritimecloud.core.message;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.util.Binary;

/**
 * Abstract class for reading message streams.
 *
 * @author Kasper Nielsen
 */
public abstract class MessageReader implements Closeable {
    public boolean isCompact() {
        return true;
    }

    public abstract boolean isNext(int tag, String name);

    public abstract Binary readBinary(int tag, String name, Binary defaultValue) throws IOException;

    public abstract Boolean readBool(int tag, String name, Boolean defaultValue) throws IOException;

    public abstract Double readDouble(int tag, String name, Double defaultValue) throws IOException;

    public abstract <T extends Enum<T> & MessageEnum> T readEnum(int tag, String name, MessageEnumParser<T> factory)
            throws IOException;

    public abstract Float readFloat(int tag, String name, Float defaultValue) throws IOException;

    public abstract int readInt32(int tag, String name) throws IOException;

    public abstract Integer readInt32(int tag, String name, Integer defaultValue) throws IOException;

    public abstract Long readInt64(int tag, String name, Long defaultValue) throws IOException;

    public abstract <T> List<T> readList(int tag, String name, ValueParser<T> parser) throws IOException;

    public abstract <K, V> Map<K, V> readMap(int tag, String name, ValueParser<K> keyParser, ValueParser<V> valueParser)
            throws IOException;

    public abstract <T extends MessageSerializable> T readMessage(int tag, String name, MessageParser<T> parser)
            throws IOException;

    public abstract double readRequiredDouble(int tag, String name) throws IOException;

    public abstract float readRequiredFloat(int tag, String name) throws IOException;

    public abstract <T> Set<T> readSet(int tag, String name, ValueParser<T> parser) throws IOException;

    public abstract String readString(int tag, String name, String defaultValue) throws IOException;
}

// public abstract int readInt32(int tag, String name, int defaultValue) throws IOException;


// public abstract float readRequiredFloat(int tag, String name) throws IOException;
//
// public abstract int readRequiredInt32(int tag, String name) throws IOException;
//
// public abstract double readRequiredDouble(int tag, String name) throws IOException;
//
// public abstract <T extends MessageSerializable> T readRequiredMessage(int tag, String name, MessageParser<T> parser)
// throws IOException;

// <K, V> Map<K, V> readMap(int tag, String name, MsdlParser<K> keyParser, MsdlParser<K> valueParser);
// public abstract <T> List<T> readList(int tag, String name, Class<T> type);
