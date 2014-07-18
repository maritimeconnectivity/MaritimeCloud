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
package net.maritimecloud.msdl.model;

import java.util.List;

/**
 * The declaration of an endpoint.
 *
 * @author Kasper Nielsen
 */
public interface EndpointDefinition extends Annotatable {

    /**
     * Returns the file this endpoint is defined in.
     *
     * @return the file this endpoint is defined in
     */
    MsdlFile getFile();

    /**
     * Returns a list of fields in the message.
     *
     * @return a list of fields in the message
     */
    List<EndpointMethod> getFunctions();

    /**
     * Returns the name of the message.
     *
     * @return the name of the message
     */
    String getName();

}
