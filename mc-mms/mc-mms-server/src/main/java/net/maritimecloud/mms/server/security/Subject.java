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

import javax.websocket.Session;
import java.util.Objects;

/**
 * <p>The {@code Subject} is - up to a point - modelled on the
 * <a href="http://shiro.apache.org/static/latest/apidocs/org/apache/shiro/subject/Subject.html">Apache Shiro Subject</a>
 * concept.
 * </p>
 *
 * <p>
 * A {@code Subject} represents state and security operations for a <em>single</em> application user.
 * These operations include authentication (login/logout) and authorization (access control).
 * </p>
 * <h3>Acquiring a Subject</h3>
 * The {@code Subject} should be instantiated in the websocket {@code @OnOpen} method using the
 * {@code SecurityManager} class.
 *
 */
public interface Subject {

    /**
     * Returns this Subject's application-wide uniquely identifying principal, or {@code null} if this
     * Subject is anonymous because it doesn't yet have any associated account data (for example,
     * if they haven't logged in).
     * @return the Subject's principal
     */
    Object getPrincipal();

    /**
     * Returns whether the subject has been authenticated
     * @return whether the subject has been authenticated
     */
    boolean isAuthenticated();

    /**
     * Returns {@code true} if this Subject has the specified role, {@code false} otherwise.
     *
     * @param roleIdentifier the application-specific role identifier (usually a role id or role name).
     * @return {@code true} if this Subject has the specified role, {@code false} otherwise.
     */
    boolean hasRole(String roleIdentifier);

    /**
     * Asserts this Subject has the specified role by returning quietly if they do or throwing an
     * {@link AuthorizationException} if they do not.
     *
     * @param roleIdentifier the application-specific role identifier (usually a role id or role name ).
     * @throws AuthorizationException
     *          if this Subject does not have the role.
     */
    void checkRole(String roleIdentifier) throws AuthorizationException;

    /**
     * Asserts that the client maritime ID is valid for this Subject by returning quietly if they do or throwing n
     * {@link ClientVerificationException} if they do not.
     *
     * @param clientId the maritime ID of the client.
     * @throws ClientVerificationException if the client is not valid for this Subject.
     */
    void checkClient(String clientId) throws ClientVerificationException;

    /**
     * Performs a login attempt for this Subject/user.  If unsuccessful,
     * an {@link AuthenticationException} is thrown.
     * <p/>
     * If successful, the account data associated with the submitted principals/credentials will be
     * associated with this {@code Subject} and the method will return quietly.
     * <p/>
     * Upon returning quietly, this {@code Subject} instance can be considered
     * authenticated and {@link #getPrincipal() getPrincipal()} will be non-null and
     * {@link #isAuthenticated() isAuthenticated()} will be {@code true}.
     *
     * @param token the token encapsulating the subject's principals and credentials to be passed to the
     *              Authentication subsystem for verification.
     * @throws AuthenticationException
     *          if the authentication attempt fails.
     */
    void login(AuthenticationToken token) throws AuthenticationException;

    /**
     * Logs out this Subject and invalidates and/or removes any associated entities
     */
    void logout();

    /**
     * Builder design pattern implementation for creating {@link Subject} instances in a simplified way without
     * requiring knowledge of the underlying construction techniques.
     */
    class Builder {

        /** The security manager */
        private final MmsSecurityManager securityManager;

        /** The websocket session */
        private Session session;

        /**
         * Instantiates the builder with the MMS security manager
         * @param securityManager the MMS security manager
         */
        public Builder(MmsSecurityManager securityManager) {
            Objects.requireNonNull(securityManager);
            this.securityManager = securityManager;
        }

        public Builder setSession(Session session) {
            this.session = session;
            return this;
        }

        public Subject build() {
            return securityManager.instantiateSubject(session);
        }
    }
}
