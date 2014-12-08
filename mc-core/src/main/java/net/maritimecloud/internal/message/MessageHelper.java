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
package net.maritimecloud.internal.message;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.ValueSerializer;

/**
 * Various utility method that are used by generated messages.
 *
 * @author Kasper Nielsen
 */
public class MessageHelper {
    public static double checkDouble(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Cannot write double value " + value);
        }
        return value;
    }

    public static float checkFloat(float value) {
        if (!Float.isFinite(value)) {
            throw new IllegalArgumentException("Cannot write float value " + value);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static <T> T convert(T value) {
        return value instanceof Message ? (T) ((Message) value).immutable() : value;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Message> T immutable(T t) {
        return t == null ? null : (T) t.immutable();
    }

    public static <T> List<T> immutableCopy(List<? extends T> list) {
        ArrayList<T> l = new ArrayList<>(list.size());
        for (T t : list) {
            l.add(convert(t));
        }
        return Collections.unmodifiableList(l);
    }

    public static <K, V> Map<K, V> immutableCopy(Map<? extends K, ? extends V> map) {
        LinkedHashMap<K, V> l = new LinkedHashMap<>(map.size());
        for (Map.Entry<? extends K, ? extends V> t : map.entrySet()) {
            l.put(convert(t.getKey()), convert(t.getValue()));
        }
        return Collections.unmodifiableMap(l);
    }

    public static <T> Set<T> immutableCopy(Set<? extends T> list) {
        LinkedHashSet<T> l = new LinkedHashSet<>(list.size());
        for (T t : list) {
            l.add(convert(t));
        }
        return Collections.unmodifiableSet(l);
    }

    public static <T> List<T> readList(int tag, String name, MessageReader reader, ValueSerializer<T> parser)
            throws IOException {
        requireNonNull(reader, "reader is null");
        // TODO make sure it is a valid list.
        return reader.readList(tag, name, parser);
    }

    public static <K, V> Map<K, V> readMap(int tag, String name, MessageReader reader, ValueSerializer<K> keyParser,
            ValueSerializer<V> valueParser) throws IOException {
        // TODO make sure it is a valid list.
        return reader.readMap(tag, name, keyParser, valueParser);
    }

    public static <T> Set<T> readSet(int tag, String name, MessageReader reader, ValueSerializer<T> parser)
            throws IOException {
        // TODO make sure it is a valid list.
        return reader.readSet(tag, name, parser);
    }

    public static <T extends Message> MessageSerializer<T> getSerializer(T message) {
        return getSerializer(message, message.getClass());
    }

    //
    // @SuppressWarnings("unchecked")
    // private static <T extends Message> MessageSerializer<T> getSerializer(Class<?> c) {
    // return getSerializer(null, message.getClass());
    // }

    @SuppressWarnings("unchecked")
    private static <T extends Message> MessageSerializer<T> getSerializer(T message, Class<?> messageType) {
        try {
            Field field = messageType.getField("SERIALIZER");
            return (MessageSerializer<T>) field.get(null);
        } catch (NoSuchFieldException e) {
            if (message != null) {
                try {
                    Method m = messageType.getMethod("serializer");
                    return (MessageSerializer<T>) m.invoke(message);
                } catch (ReflectiveOperationException e1) {
                    throw new RuntimeException(
                            "All messages must have a public static final String NAME field or a name() method, offending class = "
                                    + messageType.getCanonicalName(), e1);
                }
            }
            throw new RuntimeException("All messages must have a public static final "
                    + MessageSerializer.class.getSimpleName() + " SERIALIZER field, offending class = "
                    + messageType.getCanonicalName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getName(Message message) {
        return getName(message, message.getClass());
    }

    public static String getName(Class<? extends Message> messageType) {
        return getName(null, messageType);
    }

    private static String getName(Message message, Class<? extends Message> messageType) {
        requireNonNull(messageType, "message type is null");
        try {
            Field field = messageType.getField("NAME");
            return (String) field.get(null);
        } catch (NoSuchFieldException e) {
            // Lets see if they have a name method
            if (message != null) {
                try {
                    Method m = messageType.getMethod("name");
                    return (String) m.invoke(message);
                } catch (ReflectiveOperationException e1) {
                    throw new RuntimeException(
                            "All messages must have a public static final String NAME field or a name() method, offending class = "
                                    + messageType.getCanonicalName(), e1);
                }
            }
            throw new RuntimeException(
                    "All messages must have a public static final String NAME field, offending class = "
                            + messageType.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
