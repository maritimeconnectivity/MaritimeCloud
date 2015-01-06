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

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * A interface describing a way to get the current position (and timestamp of the reading) for an object.
 * <p>
 * Any implementations are not required to be thread safe.
 *
 * @see PositionReaderSimulator
 * @author Kasper Nielsen
 */
public abstract class PositionReader {

    /**
     * Returns the current position and a timestamp for when the position was read. The timestamp, measured in
     * milliseconds, must be the difference between the current time and midnight, January 1, 1970 UTC.
     *
     * @return the current position and time
     */
    public abstract PositionTime getCurrentPosition();

    /**
     * Returns a reader that returns the same position every time.
     *
     * @param position
     *            the position to return every time
     * @return a new fixed position reader
     */
    public static PositionReader fixedPosition(final Position position) {
        requireNonNull(position, "position is null");
        return new PositionReader() {
            public PositionTime getCurrentPosition() {
                return position.withTime(System.currentTimeMillis());
            }
        };
    }

    /**
     * Returns a reader that returns the same position and time every time.
     *
     * @param positionTime
     *            the position time to return every time
     * @return a new fixed position reader
     */
    public static PositionReader fixedPosition(final PositionTime positionTime) {
        requireNonNull(positionTime, "positionTime is null");
        return new PositionReader() {
            public PositionTime getCurrentPosition() {
                return positionTime;
            }
        };
    }

    /**
     * Returns a position reader that will return the current position on a native devices.
     *
     * @return a position reader that will return the current position on a native devices
     * @throws UnsupportedOperationException
     *             if a native position reader is not available
     */
    public static PositionReader nativeReader() {
        // Taenker man saetter en System property, og saa cacher vi den
        throw new UnsupportedOperationException("This method is not supported on the current platform");
    }

    public static PositionReader fromString(String pos) {
        pos = pos.trim();
        if (pos.startsWith("file:///")) {
            try {
                URL url = new URL(pos);
                Path p = Paths.get(url.toURI());
                return new FileReader(p);
            } catch (IOException | URISyntaxException e) {
                throw new IllegalArgumentException("Not a valid file url '" + pos + "'", e);
            }
        } else {
            return fixedPosition(PositionTime.create(pos));
        }
    }

    static class FileReader extends PositionReader {

        final Path p;

        FileReader(Path p) {
            this.p = requireNonNull(p);
        }

        /** {@inheritDoc} */
        @Override
        public PositionTime getCurrentPosition() {
            try {
                List<String> readAllLines = Files.readAllLines(p);
                if (readAllLines.size() > 0) {
                    return PositionTime.create(readAllLines.get(0));
                }
                throw new IllegalStateException("File was empty at '" + p + "'");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                throw new IllegalStateException("Could not read position from '" + p + "'", e);
            }
        }

    }
}
