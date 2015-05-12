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
package net.maritimecloud.internal.msdl.dynamic;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.msdl.model.EndpointDefinition;
import net.maritimecloud.msdl.model.EndpointMethod;
import net.maritimecloud.net.EndpointImplementation;
import net.maritimecloud.net.MessageHeader;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractAsynchronousDynamicEndpointImplementation implements EndpointImplementation {

    final EndpointDefinition endpoint;

    final Map<String, EndpointMethod> methods = new HashMap<>();

    public AbstractAsynchronousDynamicEndpointImplementation(EndpointDefinition endpoint) {
        this.endpoint = requireNonNull(endpoint);
        for (EndpointMethod m : endpoint.getFunctions()) {
            methods.put(m.getName(), m);
        }
    }

    /**
     * @return the endpoint
     */
    public EndpointDefinition getEndpoint() {
        return endpoint;
    }

    /** {@inheritDoc} */
    @Override
    public String getEndpointName() {
        return endpoint.getFullName();
    }

    protected abstract void invoke(EndpointMethod method, MessageHeader header, DynamicMessage parameters,
            CompletableFuture<Object> f);

    /** {@inheritDoc} */
    @Override
    public final void invoke(String name, MessageHeader header, MessageReader parameterReader, ValueWriter resultWriter)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void invokeAsync(String name, MessageHeader header, MessageReader parameterReader, ValueWriter resultWriter,
            CompletableFuture<Void> f) {
        EndpointMethod m = methods.get(name);
        if (m == null) {
            f.completeExceptionally(new Exception("Unknown method '" + name + "'"));
            return;
        }

        ValueSerializer resultSerializer = DynamicMessage.valueSerializer(m.getReturnType());
        CompletableFuture<Object> cf = new CompletableFuture<>();
        cf.handle((o, t) -> {
            if (t == null) {
                if (resultSerializer != null) { // == null if void method
                    try {
                        resultSerializer.write(o, resultWriter);
                    } catch (Exception e) {
                        f.completeExceptionally(e);
                    }
                }
                f.complete(null);
            } else {
                f.completeExceptionally(t);
            }
            return null;
        });

        DynamicMessage dm;
        try {
            dm = DynamicMessage.readFrom(m.getFullName(), m.getParameters(), parameterReader);
        } catch (IOException e) {
            cf.completeExceptionally(e);
            return;
        }
        invoke(m, header, dm, cf);
    }
}
