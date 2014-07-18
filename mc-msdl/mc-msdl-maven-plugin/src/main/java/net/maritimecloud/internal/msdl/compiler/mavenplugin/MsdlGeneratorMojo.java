package net.maritimecloud.internal.msdl.compiler.mavenplugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.google.common.collect.ImmutableList;

/**
 * Goal which touches a timestamp file.
 *
 */
@Mojo(name = "compile", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true)
public class MsdlGeneratorMojo extends AbstractMsdlMojo {

    /** The source directories containing the msdl files to be compiled. */
    @Parameter(required = true, defaultValue = "${basedir}/src/main/msdl")
    private File msdlSourceRoot;

    /** This is the directory into which the {@code .java} will be created. */
    @Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/msdl/java")
    private File outputDirectory;


    @Override
    protected void attachFiles() {
        project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
        projectHelper.addResource(project, msdlSourceRoot.getAbsolutePath(), ImmutableList.of("**/*.msdl"),
                ImmutableList.of());
        buildContext.refresh(outputDirectory);
    }

    /** {@inheritDoc} */
    @Override
    protected File getMsdlSourceRoot() {
        return msdlSourceRoot;
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    protected List<Artifact> getDependencyArtifacts() {
        return project.getCompileArtifacts();
    }


    /** {@inheritDoc} */
    @Override
    protected File getOutputDirectory() {
        return outputDirectory;
    }


    /** {@inheritDoc} */
    @Override
    protected File getDescriptorSetOutputDirectory() {
        return outputDirectory;
    }
}
