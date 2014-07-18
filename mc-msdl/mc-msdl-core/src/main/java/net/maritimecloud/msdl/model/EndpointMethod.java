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
 *
 * @author Kasper Nielsen
 */
public interface EndpointMethod {

    /**
     * Returns a non-null comment for this method.
     *
     * @return a non-null comment for this method
     */
    CommentDeclaration getComment();

    /**
     * Returns the name of this method.
     *
     * @return the name of this method
     */
    String getName();

    /**
     * Returns a list of parameters to the method.
     *
     * @return a list of parameters to the method
     */
    List<FieldOrParameter> getParameters();

    /**
     * Returns the return type of the method
     *
     * @return the return type of the method
     */
    Type getReturnType();
}
