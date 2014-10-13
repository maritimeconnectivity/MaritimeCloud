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
package net.maritimecloud.internal.msdl.parser;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.maritimecloud.internal.msdl.parser.antlr.MsdlParser.AnnotationContext;
import net.maritimecloud.internal.msdl.parser.antlr.MsdlParser.ElementValueArrayInitializerContext;
import net.maritimecloud.internal.msdl.parser.antlr.MsdlParser.ElementValueContext;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author Kasper Nielsen
 */
public class AnnotationContainer {
    private final Map<String, Anno> annotations = new LinkedHashMap<>();

    final ParsedMsdlFile file;

    AnnotationContainer(ParsedMsdlFile file) {
        this.file = requireNonNull(file);
    }

    @SafeVarargs
    final void addAnnotation(String name, Map.Entry<String, Object>... pairs) {
        annotations.put(name, new Anno(name, pairs));
    }

    @SuppressWarnings("unchecked")
    <T extends Annotation> T getAnnotation(Class<T> type) {
        Anno anno = annotations.get(type.getSimpleName());
        if (anno == null) {
            return null;
        }
        return (T) Proxy.newProxyInstance(AnnotationContainer.class.getClassLoader(), new Class<?>[] { type }, anno);
    }

    boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
        return annotations.containsKey(annotation.getSimpleName());
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    AnnotationContainer parse(ParserRuleContext c) {
        for (ParseTree pt : c.children) {
            if (pt instanceof AnnotationContext) {
                AnnotationContext ac = (AnnotationContext) pt;
                String name = ac.annotationName().qualifiedName().getText();
                // System.out.println(name);

                ElementValueContext ev = ac.elementValue();
                final Anno anno;
                if (ev != null) {
                    // System.out.println("Y");
                    List<String> list = new ArrayList<>();

                    if (ev.StringLiteral() != null) {
                        String text = ev.StringLiteral().getText();
                        text = text.replace("\"", "");
                        list.add(text);
                    } else /* if (ev.elementValueArrayInitializer() != null) */{
                        ElementValueArrayInitializerContext ec = ev.elementValueArrayInitializer();
                        for (int i = 0; i < ec.children.size(); i++) {
                            ElementValueContext evc = ec.elementValue(i);
                            if (evc != null) {
                                // System.out.println(ev.getChildCount());
                                String text = evc.StringLiteral().getText();
                                text = text.replace("\"", "");
                                list.add(text);
                            }
                        }
                    }
                    // System.out.println(list);

                    anno = new Anno(name, new Map.Entry[] { new AbstractMap.SimpleImmutableEntry("value",
                            list.toArray(new String[0])) });

                } else {
                    anno = new Anno(name, new Map.Entry[0]);
                }
                annotations.put(name, anno);
            }
        }
        return this;
    }

    static class Anno implements InvocationHandler {
        String name;

        Map.Entry<String, Object>[] pairs;

        Anno(String name, Map.Entry<String, Object>[] pairs) {
            this.name = name;
            this.pairs = pairs;
        }

        /** {@inheritDoc} */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            for (Entry<String, Object> e : pairs) {
                if (e.getKey().equals(method.getName())) {
                    if (method.getReturnType().equals(String[].class)) {
                        // System.out.println("Ret " + Arrays.toString((String[]) e.getValue()));
                        return e.getValue();
                    }
                    return ((String[]) e.getValue())[0];
                }
            }
            return null;
        }
    }
}
