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

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.maritimecloud.msdl.MsdlPlugin;
import net.maritimecloud.msdl.MsdlProcessor;
import net.maritimecloud.msdl.model.EndpointDefinition;
import net.maritimecloud.msdl.model.MsdlFile;
import net.maritimecloud.msdl.model.Project;


/**
 *
 * @author Kasper Nielsen
 */
public class TestIt {

    public static void main(String[] args) {
        Logger.getLogger("msdl").setLevel(Level.FINE);

        MsdlProcessor processor = new MsdlProcessor();
        processor.setSourceDirectory(Paths.get("/Users/kasperni/dma-workspace/MaritimeCloud/private/msdl/"));
        // processor.addDependencyDirectory(Paths.get("/Users/kasperni/dma-workspace/MaritimeCloud/private/msdl/"));
        processor.addFile("cmm.msdl"); // relative to source directory
        processor.addFile("mms.msdl"); // relative to source directory

        processor.addPlugin(new MsdlPlugin() {

            @Override
            protected void process(Project project) throws Exception {
                for (MsdlFile f : project) {
                    for (EndpointDefinition e : f.getEndpoints()) {
                        System.out.println(e.getFullName());
                    }
                }
            }
        });
        processor.executePlugins();
    }
}
