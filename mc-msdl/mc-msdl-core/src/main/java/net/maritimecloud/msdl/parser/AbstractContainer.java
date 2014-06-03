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
package net.maritimecloud.msdl.parser;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;

import net.maritimecloud.msdl.model.FileDeclaration;


/**
 *
 * @author Kasper Nielsen
 */
abstract class AbstractContainer {
    final AnnotationContainer ac;

    final ParsedFile file;

    String name;

    AbstractContainer(ParsedFile file, AnnotationContainer ac) {
        this.file = requireNonNull(file);
        this.ac = ac;
    }

    public <T extends Annotation> T getAnnotation(Class<T> type) {
        return ac.getAnnotation(type);
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
        return ac.isAnnotationPresent(annotation);
    }

    public String getName() {
        return name;
    }

    public FileDeclaration getFile() {
        return file;
    }
}
