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

import java.nio.file.Path;

/**
 * Information about
 *
 * @author Kasper Nielsen
 */
public interface SourceToken {

    /**
     * Returns the absolute path where the .msdl is defined.
     *
     * @return the absolute path where the .msdl is defined.
     */
    Path getPath();

    /**
     * Returns the ending column of the entity.
     *
     * @return the ending column of the entity
     */
    int getEndColumn();

    /**
     * Returns the ending line of the entity.
     *
     * @return the ending line of the entity
     */
    int getEndLine();

    /**
     * Returns the starting column of the entity.
     *
     * @return the starting column of the entity
     */
    int getStartColumn();

    /**
     * Returns the starting line of the entity.
     *
     * @return the starting line of the entity
     */
    int getStartLine();
}
