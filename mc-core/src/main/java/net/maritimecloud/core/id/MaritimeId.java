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
package net.maritimecloud.core.id;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/** The id of some kind of maritime object such as a ship, VTS station, voyage or an object on a map. */
public abstract class MaritimeId implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    final String scheme;

    protected MaritimeId(String scheme) {
        this.scheme = requireNonNull(scheme);
    }

    /**
     * Creates a id from the specified string based representation of the id.
     *
     * @param id
     *            the string based representation to create the id from
     * @return the id
     * @throws NullPointerException
     *             if the specified id string is null
     * @throws IllegalArgumentException
     *             if the specified string is not a valid maritime id
     */
    public static MaritimeId create(String id) {
        requireNonNull(id, "id is null");
        final URI uri;
        try {
            uri = new URI(id);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
        String scheme = uri.getScheme();
        if (scheme == null) {
            throw new IllegalArgumentException("Illegal id, must start with 'some_scheme://', was " + id);
        }
        if (uri.getScheme().equals("mmsi")) {
            int mmsi = Integer.parseInt(uri.getSchemeSpecificPart());
            return new MmsiId(mmsi);
        }
        if (uri.getScheme().equals("test")) {
            return new TestId(uri.getSchemeSpecificPart());
        }
        throw new IllegalArgumentException("Unknown scheme " + uri.getScheme());
    }

    public abstract String getId();

    public int getIdAsInt() {
        return Integer.parseInt(getId());
    }
}
