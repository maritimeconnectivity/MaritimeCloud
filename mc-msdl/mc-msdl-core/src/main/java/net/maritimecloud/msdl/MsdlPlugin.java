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
package net.maritimecloud.msdl;

import net.maritimecloud.msdl.model.Project;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class MsdlPlugin {

    /** The logger, initialized before the processor invokes {@link #process(Project)}. */
    MsdlLogger logger;

    /**
     * Returns the logger that should be used for reporting to the user.
     *
     * @return the logger that should be used for reporting to the user
     */
    public final MsdlLogger getLogger() {
        if (logger == null) {
            throw new IllegalStateException("Can only access the logger from the #process method");
        }
        return logger;
    }

    /**
     * Invokes the plugin.
     *
     * @param project
     *            the project to process
     * @throws Exception
     *             if the plugin failed to process the project
     */
    protected abstract void process(Project project) throws Exception;
}
