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
package net.maritimecloud.msdl.plugins.javagen;

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.maritimecloud.msdl.MsdlProcessor;


/**
 *
 * @author Kasper Nielsen
 */
public class JavaGenTest {

    public static void main(String[] args) {
        Logger lo = Logger.getLogger("msdl");
        lo.setLevel(Level.FINE);
        MsdlProcessor g = new MsdlProcessor();
        g.setSourceDirectory(Paths.get("/Users/kasperni/dma-workspace/MaritimeCloud/mc-msdl/"));
        g.addDependencyDirectory(Paths.get("/Users/kasperni/dma-workspace/MaritimeCloud/mc-msdl/"));
        g.addFile("geometry.msdl");
        g.addFile("indentedrouted.msdl");
        g.addPlugin(JavaGenPlugin.create("/Users/kasperni/dma-workspace/MaritimeCloud/mc-msdl/generated"));
        g.process();
    }
}
