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
package net.maritimecloud.net.mms;

import java.io.Serializable;

/**
 * A class indicating a status code for the close. As well as an optional string message.
 *
 * @author Kasper Nielsen
 */
public final class MmsConnectionClosingCode implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The connection was closed normally */
    public static final MmsConnectionClosingCode NORMAL = new MmsConnectionClosingCode(1000, "Normal closure");

    /**
     * 1003 indicates that an endpoint is terminating the connection because it has received a type of data it cannot
     * accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public static final MmsConnectionClosingCode BAD_DATA = new MmsConnectionClosingCode(1003, "Bad data");

    /**
     * Another client connected with the same identify. Only one client can be connected with the same id. Whenever a
     * new client connects with the same identify as a client that is already connected. The connection to the existing
     * client is automatically closed with this reason.
     */
    public static final MmsConnectionClosingCode DUPLICATE_CONNECT = new MmsConnectionClosingCode(4012,
            "Duplicate connect");

    public static final MmsConnectionClosingCode WRONG_MESSAGE = new MmsConnectionClosingCode(4100, "Wrong msgtype");

    public static final MmsConnectionClosingCode CONNECT_CANCELLED = new MmsConnectionClosingCode(4101,
            "Connect Cancelled");

    /**
     * Sent from a MMS server if a client tries to reconnect with an existing session id that is no longer valid, for
     * example, if the session has timed out at server side. Or the server was rebooted and lost
     */
    public static final MmsConnectionClosingCode INVALID_SESSION = new MmsConnectionClosingCode(4107, "Session Invalid");


    public static final MmsConnectionClosingCode CLIENT_TIMEOUT = new MmsConnectionClosingCode(4108, "Client Timeout");

    /** The status code. */
    private final int id;

    /** An optional string message. */
    private final String message;

    private MmsConnectionClosingCode(int id, String message) {
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

    public boolean equals(Object other) {
        return other instanceof MmsConnectionClosingCode && ((MmsConnectionClosingCode) other).id == id;
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
    public MmsConnectionClosingCode withMessage(String message) {
        return new MmsConnectionClosingCode(id, message);
    }

    public static MmsConnectionClosingCode create(int id, String message) {
        return new MmsConnectionClosingCode(id, message);
    }

    public String toString() {
        return getId() + ":" + getMessage();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return id;
    }
}
