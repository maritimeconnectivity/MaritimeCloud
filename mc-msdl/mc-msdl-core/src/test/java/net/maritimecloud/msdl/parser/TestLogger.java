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
package net.maritimecloud.msdl.parser;

import java.util.LinkedList;
import java.util.Queue;

import net.maritimecloud.msdl.MsdlLogger;

/**
 *
 * @author Kasper Nielsen
 */
public class TestLogger extends MsdlLogger {

    private final Queue<Entry> queue = new LinkedList<>();

    public Entry nextError() {
        Entry peek;
        while ((peek = queue.poll()) != null) {
            if (peek.getLevel() == Level.ERROR) {
                return peek;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void debug(CharSequence message) {
        queue.add(new Entry(Level.DEBUG, message, null));
    }

    /** {@inheritDoc} */
    @Override
    public void debug(CharSequence message, Throwable error) {
        queue.add(new Entry(Level.DEBUG, message, error));
    }

    /** {@inheritDoc} */
    @Override
    public void error(CharSequence message) {
        queue.add(new Entry(Level.ERROR, message, null));
    }

    /** {@inheritDoc} */
    @Override
    public void error(CharSequence message, Throwable error) {
        queue.add(new Entry(Level.ERROR, message, error));
    }

    /** {@inheritDoc} */
    @Override
    public void info(CharSequence message) {
        queue.add(new Entry(Level.INFO, message, null));
    }

    /** {@inheritDoc} */
    @Override
    public void info(CharSequence message, Throwable error) {
        queue.add(new Entry(Level.INFO, message, error));
    }

    /** {@inheritDoc} */
    @Override
    public void warn(CharSequence message) {
        queue.add(new Entry(Level.WARN, message, null));
    }

    /** {@inheritDoc} */
    @Override
    public void warn(CharSequence message, Throwable error) {
        queue.add(new Entry(Level.WARN, message, error));
    }

    public static class Entry {
        private final String contents;

        private final Throwable error;

        private final Level level;

        /**
         * @param level
         * @param contents
         * @param error
         */
        public Entry(Level level, CharSequence contents, Throwable error) {
            this.level = level;
            this.contents = contents.toString();
            this.error = error;
        }

        /**
         * @return the contents
         */
        public String getMessage() {
            return contents;
        }

        /**
         * @return the error
         */
        public Throwable getError() {
            return error;
        }

        /**
         * @return the level
         */
        public Level getLevel() {
            return level;
        }
    }

    public enum Level {
        DEBUG, ERROR, INFO, WARN;
    }
}
