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

import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A LazyLogger is lazily initialized whenever the first exception is raised.
 *
 * @author Kasper Nielsen
 */
public class LazyJdkLogger extends AbstractLogger {

    private final String jdkLoggerName;

    private final String lastCaller;

    private volatile Logger logger;

    private final String msg;

    public LazyJdkLogger(String jdkLoggerName, String msg, String lastCaller) {
        this.msg = requireNonNull(msg, "msg is null");
        this.jdkLoggerName = requireNonNull(jdkLoggerName, "jdkLoggerName is null");
        this.lastCaller = requireNonNull(lastCaller, "lastCaller is null");
    }

    /**
     * Creates the default logger.
     *
     * @return the default logger
     */
    protected Logger createDefaultLogger() {
        Logger logger = LogManager.getLogManager().getLogger(jdkLoggerName);
        if (logger == null) {
            logger = java.util.logging.Logger.getLogger(jdkLoggerName);
            logger.setLevel(java.util.logging.Level.ALL);
            logger.log(new LazyLogRecord(java.util.logging.Level.INFO, msg, lastCaller));
            logger.setLevel(java.util.logging.Level.WARNING);
        }
        return logger;
    }

    Logger getLogger() {
        Logger logger = this.logger;
        if (logger != null) {
            return logger;
        }
        synchronized (this) {
            logger = this.logger;
            return logger == null ? this.logger = createDefaultLogger() : logger;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return jdkLoggerName;
    }

    /** {@inheritDoc} */
    public boolean isEnabled(Level level) {
        return getLogger().isLoggable(toJdkLevel(level));
    }

    /** {@inheritDoc} */
    @Override
    public void log(Level level, String message) {
        Logger logger = getLogger();
        LazyLogRecord r = new LazyLogRecord(toJdkLevel(level), message, lastCaller);
        logger.log(r);
    }

    /** {@inheritDoc} */
    public void log(Level level, String message, Throwable cause) {
        Logger logger = getLogger();
        LazyLogRecord r = new LazyLogRecord(toJdkLevel(level), message, lastCaller);
        r.setThrown(cause);
        logger.log(r);
    }

    static class LazyLogRecord extends LogRecord {
        /** serialVersionUID. */
        private static final long serialVersionUID = -5042636617771251558L;

        final String lastCaller;

        private transient boolean sourceInited;

        LazyLogRecord(java.util.logging.Level level, String msg, String lastCaller) {
            super(level, msg);
            this.lastCaller = lastCaller;
        }

        public String getSourceClassName() {
            initSource();
            return super.getSourceClassName();
        }

        public String getSourceMethodName() {
            initSource();
            return super.getSourceMethodName();
        }

        // Taken from Harmony
        // http://svn.apache.org/viewvc/harmony/enhanced/java/trunk/classlib/modules/logging/src/main/java/java/util/logging/LogRecord.java
        /** Initializes the sourceClass and sourceMethod fields. */
        private void initSource() {
            if (!sourceInited) {
                StackTraceElement[] elements = new Throwable().getStackTrace();
                int i = 0;
                while (i < elements.length) {
                    String current = elements[i].getClassName();
                    if (current.equals(lastCaller)) {
                        break;
                    }
                    i++;
                }
                // /CLOVER:OFF
                // I'm pretty sure it has been tested extensively in harmony, for some reason
                // I don't get any coverage
                while (++i < elements.length && elements[i].getClassName().equals(lastCaller)) {
                    // do nothing
                }
                // /CLOVER:ON
                if (i < elements.length) {
                    setSourceClassName(elements[i].getClassName());
                    setSourceMethodName(elements[i].getMethodName());
                }
                sourceInited = true;
            }
        }
    }
}
