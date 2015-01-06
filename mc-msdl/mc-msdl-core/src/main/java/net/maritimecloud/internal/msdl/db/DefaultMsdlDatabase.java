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

import net.maritimecloud.internal.msdl.parser.antlr.StringUtil;
import net.maritimecloud.msdl.model.BroadcastMessageDeclaration;
import net.maritimecloud.msdl.model.EndpointDefinition;
import net.maritimecloud.msdl.model.EndpointMethod;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.MsdlFile;

/**
 *
 * @author Kasper Nielsen
 */
public class DefaultMsdlDatabase {
    final Directory root = new Directory(null, "/");

    void addMsdlFile(MsdlFile file) {
        String namespace = file.getNamespace();
        Directory current = root;
        for (String s : namespace.split("\\.")) {
            final Directory cur = current;
            current = cur.directory.computeIfAbsent(s, n -> new Directory(cur, n));
        }
        current.files.put(file.getName(), file);
    }

    public MessageDeclaration getMessage(String path) {
        Directory current = root;
        String[] spl = path.split("\\.");
        if (spl.length > 1) {
            for (int i = 0; i < spl.length - 1; i++) {
                if (!current.directory.containsKey(spl[i])) {
                    return null;
                }
                current = current.directory.get(spl[i]);
            }
        }
        return current.getMessage(spl[spl.length - 1]);
    }

    public BroadcastMessageDeclaration getBroadcastMessage(String path) {
        Directory current = root;
        String[] spl = path.split("\\.");
        if (spl.length > 1) {
            for (int i = 0; i < spl.length - 1; i++) {
                if (!current.directory.containsKey(spl[i])) {
                    return null;
                }
                current = current.directory.get(spl[i]);
            }
        }
        return current.getBroadcastMessage(spl[spl.length - 1]);
    }

    public EndpointDefinition getEndpointDefinition(String path) {
        Directory current = root;
        String[] spl = path.split("\\.");
        if (spl.length > 1) {
            for (int i = 0; i < spl.length - 1; i++) {
                if (!current.directory.containsKey(spl[i])) {
                    return null;
                }
                current = current.directory.get(spl[i]);
            }
        }
        return current.getEndpointDefinition(spl[spl.length - 1]);
    }

    public EndpointMethod getEndpointMethod(String path) {
        Directory current = root;
        String[] spl = path.split("\\.");
        if (spl.length > 2) {
            for (int i = 0; i < spl.length - 2; i++) {
                if (!current.directory.containsKey(spl[i])) {
                    return null;
                }
                current = current.directory.get(spl[i]);
            }
        }
        return current.getEndpointMethod(spl[spl.length - 2], spl[spl.length - 1]);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(root, sb, 0);
        return sb.toString();
    }

    public void toString(Directory d, StringBuilder sb, int indent) {
        for (Directory sub : d.directory.values()) {
            sb.append(StringUtil.spaces(indent)).append(sub.name).append("/").append(StringUtil.LINE_SEPARATOR);
            toString(sub, sb, indent + 2);
        }
        for (MsdlFile f : d.files.values()) {
            sb.append(StringUtil.spaces(indent)).append(f.getName()).append(".msdl").append(StringUtil.LINE_SEPARATOR);
        }
    }
}
