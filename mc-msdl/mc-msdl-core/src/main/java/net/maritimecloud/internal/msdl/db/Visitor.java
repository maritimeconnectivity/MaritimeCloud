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
package net.maritimecloud.internal.msdl.db;

import net.maritimecloud.msdl.MsdlPlugin;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.MsdlFile;
import net.maritimecloud.msdl.model.Project;

/**
 *
 * @author Kasper Nielsen
 */
abstract class Visitor extends MsdlPlugin {

    /** {@inheritDoc} */
    @Override
    protected final void process(Project project) throws Exception {
        visitProject(project);
    }

    public void visitProject(Project project) {
        for (MsdlFile f : project) {
            visitFile(f);
        }
    }

    public void visitFile(MsdlFile file) {
        for (MessageDeclaration md : file.getMessages()) {
            visitMessage(md);
        }
    }

    public void visitMessage(MessageDeclaration message) {

    }


}
