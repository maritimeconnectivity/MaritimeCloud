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

import net.maritimecloud.msdl.model.EnumDeclaration.Constant;

/**
 * The declaration of an enum.
 *
 * @author Kasper Nielsen
 */
public interface EnumDeclaration extends Iterable<Constant>, Type, Annotatable {

    /**
     * Returns a list of all constants in this enum.
     *
     * @return a list of all constants in this enum
     */
    List<Constant> getConstants();

    /**
     * Returns the file this enum is defined in.
     *
     * @return the file this enum is defined in
     */
    MsdlFile getFile();

    /**
     * Returns the name of the enum.
     *
     * @return the name of the enum
     */
    String getName();

    /**
     * The predefined constants that make of an enum.
     */
    public interface Constant {

        /**
         * Returns the name of the enum constant.
         *
         * @return the name of the enum constant
         */
        String getName();

        /**
         * Returns the unique integer value of the enum constant.
         *
         * @return the unique integer value of the enum constant
         */
        int getValue();
    }
}
