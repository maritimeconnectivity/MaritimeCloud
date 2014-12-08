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
package net.maritimecloud.internal.net.endpoint;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import net.maritimecloud.net.LocalEndpoint;

/**
 *
 * @author Kasper Nielsen
 */
public class EndpointMirror {

    private final Class<? extends LocalEndpoint> c;

    private final String name;

    EndpointMirror(Class<? extends LocalEndpoint> c, String name) {
        this.c = c;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LocalEndpoint instantiate(LocalEndpoint.Invocator invocator) {
        Constructor<?> con;
        try {
            con = c.getConstructor(LocalEndpoint.Invocator.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Endpoint " + c.getCanonicalName() + " must have a constructor taking a single "
                    + LocalEndpoint.Invocator.class.getSimpleName());
        }
        try {
            return (LocalEndpoint) con.newInstance(invocator);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not instantiate " + c.getCanonicalName(), e);
        }
    }

    public static EndpointMirror from(Class<? extends LocalEndpoint> c) {
        requireNonNull(c, "endpoint type is null");
        return new EndpointMirror(c, getName(c));
    }


    private static String getName(Class<? extends LocalEndpoint> cl) {
        Field f;
        try {
            f = cl.getField("NAME");
            return (String) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Local endpoint must have a public static final String NAME field", e);
        }
    }

    public static String stripEndpointMethod(String sre) {
        int li = sre.lastIndexOf('.');
        return sre.substring(0, li);
    }

    public static String stripEndpointName(String sre) {
        int li = sre.lastIndexOf('.');
        return sre.substring(li + 1, sre.length());
    }
}
