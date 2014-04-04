/*
 * Copyright (c) 2008 Kasper Nielsen.
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
package org.cakeframework.internal.codegen;

import static org.cakeframework.internal.codegen.CodegenUtil.toStringg;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Kasper Nielsen
 */
public class Codegen {

    final ConcurrentHashMap<CodegenClass, CodegenClass> classes = new ConcurrentHashMap<>();

    private final String defaultPackageName;

    private Object lateInitializer;

    final Path sourcePath;

    final PrintWriter[] pws;

    final ClassLoader parent;

    public Codegen() {
        this(new CodegenConfiguration());
    }

    public Codegen(CodegenConfiguration configuration) {
        sourcePath = configuration.getSourcePath();
        defaultPackageName = configuration.getDefaultPackage();
        pws = configuration.getCodeWriters().toArray(new PrintWriter[0]);
        // pws = new PrintWriter[1];
        // pws[0] = new PrintWriter(System.out);
        final ClassLoader cl = configuration.getClassLoaderParent();
        parent = cl == null ? Thread.currentThread().getContextClassLoader() : cl;
        // TODO CHECK SECURITY
    }

    public Codegen(Codegen p) {
        sourcePath = p.sourcePath;
        defaultPackageName = p.defaultPackageName;
        pws = p.pws;
        this.parent = p.parent;
    }

    public <T extends CodegenClass> T addClass(T clazz) {
        if (clazz.getClassLoader() != null) {
            throw new IllegalArgumentException("clazz has already been added to a codegen");
        }
        if (clazz instanceof LazyInitializer) {
            create(clazz, this);
        }
        if (clazz.getPackage() == null) {
            clazz.setPackage(defaultPackageName);
        }
        classes.put(clazz, clazz);
        return clazz;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    final void create(CodegenClass claz, Codegen codegen) {
        ((LazyInitializer) claz).accept(lateInitializer);
        for (CodegenMethod a : claz.getMethods()) {
            if (a instanceof LazyInitializer) {
                LazyInitializer sm = (LazyInitializer) a;
                sm.accept(lateInitializer);
            }
        }
        if (claz.innerClasses != null) {
            for (CodegenClass cc : claz.innerClasses) {
                if (cc instanceof LazyInitializer) {
                    create(cc, codegen);
                }
            }
        }
    }


    public CodegenClass newClass() {
        return addClass(new CodegenClass());
    }

    public CodegenClass newClass(Object... header) {
        return newClass(toStringg(header));
    }

    public CodegenClass newClass(String definition) {
        return addClass(new CodegenClass().setDefinition(definition));
    }

    public void setLateInitializerObject(Object o) {
        this.lateInitializer = o;
    }
}
