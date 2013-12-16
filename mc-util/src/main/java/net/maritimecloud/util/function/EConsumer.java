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
package net.maritimecloud.util.function;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Consumer} that can throw an exception.
 * 
 * @author Kasper Nielsen
 */
public abstract class EConsumer<T> {

    public abstract void accept(T t) throws Exception;

    public EConsumer<T> chain(final EConsumer<? super T> other) {
        requireNonNull(other);
        return new EConsumer<T>() {
            public void accept(T t) throws Exception {
                EConsumer.this.accept(t);
                other.accept(t);
            }
        };
    }

    public EConsumer<T> filter(final Predicate<T> filter) {
        requireNonNull(filter);
        return new EConsumer<T>() {
            public void accept(T t) throws Exception {
                if (filter.test(t)) {
                    EConsumer.this.accept(t);
                }
            }
        };
    }
}
