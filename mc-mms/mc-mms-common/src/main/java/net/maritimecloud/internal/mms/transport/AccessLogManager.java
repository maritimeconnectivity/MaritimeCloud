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

import net.maritimecloud.internal.mms.messages.Close;
import net.maritimecloud.internal.mms.messages.Connected;
import net.maritimecloud.internal.mms.messages.Hello;
import net.maritimecloud.internal.mms.messages.PositionReport;
import net.maritimecloud.internal.mms.messages.spi.MmsMessage;
import net.maritimecloud.internal.net.messages.Broadcast;
import net.maritimecloud.internal.net.messages.BroadcastAck;
import net.maritimecloud.internal.net.messages.MethodInvoke;
import net.maritimecloud.internal.net.messages.MethodInvokeResult;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageFormatType;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Will handle logging of {@code MmsMessage} entities at the transport layer level.
 * <p>
 * The message log can be associated with a file name, whose name pattern must follow
 * the guidelines of the
 * <a href="http://docs.oracle.com/javase/7/docs/api/java/util/logging/FileHandler.html">FileHandler</a> class.<br/>
 * If the file is undefined, the messages will be logged to {@code System.out}.
 * <p>
 * The access log format determines the format of the logged messages:
 * <ul>
 *     <li>text: Logs all messages in a multi-line json format.</li>
 *     <li>text: Logs all messages as base64-encoded single-line format.</li>
 *     <li>compact: Skips certain messages, such as position reports, and formats the messages
 *                  in a simplified compact format.</li>
 * </ul>
 * <p>
 * If the <i>filter</i> is defined, only the messages matching the filter are logged.<br>
 * Example: <code>inbound && clientId == 'mmsi:565009926' && msg.m.positionTime !== undefined</code>
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
            addMessageLog(logFile, conf.getAccessLogFormat());
        }
    }

    /**
     * Adds a logger defined by the given log file.
     * If no file is specified, System.out is used instead.
     *
     * @param file the log file. If null, System.out is used instead.
     * @param format the message format
     * @return the message log or null if undefined
     */
    public MessageLog addMessageLog(String file, AccessLogFormat format) {
        return addMessageLog(file, format, null);
    }

    /**
     * Adds a logger defined by the given log file and filter.
     * If no file is specified, System.out is used instead.
     *
     * @param file the log file. If null, System.out is used instead.
     * @param format the message format
     * @param filter the log filter
     * @return the message log or null if undefined
     */
    public MessageLog addMessageLog(String file, AccessLogFormat format, String filter) {
        try {
            MessageLog log = new MessageLog(file, format, filter);
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
     * The access log message format
     */
    public enum AccessLogFormat {
        TEXT,       // Log in JSON format
        BINARY,     // Log in binary format (Base64 encoded)
        COMPACT     // Log a non-complete compact representation of the messages
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

        /**
         * Returns the access log format
         * @return the access log format
         */
        AccessLogFormat getAccessLogFormat();
    }

    /**
     * Represents a message log as defined by a log file name and optionally
     * a log filter
     */
    public static class MessageLog extends Formatter {
        private final Logger log = Logger.getLogger(MessageLog.class.getSimpleName());
        private final String file;
        private final String lineFormat = "%1$tb %1$td, %1$tY %1$tH:%1$tM:%1$tS:%1$tL %1$Tz - %2$s - %3$s - %4$s - %5$s%n";
        private final AccessLogFormat accessLogFormat;
        private Invocable filterFunction = null;

        /**
         * Constructor
         * @param file the log file
         * @param accessLogFormat the message format
         * @param filter the log filter
         */
        public MessageLog(String file, AccessLogFormat accessLogFormat, String filter) throws IOException {
            this.file = file;
            this.accessLogFormat = accessLogFormat;

            // Instantiate the filter Javascript engine
            if (filter != null && filter.trim().length() > 0) {
                try {
                    // Considerations: Various documentation suggests that the ScriptEngine is indeed threadsafe.
                    // However, shared state is not isolated, so, setting the parameters (msg, clientId, etc.) as
                    // script engine state and evaluating the filter directly would not work correctly.
                    // Instead, we wrap the filter in a function and call that function.
                    ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("JavaScript");
                    jsEngine.eval("function doLog(msg, clientId, inbound, msgType) { return " + filter + "; }");
                    filterFunction = (Invocable)jsEngine;
                } catch (Exception e) {
                    throw new IOException("Invalid access log filter: " + filter);
                }
            }

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
            boolean doLog = checkLogMessage(msg, inbound);

            // Check if a message filter has been defined
            if (filterFunction != null) {
                try {
                    doLog = (Boolean)filterFunction.invokeFunction("doLog", msg, clientId, inbound, msgType);
                } catch (Exception e) {
                    doLog = false;
                }
            }

            // Log the message
            if (doLog) {
                try {
                    String record = String.format(
                            lineFormat,
                            new Date(),
                            msgType,
                            inbound ? "in " : "out",
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
         * Check if the message should be logged. When the 'compact' access log format is selected
         * certain types of messages are omitted.
         * @param msg the message to check
         * @param inbound inbound or outbound
         * @return if the message should be logged
         */
        private boolean checkLogMessage(MmsMessage msg, boolean inbound) {
            if (accessLogFormat == AccessLogFormat.COMPACT) {
                Message m = msg.getMessage();

                // The different condition where we want to skip a log record in the 'compact' access log format
                if (m instanceof Broadcast && !inbound) {
                    return false;
                } else if (m instanceof BroadcastAck ||
                        m instanceof Connected ||
                        m instanceof PositionReport ||
                        m instanceof MethodInvokeResult) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Encodes the given message
         * @param msg the message
         * @return the encoded message
         */
        private String encodeMessage(MmsMessage msg) throws IOException {
            if (accessLogFormat == AccessLogFormat.BINARY) {
                return Base64.getEncoder().encodeToString(msg.toBinary());

            } else if (accessLogFormat == AccessLogFormat.COMPACT) {
                return formatMessageCompact(msg);

            } else {
                // Indent each line in the JSON blob
                return String.format("%n%s", msg.toText().replaceAll("(?m)^", "  "));
            }
        }

        /** Simple utility method that extracts the parameter value */
        public static String extractParam(String txt, String param, String defaultValue) {
            try {
                Matcher m = Pattern.compile(".*\"" + param + "\":\\s*\"(.*)\".*", Pattern.MULTILINE).matcher(txt);
                return m.find() ? m.group(1) : defaultValue;
            } catch (Exception e) {
                return defaultValue;
            }
        }

        /**
         * Formats the message in a compact fashion
         * @param msg the message to format
         * @return the result
         */
        private String formatMessageCompact(MmsMessage msg) {
            if (msg.getMessage() instanceof Close) {
                return String.format("Close[%s]", ((Close)msg.getMessage()).getCloseCode());

            } else if (msg.getMessage().getClass().getPackage().equals(Hello.class.getPackage())) {
                return msg.getMessage().getClass().getSimpleName();

            } else if (msg.getMessage() instanceof Broadcast) {
                return String.format("Broadcast[%s]", ((Broadcast) msg.getMessage()).getBroadcastType());

            } else if (msg.getMessage() instanceof MethodInvoke) {
                MethodInvoke invoke = (MethodInvoke)msg.getMessage();

                if ("Services.registerEndpoint".equals(invoke.getEndpointMethod())) {
                    return String.format("Services.registerEndpoint[%s]", extractParam(invoke.getParameters(), "endpointName", ""));
                } else if ("Services.unregisterEndpoint".equals(invoke.getEndpointMethod())) {
                    return String.format("Services.unregisterEndpoint[%s]", extractParam(invoke.getParameters(), "endpointName", ""));
                } else if ("Services.locate".equals(invoke.getEndpointMethod())) {
                    return String.format("Services.locate[%s]", extractParam(invoke.getParameters(), "endpointName", ""));
                } else if ("Services.subscribe".equals(invoke.getEndpointMethod())) {
                    return String.format("Services.subscribe[%s]", extractParam(invoke.getParameters(), "name", ""));
                }
                return String.format("MethodInvoke[%s]", invoke.getEndpointMethod());

            } else if (msg.getMessage().getClass().getPackage().equals(MethodInvoke.class.getPackage())) {
                return msg.getMessage().getClass().getSimpleName();

            } else {
                return msg.getMessage().getClass().getName();
            }
        }

        /** {@inheritDoc} */
        @Override
        public String format(LogRecord record) {
            return record.getMessage();
        }
    }
}
