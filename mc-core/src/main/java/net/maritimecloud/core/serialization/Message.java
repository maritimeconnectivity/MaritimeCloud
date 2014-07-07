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
package net.maritimecloud.core.serialization;


/**
 * The basic message interface that all messages must be implement. Messages are normally generated from MSDL files.
 *
 * @author Kasper Nielsen
 */
/**
 * Implemented by classes that can be serialized by a {@link MessageWriter}. Any class implementing this interface
 * should also have a <code>public static final MessageParser<T> PARSER</code> field. To allow for reading the
 * serialized message back again.
 *
 * @author Kasper Nielsen
 */
// TODO rename to serialization
public interface Message {

    /**
     * Returns an immutable copy of this message.
     *
     * @return an immutable copy of this message
     */
    Message immutable();

    /**
     * Returns a JSON representation of this message.
     *
     * @return a JSON representation of this message
     */
    String toJSON();

    /**
     * Serializes the implementing class.
     *
     * @param w
     *            the writer to use for serialization
     * @throws IOException
     *             any exception occurred while serializing
     */
    // void writeTo(MessageWriter w) throws IOException;
}
