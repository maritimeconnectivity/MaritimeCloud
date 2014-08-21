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
package net.maritimecloud.internal.id160;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Various common checks used throughout this project.
 *
 * @author Kasper Nielsen
 * */
public class Checks {

    public static Object checkArray(Object object) {
        requireNonNull(object, "array is null");
        if (!object.getClass().isArray()) {
            throw new IllegalArgumentException("The specified object is not an array but a "
                    + object.getClass().getCanonicalName());
        }
        return object;
    }

    /**
     * Checks that the parameters to a method of the type
     *
     * <pre>
     * someMethod(someArray[], int offset, int length)
     * </pre>
     *
     * are correct.
     *
     * @param arrayLength
     *            the length of the array
     * @param offset
     *            the offset to put data into the array
     * @param length
     *            the length of data
     */
    public static void checkArrayBounds(int arrayLength, int offset, int length) {
        // TODO fix
        // public static void checkBounds(int realLength, int specifiedLength) {
        // if (specifiedLength < 0) {
        // throw new IllegalArgumentException("the specified length is negative, was " + specifiedLength);
        // } else if (realLength<specifiedLength) {
        //
        // }
        // }
    }

    /**
     * Checks whether or not the specified array is {@code null} or contains a {@code null} at any index.
     *
     * @param a
     *            the array to check
     * @return the specified array
     * @throws NullPointerException
     *             if the specified array is null or contains a null at any index
     */
    @SafeVarargs
    public static <T> T[] checkArrayForNulls(String parameterName, T... a) {
        requireNonNull(a, parameterName + " is null");
        for (int i = 0; i < a.length; i++) {
            if (a[i] == null) {
                throw new NullPointerException(parameterName + " array contains a null at index " + i);
            }
        }
        return a;
    }

    /**
     * Equivalent to {@link #arrayNotNull(Object[])} except that this method will copy the specified array before
     * checking it. Checks whether or not the specified collection contains a {@code null}.
     *
     * @param a
     *            a copy of the checked array
     * @throws NullPointerException
     *             if the specified collection contains a null
     */
    @SafeVarargs
    public static <T> T[] checkArrayForNullsAndCopy(String parameterName, T... a) {
        return checkArrayForNulls(parameterName, Arrays.copyOf(a, a.length));
    }

    @SafeVarargs
    public static <T> List<T> checkArrayForNullsAndCopyToUnmodifiableList(String parameterName, T... elements) {
        return Collections.unmodifiableList(Arrays.asList(checkArrayForNullsAndCopy(parameterName, elements)));
    }

    @SafeVarargs
    public static <T> Set<T> checkArrayForNullsAndCopyToUnmodifiableSet(String parameterName, T... elements) {
        return Collections.unmodifiableSet(new HashSet<>(checkArrayForNullsAndCopyToUnmodifiableList(parameterName,
                elements)));
    }

    public static int checkIntIsOneOrGreater(int value, String parameter) {
        if (value <= 0) {
            throw new IllegalArgumentException(parameter + " must be positive (>0), was " + value);
        }
        return value;
    }

    public static int checkIntIsZeroOrGreater(int value, String parameter) {
        if (value < 0) {
            throw new IllegalArgumentException(parameter + " must be non-negative (>=0), was " + value);
        }
        return value;
    }

    public static void checkIntNotEquals(int a, int b, String nameA, String nameB) {
        if (a == b) {
            throw new IllegalArgumentException(nameA + " cannot be equal to " + nameB + ",  [" + nameA + ", " + nameB
                    + " = " + a + "]");
        }
    }

    public static void checkIntNotGreaterThan(int a, int b, String nameA, String nameB) {
        if (a > b) {
            throw new IllegalArgumentException(nameA + " cannot be greater than " + nameB + ", [" + nameA + " = " + a
                    + ", " + nameB + " = " + b + "]");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] checkIterableForNullsAndCopyToArray(String name, Class<T> type, Iterable<? extends T> iterable) {
        if (iterable instanceof Collection) {
            Collection<T> col = (Collection<T>) iterable;
            return checkArrayForNulls(name, col.toArray((T[]) Array.newInstance(type, col.size())));
        }

        ArrayList<T> al = new ArrayList<>();
        for (T o : iterable) {
            al.add(o);
        }
        return checkArrayForNulls(name, al.toArray((T[]) Array.newInstance(type, al.size())));
    }

    public static Object[] checkIterableForNullsAndCopyToArray(String name, Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return checkArrayForNulls(name, ((Collection<?>) iterable).toArray());
        }

        ArrayList<Object> al = new ArrayList<>();
        for (Object o : iterable) {
            al.add(o);
        }
        return checkArrayForNulls(name, al.toArray());
    }

    public static long checkLongIsZeroOrGreater(long value, String parameter) {
        if (value < 0) {
            throw new IllegalArgumentException(parameter + " must be non negative (>=0), was " + value);
        }
        return value;
    }

    public static void checkLongNotGreaterThan(long a, long b, String nameA, String nameB) {
        if (a > b) {
            throw new IllegalArgumentException(nameA + " cannot be greater than " + nameB + ", [" + nameA + " = " + a
                    + ", " + nameB + " = " + b + "]");
        }
    }

    /**
     * Checks that the specified path exists.
     *
     * @param path
     *            the path to check
     * @return the specified path
     * @throws IllegalArgumentException
     *             if the specified path does not exist
     */
    public static Path checkPathExists(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("The specified path does not exist, path = " + path);
        }
        return path;
    }

    /**
     * Checks that the specified path is a directory.
     *
     * @param path
     *            the path to check
     * @return the specified path
     * @throws IllegalArgumentException
     *             if the specified path is not a directory
     */
    public static Path checkPathIsDirectory(Path path) {
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("The specified path was not a directory, path = " + path);
        }
        return path;
    }

    public static void checkPermission(Permission perm) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(perm);
        }
    }

    public static String checkStringIsAlphaNumeric(String string, String name) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                throw new IllegalArgumentException("The specified string (" + name
                        + ") contains a non alpha-numeric character at index " + i + ", string = '" + string + "'");
            }
        }
        return string;
    }
}
