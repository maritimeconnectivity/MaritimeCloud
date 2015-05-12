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


/**
 * An AbstractLogger that all logger wrappers extend.
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractLogger implements Logger {
    /** {@inheritDoc} */
    public void debug(String message) {
        log(Level.Debug, message);
    }

    /** {@inheritDoc} */
    public void debug(String message, Throwable cause) {
        log(Level.Debug, message, cause);
    }

    /** {@inheritDoc} */
    public void error(String message) {
        log(Level.Error, message);
    }

    /** {@inheritDoc} */
    public void error(String message, Throwable cause) {
        log(Level.Error, message, cause);
    }

    /** {@inheritDoc} */
    public void fatal(String message) {
        log(Level.Fatal, message);
    }

    /** {@inheritDoc} */
    public void fatal(String message, Throwable cause) {
        log(Level.Fatal, message, cause);
    }

    /** {@inheritDoc} */
    public void info(String message) {
        log(Level.Info, message);
    }

    /** {@inheritDoc} */
    public void info(String message, Throwable cause) {
        log(Level.Info, message, cause);
    }

    /** {@inheritDoc} */
    public boolean isDebugEnabled() {
        return isEnabled(Level.Debug);
    }

    /** {@inheritDoc} */
    public boolean isErrorEnabled() {
        return isEnabled(Level.Error);
    }

    /** {@inheritDoc} */
    public boolean isFatalEnabled() {
        return isEnabled(Level.Fatal);
    }

    /** {@inheritDoc} */
    public boolean isInfoEnabled() {
        return isEnabled(Level.Info);
    }

    /** {@inheritDoc} */
    public boolean isTraceEnabled() {
        return isEnabled(Level.Trace);
    }

    /** {@inheritDoc} */
    public boolean isWarnEnabled() {
        return isEnabled(Level.Warn);
    }

    /** {@inheritDoc} */
    public void log(Logger.Level l, String message) {
        log(l, message, null);
    }

    /** {@inheritDoc} */
    public void trace(String message) {
        log(Level.Trace, message);
    }

    /** {@inheritDoc} */
    public void trace(String message, Throwable cause) {
        log(Level.Trace, message, cause);
    }

    /** {@inheritDoc} */
    public void warn(String message) {
        log(Level.Warn, message);
    }

    /** {@inheritDoc} */
    public void warn(String message, Throwable cause) {
        log(Level.Warn, message, cause);
    }

    /**
     * Converts from a {@link net.maritimecloud.internal.util.logging.Logger.Level} to a {@link java.util.logging.Level}
     * .
     *
     * @param level
     *            the level to convert
     * @return the converted level
     */
    protected static java.util.logging.Level toJdkLevel(Level level) {
        switch (level) {
        case Trace:
            return java.util.logging.Level.FINEST;
        case Debug:
            return java.util.logging.Level.FINE;
        case Error:
            return java.util.logging.Level.SEVERE;
        case Fatal:
            return java.util.logging.Level.SEVERE;
        case Info:
            return java.util.logging.Level.INFO;
        default /* Warn */:
            return java.util.logging.Level.WARNING;
        }
    }
}
