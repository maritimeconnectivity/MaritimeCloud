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


/**
 * The basic message interface that all messages must be implement. Messages are normally generated from MSDL files.
 * <p>
 * Normally, there should be no need to manually extend this class, as broadcast messages are automatically generated
 * from a MSDL file.
 * <p>
 * Any class implementing this interface should also have a
 * <code>public static final MessageSerializer SERIALIZER</code> field. To allow for reading the serialized message back
 * again.
 * <p>
 * Any implementation of this message must contain a <tt>public static final String NAME = "name"</tt> field. With the
 * full name of this broadcast message.
 *
 * @author Kasper Nielsen
 */
public interface Message {

    /**
     * Returns an immutable copy of this message. If this message is already immutable this method should return this.
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
}
