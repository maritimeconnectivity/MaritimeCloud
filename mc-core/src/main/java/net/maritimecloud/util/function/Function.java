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
 * 
 * @author Kasper Nielsen
 */
public abstract class Function<T, R> {

    /**
     * Yield an appropriate result object for the input object.
     * 
     * @param t
     *            the input object
     * @return the function result
     */
    public abstract R apply(T t);

    /**
     * Combine with another function returning a function which preforms both functions.
     * 
     * @param <V>
     *            Type of output objects from the combined function. May be the same type as {@code <U>}.
     * @param after
     *            An additional function to be applied to the result of this function.
     * @return A function which performs both the original function followed by a second function.
     */
    public final <V> Function<T, V> compose(final Function<? super R, ? extends V> after) {
        requireNonNull(after);
        return new Function<T, V>() {
            public V apply(T t) {
                return after.apply(Function.this.apply(t));
            }
        };
    }
}
