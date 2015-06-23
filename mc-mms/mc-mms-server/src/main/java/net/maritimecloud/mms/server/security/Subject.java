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
package net.maritimecloud.mms.server.security;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.util.Objects;

/**
 * <p>The {@code Subject} is - up to a point - modelled on the
 * <a href="http://shiro.apache.org/static/latest/apidocs/org/apache/shiro/subject/Subject.html">
 *     Apache Shiro Subject</a> concept.
 * </p>
 * <p>
 * A {@code Subject} encapsulates the identity and state of the current client. It contains
 * methods for logging the client in and out.
 * </p>
 * The {@code Subject} should be instantiated in the websocket {@code @OnOpen} method using the
 * {@code SecurityManager} class.
 *
 */
public interface Subject {

    /**
     * Returns the Principal associated with the subject
     * @return the principal
     */
    Object getPrincipal();

    /**
     * Returns whether the subject has been authenticated
     * @return whether the subject has been authenticated
     */
    boolean isAuthenticated();

    /**
     * Asserts that the client maritime ID is valid for this Subject by returning quietly if they do or throwing n
     * {@link ClientVerificationException} if they do not.
     *
     * @param clientId the maritime ID of the client.
     * @throws ClientVerificationException if the client is not valid for this Subject.
     */
    void checkClient(String clientId) throws ClientVerificationException;

    /**
     * Logs in the current subject using the given authentication token.
     * If the login attempt fails, an exception is thrown.
     *
     * @param token authentication token.
     */
    void login(AuthenticationToken token) throws AuthenticationException;

    /**
     * Logs out the Subject
     */
    void logout();

    /**
     * Builds a {@link Subject} from either a WebSocket Session or a
     * from a http servlet request.
     */
    class Builder {

        /** The security manager */
        private final MmsSecurityManager securityManager;

        /** The websocket session */
        private Session session;

        /** The http servlet request - used for REST security **/
        private HttpServletRequest request;

        /**
         * Instantiates the builder with the MMS security manager
         * @param securityManager the MMS security manager
         */
        public Builder(MmsSecurityManager securityManager) {
            Objects.requireNonNull(securityManager);
            this.securityManager = securityManager;
        }

        public Builder setSession(Session session) {
            if (request != null) {
                throw new IllegalArgumentException("Subject must be built from either a WebSocket session or a servlet request, not both");
            }
            this.session = session;
            return this;
        }

        public Builder setRequest(HttpServletRequest request) {
            if (session != null) {
                throw new IllegalArgumentException("Subject must be built from either a WebSocket session or a servlet request, not both");
            }
            this.request = request;
            return this;
        }

        public Subject build() {
            if (request == null && session == null) {
                throw new IllegalArgumentException("Subject must be built from either a WebSocket session or a servlet request");
            } else if (request != null) {
                return securityManager.instantiateSubject(request);
            } else {
                return securityManager.instantiateSubject(session);
            }
        }
    }
}
