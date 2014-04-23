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
import static org.cakeframework.internal.codegen.CodegenUtil.toStringg;

import java.util.ArrayList;

/**
 *
 * @author Kasper Nielsen
 */
public class CodegenEnum extends CodegenClass {
    final ArrayList<CodegenEnumConstant> constants = new ArrayList<>();

    CodegenEnumConstant newConstant(Object... definition) {
        return newConstant(toStringg(definition));
    }

    public CodegenEnumConstant newConstant(String definition) {
        CodegenEnumConstant cec = new CodegenEnumConstant(definition);
        cec.setParent(this);
        constants.add(cec);
        return cec;
    }

    public static class CodegenEnumConstant extends AbstractCodegenEntity {

        final String definition;

        final ArrayList<CodegenMethod> methods = new ArrayList<>(5);

        JavadocBuilder b;

        CodegenEnumConstant(String definition) {
            this.definition = definition;
        }

        public final CodegenMethod addMethod(boolean predicate, CodegenMethod method) {
            if (predicate) {
                addMethod(method);
            }
            return method;
        }

        public JavadocBuilder javadoc() {
            if (b == null) {
                b = new JavadocBuilder();
            }
            return b;
        }

        public final CodegenMethod addMethod(CodegenMethod method) {
            methods.add(requireNonNull(method));
            method.setParent(this);
            return method;
        }

        public final void addMethods(CodegenMethod... methods) {
            for (CodegenMethod o : methods) {
                addMethod(o);
            }
        }

        public final CodegenMethod addNewMethod() {
            return addMethod(new CodegenMethod());
        }

        public final CodegenMethod newMethod(Object... definition) {
            return newMethod(toStringg(definition));
        }

        /** Creates a new method using the specified definition. */
        public final CodegenMethod newMethod(String definition) {
            CodegenMethod m = addMethod(new CodegenMethod());
            m.setDeclaration(definition);
            return m;
        }

    }
    //
    // public static void main(String[] args) {
    // CodegenEnum ce = new CodegenEnum();
    // ce.setDefinition("public enum FooBar");
    //
    // CodegenEnumConstant c1 = ce.newConstant("FFFF");
    // CodegenMethod newMethod = c1.newMethod("public void hello()");
    // newMethod.add("System.out.println(\"Hello\");");
    //
    // CodegenEnumConstant c2 = ce.newConstant("FFF");
    //
    // newMethod = c2.newMethod("public void hello()");
    // newMethod.add("System.out.println(\"Hello\");");
    //
    // CodegenMethod m = ce.newMethod("public abstract void hello()");
    //
    // System.out.println(ce);
    //
    // }
}

// enum Fff {
// ff(23);
// Enum<Enum<E>>
//
// Fff(int val) {}
// }
