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
public abstract class Predicate<T> {

    /** A predicate that always returns false */
    public static final Predicate<Object> FALSE = new Predicate<Object>() {
        public boolean test(Object element) {
            return false;
        }
    };

    /** A predicate that always returns true */
    public static final Predicate<Object> TRUE = new Predicate<Object>() {
        public boolean test(Object element) {
            return true;
        }
    };

    /**
     * Returns a predicate which evaluates to {@code true} only if this predicate and the provided predicate both
     * evaluate to {@code true}. If this predicate returns {@code false} then the remaining predicate is not evaluated.
     *
     * @return a new predicate which returns {@code true} only if both predicates return {@code true}.
     */
    public final Predicate<T> and(final Predicate<? super T> p) {
        requireNonNull(p);
        return new Predicate<T>() {
            @Override
            public boolean test(T element) {
                return Predicate.this.test(element) && p.test(element);
            }

            public String toString() {
                return Predicate.this + " && " + p;
            }
        };
    }

    /**
     * Returns a predicate which negates the result of this predicate.
     *
     * @return a new predicate who's result is always the opposite of this predicate.
     */
    public final Predicate<T> negate() {
        return new Predicate<T>() {
            @Override
            public boolean test(T element) {
                return !Predicate.this.test(element);
            }

            public String toString() {
                return "!" + Predicate.this;
            }
        };
    }

    /**
     * Returns a predicate which evaluates to {@code true} if either this predicate or the provided predicate evaluates
     * to {@code true}. If this predicate returns {@code true} then the remaining predicate is not evaluated.
     *
     * @return a new predicate which returns {@code true} if either predicate returns {@code true}.
     */
    public final Predicate<T> or(final Predicate<? super T> p) {
        requireNonNull(p);
        return new Predicate<T>() {
            @Override
            public boolean test(T element) {
                return Predicate.this.test(element) || p.test(element);
            }

            public String toString() {
                return Predicate.this + " || " + p;
            }
        };
    }

    public abstract boolean test(T element);

    /**
     * Returns a predicate that evaluates to {@code true} if all or none of the component predicates evaluate to
     * {@code true}.
     *
     * @return a predicate that evaluates to {@code true} if all or none of the component predicates evaluate to
     *         {@code true}
     */
    public final Predicate<T> xor(final Predicate<? super T> p) {
        requireNonNull(p);
        return new Predicate<T>() {
            @Override
            public boolean test(T element) {
                return Predicate.this.test(element) ^ p.test(element);
            }

            public String toString() {
                return Predicate.this + " ^ " + p;
            }
        };
    }
}
