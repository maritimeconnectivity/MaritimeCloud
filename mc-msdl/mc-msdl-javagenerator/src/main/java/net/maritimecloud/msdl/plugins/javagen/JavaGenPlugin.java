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

    Path licensePath;

    String license;

    Path outputPath;

    String packagePrefix;

    JavaGenPlugin(Path p) {
        this.outputPath = requireNonNull(p);
    }

    /** {@inheritDoc} */
    @Override
    protected void process(Project project) throws Exception {
        if (licensePath != null) {
            StringBuilder sb = new StringBuilder();
            for (String s : Files.readAllLines(licensePath)) {
                sb.append(s).append(CodegenUtil.LS);
            }
            license = sb.toString();
        }
        for (MsdlFile f : project) {
            generate(this, f, outputPath);
        }

    }

    void generate(JavaGenPlugin p, MsdlFile definition, Path rootpath) throws IOException {
        List<CodegenClass> classes = new ArrayList<>();
        for (EnumDeclaration ed : definition.getEnums()) {
            if (!ed.isAnnotationPresent(JavaImplementation.class)) {
                classes.add(JavaGenEnumGenerator.generateEnum(null, ed));
            }
        }
        for (MessageDeclaration md : definition.getMessages()) {
            if (!md.isAnnotationPresent(JavaImplementation.class)) {
                classes.add(new JavaGenMessageGenerator(null, md).generate().c);
            }
        }

        for (MessageDeclaration md : definition.getMessages()) {
            if (!md.isAnnotationPresent(JavaImplementation.class)) {
                classes.add(new JavaGenMessageGenerator(null, md).generate().c);
            }
        }
        for (BroadcastMessageDeclaration bd : definition.getBroadcasts()) {
            // if (!bd.isAnnotationPresent(JavaImplementation.class)) {
            classes.add(new JavaGenBroadcastMessageGenerator(null, bd).generate().c);
            // }
        }
        for (EndpointDefinition ed : definition.getEndpoints()) {
            JavaGenEndpointGenerator g = new JavaGenEndpointGenerator(null, ed).generate();
            classes.add(g.cClient);
            classes.add(g.cServer);
        }

        // for (ServiceDeclaration sd : definition.getServices()) {
        // classes.add(new JavaGenServiceGenerator(sd).generate().c);
        // }
        for (CodegenClass cc : classes) {
            if (definition.getNamespace() != null) {
                cc.setPackage(definition.getNamespace());
            }
            if (license != null) {
                cc.setLicense(license);
            }

            Path path = cc.writeSource(rootpath);
            if (path != null) {
                p.getLogger().info("Wrote " + path);
            }
        }
    }

    /**
     * @param packagePrefix
     *            the packagePrefix to set
     */
    public JavaGenPlugin setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
        return this;
    }

    public JavaGenPlugin setHeader(Path path) {
        this.licensePath = path;
        return this;
    }

    public static JavaGenPlugin create(Path path) {
        return new JavaGenPlugin(path);
    }

    public static JavaGenPlugin create(String path) {
        return create(Paths.get(path));
    }
}
