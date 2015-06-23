package net.maritimecloud.mms.server.rest;

import net.maritimecloud.mms.server.security.AuthenticationException;
import net.maritimecloud.mms.server.security.AuthenticationToken;
import net.maritimecloud.mms.server.security.MmsSecurityManager;
import net.maritimecloud.mms.server.security.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * Can be used as a base class for REST endpoints
 * that have protected access
 */
public abstract class ProtectedResource {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtectedResource.class);

    /** The security manager */
    final MmsSecurityManager securityManager;

    /** The current HTTP servlet request */
    @Context
    protected HttpServletRequest servletRequest;

    /**
     * Constructor
     * @param securityManager the security manager
     */
    public ProtectedResource(MmsSecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * Initializes the client subject
     */
    protected Subject initializeSubject() throws AuthenticationException {
        // Instantiate the client subject
        Subject subject = new Subject.Builder(securityManager)
                .setRequest(servletRequest)
                .build();

        // Attempt to resolve an authentication token
        AuthenticationToken token = securityManager.resolveAuthenticationToken(servletRequest);
        if (token != null) {
            // Log in the subject using the authentication token
            subject.login(token);
            LOGGER.info("Successfully authenticated " + token.getPrincipal());
        }
        return subject;
    }

}
