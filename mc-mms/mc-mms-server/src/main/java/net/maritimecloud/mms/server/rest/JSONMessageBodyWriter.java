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
package net.maritimecloud.mms.server.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;

/**
 * If the system fails to find this message body writer. Make sure the annotated method using this writer has this
 * annotation <code>@Produces("application//json")</code>
 *
 * @author Kasper Nielsen
 */
@Produces("application/json")
@Provider
public class JSONMessageBodyWriter implements MessageBodyWriter<Message> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Message.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Message myBean, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1; // deprecated by JAX-RS 2.0 and ignored by Jersey runtime
    }

    @Override
    public void writeTo(Message myBean, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
        MessageSerializer.writeToJSON(myBean, MessageHelper.getSerializer(myBean), new PrintWriter(entityStream));
    }
}
