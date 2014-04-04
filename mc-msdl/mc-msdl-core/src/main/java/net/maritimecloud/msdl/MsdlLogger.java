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
package net.maritimecloud.msdl;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main logging interface for plugins.
 *
 * @author Kasper Nielsen
 */
public abstract class MsdlLogger {

    /**
     * Send a message to the user at <b>debug</b> error level.
     *
     * @param message
     *            the message to send
     */
    public abstract void debug(CharSequence message);

    /**
     * Send a message and exception to the user at the <b>debug</b> error level.
     *
     * @param message
     *            the message to send
     * @param error
     *            the exception
     */
    public abstract void debug(CharSequence message, Throwable error);

    /**
     * Send a message to the user at the <b>error</b> error level.
     *
     * @param message
     *            the message to send
     */
    public abstract void error(CharSequence message);

    /**
     * Send a message and exception to the user at the <b>error</b> error level.
     *
     * @param message
     *            the message to send
     * @param error
     *            the exception
     */
    public abstract void error(CharSequence message, Throwable error);


    /**
     * Send a message to the user at the <b>info</b> error level.
     *
     * @param message
     *            the message to send
     */
    public abstract void info(CharSequence message);

    /**
     * Send a message and exception to the user at the <b>info</b> error level.
     *
     * @param message
     *            the message to send
     * @param error
     *            the exception
     */
    public abstract void info(CharSequence message, Throwable error);

    /**
     * Send a message to the user at the <b>warn</b> error level.
     *
     * @param message
     *            the message to send
     */
    public abstract void warn(CharSequence message);

    /**
     * Send a message and exception to the user at the <b>warn</b> error level.
     *
     * @param message
     *            the message to send
     * @param error
     *            the exception
     */
    public abstract void warn(CharSequence message, Throwable error);

    /**
     * Returns a logger that will delegate all invocations to the specified logger. Furthermore, each time a message is
     * logged at error level the specified error counter will be incremented via {@link AtomicInteger#incrementAndGet()}
     *
     * @param logger
     *            the logger to wrap
     * @param errorCounter
     *            the counter to increment each time a message is logged at the error level.
     * @return the wrapped logger
     */
    public static MsdlLogger errorCountingLogger(MsdlLogger logger, AtomicInteger errorCounter) {
        return new ErrorCountingWrapper(logger, errorCounter);
    }

    /**
     * Wraps a java util logger in a MSDL logger.
     *
     * @param logger
     *            the logger to wrap
     * @return the wrapped logger
     */
    public static MsdlLogger wrapJUL(Logger logger) {
        return new JULLogger(logger);
    }

    static class ErrorCountingWrapper extends MsdlLogger {
        final AtomicInteger errorCounter;

        final MsdlLogger logger;

        /**
         * @param logger
         */
        ErrorCountingWrapper(MsdlLogger logger, AtomicInteger errorCounter) {
            this.logger = requireNonNull(logger);
            this.errorCounter = requireNonNull(errorCounter);
        }

        /** {@inheritDoc} */
        @Override
        public void debug(CharSequence message) {
            logger.debug(message);
        }

        /** {@inheritDoc} */
        @Override
        public void debug(CharSequence message, Throwable error) {
            logger.debug(message, error);
        }

        /** {@inheritDoc} */
        @Override
        public void error(CharSequence message) {
            errorCounter.incrementAndGet();
            logger.error(message);
        }

        /** {@inheritDoc} */
        @Override
        public void error(CharSequence message, Throwable error) {
            errorCounter.incrementAndGet();
            logger.error(message, error);
        }

        /** {@inheritDoc} */
        @Override
        public void info(CharSequence message) {
            logger.info(message);
        }

        /** {@inheritDoc} */
        @Override
        public void info(CharSequence message, Throwable error) {
            logger.info(message, error);
        }

        /** {@inheritDoc} */
        @Override
        public void warn(CharSequence message) {
            logger.warn(message);
        }

        /** {@inheritDoc} */
        @Override
        public void warn(CharSequence message, Throwable error) {
            logger.warn(message, error);
        }
    }

    static class JULLogger extends MsdlLogger {
        /** The wrapped logger */
        private final Logger logger;

        /**
         * @param logger
         *            the logger to wrap
         */
        JULLogger(Logger logger) {
            this.logger = requireNonNull(logger);
        }

        /** {@inheritDoc} */
        @Override
        public void debug(CharSequence message) {
            logger.log(Level.FINE, message.toString());
        }

        /** {@inheritDoc} */
        @Override
        public void debug(CharSequence message, Throwable error) {
            logger.log(Level.FINE, message.toString(), error);
        }

        /** {@inheritDoc} */
        @Override
        public void error(CharSequence message) {
            logger.log(Level.SEVERE, message.toString());
        }

        /** {@inheritDoc} */
        @Override
        public void error(CharSequence message, Throwable error) {
            logger.log(Level.SEVERE, message.toString(), error);
        }

        /** {@inheritDoc} */
        @Override
        public void info(CharSequence message) {
            logger.log(Level.INFO, message.toString());
        }

        /** {@inheritDoc} */
        @Override
        public void info(CharSequence message, Throwable error) {
            logger.log(Level.INFO, message.toString(), error);
        }

        /** {@inheritDoc} */
        @Override
        public void warn(CharSequence message) {
            logger.log(Level.WARNING, message.toString());
        }

        /** {@inheritDoc} */
        @Override
        public void warn(CharSequence message, Throwable error) {
            logger.log(Level.WARNING, message.toString(), error);
        }
    }
}
