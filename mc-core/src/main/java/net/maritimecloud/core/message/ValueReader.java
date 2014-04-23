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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class ValueReader {

    public abstract Boolean readBool() throws IOException;

    public abstract Float readFloat() throws IOException;

    public abstract Double readDouble() throws IOException;

    public abstract Integer readInt32() throws IOException;

    public abstract Long readInt64() throws IOException;

    public abstract Binary readBinary() throws IOException;

    public abstract String readString() throws IOException;

    public abstract <T extends Enum<T> & MessageEnum> T readEnum(MessageEnumParser<T> factory) throws IOException;

    public abstract <T extends MessageSerializable> T readMessage(MessageParser<T> parser) throws IOException;

    public abstract <T> List<T> readList(MessageParser<T> parser) throws IOException;

    public abstract <T> Set<T> readSet(MessageParser<T> parser) throws IOException;

    public abstract <K, V> Map<K, V> readMap(MessageParser<K> keyParser, MessageParser<V> valueParser)
            throws IOException;
}
