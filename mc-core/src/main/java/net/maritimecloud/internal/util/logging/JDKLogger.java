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
package net.maritimecloud.internal.util.logging;

import static java.util.Objects.requireNonNull;


/**
 * The wrapper class for a JDK logger.
 *
 * @author Kasper Nielsen
 */
final class JDKLogger extends AbstractLogger {

    /** The logger we are wrapping. */
    final java.util.logging.Logger logger;

    /**
     * Creates a new JDKLogger from the specified logger.
     *
     * @param logger
     *            the logger to wrap
     */
    JDKLogger(java.util.logging.Logger logger) {
        this.logger = requireNonNull(logger, "logger is null");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JDKLogger && ((JDKLogger) obj).logger == logger;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public int hashCode() {
        return logger.hashCode();
    }

    /** {@inheritDoc} */
    public boolean isEnabled(Level level) {
        return logger.isLoggable(toJdkLevel(level));
    }

    /** {@inheritDoc} */
    @Override
    public void log(Logger.Level level, String message) {
        logger.log(toJdkLevel(level), message);
    }

    /** {@inheritDoc} */
    public void log(Logger.Level level, String message, Throwable cause) {
        logger.log(toJdkLevel(level), message, cause);
    }
}
