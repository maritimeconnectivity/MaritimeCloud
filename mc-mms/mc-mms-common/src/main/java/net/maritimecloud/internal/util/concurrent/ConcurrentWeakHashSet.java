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
package net.maritimecloud.internal.util.concurrent;

import static java.util.Objects.requireNonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Kasper Nielsen
 */
public class ConcurrentWeakHashSet<T> implements Iterable<T> {
    private final ConcurrentHashMap<WeakReference<T>, Boolean> map = new ConcurrentHashMap<>();

    public void add(T value) {
        requireNonNull(value, "value is null");
        map.put(new WeakReference<>(value), Boolean.FALSE);
    }

    public void cleanup() {
        for (Iterator<Entry<WeakReference<T>, Boolean>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
            Entry<WeakReference<T>, Boolean> e = iterator.next();
            if (e.getKey().get() == null) {
                iterator.remove();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<T> iterator() {
        ArrayList<T> al = new ArrayList<>();
        for (WeakReference<T> t : map.keySet()) {
            T v = t.get();
            if (v != null) {
                al.add(v);
            }
        }
        return al.iterator();
    }
}
