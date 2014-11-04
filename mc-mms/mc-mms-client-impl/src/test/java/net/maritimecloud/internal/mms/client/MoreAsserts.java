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
package net.maritimecloud.internal.mms.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.internal.ArrayComparisonFailure;

/**
 * And extension to {@link Assert} with useful asserts.
 * 
 * @author Kasper Nielsen
 */
public class MoreAsserts extends Assert {

    public static void assertThrows(Class<? extends Throwable> expected, String msg, Runnable r) {
        try {
            r.run();
        } catch (Throwable t) {
            if (!expected.isAssignableFrom(t.getClass())) {
                throw new AssertionError("expected " + expected.getSimpleName() + ", but was "
                        + t.getClass().getSimpleName(), t);
            }
            assertEquals(msg, t.getMessage());
            return;
        }
        throw new AssertionError("expected " + expected.getSimpleName());
    }

    @SafeVarargs
    public static void assertThrows(Class<? extends Throwable> expected, Runnable r,
            Class<? extends Throwable>... causes) {
        try {
            r.run();
        } catch (Throwable t) {
            if (!expected.isAssignableFrom(t.getClass())) {
                throw new AssertionError("expected " + expected.getSimpleName() + ", but was "
                        + t.getClass().getSimpleName(), t);

            }
            assertCausesEquals(t.getCause(), new LinkedList<>(Arrays.asList(causes)));
            return;
        }
        throw new AssertionError("expected " + expected.getSimpleName());
    }

    private static void assertCausesEquals(Throwable cause, Queue<Class<? extends Throwable>> causes) {
        if (cause == null) {
            assertTrue(causes.isEmpty());
        } else {
            assertFalse(causes.isEmpty());
            Class<? extends Throwable> t = causes.poll();
            assertTrue("Expected cause of type " + t + " but was " + cause.getClass(),
                    t.isAssignableFrom(cause.getClass()));
            assertCausesEquals(cause.getCause(), causes);
        }
    }

    public static void assertNullPointerException(Runnable r) {
        try {
            r.run();
            throw new AssertionError("Exception NullPointerException");
        } catch (NullPointerException ok) {}
    }

