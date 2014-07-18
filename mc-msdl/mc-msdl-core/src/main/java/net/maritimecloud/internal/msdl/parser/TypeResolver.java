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

import java.util.Collection;

import net.maritimecloud.msdl.MsdlLogger;
import net.maritimecloud.msdl.model.BaseType;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 *
 * @author Kasper Nielsen
 */
class TypeResolver {
    final Collection<ParsedFile> files;

    final MsdlLogger logger;

    final Multimap<String, Object> types = ArrayListMultimap.create();

    TypeResolver(MsdlLogger logger, Collection<ParsedFile> files) {
        this.files = requireNonNull(files);
        this.logger = requireNonNull(logger);
    }


    void resolve() {
        for (ParsedFile f : files) {
            for (ParsedService ps : f.listOf(ParsedService.class)) {
                for (ParsedBroadcastMessage m : ps.broadcastMessages) {
                    resolveMessage0(m);
                }
            }
            for (ParsedMessage m : f.listOf(ParsedMessage.class)) {
                resolveMessage0(m);
            }
            for (ParsedEnum e : f.listOf(ParsedEnum.class)) {
                resolveEnum0(e);
            }
        }

        for (ParsedFile f : files) {
            for (ParsedService ps : f.listOf(ParsedService.class)) {
                for (ParsedBroadcastMessage m : ps.broadcastMessages) {
                    resolveMessage1(m);
                }
            }
            for (ParsedMessage m : f.listOf(ParsedMessage.class)) {
                resolveMessage1(m);
            }
        }
        for (ParsedFile f : files) {
            for (ParsedEndpoint e : f.listOf(ParsedEndpoint.class)) {
                for (ParsedEndpointFunction fu : e.endpointFunction.values()) {
                    if (fu.returnType != null) {
                        resolveType1(fu.returnType);
                    }
                    for (ParsedEndpointFunctionArgument s : fu.parametersByName.values()) {
                        resolveType1(s.type);
                    }
                }
            }
        }
    }

    private void resolveEnum0(ParsedEnum e) {
        types.put(e.getName(), e);
    }

    private void resolveMessage0(ParsedMessage m) {
        types.put(m.getName(), m);
    }

    private void resolveMessage1(ParsedMessage m) {
        for (ParsedField f : m.fieldsByTag.values()) {
            resolveType1(f.type);
        }
    }

    private void resolveType1(ParsedType t) {
        if (t.type == null) {
            String name = t.referenceName;
            Collection<Object> l = types.get(name);
            if (l.size() == 0) {
                logger.error("Could not find type '" + name + "', Available types = " + types.keySet());
            } else if (l.size() > 1) {
                logger.error("Too many of types " + name);
            } else {
                Object o = l.iterator().next();
                t.messageOrEnum = o;
                if (o instanceof ParsedEnum) {
                    t.type = BaseType.ENUM;
                } else {
                    t.type = BaseType.MESSAGE;
                }
            }
        }
        for (ParsedType tt : t.arguments) {
            resolveType1(tt);
        }
    }
}
