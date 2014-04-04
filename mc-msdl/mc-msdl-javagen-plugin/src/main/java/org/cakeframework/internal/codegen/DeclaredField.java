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

/**
 *
 * @author Kasper Nielsen
 */
class DeclaredField {
    String contents;

    String javadoc;

    DeclaredField(String contents) {
        this(contents, null);
    }

    DeclaredField(String contents, String javadoc) {
        this.contents = contents;
        this.javadoc = javadoc;
    }

    void render(StringBuilder sb, int indent) {
        sb.append(LS);// Add empty line before each field declaration
        if (javadoc != null) {// add optional javadoc
            sb.append(indent(indent)).append("/** ").append(javadoc).append(" */").append(LS);
        }
        sb.append(indent(indent)).append(contents).append(LS); // add field with one indent block
    }
}
