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
package net.maritimecloud.internal.msdl.db;

import java.util.TreeMap;

import net.maritimecloud.msdl.model.BroadcastMessageDeclaration;
import net.maritimecloud.msdl.model.EndpointDefinition;
import net.maritimecloud.msdl.model.EndpointMethod;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.MsdlFile;

/**
 *
 * @author Kasper Nielsen
 */
public class Directory {

    final TreeMap<String, Directory> directory = new TreeMap<>();

    final TreeMap<String, MsdlFile> files = new TreeMap<>();

    final String name;

    final Directory parent;

    Directory(Directory parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    BroadcastMessageDeclaration getBroadcastMessage(String name) {
        for (MsdlFile f : files.values()) {
            for (BroadcastMessageDeclaration d : f.getBroadcasts()) {
                if (d.getName().equals(name)) {
                    return d;
                }
            }
        }
        return null;
    }

    EndpointDefinition getEndpointDefinition(String endpointName) {
        for (MsdlFile f : files.values()) {
            for (EndpointDefinition d : f.getEndpoints()) {
                if (d.getName().equals(endpointName)) {
                    return d;
                }
            }
        }
        return null;
    }

    EndpointMethod getEndpointMethod(String endpointName, String functionName) {
        EndpointDefinition def = getEndpointDefinition(endpointName);
        if (def != null) {
            for (EndpointMethod m : def.getFunctions()) {
                if (m.getName().equals(functionName)) {
                    return m;
                }
            }
        }
        return null;
    }

    MessageDeclaration getMessage(String name) {
        for (MsdlFile f : files.values()) {
            for (MessageDeclaration d : f.getMessages()) {
                if (d.getName().equals(name)) {
                    return d;
                }
            }
        }
        return null;
    }
}