    public static void assertUtilityClass(Class<?> c) {
        assertTrue("Class " + c.getCanonicalName() + " should be final", Modifier.isFinal(c.getModifiers()));
        assertEquals(1, c.getDeclaredConstructors().length);
        Constructor<?> constructor;
        try {
            constructor = c.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
        assertFalse(constructor.isAccessible());
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        // This is mostly for coverage
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
        constructor.setAccessible(false);
        for (Method method : c.getDeclaredMethods()) {
            assertTrue(Modifier.isStatic(method.getModifiers()));
        }
    }

    private static <T> List<T> _asList(Iterable<? extends T> iterable) {
        Iterator<? extends T> iterator = iterable.iterator();
        ArrayList<T> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public static void assertTrueWithin(Supplier<Boolean> sup, long timeout, TimeUnit unit) throws InterruptedException {
        if (sup.get()) {
            return;
        }
        long now = System.nanoTime();
        long deadline = now + unit.toNanos(timeout);
        long sleep = 1;
        while (now < deadline) {
            sleep = Math.min(sleep * 2, deadline - now);
            TimeUnit.NANOSECONDS.sleep(sleep);
            if (sup.get()) {
                return;
            }
            now = System.nanoTime();
        }
        throw new AssertionError("Timed out");
    }

    public static void assertArrayEquals(Object[] expected, Object[] actual) {
        try {
            Assert.assertArrayEquals(expected, actual);
        } catch (ArrayComparisonFailure e) {
            System.err.println("Expected " + Arrays.toString(expected));
            System.err.println("Actual   " + Arrays.toString(actual));
            throw e;
        }
    }

    public static void assertArrayEquals(Object[] expected, Object[] actuals, boolean isOrdered) {
        if (isOrdered) {
            assertArrayEquals(expected, actuals);
        } else {
            assertArrayEqualsInAnyOrder(expected, actuals);
            //
            // assertEquals(
            // "Expected another length of the array, [expected length= " + expected.length + ", was length="
            // + actuals.length + "] [expected elements=" + Arrays.toString(expected) + ", was="
            // + Arrays.toString(actuals) + "]", expected.length, actuals.length);
            // TODO compare Sets of the two
        }
    }

    static void assertArrayEqualsInAnyOrder(Object[] expected, Object[] actual) {
        assertArraysAreSameLength(expected, actual);
        assertEquals(0, assertArrayIsArraySubset0(expected, actual).size());
    }

    public static void assertArrayEqualsInAnyOrder(Object[] expected, Object[] actual, boolean isOrdered) {
        if (isOrdered) {
            assertArrayEquals(expected, actual);
        } else {
            assertArrayEqualsInAnyOrder(expected, actual);
        }
    }

    /**
     * Asserts that all elements in the specified subSet is contained in the superSet
     * 
     * @param superSet
     * @param subSet
     * @return
     */
    static Map<Object, Integer> assertArrayIsArraySubset0(Object[] superSet, Object[] subSet) {
        Map<Object, Integer> map = new HashMap<>();
        for (Object o : superSet) {
            Integer l = map.get(o);
            map.put(o, l == null ? 1 : l + 1);
        }
        for (Object o : subSet) {
            Integer l = map.get(o);
            if (l == null) {
                fail("Element " + o + " was not expected [actual type=" + o.getClass() + ": expected type = "
                        + superSet[0].getClass());
            } else if (l.equals(1)) {
                map.remove(o);
            } else {
                map.put(o, l - 1);
            }
        }
        return map;
    }

    public static void assertArrayIsSubsetInAnyOrder(Object[] expected, Object[] actual) {
        assertTrue(actual.length <= expected.length);
        assertArrayIsArraySubset0(expected, actual).size();
    }

    private static void assertArraysAreSameLength(Object expected, Object actual) {
        int actualLength = Array.getLength(actual);
        int expectedLength = Array.getLength(expected);
        if (actualLength != expectedLength) {
            fail("Array lengths differed [expected.length=" + expectedLength + ", actual.length=" + actualLength + "]");
        }
    }

    public static void assertSetEquals(Set<?> actual, Object... expected) {
        Set<?> set = new HashSet<>(Arrays.asList(expected));// Eclipse compiler
        assertEquals(set, actual);
    }

    public static void assertCollectionEqualInAnyOrder(Iterable<?> expected, Collection<?> actual) {
        assertArrayEqualsInAnyOrder(_asList(expected).toArray(), actual.toArray());
    }

    public static void assertCollectionIsCollectionSubset(Iterable<?> expected, Collection<?> actual) {
        assertArrayIsSubsetInAnyOrder(_asList(expected).toArray(), actual.toArray());
    }

    public static <T> T assertIsSerializable(T o) {
        T result = null;
        try {
            result = readWrite(o);
        } catch (NotSerializableException e) {
            throw new AssertionFailedError(o + " not serialiable");
        }

        Class<?> clz = o.getClass();
        final Field f;
        try {
            f = clz.getDeclaredField("serialVersionUID");
        } catch (NoSuchFieldException e) {
            throw new AssertionError("Clazz " + clz.getCanonicalName() + " does not declare a serialVersionUID field");
        } catch (SecurityException e) {
            throw new Error(e);
        }
        if (!Modifier.isStatic(f.getModifiers())) {
            throw new AssertionError("Clazz " + clz.getCanonicalName()
                    + " does declare a serialVersionUID field, but it is not static");
        }
        assertNotNull(result);
        return result;
    }

    public static void assertIterableEquals(Iterable<?> expected, Iterable<?> actual) {
        Iterator<?> e = expected.iterator();
        Iterator<?> a = actual.iterator();
        while (e.hasNext()) {
            assertTrue(a.hasNext());
            assertEquals(e.next(), a.next());
        }
        assertFalse(a.hasNext());
    }

    /** Compares to list ignoring the order of elements in the lists. */
    /**
     * @param i1
     * @param i2
     */
    public static void assertIterableEqualsInAnyOrder(Collection<? extends Comparable<?>> i1,
            Collection<? extends Comparable<?>> i2) {
        List<? extends Comparable<?>> l1 = new ArrayList<>(i1);
        List<? extends Comparable<?>> l2 = new ArrayList<>(i1);
        Assert.assertEquals(l1.size(), l2.size());
        Comparator<Comparable<?>> cmp = new Comparator<Comparable<?>>() {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public int compare(Comparable o1, Comparable o2) {
                return o1 == null ? -1 : o2 == null ? 1 : o1.compareTo(o2);
            }
        };
        Collections.sort(l1, cmp);
        Collections.sort(l2, cmp);

        Assert.assertEquals(l1, l2);
    }

    public static void assertMapIsMapSubset(Map<?, ?> fullMap, Map<?, ?> submap) {
        submap.forEach((k, v) -> {
            assertTrue(fullMap.containsKey(k));// need to check this if value=null
            assertEquals(fullMap.get(k), v);
        });
    }

    public static void assertNotSerializable(Object o) {
        try {
            readWrite(o);
            throw new AssertionFailedError(o + " is serialiable");
        } catch (NotSerializableException nse) {/* ok */
        }
    }

    public static <T> T assertOne(Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            throw new AssertionError("Specified iterable is empty");
        }

        T t = iterator.next();
        if (iterator.hasNext()) {
            throw new AssertionError("Specified iterable contains more than 1 element");
        }
        return t;
    }

    public static <T> T assertOne(String message, Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            T t = iterator.next();
            if (!iterator.hasNext()) {
                return t;
            }
        }
        throw new AssertionError(message);
    }

