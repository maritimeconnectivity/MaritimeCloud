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

import java.io.IOException;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.core.serialization.MessageReader;
import net.maritimecloud.core.serialization.ValueWriter;
import net.maritimecloud.util.geometry.Position;

/**
 * The implementation of an endpoint.
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
     * @param context
     *            the caller context
     * @param parameterReader
     *            a reader for reading the parameters to the method
     * @param resultWriter
     *            a writer used for writing the result of invoking the method
     * @throws IOException
     *             if an exception occurred while invoking the service
     */
    void invoke(String name, Context context, MessageReader parameterReader, ValueWriter resultWriter) throws Exception;

    interface Context {

        /**
         * Returns the identity of the calling party.
         *
         * @return the identity of the calling party
         */
        MaritimeId getCaller();

        /**
         * If the calling party has a position, returns said position. Otherwise returns <code>null</code>.
         *
         * @return if the calling party has a position, returns said position
         */
        Position getCallerPosition();
    }
}
