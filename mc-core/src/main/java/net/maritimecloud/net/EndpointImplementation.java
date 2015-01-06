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
package net.maritimecloud.net;

import java.util.concurrent.CompletableFuture;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueWriter;

/**
 * The implementation part of an endpoint. Endpoints generated from MSDL files uses this interface.
 *
 * @author Kasper Nielsen
 */
public interface EndpointImplementation {

    /**
     * Returns the unique name of the endpoint.
     *
     * @return the unique name of the endpoint
     */
    String getEndpointName();

    /**
     * Invokes a method on the endpoint.
     *
     * @param name
     *            the name of the endpoint method
     * @param header
     *            the message header
     * @param parameterReader
     *            a reader for reading the parameters to the method
     * @param resultWriter
     *            a writer used for writing the result of invoking the method
     * @throws Exception
     *             if an exception occurred while invoking the service
     */
    void invoke(String name, MessageHeader header, MessageReader parameterReader, ValueWriter resultWriter)
            throws Exception;

    default void invokeAsync(String name, MessageHeader header, MessageReader parameterReader,
            ValueWriter resultWriter, CompletableFuture<Void> f) {
        throw new UnsupportedOperationException();
    }
}
