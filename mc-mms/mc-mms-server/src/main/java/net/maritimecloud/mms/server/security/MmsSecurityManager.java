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

import com.typesafe.config.Config;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.util.Objects;

/**
 * The MMS server security manager.
 * <p/>
 * The security can be instantiated with a security configuration file that may define handler classes for the
 * following type of security-related information groups:
 * <ul>
 *     <li>ssl-conf: Configures an {@code SslHandler} handler class that instantiates a {@code SslContextFactory} used
 *                   by the MMS server.</li>
*      <li>auth-token-conf: Configures an {@code AuthenticationTokenHandler} class that resolves an
 *                   {@code AuthenticationToken} from the websocket upgrade request.</li>
 *     <li>authentication-conf: Configures an {@code AuthenticationHandler} class that will
 *                   authenticate the client using the resolved authentication token.</li>
 *     <li>client-verification-conf: Configures a {@code ClientVerificationHandler} class that will
 *                   check verify that the client is valid for the current user.</li>
 * </ul>
 *
 * The handlers are not used directly in security manager client code. Instead, the security manager
 * and the {@code Subject} class contains functions that will use the handlers in turn.
 */
public class MmsSecurityManager {

    private static final String SUBJECT_SESSION_ATTR = "mms.subject";
    private static final String HANDLER_CLASS_ATTR   = "handler-class";
    private static final Logger LOG = LoggerFactory.getLogger(MmsSecurityManager.class);

    private final Config conf;

    private final SslHandler sslHandler;
    private final AuthenticationTokenHandler authenticationTokenHandler;
    private final AuthenticationHandler authenticationHandler;
    private final ClientVerificationHandler clientVerificationHandler;

    /**
     * Constructor
     * @param securityConf the MMS security configuration file
     */
    public MmsSecurityManager(Config securityConf) {
        conf = securityConf;

        // Initialize the security handlers from the configuration file
        sslHandler = newSecurityConfHandler(SslHandler.SECURITY_CONF_GROUP);
        authenticationTokenHandler = newSecurityConfHandler(AuthenticationTokenHandler.SECURITY_CONF_GROUP);
        authenticationHandler = newSecurityConfHandler(AuthenticationHandler.SECURITY_CONF_GROUP);
        clientVerificationHandler = newSecurityConfHandler(ClientVerificationHandler.SECURITY_CONF_GROUP);
    }

    /**
     * Returns the associated configuration file
     * @return the associated configuration file
     */
    public Config getConf() {
        return conf;
    }

    /**
     * Returns a new SSL context factory based on the SSL configuration
     * @return a new SSL context factory based on the SSL configuration
     */
    public SslContextFactory getSslContextFactory() {
        return sslHandler != null ? sslHandler.getSslContextFactory() : new SslContextFactory();
    }

    /**
     * Instantiates a new security configuration handler for the given security group name
     *
     * @param name the security group name
     * @return the security configuration handler or null if undefined
     */
    private <T extends BaseSecurityHandler> T newSecurityConfHandler(String name) {

        if (conf.hasPath(name) && conf.getConfig(name).hasPath(HANDLER_CLASS_ATTR)) {
            Config handlerConfig = conf.getConfig(name);
            String handlerClass = handlerConfig.getString(HANDLER_CLASS_ATTR);
            // If the class name starts with uppercase, it is in the security package
            if (Character.isUpperCase(handlerClass.charAt(0))) {
                handlerClass = MmsSecurityManager.class.getPackage().getName() + ".impl." + handlerClass;
            }
            try {
                @SuppressWarnings("unchecked")
                T confHandler = (T)Class.forName(handlerClass).newInstance();
                confHandler.init(handlerConfig);
                LOG.info("Instantiated handler " + confHandler + " of security group " + name);
                return confHandler;
            } catch (Exception e) {
                LOG.error("Failed instantiating class " + handlerClass + " of security group " + name, e);
            }
        }

        // Not properly defined
        LOG.warn("Configuration does not define handler for security group " + name);
        return null;
    }

    /**
     * Instantiates a new {@code Subject} from the websocket session. If a subject has
     * already been associated with the session, this is returned instead.
     *
     * @param session the websocket session
     * @return the Subject
     */
    synchronized Subject instantiateSubject(Session session) {
        // Check if the subject is already instantiated
        Subject subject = (Subject) session.getUserProperties().get(SUBJECT_SESSION_ATTR);
        if (subject == null) {

            // Instantiate a new subject
            subject = new SubjectImpl(this);

            // Associate the subject with the session
            session.getUserProperties().put(SUBJECT_SESSION_ATTR, subject);
        }
        return subject;
    }

    /**
     * Resolves an {@code AuthenticationToken} from the websocket session.
     * <p>
     * If none can be resolved, null is returned.
     * <p>
     * If an authentication token has been defined but is invalid (e.g. an expired JWT bearer token),
     * an {@code AuthenticationException} may be thrown.
     *
     * @param session the websocket session
     * @return the authentication token, or null if none is resolved
     */
    public AuthenticationToken resolveAuthenticationToken(Session session) throws AuthenticationException {
        // Check if we can resolve the authentication token from the upgrade request
        WebSocketSession jettySession = (WebSocketSession)session;
        ServletUpgradeRequest upgradeRequest = (ServletUpgradeRequest)jettySession.getUpgradeRequest();
        if (authenticationTokenHandler != null) {
            return authenticationTokenHandler.resolveAuthenticationToken(upgradeRequest);
        }
        return null;
    }

    /**
     * An implementation of the [@code Subject} interface
     */
    static class SubjectImpl implements Subject {

        final MmsSecurityManager securityManager;
        boolean authenticated;
        Object principal;

        /**
         * Constructor
         * @param securityManager the security manager
         */
        public SubjectImpl(MmsSecurityManager securityManager) {
            Objects.requireNonNull(securityManager);
            this.securityManager = securityManager;
        }

        /** {@inheritDoc} */
        @Override
        public Object getPrincipal() {
            return principal;
        }

        /** {@inheritDoc} */
        @Override
        public boolean isAuthenticated() {
            return authenticated;
        }

        /** {@inheritDoc} */
        @Override
        public void checkClient(String clientId) throws ClientVerificationException {
            // If no client verification handler is defined, accept all clients
            if (securityManager.clientVerificationHandler != null) {
                securityManager.clientVerificationHandler.verifyClient(principal, clientId);
            }
        }

        /** {@inheritDoc} */
        @Override
        public synchronized void login(AuthenticationToken token) throws AuthenticationException {
            // Clear any existing logged in info
            logout();

            if (securityManager.authenticationHandler == null) {
                throw new AuthenticationException("No authentication handler configured");
            }

            // Authenticate - throws an exception if authentication fails
            securityManager.authenticationHandler.authenticate(token);

            // Flag that the subject is authenticated
            authenticated = true;
            principal = token.getPrincipal();
        }

        /** {@inheritDoc} */
        @Override
        public synchronized void logout() {
            authenticated = false;
            principal = null;
        }
    }
}
