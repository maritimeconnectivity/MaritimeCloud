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

import static org.cakeframework.internal.codegen.CodegenUtil.LS;
import static org.cakeframework.internal.codegen.CodegenUtil.indent;

import java.util.ArrayDeque;

/**
 * 
 * @author Kasper Nielsen
 */
class BlockRenderer {

    int indent;

    /** A string buffer containing the rendered code. */
    private final StringBuilder sb;

    private final CodegenBlock b;

    BlockRenderer(int indent, StringBuilder sb, CodegenBlock b) {
        this.indent = indent;
        this.sb = sb;
        this.b = b;
    }

    public void addLine(Object... oo) {
        if (oo.length > 0) {
            ArrayDeque<Object> ad = new ArrayDeque<>();
            for (int i = 0; i < oo.length; i++) {
                Object o = oo[i];
                if (!(o instanceof CharSequence) || ((CharSequence) o).length() > 0) {
                    ad.add(o);
                }
            }

            if (ad.peek() instanceof CharSequence) {
                CharSequence line = (CharSequence) ad.peek();
                if (line.charAt(0) == '}') {
                    indent--;
                }
            }
            sb.append(indent(indent));
            Object o = null;
            while (!ad.isEmpty()) {
                o = ad.pollFirst();
                if (o instanceof Object[]) {
                    Object[] a = (Object[]) o;
                    for (int i = a.length - 1; i >= 0; i--) {
                        ad.push(a[i]);
                    }
                } else if (o instanceof CodegenBlock) {
                    throw new Error();
                } else if (o.getClass() == Class.class) {
                    Class<?> cc = (Class<?>) o;
                    if (cc.getDeclaringClass() != null) {
                        sb.append(cc.getDeclaringClass().getSimpleName() + "." + cc.getSimpleName());
                    } else {
                        sb.append(((Class<?>) o).getSimpleName());
                    }
                } else {
                    sb.append(o);
                }
            }
            if (o != null && o instanceof CharSequence) {
                CharSequence line = (CharSequence) o;
                if (line.charAt(line.length() - 1) == '{') {
                    indent++;
                }
            }
        }
        sb.append(LS);
    }

    void render() {
        toString(b);
    }

    private void toString(CodegenBlock block) {
        Iterable<Object> iter = block.body;
        if (iter != null) {
            for (Object b : iter) {
                if (b instanceof CodegenBlock) {
                    toString((CodegenBlock) b);
                } else {
                    Object[] oo = (Object[]) b;
                    addLine(oo);
                }
            }
        }
    }
}
