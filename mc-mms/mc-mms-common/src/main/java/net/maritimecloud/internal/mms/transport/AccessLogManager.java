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
package net.maritimecloud.internal.mms.transport;

import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.message.MessageFormatType;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Will handle logging of {@code MmsMessage} entities at the transport layer level.
 * <p>
 * The message log can be associated with a file name, whose name pattern must follow
 * the guidelines of the
 * <a href="http://docs.oracle.com/javase/7/docs/api/java/util/logging/FileHandler.html">FileHandler</a> class.<br/>
 * If the file is undefined, the messages will be logged to {@code System.out}.
 * <p>
 * The optional <i>filter</i> parameter is currently <i>EXPERIMENTAL</i>.<br>
 * If the filter is defined, only the messages matching the filter are logged.
 * <p>
 * Example: <code>inbound && clientId == 'mmsi:565009926'</code>
 */
@SuppressWarnings("unused")
public class AccessLogManager {

    public static final String LOG_TO_STDOUT = "stdout";
    List<MessageLog> messageLogs = new CopyOnWriteArrayList<>();

    /**
     * Instantiated with an access log configuration
     * @param conf the access log configuration
     */
    public AccessLogManager(AccessLogConfiguration conf) {
        Objects.requireNonNull(conf);

        if (conf.getAccessLog() != null && conf.getAccessLog().trim().length() > 0) {
            String logFile = conf.getAccessLog().equalsIgnoreCase(LOG_TO_STDOUT) ? null : conf.getAccessLog();
            addMessageLog(logFile);
        }
    }

    /**
     * Adds a logger that logs to System.out.
     *
     * @return the message log or null if undefined
     */
    public MessageLog addMessageLog() {
        return addMessageLog(null, null);
    }

    /**
     * Adds a logger defined by the given log file.
     * If no file is specified, System.out is used instead.
     *
     * @param file the log file. If null, System.out is used instead.
     * @return the message log or null if undefined
     */
    public MessageLog addMessageLog(String file) {
        return addMessageLog(file, null);
    }

    /**
     * Adds a logger defined by the given log file and filter.
     * If no file is specified, System.out is used instead.
     *
     * @param file the log file. If null, System.out is used instead.
     * @param filter the log filter
     * @return the message log or null if undefined
     */
    public MessageLog addMessageLog(String file, String filter) {
        try {
            MessageLog log = new MessageLog(file, filter);
            messageLogs.add(log);
            return log;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Removes the given message log
     *
     * @param log the message log to remove
     * @return if the log was removed
     */
    public boolean removeMessageLog(MessageLog log) {
        return messageLogs.removeIf(l -> l == log);
    }

    /**
     * Removes the given message log
     *
     * @param file the file of the message log to remove.
     * @return if the log was removed
     */
    public boolean removeMessageLog(String file) {
        return messageLogs.removeIf(l -> l.file.equalsIgnoreCase(file));
    }

    /**
     * Logs the message
     *
     * @param msg the message to log
     * @param clientId the id of the recipient or sender, or null if undefined
     * @param inbound inbound or outbound
     * @param type the message type
     */
    public void logMessage(MmsMessage msg, String clientId, boolean inbound, MessageFormatType type) {
        messageLogs.forEach(log -> {
            try {
                log.logMessage(msg, clientId, inbound, type);
            } catch (Exception e) {
                // Do not propagate exceptions;
            }
        });
    }

    /**
     * Interface that should be implemented by the main configuration class
     */
    public interface AccessLogConfiguration {

        /**
         * Returns the file path to the access log, or 'stdout' for System.out
         * @return the accessLog
         */
        String getAccessLog();
    }

    /**
     * Represents a message log as defined by a log file name and optionally
     * a log filter
     */
    public static class MessageLog extends Formatter {
        private final Logger log = Logger.getLogger(MessageLog.class.getSimpleName());
        private final String file;
        private final String filter;
        private final String format = "%1$tb %1$td, %1$tY %1$tH:%1$tM:%1$tS:%1$tL %1$Tz - %2$s - %3$s - %4$s - %5$s%n";
        private boolean compact = Boolean.getBoolean("net.maritimecloud.mms.accessLog.compact");

        /**
         * Constructor
         * @param file the log file
         * @param filter the log filter
         */
        public MessageLog(String file, String filter) throws IOException {
            this.file = file;
            this.filter = filter;

            log.setUseParentHandlers(false);
            if (file != null) {
                FileHandler fh = new FileHandler(file, true);
                fh.setFormatter(this);
                fh.setLevel(Level.ALL);
                log.addHandler(fh);
            } else {
                ConsoleHandler ch = new ConsoleHandler();
                ch.setFormatter(this);
                ch.setLevel(Level.ALL);
                log.addHandler(ch);
            }
        }

        /**
         * Called to log a new inbound or outbound message
         * @param msg the message to log
         * @param clientId the id of the recipient or sender, or null if undefined
         * @param inbound inbound or outbound
         * @param type the message type
         */
        public void logMessage(MmsMessage msg, String clientId, boolean inbound, MessageFormatType type) {
            String msgType = type == MessageFormatType.MACHINE_READABLE ? "bin" : "txt";
            boolean doLog = true;

            // Check if a message filter has been defined
            if (filter != null && filter.trim().length() > 0) {
                // TODO: Do not create a new script engine for each call to log a message.
                // However, multi-threading must be considered carefully.
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
                engine.put("msg", msg);
                engine.put("clientId", clientId);
                engine.put("inbound", inbound);
                engine.put("msgType", msgType);
                try {
                    doLog = (Boolean)engine.eval(filter);
                } catch (ScriptException e) {
                    doLog = false;
                }
            }

            // Log the message
            if (doLog) {
                try {
                    String record = String.format(
                            format,
                            new Date(),
                            msgType,
                            inbound ? "inbound" : "outbound",
                            clientId == null ? "N/A" : clientId,
                            encodeMessage(msg)
                    );
                    log.info(record);
                } catch (Exception e) {
                    // Only include properly formatted messages in the access log
                }
            }
        }

        /**
         * Encodes the given message
         * @param msg the message
         * @return the encoded message
         */
        private String encodeMessage(MmsMessage msg) throws IOException {
            if (compact) {
                return Base64.getEncoder().encodeToString(msg.toBinary());
            } else {
                // Indent each line in the JSON blob
                return String.format("%n%s", msg.toText().replaceAll("(?m)^", "  "));
            }
        }

        /** {@inheritDoc} */
        @Override
        public String format(LogRecord record) {
            return record.getMessage();
        }

        public boolean isCompact() {
            return compact;
        }

        public void setCompact(boolean compact) {
            this.compact = compact;
        }
    }
}
