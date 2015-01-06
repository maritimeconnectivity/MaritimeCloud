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

import java.util.HashMap;
import java.util.Map;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueReader;
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
public abstract class AbstractDynamicEndpointImplementation implements EndpointImplementation {

    final EndpointDefinition endpoint;

    final Map<String, EndpointMethod> methods = new HashMap<>();

    /**
     * @param endpointName
     */
    public AbstractDynamicEndpointImplementation(EndpointDefinition endpoint) {
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

    protected abstract ValueReader invoke(EndpointMethod method, MessageHeader header, DynamicMessage parameters);

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public final void invoke(String name, MessageHeader header, MessageReader parameterReader, ValueWriter resultWriter)
            throws Exception {
        EndpointMethod m = methods.get(name);
        if (m == null) {
            throw new Exception("Unknown method '" + name + "'");
        }

        Object result = invoke(m, header, DynamicMessage.readFrom(m.getFullName(), m.getParameters(), parameterReader));

        ValueSerializer resultSerializer = DynamicMessage.valueSerializer(m.getReturnType());
        if (resultSerializer != null) { // == null if void method
            resultSerializer.write(result, resultWriter);
        }

    }
}
