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


/** The wrapper class for a SLF4J logger. */
final class SLF4JLogger extends AbstractLogger {

    final org.slf4j.Logger logger;

    SLF4JLogger(org.slf4j.Logger logger) {
        this.logger = requireNonNull(logger, "logger is null");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SLF4JLogger && ((SLF4JLogger) obj).logger.equals(logger);
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
        switch (level) {
        case Trace:
            return logger.isTraceEnabled();
        case Debug:
            return logger.isDebugEnabled();
        case Error:
            return logger.isErrorEnabled();
        case Fatal:
            return logger.isErrorEnabled();
        case Info:
            return logger.isInfoEnabled();
        default /* Warn */:
            return logger.isWarnEnabled();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void log(Logger.Level level, String message) {
        switch (level) {
        case Trace:
            logger.trace(message);
            break;
        case Debug:
            logger.debug(message);
            break;
        case Error:
            logger.error(message);
            break;
        case Fatal:
            logger.error(message);
            break;
        case Info:
            logger.info(message);
            break;
        default /* Warn */:
            logger.warn(message);
        }
    }

    /** {@inheritDoc} */
    public void log(Logger.Level level, String message, Throwable cause) {
        switch (level) {
        case Trace:
            logger.trace(message, cause);
            break;
        case Debug:
            logger.debug(message, cause);
            break;
        case Error:
            logger.error(message, cause);
            break;
        case Fatal:
            logger.error(message, cause);
            break;
        case Info:
            logger.info(message, cause);
            break;
        default /* Warn */:
            logger.warn(message, cause);
        }
    }

}
