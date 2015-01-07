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
package net.maritimecloud.internal.mms.client.broadcast;

import java.io.IOException;
import java.lang.reflect.Field;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.net.BroadcastMessage;

/**
 *
 * @author Kasper Nielsen
 */
public interface BroadcastDeserializer {

    /** A deserializer that expects received messages to be on the classpath. */
    BroadcastDeserializer CLASSPATH_DESERIALIZER = new ClasspathDeserializer();

    BroadcastMessage convert(String name, MessageReader r) throws Exception;

    /** An implementation that looks for messages on the classpath. */
    static class ClasspathDeserializer implements BroadcastDeserializer {

        /** {@inheritDoc} */
        @Override
        @SuppressWarnings("unchecked")
        public BroadcastMessage convert(String name, MessageReader r) throws Exception {
            // right now: msdl message name == full Java class name
            Class<BroadcastMessage> cl = (Class<BroadcastMessage>) Class.forName(name);
            Field field = cl.getField("SERIALIZER");
            MessageSerializer<BroadcastMessage> p = (MessageSerializer<BroadcastMessage>) field.get(null);
            try {
                return p.read(r);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read message from JSON", e);
            }
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return 1;
        }
    }
}
