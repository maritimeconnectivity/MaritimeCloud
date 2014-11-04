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
 * A simple logging interface abstracting logging APIs. The primary reason for using this is to avoid runtime
 * dependencies on any external logging libraries.
 * <p>
 * You shouldn't need to implement this interface, instead use {@link org.cakeframework.util.logging.Loggers} to create
 * wrappers from popular logging frameworks such as <a
 * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/logging/package-summary.html">Standard JDK logging</a>, <a
 * href="http://logging.apache.org/log4j/">Log4j</a> or <a href="http://www.slf4j.org/">SLF4J</a>. Using any but the
 * standard JDK logging wrapper requires that the logging framework in question has been added to the current classpath.
 * <p>
 * For example, to create a Logger that uses Log4j as the underlying logging mechanism, use
 *
 * <pre>
 * Loggers.Log4j.from(&quot;myLogName&quot;);
 * </pre>
 *
 * <p>
 * This is not an attempt to create a new logging framework. But for some unknown reason, the authors of
 * java.util.logging made {@link java.util.logging.Logger} a class instead of an interface. Furthermore all the classes
 * in java.util.logging are tightly coupled. Making it impossible to extend the Logger class it in any reasonable way.
 *
 * <p>
 * Commons logging was previously supported but it turned out to be impossible to consistently test because of
 * classloader issues across different IDE's.
 *
 * @author Kasper Nielsen
 */
public interface Logger {

    /** @return the name of the logger */
    String getName();

    /**
     * Logs the message if the logger is currently enabled for the debug log level.
     *
     * @param message
     *            the message to log
     */
    void debug(String message);

    /**
     * Logs the message if the logger is currently enabled for the debug log level.
     *
     * @param message
     *            the message to log
     * @param cause
     *            the cause to log
     */
    void debug(String message, Throwable cause);

    /**
     * Logs the message if the logger is currently enabled for the error log level.
     *
     * @param message
     *            the message to log
     */
    void error(String message);

    /**
     * Logs the message if the logger is currently enabled for the error log level.
     *
     * @param message
     *            the message to log
     * @param cause
     *            the cause to log
     */
    void error(String message, Throwable cause);

    /**
     * Logs the message if the logger is currently enabled for the fatal log level.
     *
     * @param message
     *            the message to log
     */
    void fatal(String message);

    /**
     * Logs the message if the logger is currently enabled for the fatal log level.
     *
     * @param message
     *            the message to log
     * @param cause
     *            the cause to log
     */
    void fatal(String message, Throwable cause);

    /**
     * Logs the message if the logger is currently enabled for the info log level.
     *
     * @param message
     *            the message to log
     */
    void info(String message);

    /**
     * Logs the message if the logger is currently enabled for the info log level.
     *
     * @param message
     *            the message to log
     * @param cause
     *            the cause to log
     */
    void info(String message, Throwable cause);

    /**
     * Check to see if a message of <tt>debug</tt> level would actually be logged by this logger.
     *
     * @return <code>true</code> if the debug level or higher is enabled
     */
    boolean isDebugEnabled();

    /**
     * Returns whether or not logging is enabled for the specified {@link Level}.
     *
     * @param level
     *            the level to check
     * @return whether or not logging is enabled for the specified level
     */
    boolean isEnabled(Level level);

    /**
     * Check to see if a message of <tt>error</tt> level would actually be logged by this logger.
     *
     * @return <code>true</code> if the error level or higher is enabled
     */
    boolean isErrorEnabled();

    /**
     * Check to see if a message of <tt>fatal</tt> level would actually be logged by this logger.
     *
     * @return <code>true</code> if the fatal level or higher is enabled
     */
    boolean isFatalEnabled();

    /**
     * Check to see if a message of <tt>info</tt> level would actually be logged by this logger.
     *
     * @return <code>true</code> if the info level or higher is enabled
     */
    boolean isInfoEnabled();

    /**
     * Check to see if a message of <tt>trace</tt> level would actually be logged by this logger.
     *
     * @return <code>true</code> if the trace level or higher is enabled
     */
    boolean isTraceEnabled();

    /**
     * Check to see if a message of <tt>warn</tt> level would actually be logged by this logger.
     *
     * @return <code>true</code> if the warn level or higher is enabled
     */
    boolean isWarnEnabled();

    /**
     * Logs the specified message at the specified level.
     *
     * @param level
     *            the level of the message
     * @param message
     *            the message to log
     */
    void log(Level level, String message);

    /**
     * Logs the specified message and cause at the specified level.
     *
     * @param level
     *            the level of the message
     * @param message
     *            the message to log
     * @param cause
     *            the cause
     */
    void log(Level level, String message, Throwable cause);

    /**
     * Logs the message if the logger is currently enabled for the trace log level.
     *
     * @param message
     *            the message to log
     */
    void trace(String message);

    /**
     * Logs the message if the logger is currently enabled for the trace log level.
     *
     * @param message
     *            the message to log
     * @param cause
     *            the cause to log
     */
    void trace(String message, Throwable cause);

    /**
     * Logs the message if the logger is currently enabled for the warn log level.
     *
     * @param message
     *            the message to log
     */
    void warn(String message);

    /**
     * Logs the message if the logger is currently enabled for the warn log level.
     *
     * @param message
     *            the message to log
     * @param cause
     *            the cause to log
     */
    void warn(String message, Throwable cause);

    /** The log level. */
    public enum Level {
        /** Is a log level providing tracing information. It is the finest level. */
        Debug(1),
        /** Is a log level providing tracing information. It is the finest level. */
        Error(4),
        /** Is a log level providing tracing information. It is the finest level. */
        Fatal(5),
        /** Is a log level providing tracing information. It is the finest level. */
        Info(2),
        /** Indicates that no logging will be done. */
        Off(Integer.MAX_VALUE),
        /** Is a log level providing tracing information. It is the finest level. */
        Trace(0),
        /** Is a log level providing tracing information. It is the finest level. */
        Warn(3);

        /** The level id. */
        private final int level;

        /**
         * Creates a new Log level.
         *
         * @param level
         *            the level id
         */
        private Level(int level) {
            this.level = level;
        }

        /**
         * Returns the id of the level.
         *
         * @return the id of the level
         */
        public int getLevel() {
            return level;
        }
    }

    static Logger get(Class<?> cl) {
        return LogFactory.LOG_FACTORY.apply(cl);
    }
}
