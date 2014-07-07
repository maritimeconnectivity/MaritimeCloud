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
package net.maritimecloud.internal.serialization.json;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.maritimecloud.core.serialization.MessageWriter;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractMessageWriter implements MessageWriter {

    public final void writeBinary(int tag, String name, byte[] bytes) throws IOException {
        writeBinary(tag, name, bytes, 0, bytes.length);
    }

    public void writeBinary(int tag, String name, byte[] bytes, int offset, int length) throws IOException {
        writeBinary(tag, name, Binary.copyFrom(bytes, offset, length));
    }

    public void writeBinary(int tag, String name, ByteBuffer buffer) throws IOException {
        writeBinary(tag, name, Binary.copyFrom(buffer));
    }
}
