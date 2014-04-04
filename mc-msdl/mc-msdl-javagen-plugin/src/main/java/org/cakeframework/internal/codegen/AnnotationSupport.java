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
import static org.cakeframework.internal.codegen.CodegenUtil.LS;
import static org.cakeframework.internal.codegen.CodegenUtil.indent;

import java.util.ArrayList;

/**
 * 
 * @author Kasper Nielsen
 */
class AnnotationSupport {
    private final ArrayList<String> annotations = new ArrayList<>(5);

    <T extends AbstractCodegenEntity> T addAnnotation(T ace, Class<?> annotation) {
        ace.addImport(annotation);
        annotations.add("@" + annotation.getSimpleName());
        return ace;
    }

    <T extends AbstractCodegenEntity> T addAnnotation(T ace, String annotation) {
        annotations.add(requireNonNull(annotation));
        return ace;
    }

    void toString(StringBuilder sb, int indent) {
        for (String an : annotations) {
            sb.append(indent(indent)).append(an).append(LS);
        }
    }
}
