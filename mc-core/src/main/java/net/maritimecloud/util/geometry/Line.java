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
package net.maritimecloud.util.geometry;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

/**
 * A line
 *
 */
public class Line implements Serializable {
    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The end of the line. */
    final Position end;

    /** The start of the line. */
    final Position start;

    Line(Position start, Position end) {
        this.start = requireNonNull(start, "start is null");
        this.end = requireNonNull(end, "end is null");
    }

    public boolean equals(Line other) {
        return other == this || other != null && start.equals(other.start) && start.equals(other.start);
    }

    /**
     * Equals method
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Line && equals((Line) other);
    }

    /**
     * Returns the end position of the line.
     *
     * @return the end position of the line
     */
    public Position getEnd() {
        return end;
    }

    /**
     * Returns the start position of the line.
     *
     * @return the start position of the line
     */
    public Position getStart() {
        return start;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return end.hashCode() ^ start.hashCode();
    }
}
