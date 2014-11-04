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
package net.maritimecloud.internal.net.endpoint;

import static java.util.Objects.requireNonNull;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.message.text.json.JsonMessageReader;
import net.maritimecloud.internal.message.text.json.JsonValueWriter;
import net.maritimecloud.internal.net.messages.MethodInvoke;
import net.maritimecloud.internal.net.messages.MethodInvokeFailure;
import net.maritimecloud.internal.net.messages.MethodInvokeResult;
import net.maritimecloud.internal.net.util.DefaultMessageHeader;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class EndpointManager {

    /** A map of subscribers. ChannelName -> List of listeners. */
    final ConcurrentHashMap<String, Registration> endpoints = new ConcurrentHashMap<>();

    public Registration endpointRegister(EndpointImplementation implementation) {
        Registration reg = new Registration(implementation);
        if (endpoints.putIfAbsent(implementation.getEndpointName(), reg) != null) {
            throw new IllegalArgumentException(
                    "An endpoint of the specified type has already been registered. Can only register one endpoint of the same type at a time");
        }
        return reg;
    }

    public MethodInvokeResult execute(MethodInvoke ei) {
        return execute(ei, Collections.emptyMap());
    }

    public MethodInvokeResult execute(MethodInvoke ei, Map<String, Object> context) {
        String endpointMethod = ei.getEndpointMethod();
        String method = EndpointMirror.stripEndpointMethod(endpointMethod);
        Registration endpoint = endpoints.get(method);

        MethodInvokeResult ack = new MethodInvokeResult();
        ack.setOriginalSenderId(ei.getSenderId());
        ack.setResultForMessageId(ei.getMessageId());
        if (endpoint == null) {
            MethodInvokeFailure mif = new MethodInvokeFailure();
            mif.setExceptionType("Could not find service " + method);
            mif.setErrorCode(0);
            ack.setFailure(mif);
        } else {
            endpoint.execute(ei, ack, context);
        }

        ack.setReceiverId(ei.getReceiverId());
        return ack;
    }

    public static class Registration {

        final EndpointImplementation implementation;

        Registration(EndpointImplementation implementation) {
            this.implementation = requireNonNull(implementation);
        }

        void execute(MethodInvoke ei, MethodInvokeResult ack, Map<String, Object> context) {
            MaritimeId sourceId = MaritimeId.create(ei.getSenderId());
            MessageReader r = null;
            if (ei.getParameters() != null) {
                r = new JsonMessageReader(ei.getParameters());
            }

            DefaultMessageHeader mc = new DefaultMessageHeader(sourceId, ei.getMessageId(), ei.getSenderTimestamp(),
                    ei.getSenderPosition(), context);

            StringWriter sw = new StringWriter();
            try {
                implementation.invoke(EndpointMirror.stripEndpointName(ei.getEndpointMethod()), mc, r,
                        new JsonValueWriter(sw));
            } catch (Exception e) {
                MethodInvokeFailure mif = new MethodInvokeFailure();
                mif.setExceptionType(e.getClass().getName());
                mif.setDescription(e.getMessage());
                mif.setErrorCode(1);
                ack.setFailure(mif);
                e.printStackTrace();
            }

            if (!ack.hasFailure()) {
                ack.setResult(Binary.copyFromUtf8(sw.toString()));
            }

        }

        public String getName() {
            return implementation.getEndpointName();
        }
    }
}
