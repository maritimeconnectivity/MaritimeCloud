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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.maritimecloud.mms.server.security.AuthenticationException;
import net.maritimecloud.mms.server.security.AuthenticationToken;
import net.maritimecloud.mms.server.security.AuthenticationTokenHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;

/**
 * Implementation of the {@code AuthenticationTokenHandler} interface that attempts
 * to extract and decode the JWT token of the bearer authentication header.
 * <p/>
 * The "jwt-secret" configuration option must be used to set the shared JWT secret that was used
 * for signing the JWT token.
 * <p/>
 * The JWT authentication token handler might e.g. be combined with the LdapSecurityHandler to
 * verify that the subject is present in LDAP, in which case you would probably use a "user-search-filter"
 * with the value "(mail={0})", assuming the JWT subject is the user email address.
 * <p/>
 * For test purposes, you can e.g. generate JWT tokens at: http://jwtbuilder.jamiekurtz.com/
 * <p/>
 * Future improvements:
 * <ul>
 *     <li>Consider using JWT encryption</li>
 *     <li>Maintain JWT revocation list based on 'jti', or ('aud','jti')</li>
 *     <li>Better protection of shared secret</li>
 *     <li>Multi-tenancy support (can you tie shared secret to 'aud'? ... see express-jwt)
 * </ul>
 */
@SuppressWarnings("unused")
public class JwtAuthenticationTokenHandler implements AuthenticationTokenHandler {

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

    /** {@inheritDoc} */
    @Override
    public AuthenticationToken resolveAuthenticationToken(ServletUpgradeRequest upgradeRequest) throws AuthenticationException {

        String jwtSecret = conf.getString("jwt-secret");
        String authHeader = upgradeRequest.getHeader("Authorization");

        return resolveAuthenticationToken(jwtSecret, authHeader);
    }

    /**
     * Resolves an {@code AuthenticationToken} from the websocket upgrade request bearer authorization header.
     * If none can be resolved, null is returned.
     * If the JWT token is present but invalid, an AuthenticationException is thrown
     *
     * @param jwtSecret the shared JWT secret
     * @param authHeader the authorization header
     * @return the authentication token, or null if none is resolved
     */
    public static AuthenticationToken resolveAuthenticationToken(String jwtSecret, String authHeader) throws AuthenticationException {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract the user part from the header
            String jwt = authHeader.substring("Bearer ".length());

            try {
                // Throws an JwtException in case of error (e.g. expired)
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret.getBytes("UTF-8"))
                        .parseClaimsJws(jwt)
                        .getBody();

                return new JwtAuthenticationToken(claims.getSubject());

            } catch (Exception e) {
                throw new AuthenticationException("Invalid JWT Token", e);
            }
        }

        // No principal resolved
        return null;
    }

    /**
     * Implementation of an JWT authentication tokens
     */
    public static class JwtAuthenticationToken implements AuthenticationToken {

        String subject;

        /**
         * Constructor
         * @param subject the JWT subject
         */
        public JwtAuthenticationToken(String subject) {
            this.subject = subject;
        }

        /** {@inheritDoc} */
        @Override
        public Object getPrincipal() {
            return subject;
        }

        /** {@inheritDoc} */
        @Override
        public Object getCredentials() {
            return null;
        }

        public String getSubject() {
            return subject;
        }
    }
}
