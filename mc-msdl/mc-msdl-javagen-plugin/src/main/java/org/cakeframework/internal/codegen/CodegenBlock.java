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

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Kasper Nielsen
 */
// This was previously an interface. But there was no real benefits.
// I tried with some kind of multiplexing where you could delegate to multiple codegenblocks
// But it just didnt work with dynamic types
// So no reason to just use an interface
public class CodegenBlock extends AbstractCodegenEntity {

    /** The body of the block, lazy initialized. */
    ArrayList<Object> body;

    public final CodegenBlock add(Object... elements) {
        if (body == null) {
            body = new ArrayList<>(8);
        }
        elements = CodegenUtil.flatten(requireNonNull(elements));
        int left = 0;

        for (int i = 0; i < elements.length; i++) {
            Object o = requireNonNull(elements[i]);
            if (o instanceof CodegenBlock) {
                CodegenBlock cb = (CodegenBlock) o;
                if (left != i) {
                    body.add(Arrays.copyOfRange(elements, left, i - 1));
                }
                if (cb.importSet != null && cb.importSet.size() > 0) {
                    addImports(cb.importSet);
                }
                body.add(o);
                left = i + 1;
            }
        }
        if (elements.length == 0 || left < elements.length) {
            body.add(left == 0 ? elements : Arrays.copyOfRange(elements, left, elements.length));
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final CodegenBlock addImport(Class<?>... classes) {
        return (CodegenBlock) super.addImport(classes);
    }

    /** {@inheritDoc} */
    @Override
    public final CodegenBlock addImports(ImportSet imports) {
        return (CodegenBlock) super.addImports(imports);
    }

    public final CodegenBlock newBlock() {
        CodegenBlock cb = new CodegenBlock();
        cb.setParent(this);
        add(cb);
        return cb;
    }

    public final CodegenBlock newNestedBlock(Object... header) {
        CodegenBlock cb = new CodegenBlock();
        cb.setParent(this);
        add(CodegenUtil.addLast(" {", header)).add(cb).add("}");
        return cb;
    }

    /** {@inheritDoc} */
    public String toString() {
        return toString(0);
    }

    private String toString(int indent) {
        StringBuilder sb = new StringBuilder();
        new BlockRenderer(indent, sb, this).render();
        return sb.toString();
    }
}
