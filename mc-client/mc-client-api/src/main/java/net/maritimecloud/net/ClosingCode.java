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
package net.maritimecloud.net;

import java.io.Serializable;

/**
 * A class indicating a status code for the close. As well as an optional string message.
 * 
 * @author Kasper Nielsen
 */
public final class ClosingCode implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The connection was closed normally */
    public static final ClosingCode NORMAL = new ClosingCode(1000, "Normal closure");

    /**
     * 1003 indicates that an endpoint is terminating the connection because it has received a type of data it cannot
     * accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public static final ClosingCode BAD_DATA = new ClosingCode(1003, "Bad data");

    /**
     * Another client connected with the same identify. Only one client can be connected with the same id. Whenever a
     * new client connects with the same identify as a client that is already connected. The connection to the existing
     * client is automatically closed with this reason.
     */
    public static final ClosingCode DUPLICATE_CONNECT = new ClosingCode(4012, "Duplicate connect");

    public static final ClosingCode WRONG_MESSAGE = new ClosingCode(4100, "Wrong msgtype");

    public static final ClosingCode CONNECT_CANCELLED = new ClosingCode(4101, "Connect Cancelled");

    /** The status code. */
    private final int id;

    /** An optional string message. */
    private final String message;

    private ClosingCode(int id, String message) {
        this.id = id;
        this.message = message;
    }

    /**
     * Returns the id.
     * 
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns an optional close reason.
     * 
     * @return an optional close reason
     */
    public String getMessage() {
        return message;
    }

    public boolean isReconnectable() {
        if (id == 1000) {
            return false;// The connection closed normally
        }
        return false;
    }

    /**
     * Returns a new CloseReason with the same status code, but with a different message.
     * 
     * @param message
     *            the message of the returned close reason
     * @return the new close reason
     */
    public ClosingCode withMessage(String message) {
        return new ClosingCode(id, message);
    }

    public static ClosingCode create(int id, String message) {
        return new ClosingCode(id, message);
    }

    public String toString() {
        return getId() + ":" + getMessage();
    }
}
