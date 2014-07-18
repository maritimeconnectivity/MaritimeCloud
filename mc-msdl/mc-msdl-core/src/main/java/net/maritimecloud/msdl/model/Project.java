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

import java.util.Map;

/**
 * A project is root node in the MSDL model and basically consist of all the files that should be processed by each
 * plugin.
 *
 * @author Kasper Nielsen
 */
public interface Project extends Iterable<MsdlFile> {

    /**
     * Returns a map of all MSDL files that should be processed. The relative filename is the key in the returned map
     *
     * @return a map of all MSDL files that should be processed
     */
    Map<String, MsdlFile> getFiles();
}
