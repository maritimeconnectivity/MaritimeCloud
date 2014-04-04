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
package net.maritimecloud.internal.message.util;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Kasper Nielsen
 */
public class InfoList implements Iterator<Object> {

    final Iterator<?> iterator;

    List<?> list;

    Object next;

    final MSDLBaseType type;

    InfoList(List<?> l) {
        this.iterator = l.iterator();
        if (!iterator.hasNext()) {
            type = null;
        } else {
            Object n = next0();
            type = determineType(n);
        }
    }

    public MSDLBaseType getType() {
        return type;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext() {
        return next != null;
    }

    public Object next() {
        Object r = next;
        if (iterator.hasNext()) {
            next = iterator.next();
        }
        return r;
    }

    Object next0() {
        Object r = next;
        if (iterator.hasNext()) {
            next = iterator.next();
        }
        return r;
    }

    /** {@inheritDoc} */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported");
    }

    static MSDLBaseType determineType(Object o) {
        return null;
    }
}
