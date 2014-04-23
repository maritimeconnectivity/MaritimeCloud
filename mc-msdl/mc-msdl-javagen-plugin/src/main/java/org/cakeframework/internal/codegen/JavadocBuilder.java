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

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author Kasper Nielsen
 */
public class JavadocBuilder {

    private String main;

    private final List<Entry<String, String>> parameters = new ArrayList<>();

    private String retur;

    public JavadocBuilder addParameter(String name, String contents) {
        parameters.add(new SimpleImmutableEntry<>(name, contents));
        return this;
    }

    public JavadocBuilder addReturn(String contents) {
        retur = contents;
        return this;
    }

    public JavadocBuilder set(String main) {
        this.main = main;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb, 0);
        return sb.toString();
    }

    /**
     * @param b
     *            foo
     * @param indent
     */
    public void toString(StringBuilder b, int indent) {
        if (main == null && parameters.size() == 0 && retur == null) {
            return;
        }

        if (retur == null && parameters.size() == 0 && main.length() <= 90) {
            b.append(indent(indent)).append("/** " + main + " */").append(LS);
        } else {
            b.append(indent(indent)).append("/**").append(LS);
            // TODO wordwrap
            if (main != null) {
                b.append(indent(indent)).append(" * " + main).append(LS);
            }
            if (main != null & parameters.size() > 0) {
                // Add an empty line between the description and the list of parameters
                b.append(indent(indent)).append(" *").append(LS);
            }
            for (Entry<String, String> e : parameters) {
                b.append(indent(indent)).append(" * @param " + e.getKey()).append(LS);
                b.append(indent(indent)).append(" *            " + e.getValue()).append(LS);
            }
            if (retur != null) {
                b.append(indent(indent)).append(" * @return " + retur).append(LS);
            }

            b.append(indent(indent)).append(" */").append(LS);
        }
    }

    public static JavadocBuilder create(String main) {
        JavadocBuilder b = new JavadocBuilder();
        b.set(main);
        return b;
    }

}
