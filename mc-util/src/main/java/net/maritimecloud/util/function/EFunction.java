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

import java.util.Objects;

/**
 * 
 * @author Kasper Nielsen
 */
public abstract class EFunction<T, R> {
    public abstract R apply(T t) throws Exception;

    public <V> EFunction<T, V> compose(final EFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return new EFunction<T, V>() {
            public V apply(T t) throws Exception {
                return after.apply(EFunction.this.apply(t));
            }
        };
    }
}
