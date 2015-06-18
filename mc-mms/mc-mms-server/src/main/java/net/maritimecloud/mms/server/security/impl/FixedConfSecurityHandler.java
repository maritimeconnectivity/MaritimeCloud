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
package net.maritimecloud.mms.server.security.impl;

import com.typesafe.config.Config;
import net.maritimecloud.mms.server.security.AuthenticationException;
import net.maritimecloud.mms.server.security.AuthenticationHandler;
import net.maritimecloud.mms.server.security.AuthenticationToken;
import net.maritimecloud.mms.server.security.AuthenticationTokenHandler;
import net.maritimecloud.mms.server.security.AuthorizationException;
import net.maritimecloud.mms.server.security.AuthorizationHandler;
import net.maritimecloud.mms.server.security.ClientVerificationException;
import net.maritimecloud.mms.server.security.ClientVerificationHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles authentication and authorization by static configuration rules.
 *
 * <h2>Authentication Token Resolution</h2>
 * Implements the {@code AuthenticationTokenHandler} interface and may be used if a proxy SSL-server
 * (e.g. nginx) has already authenticated the client and stamped the principal into a header
 * of the upgrade request.<br>
 * The security configuration must name the request header via the "principal-header" configuration option.
 *
 * <h2>Authentication</h2>
 * Implements the {@code AuthenticationHandler} interface and will
 * flag a successful authentication attempt if a principal is defined.
 *
 * <h2>Authorization</h2>
 * Implements the {@code AuthorizationHandler} interface and will
 * assign a fixed set of roles to the client, as defined by the mandatory
 * "authenticated-roles" and the optional "unauthenticated-roles" attributes.
 *
 * <h2>Client Verification</h2>
 * Implements the {@code ClientVerificationHandler} interface and will
 * check that the principal is identical to the client ID.
 */
@SuppressWarnings("unused")
public class FixedConfSecurityHandler
        implements AuthenticationTokenHandler, AuthenticationHandler, AuthorizationHandler, ClientVerificationHandler {

    private Config conf;

    /** {@inheritDoc} */
    @Override
    public void init(Config conf) {
        this.conf = conf;
    }

    /** {@inheritDoc} */
    @Override
    public Config getConf() {
        return conf;
    }

    /*************************************************/
    /** AuthenticationToken Resolution Support      **/
    /*************************************************/

    /** {@inheritDoc} */
    @Override
    public AuthenticationToken resolveAuthenticationToken(ServletUpgradeRequest upgradeRequest) {

        if (conf.hasPath("principal-header")) {

            String principal = conf.getString("principal-header");
            if (principal != null && principal.trim().length() > 0) {
                return new PrincipalAuthenticationToken(principal);
            }
        }

        // No principal resolved
        return null;
    }

    /*************************************************/
    /** Authentication Support                      **/
    /*************************************************/

    /** {@inheritDoc} */
    @Override
    public void authenticate(AuthenticationToken token) throws AuthenticationException {

        if (token == null || token.getPrincipal() == null) {
            throw new AuthenticationException("No principal defined");
        }
    }

    /*************************************************/
    /** Authorization Support                       **/
    /*************************************************/

    /** {@inheritDoc} */
    @Override
    public void checkRoles(Object principal, boolean matchAll, String... roleIdentifiers) throws AuthorizationException {

        String roleAttr = principal == null ? "unauthenticated-roles" : "authenticated-roles";
        checkRoles(roleAttr, matchAll, roleIdentifiers);
    }

    /**
     * Check if the roles are matched by the roles of the given configuration attribute
     * @param attr the configuration attribute
     * @param matchAll whether to match any or all of the roles
     * @param roleIdentifiers the roles to check
     */
    private void checkRoles(String attr, boolean matchAll, String... roleIdentifiers) throws AuthorizationException {
        // Sanity checks
        if (roleIdentifiers == null || roleIdentifiers.length == 0) {
            return;
        }

        if (!conf.hasPath(attr)) {
            throw new AuthorizationException("Authorization error: No roles");
        }


        Set<String> roles = new HashSet<>(Arrays.asList(conf.getString(attr).split("\\s+")));
        if (!matchRoles(roles, matchAll, false, roleIdentifiers)) {
            throw new AuthorizationException("Authorization error");
        }
    }

    /*************************************************/
    /** Client Verification Support                 **/
    /*************************************************/

    /** {@inheritDoc} */
    @Override
    public void verifyClient(Object principal, String id) throws ClientVerificationException {

        if (principal == null || id == null) {
            throw new ClientVerificationException("Client verification error");
        }

        if (!principal.toString().equalsIgnoreCase(id)) {
            throw new ClientVerificationException("Client verification error. " + principal + " not identical to " + id);
        }
    }

    /*************************************************/
    /** Support classes                             **/
    /*************************************************/

    /**
     * An authentication token that defines a principal but no credentials
     */
    public static class PrincipalAuthenticationToken implements AuthenticationToken {

        private String principal;

        /** No-arg constructor */
        public PrincipalAuthenticationToken() {
        }

        /**
         * Constructor
         * @param principal the principal
         */
        public PrincipalAuthenticationToken(String principal) {
            this.principal = principal;
        }

        /** {@inheritDoc} */
        @Override
        public Object getPrincipal() {
            return principal;
        }

        /** {@inheritDoc} */
        @Override
        public Object getCredentials() {
            return null;
        }
    }

}
