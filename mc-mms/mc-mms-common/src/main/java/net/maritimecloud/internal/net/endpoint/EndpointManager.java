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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.message.text.json.JsonMessageReader;
import net.maritimecloud.internal.message.text.json.JsonValueWriter;
import net.maritimecloud.internal.msdl.dynamic.AbstractAsynchronousDynamicEndpointImplementation;
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

    final ScheduledExecutorService ses;

    public EndpointManager() {
        ses = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = Executors.defaultThreadFactory().newThread(runnable);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    public Registration endpointRegister(EndpointImplementation implementation) {
        Registration reg = new Registration(implementation);
        if (endpoints.putIfAbsent(implementation.getEndpointName(), reg) != null) {
            throw new IllegalArgumentException(
                    "An endpoint of the specified type has already been registered. Can only register one endpoint of the same type at a time");
        }
        return reg;
    }


    public void execute(MethodInvoke ei, Consumer<MethodInvokeResult> consumer) {
        execute(ei, Collections.emptyMap(), consumer);
    }

    public void executeAsync(MethodInvoke ei, CompletableFuture<MethodInvokeResult> future) {
        executeAsync(ei, Collections.emptyMap(), future);
    }

    public void execute(MethodInvoke ei, Map<String, Object> context, Consumer<MethodInvokeResult> consumer) {
        String endpointMethod = ei.getEndpointMethod();
        String method = EndpointMirror.stripEndpointMethod(endpointMethod);
        Registration endpoint = endpoints.get(method);


        MethodInvokeResult ack = new MethodInvokeResult();
        ack.setOriginalSenderId(ei.getSenderId());
        ack.setResultForMessageId(ei.getMessageId());
        ack.setReceiverId(ei.getReceiverId());
        if (endpoint == null) {
            MethodInvokeFailure mif = new MethodInvokeFailure();
            mif.setExceptionType("Could not find service " + method);
            mif.setErrorCode(0);
            ack.setFailure(mif);
            consumer.accept(ack);
        } else if (endpoint.implementation instanceof AbstractAsynchronousDynamicEndpointImplementation) {
            endpoint.executeAsync(ei, ack, context, consumer);

        } else {
            endpoint.execute(ei, ack, context);
            consumer.accept(ack);
        }

    }

    public void executeAsync(MethodInvoke ei, Map<String, Object> context, CompletableFuture<MethodInvokeResult> future) {
        String endpointMethod = ei.getEndpointMethod();
        String method = EndpointMirror.stripEndpointMethod(endpointMethod);
        Registration endpoint = endpoints.get(method);

        MethodInvokeResult ack = new MethodInvokeResult();
        ack.setOriginalSenderId(ei.getSenderId());
        ack.setResultForMessageId(ei.getMessageId());
        ack.setReceiverId(ei.getReceiverId());
        if (endpoint == null) {
            MethodInvokeFailure mif = new MethodInvokeFailure();
            mif.setExceptionType("Could not find service " + method);
            mif.setErrorCode(0);
            ack.setFailure(mif);
            future.complete(ack);
        } else {
            endpoint.execute(ei, ack, context);
        }
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

        @SuppressWarnings({ "rawtypes", "unchecked" })
        void executeAsync(MethodInvoke ei, MethodInvokeResult ack, Map<String, Object> context,
                Consumer<MethodInvokeResult> consumer) {
            MaritimeId sourceId = MaritimeId.create(ei.getSenderId());
            MessageReader r = null;
            if (ei.getParameters() != null) {
                r = new JsonMessageReader(ei.getParameters());
            }

            DefaultMessageHeader mc = new DefaultMessageHeader(sourceId, ei.getMessageId(), ei.getSenderTimestamp(),
                    ei.getSenderPosition(), context);

            StringWriter sw = new StringWriter();

            CompletableFuture<Object> f = new CompletableFuture<>();
            f.handle((v, t) -> {
                if (t != null) {
                    MethodInvokeFailure mif = new MethodInvokeFailure();
                    mif.setExceptionType(t.getClass().getName());
                    mif.setDescription(t.getMessage());
                    mif.setErrorCode(1);
                    ack.setFailure(mif);
                    t.printStackTrace();
                }
                if (!ack.hasFailure()) {
                    ack.setResult(Binary.copyFromUtf8(sw.toString()));
                }
                consumer.accept(ack);
                return null;
            });

            try {
                implementation.invokeAsync(EndpointMirror.stripEndpointName(ei.getEndpointMethod()), mc, r,
                        new JsonValueWriter(sw), (CompletableFuture) f);
            } catch (Exception e) {
                MethodInvokeFailure mif = new MethodInvokeFailure();
                mif.setExceptionType(e.getClass().getName());
                mif.setDescription(e.getMessage());
                mif.setErrorCode(1);
                ack.setFailure(mif);
                f.complete(null);
            }
        }

        public String getName() {
            return implementation.getEndpointName();
        }
    }
}