    /**
     * A best effort tet
     * 
     * @param text
     *            the error text in case the reference was not garbage collected.
     * @param ref
     *            the reference to test
     */
    public static void assertReferenceIsGCed(String text, Reference<?> ref) {
        List<byte[]> alloc = new ArrayList<>();
        int size = 100000;
        for (int i = 0; i < 50; i++) {
            if (ref.get() == null) {
                return;
            }
            try {
                System.gc();
            } catch (OutOfMemoryError ignore) {}

            try {
                System.runFinalization();
            } catch (OutOfMemoryError ignore) {}

            try {
                alloc.add(new byte[size]);
                size = (int) (size * 1.3d);
            } catch (OutOfMemoryError ignore) {
                size = size / 2;
            }

            try {
                if (i % 3 == 0) {
                    Thread.sleep(i * 5);
                }
            } catch (InterruptedException ignore) {}
        }
        fail(text);
    }

    public static void assertSerializableAndEquals(Object expectedAndActual) {
        assertSerializableAndEquals(expectedAndActual, expectedAndActual);
    }

    public static void assertSerializableAndEquals(Object expected, Object actual) {
        final Object o;
        try {
            o = readWrite(actual);
        } catch (NotSerializableException e) {
            throw new AssertionFailedError(actual + " not serialiable");
        }
        assertEquals(expected, o);
    }

    public static void assertSingletonSerializable(Object o) {
        assertIsSerializable(o);
        Assert.assertSame(o, serializeAndUnserialize(o));
    }

    @SuppressWarnings("unchecked")
    private static <T> T readWrite(T o) throws NotSerializableException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(20000);
            try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(bout));) {
                out.writeObject(o);
            }
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(bin));
            return (T) in.readObject();
        } catch (NotSerializableException nse) {
            throw nse;
        } catch (ClassNotFoundException | IOException e) {
            throw new Error(e);// should'nt happen
        }
    }

    /**
     * A helper method that serializes and deserializes an object.
     * 
     * @param o
     *            the object to serialize and deserialize
     * @return a serialized and deserialized instance of the specified object
     */
    public static <T> T serializeAndUnserialize(T o) {
        try {
            return readWrite(o);
        } catch (Exception e) {
            throw new AssertionError(o + " not serialiable", e);
        }
    }

}
