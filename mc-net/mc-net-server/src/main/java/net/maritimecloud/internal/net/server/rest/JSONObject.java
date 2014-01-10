/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.maritimecloud.internal.net.server.rest;

import static java.util.Objects.requireNonNull;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Kasper Nielsen
 */
public final class JSONObject {

    /** The Unix line separator. */
    public static final String LS = "\n";

    private final List<Map.Entry<String, Object>> elements = new ArrayList<>();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void add(String name, Object o) {
        elements.add(new AbstractMap.SimpleImmutableEntry(requireNonNull(name), o));
    }

    public JSONObject addElement(String name, Object value) {
        add(name, value);
        return this;
    }

    public JSONObject addList(String name, Object... values) {
        add(name, Arrays.asList(values));
        return this;
    }

    public JSONObject newChild(String name) {
        JSONObject o2 = new JSONObject();
        add(name, o2);
        return o2;
    }

    public static JSONObject single(String name, Object value) {
        return new JSONObject().addElement(name, value);
    }

    public static JSONObject single(String name, Object value, String name1, Object value1) {
        return new JSONObject().addElement(name, value).addElement(name1, value1);
    }

    public static JSONObject single(String name, Object value, String name1, Object value1, String name2, Object value2) {
        return new JSONObject().addElement(name, value).addElement(name1, value1).addElement(name2, value2);
    }

    public static JSONObject singleList(String name, Object... value) {
        return new JSONObject().addElement(name, Arrays.asList(value));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(LS);
        toString(sb, 1);
        sb.append("}").append(LS);
        return sb.toString();
    }

    private void toString(StringBuilder sb, int indent) {
        for (int i = 0; i < elements.size(); i++) {
            Map.Entry<String, Object> e = elements.get(i);
            spaces(sb, indent * 2);
            sb.append('"').append(e.getKey()).append("\":");
            toString(sb, indent, e.getValue());
            if (i < elements.size() - 1) {
                sb.append(",");
            }
            sb.append(LS);
        }
    }

    private void toString(StringBuilder sb, int indent, Object o) {
        if (o instanceof List) {
            List<?> l = (List<?>) o;
            sb.append("[").append(LS);
            for (int i = 0; i < l.size(); i++) {
                if (i != 0) {
                    sb.append(",").append(LS);
                }
                spaces(sb, (indent + 1) * 2);
                toString(sb, indent + 1, l.get(i));
            }
            sb.append(LS);
            spaces(sb, indent * 2);
            sb.append("]");
        } else if (o instanceof JSONObject) {
            sb.append("{").append(LS);
            ((JSONObject) o).toString(sb, indent + 1);
            spaces(sb, indent * 2).append("}");
        } else {
            if (o == null || o instanceof Number) {
                sb.append(o);
            } else {
                sb.append('"').append(o).append("\"");
            }
        }
    }

    /**
     * Adds the specified count of spaces to the specified string builder.
     * 
     * @param sb
     *            the string builder to add to
     * @param count
     *            the number of spaces to add
     * @return the specified string builder
     */
    public static StringBuilder spaces(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append(' ');
        }
        return sb;
    }
}
