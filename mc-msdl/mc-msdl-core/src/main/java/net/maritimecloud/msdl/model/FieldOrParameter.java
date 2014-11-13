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


/**
 * The definition of a message field or an endpoint argument.
 *
 * @author Kasper Nielsen
 */
public interface FieldOrParameter {

    /**
     * Returns a non-null comment for the field.
     *
     * @return a non-null comment for the field
     */
    CommentDeclaration getComment();

    /**
     * Returns the name of the field.
     *
     * @return the name of the field
     */
    String getName();

    /**
     * Returns the tag of the field.
     *
     * @return the tag of the field
     */
    int getTag();

    /**
     * Returns the type of the field
     *
     * @return the type of the field
     */
    Type getType();

    /**
     * Returns the default value of the field or parameter.
     * <p>
     * Currently returns <code>null</code> always
     *
     * @return the default value of the field or parameter
     */
    default Object getDefaultValue() {
        return null;
    }
}
