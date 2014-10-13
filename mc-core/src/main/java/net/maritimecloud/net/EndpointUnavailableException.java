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

/**
 *
 * @author Kasper Nielsen
 */
public class EndpointUnavailableException extends Exception {

    /** <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new EndpointUnavailableException with the specified detailed message. The cause is not initialized,
     * and may subsequently be initialized by a call to {@link Throwable#initCause}.
     *
     * @param message
     *            the detailed message. The detailed message is saved for later retrieval by the {@link #getMessage()}
     *            method.
     */
    public EndpointUnavailableException(String message) {
        super(message);
    }

    /**
     * Constructs a new EndpointUnavailableException with the specified detailed message and cause.
     *
     * @param cause
     *            the cause (which is saved for later retrieval by the {@link #getCause()}method). (A{@code null} value
     *            is permitted, and indicates that the cause is nonexistent or unknown.)
     * @param message
     *            the detailed message. The detailed message is saved for later retrieval by the {@link #getMessage()}
     *            method.
     */
    public EndpointUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
