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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.maritimecloud.msdl.MsdlPlugin;
import net.maritimecloud.msdl.model.BroadcastMessageDeclaration;
import net.maritimecloud.msdl.model.EndpointDefinition;
import net.maritimecloud.msdl.model.EnumDeclaration;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.MsdlFile;
import net.maritimecloud.msdl.model.Project;
import net.maritimecloud.msdl.plugins.javagen.annotation.JavaImplementation;

import org.cakeframework.internal.codegen.CodegenClass;
import org.cakeframework.internal.codegen.CodegenUtil;

/**
 *
 * @author Kasper Nielsen
 */
public class JavaGenPlugin extends MsdlPlugin {

    Path fileHeader;

    String license;

    Path outputPath;

    String packagePrefix;

    JavaGenPlugin(Path p) {
        this.outputPath = requireNonNull(p);
    }

    void generateSourceForFile(MsdlFile file) throws IOException {
        List<CodegenClass> classes = new ArrayList<>();

        // Generate enums in the file
        for (EnumDeclaration ed : file.getEnums()) {
            if (!ed.isAnnotationPresent(JavaImplementation.class)) {
                classes.add(JavaGenEnumGenerator.generateEnum(null, ed));
            }
        }

        // Generate ordinary messages in the file
        for (MessageDeclaration md : file.getMessages()) {
            if (!md.isAnnotationPresent(JavaImplementation.class)) {
                classes.add(new JavaGenMessageGenerator(null, md).generate().c);
            }
        }

        // Generate broadcast messages in the file
        for (BroadcastMessageDeclaration bd : file.getBroadcasts()) {
            if (!bd.isAnnotationPresent(JavaImplementation.class)) {
                classes.add(new JavaGenBroadcastMessageGenerator(null, bd).generate().c);
            }
        }

        // Generate endpoints in the file
        for (EndpointDefinition ed : file.getEndpoints()) {
            JavaGenEndpointGenerator g = new JavaGenEndpointGenerator(null, ed).generate();
            classes.add(g.cClient); // add client part of endpoint
            classes.add(g.cServer); // add server part of endpoint
        }

        for (CodegenClass cc : classes) {
            if (file.getNamespace() != null) {
                cc.setPackage(file.getNamespace());
            }
            if (license != null) {
                cc.setLicense(license);
            }

            Path path = cc.writeSource(outputPath);
            if (path != null) {
                getLogger().info("Wrote " + path);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void process(Project project) throws Exception {
        // If user has a set a file header/license generate it first
        if (fileHeader != null) {
            StringBuilder sb = new StringBuilder();
            for (String s : Files.readAllLines(fileHeader)) {
                sb.append(s).append(CodegenUtil.LS);
            }
            license = sb.toString();
        }

        for (MsdlFile f : project) {
            generateSourceForFile(f);
        }

    }

    public JavaGenPlugin setHeader(Path path) {
        this.fileHeader = path;
        return this;
    }

    /**
     * @param packagePrefix
     *            the packagePrefix to set
     */
    public JavaGenPlugin setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
        return this;
    }

    public static JavaGenPlugin create(Path path) {
        return new JavaGenPlugin(path);
    }

    public static JavaGenPlugin create(String path) {
        return create(Paths.get(path));
    }
}
